package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseSectionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseKnowpointConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseSectionConvert;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseKnowpoint;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseSection;

/**
 * 学生章节知识点练习相关接口
 * 
 * @since yoomath V1.6
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月3日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/exe")
public class ZyStuExerciseController {
	@Autowired
	private MetaKnowSectionService metaKnowSectionService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private StudentQuestionAnswerService stuQuestionAnswerService;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZyStudentExerciseSectionConvert studentExerciseSectionConvert;
	@Autowired
	private ZyStudentExerciseSectionService studentExerciseSectionService;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TextbookCategoryService tbCateService;
	@Autowired
	private TextbookCategoryConvert tbCateConvert;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private ZyStudentExerciseKnowpointConvert studentExerciseKnowpointConvert;
	@Autowired
	private MqSender mqSender;

	/**
	 * 获得此学生的掌握情况教材与版本信息
	 *
	 * 学生加入不同阶段的班级返回数据以教材->版本列表为数据格式
	 *
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index(@RequestParam(value = "studentId", required = false) Long studentId) {
		// 教师也可以查看学生掌握情况
		List<Map<String, Object>> dataList = null;
		studentId = studentId == null ? Security.getUserId() : studentId;
		List<HomeworkStudentClazz> stuClazzs = zyHkStuClazzService.listCurrentClazzs(studentId);
		if (CollectionUtils.isNotEmpty(stuClazzs)) {
			int size = stuClazzs.size();
			dataList = new ArrayList<Map<String, Object>>(size);
			Map<Long, Long> clazzTeacer = new HashMap<Long, Long>(size);

			List<Long> clazzIds = new ArrayList<Long>(size);
			for (HomeworkStudentClazz clazz : stuClazzs) {
				clazzIds.add(clazz.getClassId());
			}
			Map<Long, HomeworkClazz> clazzs = zyHkClassService.mget(clazzIds);
			Set<Long> teacherIds = new HashSet<Long>(size);
			for (HomeworkClazz clz : clazzs.values()) {
				teacherIds.add(clz.getTeacherId());
				clazzTeacer.put(clz.getId(), clz.getTeacherId());
			}

			Map<Long, UserInfo> teachers = teacherService.getUserInfos(UserType.TEACHER, teacherIds);

			Set<Integer> cateCodes = new HashSet<Integer>(teacherIds.size());
			for (HomeworkStudentClazz stuClz : stuClazzs) {
				Long teaId = clazzTeacer.get(stuClz.getClassId());
				Teacher teacher = (Teacher) teachers.get(teaId); // 四川平台会有教师ID不存在的情况
				if (teacher != null && teacher.getTextbookCategoryCode() != null
						&& !cateCodes.contains(teacher.getTextbookCategoryCode())) {
					Map<String, Object> dataMap = new HashMap<String, Object>(2);
					dataMap.put("textbookCategory",
							tbCateConvert.to(tbCateService.get(teacher.getTextbookCategoryCode())));
					dataMap.put("textbooks", tbConvert.to(tbService.find(teacher.getPhaseCode(),
							teacher.getTextbookCategoryCode(), teacher.getSubjectCode())));
					dataList.add(dataMap);
					cateCodes.add(teacher.getTextbookCategoryCode());
				}
			}
		} else {
			dataList = new ArrayList<Map<String, Object>>(0);
		}
		return new Value(dataList);
	}

	/**
	 * 根据版本获得所有章节
	 *
	 * @param textbookCode
	 *            版本码
	 * @return {@link VStudentExerciseSection}
	 */

	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "get_section_complete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getSectionComplete(@RequestParam(value = "textbookCode") int textbookCode,
			@RequestParam(value = "studentId", required = false) Long studentId) {

		List<VSection> sections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
		List<VStudentExerciseSection> vs = studentExerciseSectionConvert.to(sections);
		if (studentId == null) {
			studentId = Security.getUserId();
		}
		studentExerciseSectionConvert.statisticsBeforeAssembleTree(vs, studentId, null);
		return new Value(studentExerciseSectionConvert.assembleTree(vs));
	}

	/**
	 * 知识图谱接口
	 *
	 * @param sectionCodes
	 *            章节码
	 * @param studentId
	 *            学生id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "get_knowpoint_complete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKnowpointComplete(@RequestParam(value = "sectionCodes") List<Long> sectionCodes,
			@RequestParam(value = "studentId", required = false) Long studentId) {
		List<Integer> metaCodes = metaKnowSectionService.findBySectionCodes(sectionCodes);

		if (CollectionUtils.isEmpty(metaCodes)) {
			return new Value();
		}
		List<MetaKnowpoint> points = metaKnowpointService.mgetList(metaCodes);

		List<VStudentExerciseKnowpoint> vs = studentExerciseKnowpointConvert.to(points);

		if (studentId == null) {
			studentId = Security.getUserId();
		}

		studentExerciseKnowpointConvert.assembleData(metaCodes, vs, studentId);
		return new Value(vs);
	}

	/**
	 * 加强练习题目提交
	 *
	 * @since 1.6
	 *
	 * @param questionId
	 *            题目ID
	 * @param answer
	 *            答案JSON格式 <br>
	 *
	 *            <pre>
	 *            [
	 *            	{qid:题目ID,answers:[]},
	 *            	{qid:子题ID,answers:[]},
	 *            	{qid:子题ID,answers:[]}
	 *            ]
	 * </pre>
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "exercise_commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value exerciseCommit(Long questionId, String answer, HomeworkAnswerResult result,
			StudentQuestionAnswerSource source, Long answerImg, Integer rightRate,
			@RequestParam(required = false) List<HomeworkAnswerResult> itemResults) {
		if (result == null || result == HomeworkAnswerResult.UNKNOW) {
			return new Value(new NoPermissionException());
		}
		Map<Long, List<String>> latex_answers = Maps.newHashMap();
		Map<Long, List<String>> asciimath_answers = Maps.newHashMap();
		if (answer != null) {
			JSONArray answerArray = JSONArray.parseArray(answer);
			for (int i = 0; i < answerArray.size(); i++) {
				JSONObject answerObject = (JSONObject) answerArray.get(i);
				long qid = answerObject.getLong("qid");

				JSONArray array = answerObject.getJSONArray("answers");
				List<String> latexs = new ArrayList<String>(array.size());
				List<String> asciis = new ArrayList<String>(array.size());
				for (Object object : array) {
					latexs.add(((JSONObject) object).getString("content"));
					asciis.add(((JSONObject) object).getString("contentAscii"));
				}
				latex_answers.put(qid, latexs);
				asciimath_answers.put(qid, asciis);
			}
		}
		// 多图预留
		List<Long> answerImgs = null;
		if (answerImg != null && answerImg > 0) {
			answerImgs = new ArrayList<Long>(1);
			answerImgs.add(answerImg);
		}
		stuQuestionAnswerService.create(Security.getUserId(), questionId, latex_answers, asciimath_answers, answerImgs,
				itemResults, rightRate, result, source, new Date());

		// 获得成长值及金币值
		/*
		 * GrowthLog growthLog =
		 * growthService.grow(GrowthAction.DOING_DAILY_EXERCISE,
		 * Security.getUserId(), -1, Biz.QUESTION, questionId, true); CoinsLog
		 * coinsLog = coinsService.earn(CoinsAction.DOING_DAILY_EXERCISE,
		 * Security.getUserId(), -1, Biz.QUESTION, questionId);
		 * 
		 * Map<String, Object> rtnMap = new HashMap<String, Object>(2);
		 * rtnMap.put("max", growthLog.getHonor() == null);
		 * 
		 * VUserReward v = new VUserReward();
		 * v.setCoinsValue(coinsLog.getCoinsValue());
		 * v.setGrowthValue(growthLog.getGrowthValue());
		 * v.setOrder(growthLog.getP1() != null ?
		 * Integer.valueOf(growthLog.getP1()) : null); rtnMap.put("reward", v);
		 * 
		 * return new Value(rtnMap);
		 */
		return new Value();
	}

	/**
	 * 练习拉取题目
	 *
	 * @param form
	 *            {@link PullQuestionForm}
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "pull_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pullQuestions(PullQuestionForm form) {
		if (form == null || form.getType() == null || form.getSectionCode() == null || form.getSectionCode() <= 0
				|| form.getCount() == null || form.getCount() <= 0) {
			return new Value(new IllegalArgException());
		}
		form.setVersion("1.3");
		Map<String, Object> map = Maps.newHashMap();
		List<Long> ids = pullQuestionService.pull(form);
		if (CollectionUtils.isEmpty(ids)) {
			return new Value(Collections.EMPTY_LIST);
		} else {
			List<Question> qs = new ArrayList<Question>(ids.size());
			Map<Long, Question> questions = questionService.mget(ids);
			for (Long qid : ids) {
				qs.add(questions.get(qid));
			}
			List<VQuestion> vs = questionConvert.to(qs, new QuestionConvertOption(false, true, true, null));
			map.put("questions", vs);
			map.put("questionExNum", sfqService.mgetQuestionExerciseNums(ids, Security.getUserId()));
			return new Value(map);
		}
	}

	/**
	 * 练习拉取题目(包含解答题)
	 *
	 * @since yoomath V1.9.1
	 * @param form
	 *            {@link PullQuestionForm}
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/pull_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pullQuestions2(PullQuestionForm form) {
		if (form == null || form.getType() == null || form.getSectionCode() == null || form.getSectionCode() <= 0
				|| form.getCount() == null || form.getCount() <= 0) {
			return new Value(new IllegalArgException());
		}
		form.setVersion("1.5");
		Map<String, Object> map = Maps.newHashMap();
		List<Long> ids = pullQuestionService.pull(form);
		if (CollectionUtils.isEmpty(ids)) {
			return new Value(Collections.EMPTY_LIST);
		} else {
			List<Question> qs = new ArrayList<Question>(ids.size());
			Map<Long, Question> questions = questionService.mget(ids);
			for (Long qid : ids) {
				qs.add(questions.get(qid));
			}
			List<VQuestion> vs = questionConvert.to(qs, new QuestionConvertOption(false, true, true, null));
			map.put("questions", vs);
			map.put("questionExNum", sfqService.mgetQuestionExerciseNums(ids, Security.getUserId()));
			return new Value(map);
		}
	}

}
