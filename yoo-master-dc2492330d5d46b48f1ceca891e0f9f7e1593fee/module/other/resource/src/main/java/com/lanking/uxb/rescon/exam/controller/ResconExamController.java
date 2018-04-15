package com.lanking.uxb.rescon.exam.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperStatus;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopicType;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory.OperateType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.exam.api.ResconExamHistoryManage;
import com.lanking.uxb.rescon.exam.api.ResconExamManage;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperQuestionManage;
import com.lanking.uxb.rescon.exam.api.ResconExamTopicManage;
import com.lanking.uxb.rescon.exam.convert.ResconExamConvert;
import com.lanking.uxb.rescon.exam.convert.ResconExamOption;
import com.lanking.uxb.rescon.exam.convert.ResconExamPaperTopicConvert;
import com.lanking.uxb.rescon.exam.form.ExamForm;
import com.lanking.uxb.rescon.exam.form.QueryForm;
import com.lanking.uxb.rescon.exam.value.VExam;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.counter.api.impl.ExamCounterProvider;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 中央资源库 试卷controller
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月21日 下午2:59:54
 */
@RestController
@RequestMapping(value = "rescon/exam")
public class ResconExamController {
	@Autowired
	private ResconExamManage resconExamManage;
	@Autowired
	private ResconQuestionManage resconQuestionManage;
	@Autowired
	private ResconQuestionConvert resconQuestionConvert;
	@Autowired
	private ResconExamPaperQuestionManage examPaperQuestionManage;
	@Autowired
	private ResconExamTopicManage examPaperTopicManage;
	@Autowired
	private ResconExamPaperTopicConvert examPaperTopicConvert;
	@Autowired
	private ResconExamConvert examConvert;
	@Autowired
	private IndexService indexService;
	@Autowired
	private ExamCounterProvider examCounterProvider;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconExamHistoryManage examHistoryManage;

	@RequestMapping(value = "create", method = {RequestMethod.GET, RequestMethod.POST})
	public Value create(ExamForm examForm) {
		ExamPaper ep = resconExamManage.create(Security.getUserId(), examForm);
		// 记录操作
		examHistoryManage.save(Security.getUserId(), examForm.getId(), OperateType.CREATE);
		indexService.add(IndexType.EXAM_PAPER, ep.getId());
		examCounterProvider.incrExams(vendorUserManage.getVendorUser(Security.getUserId()).getVendorId(), 1);
		examCounterProvider.incrEditingExams(vendorUserManage.getVendorUser(Security.getUserId()).getVendorId(), 1);
		return new Value(ep.getId());
	}

	/**
	 * 检查习题是否有不存在
	 * 
	 * @param qcodes
	 *            习题编码集合
	 * @return
	 */
	@RequestMapping(value = "checkQuestions", method = {RequestMethod.POST, RequestMethod.GET})
	public Value checkQuestions(@RequestParam(value = "qCodes", required = false) List<String> qCodes,
			@RequestParam(value = "examId", required = false) Long examId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		ExamPaper exam = resconExamManage.get(examId);
		List<String> notExistCodes = new ArrayList<String>();
		List<String> errorSchoolCodes = Lists.newArrayList(); // 普通试卷不能加入校本习题
		List<String> statusCodes = Lists.newArrayList(); // 只能添加已通过题目
		if (qCodes == null) {
			return new Value(data);
		}
		List<String> qCodeList = new ArrayList<String>();
		List<String> subjectWrongList = new ArrayList<String>();
		List<Question> questions = resconQuestionManage.findQuestionByCode(qCodes, null);

		for (Question question : questions) {
			qCodeList.add(question.getCode());
			// 不能输入其他学科的题目
			if (!question.getSubjectCode().equals(exam.getSubjectCode())) {
				subjectWrongList.add(question.getCode());
			} else if (exam.getSchoolId() == null && question.getSchoolId() > 0) {
				errorSchoolCodes.add(question.getCode());
			}
			if (question.getStatus() != CheckStatus.PASS) {
				statusCodes.add(question.getCode());
			}
		}
		for (String long1 : qCodes) {
			if (!qCodeList.contains(long1)) {
				notExistCodes.add(long1);
			}
		}
		// 习题在试卷中已经存在
		List<ExamPaperQuestion> examPaperQuestionList = examPaperQuestionManage.getExamQuestionByExam(examId);
		List<String> existedCode = Lists.newArrayList();
		for (ExamPaperQuestion examPaperQuestion : examPaperQuestionList) {
			for (Question question : questions) {
				if (examPaperQuestion.getQuestionId().equals(question.getId())) {
					existedCode.add(question.getCode());
				}
			}
		}
		// notExistCodes为空的话说明习题都存在
		data.put("notExistCode", notExistCodes);
		data.put("subjectWrongList", subjectWrongList);
		data.put("existedCodes", existedCode);
		data.put("errorSchoolCodes", errorSchoolCodes);
		data.put("statusCodes", statusCodes);
		return new Value(data);
	}

	@RequestMapping(value = "addQuestionsInExam", method = {RequestMethod.POST, RequestMethod.GET})
	public Value addQuestionInExam(@RequestParam(value = "qCodes", required = false) List<String> qCodes,
			@RequestParam(value = "examId", required = false) Long examId,
			@RequestParam(value = "isSave") boolean isSave, String topicScoreMap) {
		Map<String, Integer> examTopicScoreMap = JSONObject.parseObject(topicScoreMap, Map.class);
		Map<String, Integer> examTopicScoreMapTemp = Maps.newHashMap();
		if (!CollectionUtils.isEmpty(examTopicScoreMap)) {
			for (String key : examTopicScoreMap.keySet()) {
				Object scoreObj = examTopicScoreMap.get(key);
				if (scoreObj == null || StringUtils.isBlank(scoreObj.toString())) {
					examTopicScoreMapTemp.put(key, 0);
				} else {
					examTopicScoreMapTemp.put(key, examTopicScoreMap.get(key));
				}
			}
		}
		if (CollectionUtils.isEmpty(qCodes)) {
			return new Value();
		}
		Map<String, Object> data = new HashMap<String, Object>(2);
		if (CollectionUtils.isEmpty(qCodes)) {
			return new Value(new IllegalArgException());
		}
		List<Long> qIds = Lists.newArrayList();
		List<Question> qList = questionManage.findQuestionByCode(qCodes, null);
		for (Question question : qList) {
			qIds.add(question.getId());
		}

		if (isSave) {
			List<ExamPaperTopic> eptList = examPaperTopicManage.getTopicsByExam(examId);
			List<VExamPaperTopic> vtopicList = Lists.newArrayList();
			vtopicList = examPaperTopicConvert.to(eptList);
			if (vtopicList.isEmpty()) {
				vtopicList = Lists.newArrayList();
			}
			Map<Long, VExamPaperTopic> vmap = initQuestionTopic(qIds);
			List<VExamPaperTopic> vtopicList2 = Lists.newArrayList(vmap.values());
			int topicLength = vtopicList.size();
			// 判断新加入的题目类型是否已经存在
			for (VExamPaperTopic examPaperTopic : vtopicList2) {
				boolean notExist = true;
				for (VExamPaperTopic vExamPaperTopic : vtopicList) {
					if (examPaperTopic.getType() == vExamPaperTopic.getType()) {
						notExist = false;
					}
				}
				if (notExist) {
					vtopicList.add(examPaperTopic);
				} else if (CollectionUtils.isEmpty(vtopicList)) {
					vtopicList.add(examPaperTopic);
				}

			}
			if (topicLength != vtopicList.size()) {
				List<VExamPaperTopic> topics = Lists.newArrayList();
				for (VExamPaperTopic topic : vtopicList) {
					if (topic.getId() == null) {
						topics.add(topic);
					}
				}
				examPaperTopicManage.updateByTopic(examId, topics);
			}
			List<ExamPaperQuestion> epqListTemp = examPaperQuestionManage.getExamQuestionByExam(examId);
			List<Long> qIdsTemp = Lists.newArrayList();
			for (ExamPaperQuestion examPaperQuestion : epqListTemp) {
				qIdsTemp.add(examPaperQuestion.getQuestionId());
			}
			for (Long long1 : qIdsTemp) {
				for (Long long2 : qIds) {
					if (long1.equals(long2)) {
						return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_EXISTED));
					}
				}
			}
			qIds.addAll(qIdsTemp);
			examPaperQuestionManage.updateExamQuesions(examId, qIds, Security.getUserId(), vmap, examTopicScoreMapTemp);

			List<ExamPaperQuestion> epqList = examPaperQuestionManage.getExamQuestionByExam(examId);
			List<Long> existQIds = Lists.newArrayList();
			for (ExamPaperQuestion examPaperQuestion : epqList) {
				existQIds.add(examPaperQuestion.getQuestionId());
			}
			for (ExamPaperQuestion examPaperQuestion : epqList) {
				existQIds.add(examPaperQuestion.getQuestionId());
			}
			existQIds.addAll(qIds);
			Map<Long, VQuestion> vqExistMap = resconQuestionConvert.to(resconQuestionManage.mget(existQIds));
			// 构造题型中包含题目的结构
			for (VExamPaperTopic examTopic : vtopicList) {
				List<Long> questionIds = Lists.newArrayList();
				for (VQuestion question : vqExistMap.values()) {
					if (examTopic.getType() == ExamPaperTopicType.FILL_BLANK && question.getType() == Type.FILL_BLANK) {
						questionIds.add(question.getId());
					}
					if (examTopic.getType() == ExamPaperTopicType.MULTIPLE_CHOICE
							&& question.getType() == Type.MULTIPLE_CHOICE) {
						questionIds.add(question.getId());
					}
					if (examTopic.getType() == ExamPaperTopicType.SINGLE_CHOICE
							&& question.getType() == Type.SINGLE_CHOICE) {
						questionIds.add(question.getId());
					}
					if (examTopic.getType() == ExamPaperTopicType.QUESTION_ANSWERING
							&& (question.getType() == Type.COMPOSITE || question.getType() == Type.TRUE_OR_FALSE
									|| question.getType() == Type.QUESTION_ANSWERING)) {
						questionIds.add(question.getId());
					}
				}
				examTopic.setQuestionList(resconQuestionConvert.mgetList(questionIds));
			}
			data.put("examQuestion", vtopicList);
		} else {
			List<ExamPaperTopic> eptList = examPaperTopicManage.getTopicsByExam(examId);
			List<ExamPaperQuestion> epqList = examPaperQuestionManage.getExamQuestionByExam(examId);
			List<Long> existQIds = Lists.newArrayList();
			for (ExamPaperQuestion examPaperQuestion : epqList) {
				existQIds.add(examPaperQuestion.getQuestionId());
			}
			existQIds.addAll(qIds);
			Map<Long, VQuestion> vqExistMap = resconQuestionConvert.to(resconQuestionManage.mget(existQIds));
			List<VExamPaperTopic> vtopicList = Lists.newArrayList();
			vtopicList = examPaperTopicConvert.to(eptList);
			if (vtopicList.isEmpty())
				vtopicList = Lists.newArrayList();
			List<VExamPaperTopic> vtopicList2 = Lists.newArrayList(initQuestionTopic(qIds).values());
			// 判断新加入的题目类型是否已经存在
			for (VExamPaperTopic examPaperTopic : vtopicList2) {
				boolean notExist = true;
				for (VExamPaperTopic vExamPaperTopic : vtopicList) {
					if (examPaperTopic.getType() == vExamPaperTopic.getType()) {
						notExist = false;
					}
				}
				if (notExist) {
					vtopicList.add(examPaperTopic);
				} else if (CollectionUtils.isEmpty(vtopicList)) {
					vtopicList.add(examPaperTopic);
				}

			}
			// 构造题型中包含题目的结构
			for (VExamPaperTopic examTopic : vtopicList) {
				List<Long> questionIds = Lists.newArrayList();
				for (VQuestion question : vqExistMap.values()) {
					if (examTopic.getType() == ExamPaperTopicType.FILL_BLANK && question.getType() == Type.FILL_BLANK) {
						questionIds.add(question.getId());
					}
					if (examTopic.getType() == ExamPaperTopicType.MULTIPLE_CHOICE
							&& question.getType() == Type.MULTIPLE_CHOICE) {
						questionIds.add(question.getId());
					}
					if (examTopic.getType() == ExamPaperTopicType.SINGLE_CHOICE
							&& question.getType() == Type.SINGLE_CHOICE) {
						questionIds.add(question.getId());
					}
					if (examTopic.getType() == ExamPaperTopicType.QUESTION_ANSWERING
							&& (question.getType() == Type.COMPOSITE || question.getType() == Type.TRUE_OR_FALSE
									|| question.getType() == Type.QUESTION_ANSWERING)) {
						questionIds.add(question.getId());
					}
				}
				examTopic.setQuestionList(resconQuestionConvert.mgetList(questionIds));
			}
			data.put("examQuestion", vtopicList);
		}

		examHistoryManage.save(Security.getUserId(), examId, OperateType.EDIT);
		indexService.update(IndexType.EXAM_PAPER, examId);
		// 更新索引
		if (qIds.size() > 0) {
			indexService.syncUpdate(IndexType.QUESTION, qIds);
		}
		return new Value(data);
	}

	/**
	 * 试卷添加题目.
	 * 
	 * @since 2017-02-23 新加的题目排到最后
	 * @since yoomath v2.3.2 添加对平均难度的处理
	 * @param examId
	 * @param questionId
	 * @param topicScoreMap
	 * @return
	 */
	@RequestMapping(value = "addQuestionInExam", method = {RequestMethod.POST})
	public Value create(@RequestParam(value = "examId") Long examId,
			@RequestParam(value = "questionId") Long questionId, String topicScoreMap) {
		Map<String, Integer> examTopicScoreMap = JSONObject.parseObject(topicScoreMap, Map.class);
		Map<String, Integer> examTopicScoreMapTemp = Maps.newHashMap();
		for (String key : examTopicScoreMap.keySet()) {
			Object scoreObj = examTopicScoreMap.get(key);
			if (scoreObj == null || StringUtils.isBlank(scoreObj.toString())) {
				examTopicScoreMapTemp.put(key, 0);
			} else {
				examTopicScoreMapTemp.put(key, examTopicScoreMap.get(key));
			}
		}
		List<ExamPaperTopic> eptList = examPaperTopicManage.getTopicsByExam(examId);
		List<VExamPaperTopic> vtopicList = Lists.newArrayList();
		vtopicList = examPaperTopicConvert.to(eptList);
		if (vtopicList.isEmpty()) {
			vtopicList = Lists.newArrayList();
		}
		List<Long> qIds = Lists.newArrayList(questionId);
		Map<Long, VExamPaperTopic> vmap = initQuestionTopic(qIds);
		List<VExamPaperTopic> vtopicList2 = Lists.newArrayList(vmap.values());
		int topicLength = vtopicList.size();
		// 判断新加入的题目类型是否已经存在
		for (VExamPaperTopic examPaperTopic : vtopicList2) {
			boolean notExist = true;
			for (VExamPaperTopic vExamPaperTopic : vtopicList) {
				if (examPaperTopic.getType() == vExamPaperTopic.getType()) {
					notExist = false;
				}
			}
			if (notExist) {
				vtopicList.add(examPaperTopic);
			} else if (CollectionUtils.isEmpty(vtopicList)) {
				vtopicList.add(examPaperTopic);
			}

		}
		if (topicLength != vtopicList.size()) {
			List<VExamPaperTopic> topics = Lists.newArrayList();
			for (VExamPaperTopic topic : vtopicList) {
				if (topic.getId() == null) {
					topics.add(topic);
				}
			}
			examPaperTopicManage.updateByTopic(examId, topics);
		}
		List<ExamPaperQuestion> epqListTemp = examPaperQuestionManage.getExamQuestionByExam(examId);
		List<Long> qIdsTemp = Lists.newArrayList();
		for (ExamPaperQuestion examPaperQuestion : epqListTemp) {
			qIdsTemp.add(examPaperQuestion.getQuestionId());
		}
		for (Long long1 : qIdsTemp) {
			for (Long long2 : qIds) {
				if (long1.equals(long2)) {
					return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_EXISTED));
				}
			}
		}
		qIds.addAll(0, qIdsTemp); // 新加的题目排到最后
		examPaperQuestionManage.updateExamQuesions(examId, qIds, Security.getUserId(), vmap, examTopicScoreMapTemp);

		// 校本试卷处理
		ExamPaper examPaper = resconExamManage.get(examId);
		if (examPaper.getOwnSchoolId() != null) {
			// 更新校本试卷题目schoolId
			questionManage.saveQuestionSchool(questionId, examPaper.getOwnSchoolId());
		}

		List<ExamPaperQuestion> epqList = examPaperQuestionManage.getExamQuestionByExam(examId);
		List<Long> existQIds = Lists.newArrayList();
		for (ExamPaperQuestion examPaperQuestion : epqList) {
			existQIds.add(examPaperQuestion.getQuestionId());
		}
		for (ExamPaperQuestion examPaperQuestion : epqList) {
			existQIds.add(examPaperQuestion.getQuestionId());
		}
		existQIds.addAll(qIds);
		Map<Long, VQuestion> vqExistMap = resconQuestionConvert.to(resconQuestionManage.mget(existQIds));
		// 构造题型中包含题目的结构
		for (VExamPaperTopic examTopic : vtopicList) {
			List<Long> questionIds = Lists.newArrayList();
			for (VQuestion question : vqExistMap.values()) {
				if (examTopic.getType() == ExamPaperTopicType.FILL_BLANK && question.getType() == Type.FILL_BLANK) {
					questionIds.add(question.getId());
				}
				if (examTopic.getType() == ExamPaperTopicType.MULTIPLE_CHOICE
						&& question.getType() == Type.MULTIPLE_CHOICE) {
					questionIds.add(question.getId());
				}
				if (examTopic.getType() == ExamPaperTopicType.SINGLE_CHOICE
						&& question.getType() == Type.SINGLE_CHOICE) {
					questionIds.add(question.getId());
				}
				if (examTopic.getType() == ExamPaperTopicType.QUESTION_ANSWERING
						&& (question.getType() == Type.COMPOSITE || question.getType() == Type.TRUE_OR_FALSE
								|| question.getType() == Type.QUESTION_ANSWERING)) {
					questionIds.add(question.getId());
				}
			}
			examTopic.setQuestionList(resconQuestionConvert.mgetList(questionIds));
		}

		// 更新试卷操作时间
		resconExamManage.updateExamUpdateAt(examPaper.getId(), Security.getUserId());

		// 更新索引
		if (qIds.size() > 0) {
			indexService.syncUpdate(IndexType.QUESTION, qIds);
		}
		return new Value();
	}

	@RequestMapping(value = "editExamAttr", method = {RequestMethod.POST, RequestMethod.GET})
	public Value editExamAttr(ExamForm examForm) {

		// 更新试卷题目索引使用
		Set<Long> questionIds = new HashSet<Long>();
		List<ExamPaperQuestion> oldExamQuestions = examPaperQuestionManage.getExamQuestionByExam(examForm.getId());
		for (ExamPaperQuestion examPaperQuestion : oldExamQuestions) {
			questionIds.add(examPaperQuestion.getQuestionId());
		}

		resconExamManage.edit(examForm, Security.getUserId());
		examHistoryManage.save(Security.getUserId(), examForm.getId(), OperateType.EDIT);

		// 更新索引
		if (questionIds.size() > 0) {
			indexService.syncUpdate(IndexType.QUESTION, questionIds);
		}

		indexService.update(IndexType.EXAM_PAPER, examForm.getId());
		return new Value();
	}

	@RequestMapping(value = "reedit", method = {RequestMethod.POST, RequestMethod.GET})
	public Value reedit(long examId) {
		// 记录操作
		if (resconExamManage.get(examId).getStatus().equals(ExamPaperStatus.PASS)) {
			examHistoryManage.save(Security.getUserId(), examId, OperateType.REEDIT);
			examCounterProvider.incrPublishedExams(vendorUserManage.getVendorUser(Security.getUserId()).getVendorId(),
					-1);
			examCounterProvider.incrEditingExams(vendorUserManage.getVendorUser(Security.getUserId()).getVendorId(), 1);
		} else {
			examCounterProvider.incrNoCheckExams(vendorUserManage.getVendorUser(Security.getUserId()).getVendorId(),
					-1);
			examCounterProvider.incrEditingExams(vendorUserManage.getVendorUser(Security.getUserId()).getVendorId(), 1);
		}
		List<Long> needUpdateQuestions = resconExamManage.updateExamStatus(examId, ExamPaperStatus.EDITING);
		if (CollectionUtils.isNotEmpty(needUpdateQuestions)) {
			indexService.update(IndexType.QUESTION, needUpdateQuestions);
		}
		indexService.update(IndexType.EXAM_PAPER, examId);
		return new Value();
	}

	@RequestMapping(value = "submitExam", method = {RequestMethod.POST, RequestMethod.GET})
	public Value submitExam(long examId) {
		resconExamManage.updateExamStatus(examId, ExamPaperStatus.NOCHECK);
		indexService.update(IndexType.EXAM_PAPER, examId);
		// 记录操作
		examHistoryManage.save(Security.getUserId(), examId, OperateType.SUBMIT);
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		examCounterProvider.incrNoCheckExams(vendorId, 1);
		examCounterProvider.incrEditingExams(vendorId, -1);

		// 更新操作时间
		resconExamManage.updateExamUpdateAt(examId, Security.getUserId());
		return new Value();
	}

	/**
	 * 试卷发布.
	 * 
	 * @since yoomath v2.3.2 添加平均难度
	 * @param examId
	 *            试卷ID
	 * @return
	 */
	@RequestMapping(value = "publishExam", method = {RequestMethod.POST, RequestMethod.GET})
	public Value publishExam(long examId) {
		List<Long> needUpdateQuestions = resconExamManage.updateExamStatus(examId, ExamPaperStatus.PASS);
		if (CollectionUtils.isNotEmpty(needUpdateQuestions)) {
			indexService.update(IndexType.QUESTION, needUpdateQuestions);
		}

		// 更新平均难度，只计算通过的题目
		double avgDifficulty = 0;
		List<ExamPaperQuestion> epqList = examPaperQuestionManage.getExamQuestionByExam(examId);
		List<Long> qIds = Lists.newArrayList();
		for (ExamPaperQuestion question : epqList) {
			qIds.add(question.getQuestionId());
		}
		Map<Long, Question> qExistMap = resconQuestionManage.mget(qIds);
		if (qExistMap.size() > 0) {
			for (Question question : qExistMap.values()) {
				if (question.getStatus() == CheckStatus.PASS) {
					avgDifficulty += question.getDifficulty();
				}
			}
			avgDifficulty = avgDifficulty / qExistMap.size();
		}
		BigDecimal avgDiff = new BigDecimal(avgDifficulty).setScale(2, RoundingMode.HALF_UP);
		resconExamManage.updateExamAvgDifficulty(examId, avgDiff);

		// 记录操作
		examHistoryManage.save(Security.getUserId(), examId, OperateType.PUBLISH);
		indexService.update(IndexType.EXAM_PAPER, examId);
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		examCounterProvider.incrPublishedExams(vendorId, 1);
		examCounterProvider.incrNoCheckExams(vendorId, -1);
		return new Value();
	}

	@RequestMapping(value = "editExamTopic", method = {RequestMethod.POST, RequestMethod.GET})
	public Value editExamTopic(@RequestParam(value = "examId", required = true) Long examId,
			@RequestParam(value = "examTopicId", required = true) Long examTopicId,
			@RequestParam(value = "examTopicName", required = false) String examTopicName,
			@RequestParam(value = "score", required = false) Integer score) {
		examPaperTopicManage.edit(examTopicId, examTopicName, score);
		// 记录操作
		examHistoryManage.save(Security.getUserId(), examId, OperateType.EDIT);
		// 更新操作时间
		resconExamManage.updateExamUpdateAt(examId, Security.getUserId());
		return new Value();
	}

	@RequestMapping(value = "setQuestionScore", method = {RequestMethod.POST, RequestMethod.GET})
	public Value setQuestionScore(@RequestParam(value = "examId", required = true) Long examId,
			@RequestParam(value = "questionId", required = true) Long questionId,
			@RequestParam(value = "score", required = true) Integer score) {
		examPaperQuestionManage.editScore(examId, questionId, score);
		// 记录操作
		examHistoryManage.save(Security.getUserId(), examId, OperateType.EDIT);

		// 更新操作时间
		resconExamManage.updateExamUpdateAt(examId, Security.getUserId());

		return new Value();
	}

	/**
	 * 查看试卷
	 * 
	 * @param examId
	 * @return
	 */
	@RequestMapping(value = "viewExam", method = {RequestMethod.POST, RequestMethod.GET})
	public Value viewExam(@RequestParam(value = "examId") long examId) {
		VExam exam = examConvert.to(resconExamManage.get(examId), new ResconExamOption(true, true, true));
		List<ExamPaperQuestion> epqList = examPaperQuestionManage.getExamQuestionByExam(examId);
		List<Long> qIds = Lists.newArrayList();
		for (ExamPaperQuestion question : epqList) {
			qIds.add(question.getQuestionId());
		}
		Map<Long, VQuestion> vqMap = resconQuestionConvert.to(resconQuestionManage.mget(qIds));
		if (CollectionUtils.isNotEmpty(exam.getTopics())) {
			int questionCount = 0;
			int totalScore = 0;
			// 构造题型中包含题目的结构
			for (VExamPaperTopic examTopic : exam.getTopics()) {
				List<VQuestion> vqListTemp = Lists.newArrayList();
				for (ExamPaperQuestion examPaperQuestion : epqList) {
					if (examTopic.getExamId().equals(examPaperQuestion.getExamPaperId())
							&& examTopic.getId().equals(examPaperQuestion.getTopicId())) {
						if (examPaperQuestion.getScore() != null) {
							totalScore = totalScore + examPaperQuestion.getScore();
						}
						vqMap.get(examPaperQuestion.getQuestionId())
								.setScore(examPaperQuestion.getScore() == null ? null : examPaperQuestion.getScore());
						vqListTemp.add(vqMap.get(examPaperQuestion.getQuestionId()));
					}
				}
				examTopic.setQuestionList(vqListTemp);
				questionCount = questionCount + vqListTemp.size();
			}
			exam.setQuestionCount(questionCount);
			exam.setScore(totalScore);
		} else {
			exam.setTopics(new ArrayList<VExamPaperTopic>());
			exam.setQuestionCount(0);
			exam.setScore(0);
		}
		return new Value(exam);
	}

	/**
	 * 试卷查询接口
	 * 
	 * @param queryForm
	 *            查询信息
	 * @return
	 */
	@RequestMapping(value = "queryExam", method = {RequestMethod.GET, RequestMethod.POST})
	public Value queryExam(QueryForm queryForm) {
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		queryForm.setVendorId(vendorId);
		Page<ExamPaper> page = resconExamManage.queryResconExam(queryForm);
		VPage<VExam> vpage = new VPage<VExam>();
		vpage.setPageSize(queryForm.getPageSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(examConvert.to(page.getItems()));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(queryForm.getPage());
		return new Value(vpage);
	}

	/**
	 * 获取试卷计数相关数据
	 * 
	 * @param queryForm
	 *            查询信息
	 * @return
	 */
	@RequestMapping(value = "getExamCounts", method = {RequestMethod.GET, RequestMethod.POST})
	public Value getExamCounts() {
		Map<String, Long> data = Maps.newHashMap();
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		data.put("totalCount", examCounterProvider.getExams(vendorId));
		data.put("publishCount", examCounterProvider.getPublishedExams(vendorId));
		data.put("noCheckCount", examCounterProvider.getNoCheckExams(vendorId));
		data.put("editingCount", examCounterProvider.getEditingExams(vendorId));
		return new Value(data);
	}

	/**
	 * 获取供应商成员列表
	 * 
	 * @param queryForm
	 *            查询信息
	 * @return
	 */
	@RequestMapping(value = "getVendorUsers", method = {RequestMethod.GET, RequestMethod.POST})
	public Value getVendorUsers() {
		Map<String, Object> data = Maps.newHashMap();
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		data.put("vendors", vendorUserManage.listVendorUsers(vendorId,
				Lists.newArrayList(UserType.VENDOR_BUILD, UserType.VENDOR_HEAD, UserType.VENDOR_ADMIN)));
		return new Value(data);
	}

	/**
	 * 保存试卷.
	 * 
	 * @since yoomath v3.2.3 处理平均难度
	 * 
	 * @param exam
	 *            保存试卷参数
	 * @return
	 */
	@RequestMapping(value = "save", method = {RequestMethod.POST})
	public Value saveEditExam(String exam) {
		VExam exam1 = JSONObject.parseObject(exam, VExam.class);
		List<VExamPaperTopic> topicList = exam1.getTopics();
		List<ExamPaperTopic> eptList = examPaperTopicManage.save(topicList, exam1.getId());
		for (ExamPaperTopic examPaperTopic : eptList) {
			for (VExamPaperTopic topic : topicList) {
				if (examPaperTopic.getExamPaperId().equals(topic.getExamId())
						&& examPaperTopic.getType().equals(topic.getType())) {
					topic.setId(examPaperTopic.getId());
				}
			}
		}
		// 更新试卷题目索引使用
		Set<Long> questionIds = new HashSet<Long>();
		Set<Long> hasIds = new HashSet<Long>();
		List<ExamPaperQuestion> oldExamQuestions = examPaperQuestionManage.getExamQuestionByExam(exam1.getId());
		for (ExamPaperQuestion examPaperQuestion : oldExamQuestions) {
			questionIds.add(examPaperQuestion.getQuestionId());
		}
		for (VExamPaperTopic vExamPaperTopic : topicList) {
			for (VQuestion question : vExamPaperTopic.getQuestionList()) {
				questionIds.add(question.getId());
				hasIds.add(question.getId());
			}
		}

		examPaperQuestionManage.save(topicList, exam1.getId(), Security.getUserId());
		examHistoryManage.save(Security.getUserId(), exam1.getId(), OperateType.EDIT);

		// 更新操作时间
		resconExamManage.updateExamUpdateAt(exam1.getId(), Security.getUserId());

		// 更新索引
		if (questionIds.size() > 0) {
			indexService.syncUpdate(IndexType.QUESTION, questionIds);
		}
		indexService.syncUpdate(IndexType.EXAM_PAPER, exam1.getId());
		return new Value();
	}

	private Map<Long, VExamPaperTopic> initQuestionTopic(List<Long> qIds) {
		Map<Long, VExamPaperTopic> vExamPaperTopicMap = Maps.newHashMap();
		Map<Long, VQuestion> vqMap = resconQuestionConvert.to(resconQuestionManage.mget(qIds));
		for (Long qId : qIds) {
			VQuestion vq = vqMap.get(qId);
			VExamPaperTopic vep = new VExamPaperTopic();
			if (vq.getType() == Type.TRUE_OR_FALSE) {
				vep.setType(ExamPaperTopicType.TRUE_OR_FALSE);
				vep.setName("判断题");
				vep.setSequence(4);
			}
			if (vq.getType() == Type.FILL_BLANK) {
				vep.setType(ExamPaperTopicType.FILL_BLANK);
				vep.setName("填空题");
				vep.setSequence(3);
			}
			if (vq.getType() == Type.SINGLE_CHOICE) {
				vep.setType(ExamPaperTopicType.SINGLE_CHOICE);
				vep.setName("单选");
				vep.setSequence(1);
			}
			if (vq.getType() == Type.MULTIPLE_CHOICE) {
				vep.setType(ExamPaperTopicType.MULTIPLE_CHOICE);
				vep.setName("多选题");
				vep.setSequence(2);
			}
			if (vq.getType() == Type.COMPOSITE || vq.getType() == Type.QUESTION_ANSWERING) {
				vep.setType(ExamPaperTopicType.QUESTION_ANSWERING);
				vep.setName("解答题");
				vep.setSequence(5);
			}
			if (!vExamPaperTopicMap.values().contains(vep)) {
				vExamPaperTopicMap.put(qId, vep);
			}
		}
		return vExamPaperTopicMap;
	}
}
