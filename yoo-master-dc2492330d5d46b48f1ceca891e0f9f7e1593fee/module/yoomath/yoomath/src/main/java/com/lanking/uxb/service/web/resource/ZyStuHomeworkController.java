package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.homework.AppealType;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.common.convert.VStudentHomeworkConvert;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassKnowpointConvert;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomework;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.QuestionAppealService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkAnswerConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkQuestionConvert;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.web.form.HomeworkQueryForm;
import com.lanking.uxb.service.zuoye.api.ZyCorrectUserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseKnowpointService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkPage;

/**
 * 悠作业学生作业接口
 * 
 * @since yoomath V1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/hk")
public class ZyStuHomeworkController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private StudentHomeworkService stuHomeworkService;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private HomeworkConvert hkConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyStudentHomeworkAnswerService stuHkAnswerService;
	@Autowired
	private ZyStudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ZyCorrectUserService zyCorrectUserService;
	@Autowired
	private CorrectProcessor correctProcessor;
	@Autowired
	private ZyStudentExerciseKnowpointService knowpointService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private VStudentHomeworkConvert vstudentHomeworkConvert;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private StudentHomeworkAnswerConvert shaConvert;
	@Autowired
	private StudentHomeworkAnswerService shaService;
	@Autowired
	private StudentHomeworkQuestionConvert shqConvert;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private ZyStudentFallibleQuestionService zyStudentFallibleQuestionService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private DiagnosticStudentClassKnowpointService diagStuKpService;
	@Autowired
	private DiagnosticStudentClassKnowpointConvert diagStuKpConvert;
	@Autowired
	private QuestionAppealService questionAppealService;

	/**
	 * 获取当前用户某一个班级下面的作业列表
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            作业班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "2/homeworks", method = { RequestMethod.POST, RequestMethod.GET })
	public Value homeworks2(@RequestParam(required = true) long classId, @RequestParam(required = false) Long studentId,
			@RequestParam(required = false) Set<StudentHomeworkStatus> statues) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		data.put("me", true);
		long localUserId = Security.getUserId();
		if (Security.getUserType() == UserType.STUDENT && null != studentId && localUserId != studentId) {
			// 学生不能查看其他学生的作业
			return new Value(new NoPermissionException());
		} else if (Security.getUserType() == UserType.TEACHER && null != studentId) {
			localUserId = studentId;
			UserInfo user = studentService.getUser(com.lanking.cloud.domain.yoo.user.UserType.STUDENT, localUserId);
			data.put("me", false);
			data.put("name", user.getName());
		}

		ZyStudentHomeworkQuery query = new ZyStudentHomeworkQuery();
		query.setClassId(classId);
		query.setStudentId(localUserId);
		query.setCourse(false);
		if (statues == null) {// 若没有设置此参数则只查询已经下发的作业
			query.setStatus(Sets.newHashSet(StudentHomeworkStatus.ISSUED));
		} else {
			query.setStatus(statues);
		}
		Page<StudentHomework> page = stuHkService.query(query, P.index(1, 500));
		StudentHomeworkConvertOption option = new StudentHomeworkConvertOption();
		option.setInitHomework(true);
		option.setSimpleHomework(true);
		List<VStudentHomework> vs = stuHkConvert.to(page.getItems(), option);
		data.put("studentHomeworks", vs);
		return new Value(data);
	}

	/**
	 * 查看学生作业结果<br>
	 * 新知识点
	 * 
	 * @param stuHkId
	 *            学生作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "2/view", method = { RequestMethod.POST, RequestMethod.GET })
	public Value view2(long stuHkId) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		StudentHomework sh = stuHomeworkService.get(stuHkId);
		if (sh.getStatus() == StudentHomeworkStatus.ISSUED && !sh.isViewed()) {
			stuHkService.updateViewStatus(stuHkId);
		}
		StudentHomeworkConvertOption option = new StudentHomeworkConvertOption();
		option.setStatisticTobeCorrected(true);
		option.setInitHomework(true);
		option.setInitStuHomeworkWrongAndCorrect(true);
		VStudentHomework vstudentHomework = stuHkConvert.to(sh, option);
		map.put("studentHomework", vstudentHomework);
		map.put("homework", hkConvert.to(hkService.get(sh.getHomeworkId()), new HomeworkConvertOption()));
		List<Long> qids = hqService.getQuestion(sh.getHomeworkId());
		List<Long> correctQIds = stuHkQuestionService.getNewCorrectQuestions(stuHkId);
		qids.addAll(correctQIds);
		List<Question> questions = new ArrayList<Question>();
		Map<Long, Question> qs = questionService.mget(qids);
		for (Long qid : qids) {
			Question question = qs.get(qid);
			if (question != null) {
				if (correctQIds.contains(qid)) {
					question.setCorrectQuestion(true);
				}
				questions.add(question);
			}
		}
		List<VQuestion> vqs = questionConvert.to(questions, new QuestionConvertOption(false, true, true, stuHkId));
		Map<Long, StudentFallibleQuestion> fallMap = zyStudentFallibleQuestionService.mgetQuestion(qids,
				Security.getUserId());
		for (VQuestion v : vqs) {
			if (fallMap.get(v.getId()) != null) {
				// 删除掉的就不显示
				v.setInStuFallQuestion(true);
			}
		}
		map.put("questions", vqs);

		// 弱项知识点处理
		List<VKnowledgePoint> knowledgePoints = vstudentHomework.getHomework().getKnowledgePoints();
		if (CollectionUtils.isNotEmpty(knowledgePoints)) {
			// 新知识点
			Set<Long> codes = new HashSet<Long>(knowledgePoints.size());
			for (VKnowledgePoint knowledgePoint : knowledgePoints) {
				codes.add(knowledgePoint.getCode());
			}
			List<DiagnosticStudentClassKnowpoint> list = diagStuKpService.queryHistoryWeakListByCodes(sh.getStudentId(),
					vstudentHomework.getHomework().getHomeworkClazzId(), codes);
			map.put("weakKnowpoints", diagStuKpConvert.to(list));
		} else {
			// 旧知识点
			List<VMetaKnowpoint> weakKnowpoints = new ArrayList<VMetaKnowpoint>();
			List<VMetaKnowpoint> metaKnowpoints = vstudentHomework.getHomework().getMetaKnowpoints();
			Set<Integer> codes = new HashSet<Integer>(metaKnowpoints.size());
			for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
				codes.add(metaKnowpoint.getCode());
			}
			Map<Integer, StudentExerciseKnowpoint> knowpointMap = knowpointService.mgetByCodes(codes,
					sh.getStudentId());
			for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
				StudentExerciseKnowpoint studentExerciseKnowpoint = knowpointMap.get(metaKnowpoint.getCode());
				if (studentExerciseKnowpoint != null && studentExerciseKnowpoint.getDoCount() > 0) {
					// 做过的题数量>20
					double rightRate;
					if (studentExerciseKnowpoint.getDoCount() > 20) {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / studentExerciseKnowpoint.getDoCount();
					} else {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / 20;
					}
					if (rightRate <= 50) {
						weakKnowpoints.add(metaKnowpoint);
					}
				}
			}
			map.put("weakKnowpoints", weakKnowpoints);
		}

		return new Value(map);
	}

	/**
	 * 查看学生作业结果<br>
	 * yoomath V1.4返回订正作业的题目(题目ID查询需要优化)
	 * 
	 * @since 2.1
	 * @param stuHkId
	 *            学生作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "view", method = { RequestMethod.POST, RequestMethod.GET })
	public Value view(long stuHkId) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		StudentHomework sh = stuHomeworkService.get(stuHkId);
		if (sh.getStatus() == StudentHomeworkStatus.ISSUED && !sh.isViewed()) {
			stuHkService.updateViewStatus(stuHkId);
		}
		VStudentHomework vstudentHomework = stuHkConvert.to(sh, false, true, false, false);
		map.put("studentHomework", vstudentHomework);
		map.put("homework", hkConvert.to(hkService.get(sh.getHomeworkId()), new HomeworkConvertOption()));
		List<Long> qids = hqService.getQuestion(sh.getHomeworkId());
		List<Long> correctQIds = stuHkQuestionService.getCorrectQuestions(stuHkId);
		qids.addAll(correctQIds);
		List<Question> questions = new ArrayList<Question>(qids.size());
		Map<Long, Question> qs = questionService.mget(qids);
		for (Long qid : qids) {
			Question question = qs.get(qid);
			if (correctQIds.contains(qid)) {
				question.setCorrectQuestion(true);
			}
			questions.add(question);
		}
		List<VQuestion> vqs = questionConvert.to(questions, new QuestionConvertOption(false, true, true, stuHkId));
		Map<Long, StudentFallibleQuestion> fallMap = zyStudentFallibleQuestionService.mgetQuestion(qids,
				Security.getUserId());
		for (VQuestion v : vqs) {
			if (fallMap.get(v.getId()) != null) {
				// 删除掉的就不显示
				v.setInStuFallQuestion(true);
			}
		}
		map.put("questions", vqs);

		// 弱项知识点处理
		List<VMetaKnowpoint> weakKnowpoints = new ArrayList<VMetaKnowpoint>();
		List<VMetaKnowpoint> metaKnowpoints = vstudentHomework.getHomework().getMetaKnowpoints();
		Set<Integer> codes = new HashSet<Integer>(metaKnowpoints.size());
		for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
			codes.add(metaKnowpoint.getCode());
		}
		Map<Integer, StudentExerciseKnowpoint> knowpointMap = knowpointService.mgetByCodes(codes, sh.getStudentId());
		for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
			StudentExerciseKnowpoint studentExerciseKnowpoint = knowpointMap.get(metaKnowpoint.getCode());
			if (studentExerciseKnowpoint != null && studentExerciseKnowpoint.getDoCount() > 0) {
				// 做过的题数量>20
				double rightRate;
				if (studentExerciseKnowpoint.getDoCount() > 20) {
					rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
							* 100d) / studentExerciseKnowpoint.getDoCount();
				} else {
					rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
							* 100d) / 20;
				}
				if (rightRate <= 50) {
					weakKnowpoints.add(metaKnowpoint);
				}
			}
		}
		map.put("weakKnowpoints", weakKnowpoints);

		return new Value(map);
	}

	/**
	 * 学生做作业过程中的数据获取<br>
	 * yoomath V1.4返回订正作业的题目(题目ID查询需要优化)
	 * 
	 * @since 2.1
	 * @since 2.1.2 添加新知识点 wanlong.che 2016-11-28
	 * @param stuHkId
	 *            学生作业ID
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "doing_view", method = { RequestMethod.POST, RequestMethod.GET })
	public Value doingView(Long stuHkId) {
		if (stuHkId == null) {
			return new Value(new MissingArgumentException());
		}
		StudentHomework studentHomework = stuHomeworkService.get(stuHkId);
		if (null == studentHomework) {
			return new Value(new EntityNotFoundException());
		}
		studentHomework.setInitHomework(true);
		Map<String, Object> data = new HashMap<String, Object>(2);
		VStudentHomework vstudentHomework = stuHkConvert.to(studentHomework, false, true, false, false);
		// 添加返回班级ID 用于前端页面跳转
		Long classId = hkService.get(studentHomework.getHomeworkId()).getHomeworkClassId();
		data.put("classId", classId);
		data.put("studentHomework", vstudentHomework);
		List<Long> qid = hqService.getQuestion(studentHomework.getHomeworkId());
		List<Long> correctQIds = stuHkQuestionService.getNewCorrectQuestions(stuHkId);
		qid.addAll(correctQIds);
		List<Question> qs = new ArrayList<Question>(qid.size());
		Map<Long, Question> qsMap = questionService.mget(qid);
		for (Long id : qid) {
			if (correctQIds.contains(id)) {
				qsMap.get(id).setCorrectQuestion(true);
			}
			qs.add(qsMap.get(id));
		}
		List<VQuestion> vqs = questionConvert.to(qs, new QuestionConvertOption(false, false, false, stuHkId));
		data.put("questions", vqs);
		Date d = vstudentHomework.getHomework().getDeadline();
		long deadline = d.getTime() - new Date().getTime();
		data.put("deadline", deadline < 0 ? 0 : deadline);

		// 弱项知识点处理
		List<VKnowledgePoint> knowledgePoints = vstudentHomework.getHomework().getKnowledgePoints();
		if (CollectionUtils.isNotEmpty(knowledgePoints)) {
			// 新知识点
			List<VKnowledgePoint> weakKnowledgePoints = new ArrayList<VKnowledgePoint>();
			Set<Long> codes = new HashSet<Long>(knowledgePoints.size());
			for (VKnowledgePoint knowledgePoint : knowledgePoints) {
				codes.add(knowledgePoint.getCode());
			}
			Map<Long, StudentExerciseKnowpoint> knowledgePointMap = null;
			if (codes.size() > 0) {
				knowledgePointMap = knowpointService.mgetNewByCodes(codes, studentHomework.getStudentId());
			} else {
				knowledgePointMap = Collections.EMPTY_MAP;
			}
			for (VKnowledgePoint knowledgePoint : knowledgePoints) {
				StudentExerciseKnowpoint studentExerciseKnowpoint = knowledgePointMap.get(knowledgePoint.getCode());
				if (studentExerciseKnowpoint != null && studentExerciseKnowpoint.getDoCount() > 0) {
					// 做过的题数量>20
					double rightRate;
					if (studentExerciseKnowpoint.getDoCount() > 20) {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / studentExerciseKnowpoint.getDoCount();
					} else {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / 20;
					}
					if (rightRate <= 50) {
						weakKnowledgePoints.add(knowledgePoint);
					}
				}
			}
			data.put("weakKnowledgePoints", weakKnowledgePoints);
		} else {
			// 旧知识点
			List<VMetaKnowpoint> weakKnowpoints = new ArrayList<VMetaKnowpoint>();
			List<VMetaKnowpoint> metaKnowpoints = vstudentHomework.getHomework().getMetaKnowpoints();
			Set<Integer> codes = new HashSet<Integer>(metaKnowpoints.size());
			for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
				codes.add(metaKnowpoint.getCode());
			}
			Map<Integer, StudentExerciseKnowpoint> knowpointMap = knowpointService.mgetByCodes(codes,
					studentHomework.getStudentId());
			for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
				StudentExerciseKnowpoint studentExerciseKnowpoint = knowpointMap.get(metaKnowpoint.getCode());
				if (studentExerciseKnowpoint != null && studentExerciseKnowpoint.getDoCount() > 0) {
					// 做过的题数量>20
					double rightRate;
					if (studentExerciseKnowpoint.getDoCount() > 20) {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / studentExerciseKnowpoint.getDoCount();
					} else {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / 20;
					}
					if (rightRate <= 50) {
						weakKnowpoints.add(metaKnowpoint);
					}
				}
			}
			data.put("weakKnowledgePoints", weakKnowpoints);
		}
		return new Value(data);
	}

	/**
	 * 保存做作业时主动调用(可能用不到哦)
	 * 
	 * @since 2.1
	 * @param stuHkId
	 *            学生作业ID
	 * @param homeworkTime
	 *            作业用时
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
	public Value save(long stuHkId, int homeworkTime) {
		stuHkService.updateHomeworkTime(stuHkId, Security.getUserId(), homeworkTime, null);
		return new Value();
	}

	/**
	 * 做作业
	 * 
	 * @since 2.1
	 * @param qId
	 *            题目ID
	 * @param stuHkId
	 *            学生作业ID
	 * @param answer
	 *            答题内容同悠学中StudentHomeworkController的doHomework接口<br>
	 *            (龙哥你懂的!!!O(∩_∩)O哈哈~)
	 * @param homeworkTime
	 *            作业总的用时
	 * @param updateData
	 *            是否返回此题的最新数据(通过此参数true可以更新返回此学生此条题目的最新数据)
	 * @param stuData
	 *            是否仅返回学生答题数据
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "do_one", method = { RequestMethod.POST, RequestMethod.GET })
	public Value doOneQuestion(Long qId, Long stuHkId, String answer, Long solvingImg, Integer homeworkTime,
			@RequestParam(defaultValue = "false") boolean updateData,
			@RequestParam(required = false) Double completionRate,
			@RequestParam(defaultValue = "false") boolean stuData) {
		try {
			if (stuHkId == null) {
				logger.error("[do_one] arg error, qId={}, answer={}, solvingImg={}, homeworkTime={}, user={}", qId,
						answer, solvingImg, homeworkTime, Security.getUserId());
				throw new IllegalArgException();
			}
			if (qId == null || qId <= 0) {
				throw new IllegalArgException();
			}
			StudentHomework studentHomework = stuHomeworkService.get(stuHkId);
			if (studentHomework == null || studentHomework.getStudentId().longValue() != Security.getUserId()) {
				// 不能做别人的作业
				throw new NoPermissionException();
			}
			if (studentHomework.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
				if (studentHomework.getStatus() == StudentHomeworkStatus.SUBMITED) {
					if (studentHomework.getStuSubmitAt() == null) {
						// 作业被自动提交了
						throw new ZuoyeException(ZuoyeException.ZUOYE_AUTO_COMMITED);
					} else {
						// 此作业学生已经主动提交过,不能再做此作业的题目了
						throw new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_COMMITED);
					}
				} else if (studentHomework.getStatus() == StudentHomeworkStatus.ISSUED) {
					if (studentHomework.getSubmitAt() == null && studentHomework.getStuSubmitAt() == null) {
						// 此时说明作业还没有提交作业就被下发了
						// 可以继续做作业
					} else {
						// 作业已经被下发不能答题
						throw new ZuoyeException(ZuoyeException.ZUOYE_ISSUED);
					}
				}
			}
			stuHkService.updateHomeworkTime(stuHkId, Security.getUserId(), homeworkTime, completionRate);
			Map<Long, List<String>> answerData = Maps.newHashMap();
			Map<Long, List<String>> answerAsciiData = Maps.newHashMap();
			JSONArray answerArray = JSONArray.parseArray(answer);
			for (int i = 0; i < answerArray.size(); i++) {
				JSONObject answerObject = (JSONObject) answerArray.get(i);
				List<String> answerList = Lists.newArrayList();
				List<String> answerAsciiList = Lists.newArrayList();
				JSONArray answers = answerObject.getJSONArray("answers");
				for (Object object : answers) {
					answerList.add(((JSONObject) object).getString("content"));
					answerAsciiList.add(((JSONObject) object).getString("contentAscii"));
				}
				answerData.put(answerObject.getLong("stuQuestionId"), answerList);
				answerAsciiData.put(answerObject.getLong("stuQuestionId"), answerAsciiList);
			}
			stuHkAnswerService.doQuestion(answerData, answerAsciiData, solvingImg, Security.getUserId());
			if (updateData && !stuData) {
				VQuestion vquestion = questionConvert.to(questionService.get(qId),
						new QuestionConvertOption(false, false, false, stuHkId));
				return new Value(vquestion);
			} else if (updateData && stuData) {
				Question question = questionService.get(qId);
				VQuestion vquestion = new VQuestion();
				vquestion.setId(question.getId());
				StudentHomeworkQuestion studentHomeworkQuestion = shqService.find(stuHkId, question.getId());

				vquestion.setStudentHomeworkQuestion(shqConvert.to(studentHomeworkQuestion));
				List<StudentHomeworkAnswer> studentHomeworkAnswers = shaService.find(studentHomeworkQuestion.getId());
				vquestion.setStudentHomeworkAnswers(shaConvert.to(studentHomeworkAnswers));
				return new Value(vquestion);
			}
			return new Value();
		} catch (ZuoyeException e) {
			return new Value(e);
		} catch (NoPermissionException e) {
			return new Value(e);
		}
	}

	/**
	 * 做作业（支持多图上传，添加题目计时）
	 * 
	 * @since yoomath v2.3.0
	 * 
	 * @param qId
	 *            习题ID
	 * @param stuHkId
	 *            学生作业ID
	 * @param answer
	 *            答案
	 * @param solvingImgs
	 *            多图
	 * @param homeworkTime
	 *            作业时间(秒数)
	 * @param dotime
	 *            做题时间(秒数)
	 * @param updateData
	 *            是否回传 question
	 * @param completionRate
	 *            正确率
	 * @param stuData
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "do_one_multi_img", method = { RequestMethod.POST, RequestMethod.GET })
	public Value doOneQuestionMultiImg(Long qId, Long stuHkId, String answer,
			@RequestParam(required = false) List<Long> solvingImgs, Integer homeworkTime, Integer dotime,
			@RequestParam(defaultValue = "false") boolean updateData,
			@RequestParam(required = false) Double completionRate,
			@RequestParam(defaultValue = "false") boolean stuData) {

		try {
			if (stuHkId == null) {
				logger.error("[do_one] arg error, qId={}, answer={}, solvingImg={}, homeworkTime={}, user={}", qId,
						answer, solvingImgs, homeworkTime, Security.getUserId());
				throw new IllegalArgException();
			}
			if (qId == null || qId <= 0) {
				throw new IllegalArgException();
			}
			StudentHomework studentHomework = stuHomeworkService.get(stuHkId);
			if (studentHomework == null || studentHomework.getStudentId().longValue() != Security.getUserId()) {
				// 不能做别人的作业
				throw new NoPermissionException();
			}
			if (studentHomework.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
				if (studentHomework.getStatus() == StudentHomeworkStatus.SUBMITED) {
					if (studentHomework.getStuSubmitAt() == null) {
						// 作业被自动提交了
						throw new ZuoyeException(ZuoyeException.ZUOYE_AUTO_COMMITED);
					} else {
						// 此作业学生已经主动提交过,不能再做此作业的题目了
						throw new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_COMMITED);
					}
				} else if (studentHomework.getStatus() == StudentHomeworkStatus.ISSUED) {
					if (studentHomework.getSubmitAt() == null && studentHomework.getStuSubmitAt() == null) {
						// 此时说明作业还没有提交作业就被下发了
						// 可以继续做作业
					} else {
						// 作业已经被下发不能答题
						throw new ZuoyeException(ZuoyeException.ZUOYE_ISSUED);
					}
				}
			}
			stuHkService.updateHomeworkTime(stuHkId, Security.getUserId(), homeworkTime, completionRate);
			Map<Long, List<String>> answerData = Maps.newHashMap();
			Map<Long, List<String>> answerAsciiData = Maps.newHashMap();
			JSONArray answerArray = JSONArray.parseArray(answer);
			for (int i = 0; i < answerArray.size(); i++) {
				JSONObject answerObject = (JSONObject) answerArray.get(i);
				List<String> answerList = Lists.newArrayList();
				List<String> answerAsciiList = Lists.newArrayList();
				JSONArray answers = answerObject.getJSONArray("answers");
				for (Object object : answers) {
					answerList.add(((JSONObject) object).getString("content"));
					answerAsciiList.add(((JSONObject) object).getString("contentAscii"));
				}
				answerData.put(answerObject.getLong("stuQuestionId"), answerList);
				answerAsciiData.put(answerObject.getLong("stuQuestionId"), answerAsciiList);
			}

			Question question = questionService.get(qId);
			StudentHomeworkQuestion studentHomeworkQuestion = shqService.find(stuHkId, qId);

			// @since yoomath v2.3.0 学生答题支持多图
			Map<Long, Question.Type> questionTypes = new HashMap<Long, Question.Type>(1);
			Map<Long, List<Long>> solvingImgMap = new HashMap<Long, List<Long>>(1);
			questionTypes.put(studentHomeworkQuestion.getId(), question.getType());
			solvingImgMap.put(studentHomeworkQuestion.getId(), solvingImgs);
			stuHkAnswerService.doQuestion(answerData, answerAsciiData, solvingImgMap, questionTypes,
					Security.getUserId(), null, null);

			// @since yoomath v2.3.0 学生答题记录做题耗时
			if (dotime != null) {
				stuHkQuestionService.updateDoQuestionTime(studentHomeworkQuestion.getId(), dotime);
				studentHomeworkQuestion.setDotime(dotime);
			}

			if (updateData && !stuData) {
				VQuestion vquestion = questionConvert.to(questionService.get(qId),
						new QuestionConvertOption(false, false, false, stuHkId));
				return new Value(vquestion);
			} else if (updateData && stuData) {
				VQuestion vquestion = new VQuestion();
				vquestion.setId(question.getId());

				vquestion.setStudentHomeworkQuestion(shqConvert.to(studentHomeworkQuestion));
				List<StudentHomeworkAnswer> studentHomeworkAnswers = shaService.find(studentHomeworkQuestion.getId());
				vquestion.setStudentHomeworkAnswers(shaConvert.to(studentHomeworkAnswers));
				return new Value(vquestion);
			}
			return new Value();
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 提交作业
	 * 
	 * @since 2.1
	 * @param hkId
	 *            作业ID(注意这里传的是作业ID哦,不要传错哦O(∩_∩)O哈哈~)
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value commit(long hkId) {
		try {
			Map<String, Object> data = new HashMap<String, Object>(3);
			long studentHomeworkId = stuHkService.commitHomework(hkId, Security.getUserId());
			if (studentHomeworkId == 0) {
				// 作业被提前下发了
				throw new ZuoyeException(ZuoyeException.ZUOYE_ISSUED_INADVANCE);
			} else {
				// 通知
				zyCorrectUserService.asyncNoticeUserAfterCommit(hkId);

				// 批改流程
				correctProcessor.afterStudentCommitHomeworkAsync(studentHomeworkId);

				// 用户任务处理
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101010002);
				messageObj.put("userId", Security.getUserId());
				messageObj.put("isClient", Security.isClient());
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}
			return new Value(data);
		} catch (HomeworkException e) {
			return new Value(e);
		} catch (ZuoyeException e) {
			return new Value(e);
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

	/**
	 * 获取作业记录首页数据.
	 * 
	 * @since web v2.0
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getRecordIndexDatas")
	public Value getRecordIndexDatas() {
		Map<String, Object> map = new HashMap<String, Object>(2);
		long userId = Security.getUserId();
		Date commonStartDate = stuHkService.getFirstStartAt(userId);
		Date holidayStartDate = holidayStuHomeworkService.getFirstStartAt(userId);
		map.put("commonStartDate", commonStartDate);
		map.put("holidayStartDate", holidayStartDate);
		return new Value(map);
	}

	/**
	 * 查询作业列表.
	 * 
	 * @param form
	 *            查询参数表单
	 * @since v2.0.3(web v2.0)
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryCommons")
	public Value queryCommons(HomeworkQueryForm form) {
		int pageNo = form.getPageNo() == null ? 1 : form.getPageNo();
		int size = form.getSize() == null ? 10 : form.getSize();
		ZyStudentHomeworkQuery query = new ZyStudentHomeworkQuery();
		if (form.getBt() != null) {
			query.setBeginTime(new Date(form.getBt()));
		}
		if (form.getEt() != null) {
			query.setEndTime(new Date(form.getEt()));
		}
		query.setClassId(form.getClassId());
		query.setKey(form.getKey());
		query.setStudentId(Security.getUserId());
		query.setSectionName(form.getSectionName());
		query.setHomeworkStatus(null == form.getShkStatus() ? 1 : form.getShkStatus());
		Page<StudentHomework> page = stuHkService.queryHomeworkWeb(query, P.index(pageNo, size));

		VStudentHomeworkPage vpage = new VStudentHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());

		List<StudentHomework> homeworks = page.getItems();
		List<VStudentHomework> items = new ArrayList<VStudentHomework>();
		if (CollectionUtils.isNotEmpty(homeworks)) {
			items = stuHkConvert.to(homeworks, false, true, false, false);
		}

		List<Long> clazzIds = Lists.newArrayList();
		for (VStudentHomework vStudentHomework : items) {
			clazzIds.add(vStudentHomework.getHomework().getHomeworkClazzId());
		}
		Map<Long, VHomeworkClazz> vClazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
		for (VStudentHomework v : items) {
			v.getHomework().setHomeworkClazz(vClazzMap.get(v.getHomework().getHomeworkClazzId()));
		}

		vpage.setItems(items);
		return new Value(vpage);
	}

	/**
	 * 查询假期作业列表.
	 * 
	 * @param form
	 *            查询参数表单
	 * @since v2.0.3(web v2.0)
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryHolidays")
	public Value queryHolidays(HomeworkQueryForm form) {
		int pageNo = form.getPageNo() == null ? 1 : form.getPageNo();
		int size = form.getSize() == null ? 10 : form.getSize();
		ZyStudentHomeworkQuery query = new ZyStudentHomeworkQuery();
		if (form.getBt() != null) {
			query.setBeginTime(new Date(form.getBt()));
		}
		if (form.getEt() != null) {
			query.setEndTime(new Date(form.getEt()));
		}
		query.setClassId(form.getClassId());
		query.setKey(form.getKey());
		query.setStudentId(Security.getUserId());
		query.setStatusIndex(form.getStatusIndex());

		Page<HolidayStuHomework> page = holidayStuHomeworkService.queryHolidayHomeworkWeb(query, P.index(pageNo, size));

		VStudentHomeworkPage vpage = new VStudentHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());

		List<Long> clazzIds = Lists.newArrayList();
		List<Long> holidayHomeworkIds = Lists.newArrayList();
		List<HolidayStuHomework> holidayHomeworks = page.getItems();
		List<VHolidayStuHomework> stuHolidaylist = holidayStuHomeworkConvert.to(holidayHomeworks);
		List<VStudentHomework> temp = new ArrayList<VStudentHomework>(size);
		if (stuHolidaylist.size() > 0) {
			temp.addAll(vstudentHomeworkConvert.to(stuHolidaylist));
			for (VHolidayStuHomework vHolidayStuHomework : stuHolidaylist) {
				holidayHomeworkIds.add(vHolidayStuHomework.getHolidayHomeworkId());
			}
		}
		for (VStudentHomework vStudentHomework : temp) {
			clazzIds.add(vStudentHomework.getHomework().getHomeworkClazz().getId());
		}

		Map<Long, VHomeworkClazz> vClazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
		Map<Long, Integer> itemCountMap = holidayHomeworkService.queryHolidayHomeworkItemCount(holidayHomeworkIds);
		for (VStudentHomework v : temp) {
			v.getHomework().setHomeworkClazz(vClazzMap.get(v.getHomework().getHomeworkClazz().getId()));
			Integer itemCount = itemCountMap.get(v.getHomework().getId());
			v.getHomework().setRightCount(itemCount == null ? 0 : itemCount); // 临时用于专项数量
		}

		vpage.setItems(temp);
		return new Value(vpage);
	}

	/**
	 * 获得
	 * 
	 * @param shid
	 *            作业ID
	 * @param type
	 *            作业类型，0：普通作业，1：假期作业
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getStudentHomework")
	public Value getStudentHomework(Long shid, Integer type) {
		if (null == shid || shid < 1 || type == null) {
			return new Value(new IllegalArgException());
		}
		if (type == 0) {
			StudentHomework sh = stuHomeworkService.get(shid);
			if (null == sh) {
				return new Value(new IllegalArgException());
			}
			VStudentHomework vstudentHomework = stuHkConvert.to(sh, false, true, false, false);
			return new Value(vstudentHomework);
		} else {
			HolidayStuHomework sh = holidayStuHomeworkService.get(shid);
			if (null == sh) {
				return new Value(new IllegalArgException());
			}
			VHolidayStuHomework vstudentHomework = holidayStuHomeworkConvert.to(sh);
			return new Value(vstudentHomework);
		}
	}

	/**
	 * 学生端申述题目批改错误
	 * 
	 * @since 小优秀快批
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "answerCanComplaint", method = { RequestMethod.POST, RequestMethod.GET })
	public Value answerCanComplaint(@RequestParam(value = "sHkQuestionId") Long sHkQuestionId) {
		StudentHomeworkQuestion question = shqService.get(sHkQuestionId);
		if (question == null) {
			return new Value(new IllegalArgException());
		}
		Date now = new Date();
		// 如果批改时间到现在已经超过168小时，不能申述
		if ((now.getTime() - question.getCorrectAt().getTime()) > 168 * 60 * 60 * 1000) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_ANSWER_APPEAL_EXCEED_TIMES));
		}

		// 如果已经有申述记录了，也不能申述
		QuestionAppeal appeal = questionAppealService.getAppeal(sHkQuestionId);
		if (appeal != null && appeal.getStatus() == QuestionAppealStatus.INIT) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_IN_APPEAL));
		} else if (appeal != null) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_PROCESSED));
		}

		return new Value();
	}

	/**
	 * 学生端申述题目批改错误
	 * 
	 * @since 小优秀快批
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "complaintAnswer", method = { RequestMethod.POST, RequestMethod.GET })
	public Value complaintAnswer(@RequestParam(value = "sHkQuestionId") Long sHkQuestionId,
			@RequestParam(value = "comment") String comment, Integer source) {
		StudentHomeworkQuestion question = shqService.get(sHkQuestionId);
		if (question == null) {
			return new Value(new IllegalArgException());
		}
		Date now = new Date();
		// 如果批改时间到现在已经超过168小时，不能申述
		if ((now.getTime() - question.getCorrectAt().getTime()) > 168 * 60 * 60 * 1000) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_ANSWER_APPEAL_EXCEED_TIMES));
		}

		// 如果已经有申述记录了，也不能申述
		QuestionAppeal appeal = questionAppealService.getAppeal(sHkQuestionId);
		if (appeal != null && appeal.getStatus() == QuestionAppealStatus.INIT) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_IN_APPEAL));
		} else if (appeal != null) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_PROCESSED));
		}

		questionAppealService.addComment(AppealType.CORRECT_ERROR, sHkQuestionId, source, comment, UserType.STUDENT);
		return new Value();
	}

	/**
	 * 学生端反馈订正题不会做
	 * 
	 * @since 小优秀快批
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "noCorrectCommit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value noCorrectCommit(Long sHkQuestionId) {
		if (sHkQuestionId <= 0) {
			return new Value(new IllegalArgException());
		}

		StudentHomeworkQuestion sHkQuestion = shqService.get(sHkQuestionId);
		// 如果已经订正，直接返回
		if (sHkQuestion.isRevised()) {
			return new Value();
		}

		// 修改订正状态为已订正
		shqService.updateReviseStatus(sHkQuestionId);

		// 调用新的批改流程
		correctProcessor.afterStudentCorrectQuestion(sHkQuestionId, false);

		return new Value();
	}

	/**
	 * 学生做作业（支持多图上传，添加题目计时）
	 * 
	 * @since 小优快批
	 * 
	 * @param qId
	 *            习题ID
	 * @param stuHkId
	 *            学生作业ID
	 * @param answer
	 *            答案
	 * @param solvingImgs
	 *            多图
	 * @param homeworkTime
	 *            作业时间(秒数)
	 * @param dotime
	 *            做题时间(秒数)
	 * @param updateData
	 *            是否返回此题的最新数据(通过此参数true可以更新返回此学生此条题目的最新数据)
	 * @param stuData
	 *            是否仅返回学生答题数据
	 * @param completionRate
	 *            作业完成率
	 * @param newCorrected
	 *            是否是订正题
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/do_one_multi_img", method = { RequestMethod.POST, RequestMethod.GET })
	public Value doOneQuestionMultiImg2(Long qId, Long stuHkId, String answer,
			@RequestParam(required = false) List<Long> solvingImgs, Integer homeworkTime, Integer dotime,
			@RequestParam(defaultValue = "false") boolean updateData,
			@RequestParam(required = false) Double completionRate,
			@RequestParam(defaultValue = "false") boolean stuData,
			@RequestParam(defaultValue = "false") boolean newCorrected) {

		try {
			if (stuHkId == null) {
				logger.error("[do_one] arg error, qId={}, answer={}, solvingImg={}, homeworkTime={}, user={}", qId,
						answer, solvingImgs, homeworkTime, Security.getUserId());
				throw new IllegalArgException();
			}
			if (qId == null || qId <= 0) {
				throw new IllegalArgException();
			}
			StudentHomework studentHomework = stuHomeworkService.get(stuHkId);
			if (studentHomework == null || studentHomework.getStudentId().longValue() != Security.getUserId()) {
				// 不能做别人的作业
				throw new NoPermissionException();
			}
			if (studentHomework.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
				if (studentHomework.getStatus() == StudentHomeworkStatus.SUBMITED) {
					if (studentHomework.getStuSubmitAt() == null) {
						// 作业被自动提交了
						throw new ZuoyeException(ZuoyeException.ZUOYE_AUTO_COMMITED);
					} else {
						// 此作业学生已经主动提交过,不能再做此作业的题目了
						throw new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_COMMITED);
					}
				} else if (studentHomework.getStatus() == StudentHomeworkStatus.ISSUED) {
					if (studentHomework.getSubmitAt() == null && studentHomework.getStuSubmitAt() == null) {
						// 此时说明作业还没有提交作业就被下发了
						// 可以继续做作业
					} else {
						// 作业已经被下发不能答题
						throw new ZuoyeException(ZuoyeException.ZUOYE_ISSUED);
					}
				}
			}
			if (!newCorrected) {
				// 如果是订正题不要去更新作业计时
				stuHkService.updateHomeworkTime(stuHkId, Security.getUserId(), homeworkTime, completionRate);
			}
			Map<Long, List<String>> answerData = Maps.newHashMap();
			Map<Long, List<String>> answerAsciiData = Maps.newHashMap();
			JSONArray answerArray = JSONArray.parseArray(answer);
			for (int i = 0; i < answerArray.size(); i++) {
				JSONObject answerObject = (JSONObject) answerArray.get(i);
				List<String> answerList = Lists.newArrayList();
				List<String> answerAsciiList = Lists.newArrayList();
				JSONArray answers = answerObject.getJSONArray("answers");
				for (Object object : answers) {
					answerList.add(((JSONObject) object).getString("content"));
					answerAsciiList.add(((JSONObject) object).getString("contentAscii"));
				}
				answerData.put(answerObject.getLong("stuQuestionId"), answerList);
				answerAsciiData.put(answerObject.getLong("stuQuestionId"), answerAsciiList);
			}

			Question question = questionService.get(qId);
			StudentHomeworkQuestion studentHomeworkQuestion = null;
			if (newCorrected) {
				List<StudentHomeworkQuestion> list = shqService.queryStuQuestions(stuHkId, qId, null, true);
				studentHomeworkQuestion = (null != list && list.size() > 0) ? list.get(0) : null;
			} else {
				studentHomeworkQuestion = shqService.find(stuHkId, qId);
			}

			// @since yoomath v2.3.0 学生答题支持多图
			Map<Long, Question.Type> questionTypes = new HashMap<Long, Question.Type>(1);
			Map<Long, List<Long>> solvingImgMap = new HashMap<Long, List<Long>>(1);
			questionTypes.put(studentHomeworkQuestion.getId(), question.getType());
			solvingImgMap.put(studentHomeworkQuestion.getId(), solvingImgs);
			stuHkAnswerService.doQuestion(answerData, answerAsciiData, solvingImgMap, questionTypes,
					Security.getUserId(), null, null);

			// @since 小优快批
			// 2018/2/28:如果是订正类型的解答题，并且需要老师自己去批改，需要去设置班级作业的待批改标签为true
			if (newCorrected) {
				// 调用新的批改流程
				correctProcessor.afterStudentCorrectQuestion(studentHomeworkQuestion.getId(), true);
			}
			// @since yoomath v2.3.0 学生答题记录做题耗时
			if (dotime != null) {
				stuHkQuestionService.updateDoQuestionTime(studentHomeworkQuestion.getId(), dotime);
				studentHomeworkQuestion.setDotime(dotime);
			}

			if (updateData && !stuData) {
				VQuestion vquestion = questionConvert.to(questionService.get(qId),
						new QuestionConvertOption(false, false, false, stuHkId));
				return new Value(vquestion);
			} else if (updateData && stuData) {
				VQuestion vquestion = new VQuestion();
				vquestion.setId(question.getId());

				vquestion.setStudentHomeworkQuestion(shqConvert.to(studentHomeworkQuestion));
				List<StudentHomeworkAnswer> studentHomeworkAnswers = shaService.find(studentHomeworkQuestion.getId());
				vquestion.setStudentHomeworkAnswers(shaConvert.to(studentHomeworkAnswers));
				return new Value(vquestion);
			}
			return new Value();
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

}
