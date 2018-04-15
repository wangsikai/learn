package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseType;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.PageNotFoundException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.ExerciseQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkQuestionConvert;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.wx.api.ZyWXMessageService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyExerciseService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyTextbookExerciseService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyTextbookExerciseConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkPage;
import com.lanking.uxb.service.zuoye.value.VStuWrong;

/**
 * 悠作业老师作业管理相关接口
 * 
 * @since yoomathV1.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/hk")
public class ZyTeaHomework2Controller {

	@Autowired
	private ZyHomeworkService zyHKService;
	@Autowired
	private ZyTextbookExerciseService tbeService;
	@Autowired
	private ZyExerciseService zyExerciseService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private MetaKnowSectionService metaKnowpointSectionService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private ZyTextbookExerciseConvert tbeConvert;
	@Autowired
	private ZyHomeworkStatisticService zyHkStatService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ZyHomeworkQuestionService zyHqService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private HomeworkConvert hkConvert;
	@Autowired
	private TextbookService textBookService;
	@Autowired
	private TextbookConvert textBookConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ExerciseQuestionService eqService;
	@Autowired
	private ZyBookService zyBookService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private ZyHomeworkStatisticService hkStatisticService;
	@Autowired
	private ZyBookService bookService;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private ZyStudentHomeworkQuestionService shkService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private ZyWXMessageService zyWXMessageService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private StudentHomeworkQuestionConvert stuHkQuestionConvert;

	/**
	 * 通过预置习题ID拉取题目列表以及其他信息
	 * 
	 * @since yoomath V1.4
	 * @since yoomath V2.1.2 新旧知识点更换 wanlong.che 2016-11-22
	 * 
	 * @param version
	 *            应用
	 * @param textbookExerciseId
	 *            预置习题ID
	 * @param sectionCode
	 *            章节代码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/listQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listQuestions2(long textbookExerciseId,
			@RequestParam(value = "version", defaultValue = "1.2") String version, Long sectionCode) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		// 代码移动，先获取章节知识点方便后续生成题目
		List<Integer> codes = null;
		List<Long> knowledgeCodes = null;
		if (sectionCode != null) {
			Section section = sectionService.get(sectionCode);
			if ("本章综合与测试".equals(section.getName())) {
				codes = metaKnowpointSectionService.getKnowCodesByCode(section.getPcode());
				knowledgeCodes = knowledgeSectionService.getBySection(section.getPcode());
			} else {
				codes = metaKnowpointSectionService.findBySectionCode(sectionCode);
				knowledgeCodes = knowledgeSectionService.findBySectionCode(sectionCode);
			}
		}
		List<VMetaKnowpoint> metaKnowpoints = null;
		if (CollectionUtils.isNotEmpty(codes)) {
			List<MetaKnowpoint> knowpoints = metaKnowpointService.mgetList(codes);
			if (CollectionUtils.isNotEmpty(knowpoints)) {
				metaKnowpoints = metaKnowpointConvert.to(knowpoints);
			}
		}
		if (metaKnowpoints == null) {
			data.put("metaKnowpoints", Collections.EMPTY_LIST);
		} else {
			data.put("metaKnowpoints", metaKnowpoints);
		}

		List<VKnowledgePoint> knowledgePoints = null;
		if (CollectionUtils.isNotEmpty(knowledgeCodes)) {
			List<KnowledgePoint> knowpoints = knowledgePointService.mgetList(knowledgeCodes);
			if (CollectionUtils.isNotEmpty(knowpoints)) {
				knowledgePoints = knowledgePointConvert.to(knowpoints);
			}
		}
		if (knowledgePoints == null) {
			data.put("knowledgePoints", Collections.EMPTY_LIST);
		} else {
			data.put("knowledgePoints", knowledgePoints);
		}

		if (textbookExerciseId != 0) {
			// 返回题目
			Exercise exercise = zyExerciseService.findLatestOne(Security.getUserId(), textbookExerciseId, sectionCode,
					null);
			List<Long> qIds = zyExerciseService.listQuestions(Security.getUserId(), textbookExerciseId,
					exercise == null ? null : exercise.getId());
			if (qIds.size() == 0) {
				data.put("questions", Collections.EMPTY_LIST);
			} else {
				Map<Long, Question> questions = questionService.mget(qIds);
				// @since 教师端v1.3.0
				QuestionConvertOption option = new QuestionConvertOption(true, true, true, true, true, null);
				option.setInitPublishCount(true);
				option.setInitQuestionSimilarCount(true);
				option.setInitExamination(true);
				Map<Long, VQuestion> vquestions = questionConvert.to(questions, option);
				List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
				for (Long qId : qIds) {
					VQuestion vq = vquestions.get(qId);
					if ("1.2".equals(version)) {// 1.2:填空题
						if (vq.getType() == Type.FILL_BLANK) {
							qs.add(vq);
						}
					} else if ("1.3".equals(version)) {// 1.3:单选、多选、填空题、判断、简答
						if (vq.getType() == Type.SINGLE_CHOICE || vq.getType() == Type.MULTIPLE_CHOICE
								|| vq.getType() == Type.TRUE_OR_FALSE || vq.getType() == Type.FILL_BLANK
								|| vq.getType() == Type.QUESTION_ANSWERING) {
							qs.add(vq);
						}
					}
				}
				data.put("questions", qs);
			}
			// 返回作业参数
			if (exercise != null) {
				Homework h = zyHKService.getLatextHomeworkByExerciseId(Security.getUserId(), exercise.getId());
				if (h != null && CollectionUtils.isNotEmpty(h.getMetaKnowpoints())) {
					List<Integer> kpCodes = new ArrayList<Integer>(h.getMetaKnowpoints().size());
					for (Long code : h.getMetaKnowpoints()) {
						kpCodes.add(code.intValue());
					}
					List<MetaKnowpoint> knowpoints = metaKnowpointService.mgetList(kpCodes);
					if (CollectionUtils.isNotEmpty(knowpoints)) {
						data.put("pMetaKnowpoints", metaKnowpointConvert.to(knowpoints));
					}
				}
				if (h != null && CollectionUtils.isNotEmpty(h.getKnowledgePoints())) {
					List<Long> kpCodes = new ArrayList<Long>(h.getKnowledgePoints().size());
					for (Long code : h.getKnowledgePoints()) {
						kpCodes.add(code);
					}
					List<KnowledgePoint> knowpoints = knowledgePointService.mgetList(kpCodes);
					if (CollectionUtils.isNotEmpty(knowpoints)) {
						data.put("pNewMetaKnowpoints", knowledgePointConvert.to(knowpoints));
					}
				} else {
					data.put("pNewMetaKnowpoints", Lists.newArrayList());
				}
			}
		} else {
			// 生成textbookexercise
			PullQuestionForm form = new PullQuestionForm();
			form.setCount(10);
			List<Long> kpCodeList = Lists.newArrayList();
			List<Long> kpNewCodeList = Lists.newArrayList();
			// if (CollectionUtils.isEmpty(codes)) {
			if (CollectionUtils.isEmpty(knowledgePoints) && CollectionUtils.isEmpty(codes)) {
				data.put("questions", Collections.EMPTY_LIST);
				return new Value(data);
			}
			for (Integer code : codes) {
				kpCodeList.add(Long.valueOf(code.toString()));
			}
			for (VKnowledgePoint vKnowledgePoint : knowledgePoints) {
				kpNewCodeList.add(vKnowledgePoint.getCode());
			}
			form.setMetaKnowpoints(kpCodeList);
			form.setKnowledgePoints(kpNewCodeList);
			form.setSectionCode(sectionCode);
			form.setType(PullQuestionType.TEXTBOOK_EXERCISE);
			List<Long> qIds = pullQuestionService.pull(form);
			TextbookExercise exercise = null;
			// 返回题目
			if (qIds.size() == 0) {
				data.put("questions", Collections.EMPTY_LIST);
			} else {
				Section section = sectionService.get(form.getSectionCode());
				exercise = tbeService.create(section.getTextbookCode(), form.getSectionCode(), Security.getUserId(),
						section.getName(), true, qIds);
				textbookExerciseId = exercise.getId();
				data.put("textbookExerciseId", textbookExerciseId);
				Map<Long, Question> questions = questionService.mget(qIds);
				// @since 教师端v1.3.0
				QuestionConvertOption option = new QuestionConvertOption(true, true, true, true, null);
				option.setInitPublishCount(true);
				option.setInitQuestionSimilarCount(true);
				option.setInitExamination(true);
				Map<Long, VQuestion> vquestions = questionConvert.to(questions, option);
				List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
				for (Long qId : qIds) {
					VQuestion vq = vquestions.get(qId);
					if (vq == null) {
						continue;
					}
					if ("1.2".equals(version)) {// 1.2:填空题
						if (vq.getType() == Type.FILL_BLANK) {
							qs.add(vq);
						}
					} else if ("1.3".equals(version)) {// 1.3:单选、多选、填空题、判断
						if (vq.getType() == Type.SINGLE_CHOICE || vq.getType() == Type.MULTIPLE_CHOICE
								|| vq.getType() == Type.TRUE_OR_FALSE || vq.getType() == Type.FILL_BLANK) {
							qs.add(vq);
						}
					}
				}
				data.put("questions", qs);
			}
			// 返回作业参数
			if (exercise != null) {
				Homework h = zyHKService.getLatextHomeworkByExerciseId(Security.getUserId(), exercise.getId());
				// if (h != null &&
				// CollectionUtils.isNotEmpty(h.getMetaKnowpoints())) {
				// List<Integer> kpCodes = new
				// ArrayList<Integer>(h.getMetaKnowpoints().size());
				// for (Long code : h.getMetaKnowpoints()) {
				// kpCodes.add(code.intValue());
				// }
				// List<MetaKnowpoint> knowpoints =
				// metaKnowpointService.mgetList(kpCodes);
				// if (CollectionUtils.isNotEmpty(knowpoints)) {
				// data.put("pMetaKnowpoints",
				// metaKnowpointConvert.to(knowpoints));
				// }
				// }
				if (h != null && CollectionUtils.isNotEmpty(h.getKnowledgePoints())) {
					List<Long> kpCodes = new ArrayList<Long>(h.getKnowledgePoints().size());
					for (Long code : h.getKnowledgePoints()) {
						kpCodes.add(code);
					}
					List<KnowledgePoint> knowpoints = knowledgePointService.mgetList(kpCodes);
					if (CollectionUtils.isNotEmpty(knowpoints)) {
						data.put("pMetaKnowpoints", knowledgePointConvert.to(knowpoints));
					}
				}
			}
		}
		// 返回此章节下面的知识点
		if (sectionCode == null && textbookExerciseId != 0) {
			TextbookExercise tbe = tbeService.get(textbookExerciseId);
			sectionCode = tbe.getSectionCode();
		}
		// 返回此章节下面的练习列表
		if (sectionCode == null) {
			data.put("tbeList", Collections.EMPTY_LIST);
		} else {
			data.put("tbeList",
					tbeConvert.to(tbeService.getTbeListBySectioCode(sectionCode, TextbookExerciseType.TEACHING_COACH)));
		}
		return new Value(data);
	}

	/**
	 * 通过预置习题ID拉取题目列表以及其他信息
	 * 
	 * @since yoomath V1.6
	 * @param textbookExerciseId
	 *            预置习题ID
	 * @param sectionCode
	 *            章节代码
	 * @param isBook
	 *            是否是书本章节(true|false)
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "3/listQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listQuestions3(long textbookExerciseId, Long sectionCode,
			@RequestParam(value = "isBook", defaultValue = "false") boolean isBook) {
		if (isBook) {
			Map<String, Object> data = new HashMap<String, Object>(1);
			// 返回题目
			List<Long> qIds = zyBookService.listQuestions(sectionCode);
			if (qIds.size() == 0) {
				data.put("questions", Collections.EMPTY_LIST);
			} else {
				Map<Long, Question> questions = questionService.mget(qIds);
				Map<Long, VQuestion> vquestions = questionConvert.to(questions,
						new QuestionConvertOption(false, true, true, true, null));
				List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
				for (Long qId : qIds) {
					VQuestion vq = vquestions.get(qId);
					if (vq.getType() == Type.SINGLE_CHOICE || vq.getType() == Type.MULTIPLE_CHOICE
							|| vq.getType() == Type.TRUE_OR_FALSE || vq.getType() == Type.FILL_BLANK) {
						qs.add(vq);
					}
				}
				data.put("questions", qs);
			}
			return new Value(data);
		} else {
			return listQuestions2(textbookExerciseId, "1.3", sectionCode);
		}
	}

	/**
	 * 通过预置习题ID拉取题目列表以及其他信息(支持简答题的返回)
	 * 
	 * @since yoomath V1.9.1
	 * @since sprint74 2017-09-02 书本返回的题目单独处理重复题
	 * 
	 * @param textbookExerciseId
	 *            预置习题ID
	 * @param sectionCode
	 *            章节代码
	 * @param isBook
	 *            是否是书本章节(true|false)
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "4/listQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listQuestions4(long textbookExerciseId, Long sectionCode,
			@RequestParam(value = "isBook", defaultValue = "false") boolean isBook) {
		if (isBook) {
			Map<String, Object> data = new HashMap<String, Object>(1);
			// 返回题目
			List<Long> qIds = zyBookService.listQuestions(sectionCode);
			if (qIds.size() == 0) {
				data.put("questions", Collections.EMPTY_LIST);
			} else {
				Map<Long, Question> questions = questionService.mget(qIds);

				// 处理重复题
				if (questions.size() > 0) {
					Set<Long> showIds = new HashSet<Long>();
					for (Question question : questions.values()) {
						if (question.getSameShowId() != null && question.getSameShowId() > 0) {
							showIds.add(question.getSameShowId());
						}
					}
					if (showIds.size() > 0) {
						questions.putAll(questionService.mget(showIds));
					}
				}

				Map<Long, VQuestion> vquestions = questionConvert.to(questions,
						new QuestionConvertOption(false, true, true, true, true, null));
				List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
				Set<Long> returnIds = new HashSet<Long>();
				for (Long qId : qIds) {
					Question question = questions.get(qId);
					VQuestion vq = null;
					// 重复题处理
					if (question.getSameShowId() != null && question.getSameShowId() > 0) {
						vq = vquestions.get(question.getSameShowId());
					} else {
						vq = vquestions.get(qId);
					}
					if (vq.getType() == Type.SINGLE_CHOICE || vq.getType() == Type.MULTIPLE_CHOICE
							|| vq.getType() == Type.TRUE_OR_FALSE || vq.getType() == Type.FILL_BLANK
							|| vq.getType() == Type.QUESTION_ANSWERING) {
						if (!returnIds.contains(vq.getId())) {
							returnIds.add(vq.getId());
							qs.add(vq);
						}
					}
				}
				data.put("questions", qs);
			}
			return new Value(data);
		} else {
			return listQuestions2(textbookExerciseId, "1.3", sectionCode);
		}
	}

	/**
	 * 通过预置习题ID拉取题目列表
	 * 
	 * @since yoomath V1.4
	 * @param textbookExerciseId
	 *            预置习题ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "exerciseListQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value exerciseListQuestions(long textbookExerciseId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<Long> qIds = zyExerciseService.listQuestions(Security.getUserId(), textbookExerciseId, null);
		if (qIds.size() == 0) {
			data.put("questions", Collections.EMPTY_LIST);
		} else {
			Map<Long, Question> questions = questionService.mget(qIds);
			Map<Long, VQuestion> vquestions = questionConvert.to(questions,
					new QuestionConvertOption(true, true, true, true, null));
			List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
			for (Long qId : qIds) {
				VQuestion vq = vquestions.get(qId);
				if (vq.getType() == Type.SINGLE_CHOICE || vq.getType() == Type.MULTIPLE_CHOICE
						|| vq.getType() == Type.TRUE_OR_FALSE || vq.getType() == Type.FILL_BLANK) {
					qs.add(vq);
				}
			}
			data.put("questions", qs);
		}
		// 返回此章节下面的知识点
		TextbookExercise tbe = tbeService.get(textbookExerciseId);
		List<Integer> codes = metaKnowpointSectionService.findBySectionCode(tbe.getSectionCode());
		List<VMetaKnowpoint> metaKnowpoints = null;
		if (CollectionUtils.isNotEmpty(codes)) {
			List<MetaKnowpoint> knowpoints = metaKnowpointService.mgetList(codes);
			if (CollectionUtils.isNotEmpty(knowpoints)) {
				metaKnowpoints = metaKnowpointConvert.to(knowpoints);
			}
		}
		if (metaKnowpoints == null) {
			data.put("metaKnowpoints", Collections.EMPTY_LIST);
		} else {
			data.put("metaKnowpoints", metaKnowpoints);
		}

		// 新知识点
		List<VKnowledgePoint> knowledgePoints = null;
		List<Long> knowledgeCodes = knowledgeSectionService.findBySectionCode(tbe.getSectionCode());
		if (CollectionUtils.isNotEmpty(knowledgeCodes)) {
			List<KnowledgePoint> knowpoints = knowledgePointService.mgetList(knowledgeCodes);
			if (CollectionUtils.isNotEmpty(knowpoints)) {
				knowledgePoints = knowledgePointConvert.to(knowpoints);
			}
		}
		if (knowledgePoints == null) {
			data.put("knowledgePoints", Collections.EMPTY_LIST);
		} else {
			data.put("knowledgePoints", knowledgePoints);
		}

		return new Value(data);
	}

	/**
	 * 删除作业
	 * 
	 * @since yoomath V1.4
	 * @param id
	 *            作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(String password, long id) {
		// check password
		Account account = accountService.getAccountByUserId(Security.getUserId());
		if (!account.getPassword().equals(Codecs.md5Hex(password))) {
			return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
		}
		int upt = zyHKService.delete(Security.getUserId(), id);
		if (upt == 0) {
			return new Value(new NoPermissionException());
		} else {
			zyHkStatService.asyncDeleteHomework(id);
		}
		return new Value();
	}

	/**
	 * 查看一次作业的某题目做错的学生列表
	 * 
	 * @since yoomath V1.4.1
	 * @param homeworkId
	 *            作业ID
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@SuppressWarnings("rawtypes")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "listWrongStu", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listWrongStu(long homeworkId, long questionId) {
		Map<String, Object> data = new HashMap<String, Object>(1);
		List<Map> resultMapLists = zyHqService.listWrongStu(homeworkId, questionId);
		List<VStuWrong> vsList = new ArrayList<VStuWrong>();
		List<Long> stuIds = new ArrayList<Long>();
		List<Long> stuHKQIds = new ArrayList<Long>();
		for (Map map : resultMapLists) {
			Long studentId = Long.parseLong(map.get("student_id").toString());
			Long studentHkQId = Long.parseLong(map.get("student_homework_question_id").toString());
			if (!stuIds.contains(studentId)) {
				stuIds.add(studentId);
			}
			if (!stuHKQIds.contains(studentId)) {
				stuHKQIds.add(studentHkQId);
			}
		}
		// key 为 stundetHKQID
		Map<Long, StudentHomeworkQuestion> shqMap = shkService.mget(stuHKQIds);
		// key 为userID
		Map<Long, StudentHomeworkQuestion> shqMap2 = Maps.newHashMap();
		for (Map map : resultMapLists) {
			shqMap2.put(Long.parseLong(map.get("student_id").toString()),
					shqMap.get(Long.parseLong(map.get("student_homework_question_id").toString())));
		}
		Map<Long, VUser> userMap = userConvert.mget(stuIds);
		List<StudentHomeworkQuestion> skList = new ArrayList<StudentHomeworkQuestion>();
		// 错误答案计算 key值为错题答案内容，value值为该相同错误答案的个数
		Map<String, Integer> wrongAnswerCounter = new HashMap<String, Integer>();
		for (Long key : userMap.keySet()) {
			VStuWrong vs = new VStuWrong();
			List<VStudentHomeworkAnswer> vsTemp = new ArrayList<VStudentHomeworkAnswer>();
			String name = userMap.get(key).getName();
			for (Map userQesMap : resultMapLists) {
				Long studentId = Long.parseLong(userQesMap.get("student_id").toString());
				VStudentHomeworkAnswer vsa = new VStudentHomeworkAnswer();
				if (studentId.equals(key)) {
					if (userQesMap.get("content_ascii") != null) {
						vsa.setContentAscii((userQesMap.get("content_ascii").toString()));
					} else {
						vsa.setContentAscii("");
					}
					if (Integer.valueOf(userQesMap.get("result").toString()) == 1) {
						vsa.setResult(HomeworkAnswerResult.RIGHT);
					} else if (Integer.valueOf(userQesMap.get("result").toString()) == 2) {
						vsa.setResult(HomeworkAnswerResult.WRONG);
						String answerContent = vsa.getContentAscii();
						if (wrongAnswerCounter.get(answerContent) == null) {
							wrongAnswerCounter.put(answerContent, 1);
						} else {
							wrongAnswerCounter.put(answerContent, wrongAnswerCounter.get(answerContent) + 1);
						}
					} else if (Integer.valueOf(userQesMap.get("result").toString()) == 3) {
						vsa.setResult(HomeworkAnswerResult.UNKNOW);
					} else if (Integer.valueOf(userQesMap.get("result").toString()) == 0) {
						vsa.setResult(HomeworkAnswerResult.INIT);
					}
				}
				if (vsa.getResult() != null) {
					vsTemp.add(vsa);
				}
			}
			StudentHomeworkQuestion shk = shqMap2.get(key);
			vs.setStuHkQuestionId(shk.getId());
			vs.setSolvingImage(FileUtil.getUrl(shk.getAnswerImg()));
			vs.setNotationAnswerImage(FileUtil.getUrl(shk.getNotationAnswerImg()));
			vs.setRightRate(shk.getRightRate() == null ? 0 : shk.getRightRate());
			vs.setName(name);
			vs.setVsTemp(vsTemp);
			vsList.add(vs);
			skList.add(shk);
		}

		Map<Long, VStudentHomeworkQuestion> skMap = stuHkQuestionConvert.toMap(skList);
		for (VStuWrong s : vsList) {
			VStudentHomeworkQuestion temp = skMap.get(s.getStuHkQuestionId());
			if (temp != null) {
				s.setAnswerImgs(temp.getAnswerImgs());
				s.setNotationAnswerImgs(temp.getNotationAnswerImgs());
			}
		}
		// 按照正确率正序
		Collections.sort(vsList, new Comparator<VStuWrong>() {
			@Override
			public int compare(VStuWrong o1, VStuWrong o2) {
				if (o1.getRightRate().compareTo(o2.getRightRate()) == 1) {
					return 1;
				} else if (o1.getRightRate().compareTo(o2.getRightRate()) == -1) {
					return -1;
				}
				return 0;
			}
		});
		List<Map.Entry<String, Integer>> wrongAnswerCountterList = new ArrayList<Map.Entry<String, Integer>>();
		for (Map.Entry<String, Integer> entry : wrongAnswerCounter.entrySet()) {
			// 获取相同错题答案1个以上的
			if (entry.getValue() > 1) {
				wrongAnswerCountterList.add(entry);
			}
		}
		// 根据错题答案个数降序排列
		Collections.sort(wrongAnswerCountterList, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}

		});
		// 最多取3个
		if (wrongAnswerCountterList.size() > 3) {
			wrongAnswerCountterList = wrongAnswerCountterList.subList(0, 3);
		}
		data.put("data", vsList);
		// 重复错题答案统计
		data.put("wrongAnswerStatic", wrongAnswerCountterList);
		return new Value(data);
	}

	/**
	 * 作业历史搜索
	 * 
	 * @since 1.5
	 * @since v2.1.2 添加新知识点 wanlong.che 2016-11-23
	 * @param sectionCode
	 *            章节code
	 * @param pageNo
	 *            页码
	 * @param size
	 *            每页数量
	 * @param sectionNameKey
	 *            搜索关键词
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param textBookCode
	 *            教材code
	 * @param isBook
	 *            是否是从书本章节历史作业查询的
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/query_history", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHistory2(@RequestParam(value = "sectionCode", required = false) Long sectionCode,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "40") int size,
			@RequestParam(value = "sectionNameKey", required = false) String sectionNameKey,
			@RequestParam(value = "beginTime", required = false) Long beginTime,
			@RequestParam(value = "endTime", required = false) Long endTime,
			@RequestParam(value = "textBookCode", required = false) Long textBookCode,
			@RequestParam(value = "classId", required = false) Long classId,
			@RequestParam(value = "isBook", required = false) boolean isBook) {
		Map<String, Object> data = Maps.newHashMap();
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		size = Math.min(size, 40);
		if (beginTime != null && endTime != null) {
			if (beginTime.compareTo(endTime) != -1) {
				return new Value(new IllegalArgException());
			}
		}
		Date beginDate = null;
		if (beginTime != null) {
			beginDate = new Date(beginTime);
		}
		Date endDate = null;
		if (endTime != null) {
			endDate = new Date(endTime);
		}
		VPage<VHomework> vpage = new VHomeworkPage();
		// 如果是开始结束时间为空，说明第一次进来，按照教师本身的textbookcode查找
		if (textBookCode == null && (beginTime == null || endTime == null) && sectionCode == null) {
			textBookCode = Long.valueOf(teacher.getTextbookCode().toString());
		}
		Long textBookCategoryCode = null;
		// 选择全部教材的时候，默认搜索用户所在版本的所有教材
		if (textBookCode == null && sectionCode == null) {
			textBookCategoryCode = Long.valueOf(teacher.getTextbookCategoryCode().toString());
		}
		// 章节CODE 包含sectionCode信息
		if (sectionCode != null) {
			textBookCode = null;
		}
		Page<Homework> hkPage = zyHKService.queryHistoryHomework(Security.getUserId(), sectionCode, sectionNameKey,
				beginDate, endDate, textBookCode, textBookCategoryCode, classId, true, P.index(pageNo, size));
		List<VHomework> vhkList = hkConvert.to(hkPage.getItems(), new HomeworkConvertOption(true));
		if (CollectionUtils.isNotEmpty(vhkList)) {
			List<Long> classIds = new ArrayList<Long>(vhkList.size());
			for (VHomework v : vhkList) {
				classIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = zyHkClassConvert.to(zyHkClassService.mget(classIds));
			for (VHomework v : vhkList) {
				v.setHomeworkClazz(vmap.get(v.getHomeworkClazzId()));
			}
		}
		vpage.setCurrentPage(pageNo);
		vpage.setItems(vhkList);
		vpage.setPageSize(size);
		vpage.setTotal(hkPage.getTotalCount());
		vpage.setTotalPage(hkPage.getPageCount());
		data.put("page", vpage);

		if (sectionCode == null) {
			data.put("key", sectionNameKey);
		} else {
			if (isBook) {
				BookCatalog bookCatalog = bookService.getBookCatalog(sectionCode);
				data.put("key", bookCatalog.getName());
				// data.put("textbook",
				// bookService.getBookVersion(bookCatalog.getBookVersionId()).getTextbookCode());
			} else {
				Section section = sectionService.get(sectionCode);
				data.put("key", section.getName());
				// data.put("textbook", section.getTextbookCode());
			}
		}
		data.put("textbook", teacher.getTextbookCode());
		if (hkPage.getTotalCount() > 0) {
			if (sectionCode != null) {
				textBookCode = null;
			}
			Date createAt = zyHKService.queryHistoryHomework(Security.getUserId(), sectionCode, sectionNameKey,
					beginDate, endDate, textBookCode, null, classId, false, P.index(pageNo, size)).getItem(0)
					.getCreateAt();
			data.put("lateCreateAt", createAt);
		} else {
			// 如果没有布置过作业，开始时间推迟到当前时间一年 said BY 宋娟
			Calendar calendar = Calendar.getInstance();
			Date date = new Date(System.currentTimeMillis());
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, -1);
			data.put("lateCreateAt", calendar.getTime());
		}

		List<VTextbookCategory> categories = tbcConvert.to(tbcService.find(Product.YOOMATH, teacher.getPhaseCode()));
		data.put("textbookCategories", categories);
		List<Integer> categoryCodes = new ArrayList<Integer>(categories.size());
		for (VTextbookCategory v : categories) {
			categoryCodes.add(v.getCode());
		}
		List<VTextbook> textbooks = textBookConvert.to(textBookService.find(Product.YOOMATH, teacher.getPhaseCode(),
				teacher.getSubjectCode(), Lists.newArrayList(teacher.getTextbookCategoryCode())));
		// 版本默认 苏教版。
		data.put("textbooks", textbooks);
		data.put("classes", zyHkClassConvert.to(zyHkClassService.listCurrentClazzs(Security.getUserId())));
		return new Value(data);
	}

	/**
	 * 获取习题页下面所有习题ID
	 * 
	 * @since 1.5
	 * @param exerciseId
	 *            习题页ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryQuestionIds", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryQuestionIds(@RequestParam(value = "exerciseId") Long exerciseId) {
		return new Value(eqService.getQuestion(exerciseId));
	}

	/**
	 * 下发作业(提前下发)（废弃）
	 * 
	 * @since yoomath V1.6
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 * 
	 * @param hkId
	 *            作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "2/issue", method = { RequestMethod.POST, RequestMethod.GET })
	@Deprecated
	public Value issue(long hkId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		try {
			Homework homework = hkService.get(hkId);
			if (homework.getCreateId() != Security.getUserId()) {
				throw new PageNotFoundException();
			}
			if (homework.getStatus() == HomeworkStatus.INIT) {
				throw new PageNotFoundException();
			}
			// 用于判断，当老师很早打开查看学生作业页面，到自动下发时间点下发按钮的时候，返回
			if (homework.getStatus() == HomeworkStatus.ISSUED) {
				return new Value(new HomeworkException(HomeworkException.HOMEWORK_HAS_ISSUE));
			}
			zyHKService.issue(homework, true);
			CoinsLog coinLog = coinsService.earn(CoinsAction.TEA_HOMEOWORK_RESULT, Security.getUserId(), 0,
					Biz.HOMEWORK, hkId);
			// 处理相关统计
			hkStatisticService.staticAfterIssue(hkId);
			// 给学生发送推送消息
			mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
					MQ.builder().data(new PushHandleForm(PushHandleAction.ISSUED_HOMEWORK, hkId)).build());
			// 微信消息
			zyWXMessageService.sendIssuedHomeworkMessage(hkId);
			// 统计相关成长值和金币
			GrowthLog growthLog = growthService.grow(GrowthAction.ISSUE_HOMEWORK, Security.getUserId(), -1,
					Biz.HOMEWORK, hkId, true);
			CoinsLog coinsLog = coinsService.earn(CoinsAction.ISSUE_HOMEWORK, Security.getUserId(), -1, Biz.HOMEWORK,
					hkId);
			if (growthLog.getHonor() != null) {
				data.put("userReward",
						new VUserReward(growthLog.getHonor().getUpRewardCoins(), growthLog.getHonor().isUpgrade(),
								growthLog.getHonor().getLevel(), growthLog.getGrowthValue(),
								coinsLog.getCoinsValue() + coinLog.getCoinsValue()));
			} else if (coinLog.getCoinsValue() > 0) {
				data.put("userReward", new VUserReward(0, false, 0, 0, coinLog.getCoinsValue()));
			}
			// 异步统计
			hkStatisticService.asyncStaticHomework(hkId);
			return new Value(data);
		} catch (HomeworkException e) {
			return new Value(e);
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 立即下发作业
	 * 
	 * @since yoomath V1.6
	 * @param hkId
	 *            分发作业
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "distribute", method = { RequestMethod.POST, RequestMethod.GET })
	public Value distribute(long hkId) {
		Homework hk = hkService.get(hkId);
		if (hk.getStartTime().getTime() > System.currentTimeMillis()) {
			zyHKService.setStartTimeNow(hkId, Security.getUserId());
			// 微信消息
			zyWXMessageService.sendIssuedHomeworkMessage(hkId);
		}
		return new Value();
	}
}
