package com.lanking.uxb.service.holiday.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.impl.HolidayStuPublishService;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkPublishForm;
import com.lanking.uxb.service.holiday.type.HolidayPublishRangeType;
import com.lanking.uxb.service.holiday.value.VTextbookSectionTreeNode;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseSectionConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseSection;

/**
 * 老师假日作业相关rest API(除查看以外的接口)
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月23日
 */
@ApiAllowed
@RestController
@RequestMapping(value = "zy/t/holiday")
public class TeaHolidayHomeworkController {
	private Logger logger = LoggerFactory.getLogger(TeaHolidayHomeworkController.class);

	@Autowired
	private TextbookService textbookService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert hmeworkClazzConvert;
	@Autowired
	private ZyStudentExerciseSectionConvert studentExerciseSectionConvert;
	@Autowired
	private ZyTeacherFallibleQuestionService teacherFallibleQuestionService;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private HolidayStuPublishService holidayStuPublishService;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;

	/**
	 * 假期作业部署页面接口 返回Map转换的JSON数据对象。
	 *
	 * textbookSectionNodes -> 树形结构共两层 第一层教材 第二层章节 clazzes -> 此教师下的所有班级
	 *
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index() {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		List<HomeworkClazz> hkClazzes = homeworkClassService.listCurrentClazzs(Security.getUserId());
		// 此教师还未创建班级
		if (CollectionUtils.isEmpty(hkClazzes)) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_HOLIDAY_HAS_NO_CLASS));
		}
		retMap.put("clazzes", hmeworkClazzConvert.to(hkClazzes));

		List<VTextbookSectionTreeNode> nodes = Lists.newArrayList();

		List<Textbook> textbooks = textbookService.find(teacher.getPhaseCode(), teacher.getTextbookCategoryCode(),
				teacher.getSubjectCode());
		// 此教师还设置教材
		if (CollectionUtils.isEmpty(textbooks)) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_HOLIDAY_HAS_NO_TEXTBOOK));
		}
		List<Integer> textBookCodes = new ArrayList<Integer>(textbooks.size());
		Map<Long, VTextbookSectionTreeNode> tmpMap = new HashMap<Long, VTextbookSectionTreeNode>(textBookCodes.size());
		for (Textbook t : textbooks) {
			textBookCodes.add(t.getCode());
			VTextbookSectionTreeNode node = new VTextbookSectionTreeNode();
			node.setId(t.getCode());
			node.setName(t.getName());
			node.setType(HolidayPublishRangeType.TEXTBOOK);
			nodes.add(node);
			tmpMap.put(Long.valueOf(t.getCode()), node);
		}
		// 查询版本下的一级章节列表
		List<Section> sections = sectionService.findByTextbookCode(textBookCodes, 1);
		for (Section s : sections) {
			VTextbookSectionTreeNode node = new VTextbookSectionTreeNode();
			node.setId(s.getCode());
			node.setName(s.getName());
			node.setType(HolidayPublishRangeType.SECTION);
			tmpMap.get(Long.valueOf(s.getTextbookCode())).getChildren().add(node);
		}

		retMap.put("textbookSectionNodes", nodes);

		return new Value(retMap);
	}

	/**
	 * exerciseSectionStatistics -> 班级章节平均掌握情况
	 *
	 * @param sectionCodes
	 *            假期作业教师选中的章节
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "previewClassStatistics", method = { RequestMethod.GET, RequestMethod.POST })
	public Value previewClassStatistics(@RequestParam(value = "sectionCodes") List<Long> sectionCodes, long classId) {
		if (sectionCodes == null || sectionCodes.size() == 0) {
			return new Value(new IllegalArgException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>(1);
		// 班级章节掌握情况
		List<VSection> sections = sectionConvert.to(sectionService.mgetList(sectionCodes));
		List<VStudentExerciseSection> studentExerciseSections = studentExerciseSectionConvert.to(sections);
		studentExerciseSectionConvert.statisticsByClassId(studentExerciseSections, classId, null);
		retMap.put("exerciseSectionStatistics", studentExerciseSections);
		return new Value(retMap);
	}

	/**
	 * 预览假期作业
	 *
	 * 返回数据结构为: sectionQuestions -> { section -> 章节信息 questions -> 此章节拉取的题目数据
	 * difficulty -> 此章节生成题目平均难度 } questionCount -> 教师选定章节生成的题目数量 difficulty ->
	 * 所有题目平均难度
	 *
	 * @param sectionCodes
	 *            教师选定的章节码
	 * @param classId
	 *            班级id
	 * @param questionCount
	 *            共需要部置多少题
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "preview", method = { RequestMethod.GET, RequestMethod.POST })
	public Value preview(@RequestParam(value = "sectionCodes") List<Long> sectionCodes, long classId, int questionCount) {
		if (sectionCodes == null || sectionCodes.size() == 0 || questionCount < sectionCodes.size() * 3
				|| questionCount > sectionCodes.size() * 25) {
			return new Value(new IllegalArgException());
		}
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		List<VSection> sections = sectionConvert.to(sectionService.mgetList(sectionCodes));
		int avgSectionQuestionCount = questionCount / sectionCodes.size();
		int leftSectionQuestionCount = questionCount % sectionCodes.size();
		Map<Long, Integer> sectionQuestionCountMap = new HashMap<Long, Integer>(sectionCodes.size());
		// ue的设定是根据总题目数进行平均，若除不尽则再从第一章节向后+1直至分完
		for (long sectionCode : sectionCodes) {
			if (leftSectionQuestionCount > 0) {
				sectionQuestionCountMap.put(sectionCode, avgSectionQuestionCount + 1);
				leftSectionQuestionCount--;
			} else {
				sectionQuestionCountMap.put(sectionCode, avgSectionQuestionCount);
			}
		}

		Map<Long, List<VQuestion>> pullMap = pullQuestion(sectionQuestionCountMap);
		List<Map<String, Object>> sectionQuestions = new ArrayList<Map<String, Object>>(pullMap.size());

		int totalQuestionCount = 0;
		double totalDifficulty = 0;
		for (VSection v : sections) {
			Map<String, Object> m = new HashMap<String, Object>(4);
			m.put("section", v);
			List<VQuestion> questions = pullMap.get(v.getCode());
			if (questions.size() == 0) {
				continue;
			}
			double sectionTotalDifficulty = 0;
			for (VQuestion q : questions) {
				totalDifficulty += q.getDifficulty();
				sectionTotalDifficulty += q.getDifficulty();
			}
			totalQuestionCount += questions.size();
			sectionTotalDifficulty = new BigDecimal(sectionTotalDifficulty / questions.size()).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			m.put("difficulty", sectionTotalDifficulty);
			m.put("questions", pullMap.get(v.getCode()));

			sectionQuestions.add(m);
		}

		if (totalQuestionCount > 0) {
			totalDifficulty = new BigDecimal(totalDifficulty / totalQuestionCount)
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		retMap.put("difficulty", totalDifficulty);
		retMap.put("questionCount", totalQuestionCount);
		retMap.put("sectionQuestions", sectionQuestions);

		return new Value(retMap);
	}

	/**
	 * 布置作业
	 *
	 * @param formStr
	 *            {@link HolidayHomeworkPublishForm}
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "publish", method = { RequestMethod.GET, RequestMethod.POST })
	public Value publish(@RequestParam(value = "formStr") String formStr) {
		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}

		HolidayHomeworkPublishForm form = JSON.parseObject(formStr, HolidayHomeworkPublishForm.class);

		if (form.getType() == null || form.getDeadline() == null || form.getQuestionCount() == null
				|| form.getQuestionCount() == 0 || form.getDifficulty() == null
				|| CollectionUtils.isEmpty(form.getSectionQuestions())) {
			return new Value(new IllegalArgException());
		}
		form.setCreateId(Security.getUserId());
		HolidayHomework homework = holidayHomeworkService.publish(form);
		// 开始时间是当前时间则将此次作业布置给学生
		if (homework.getStatus() == HomeworkStatus.PUBLISH) {
			holidayStuPublishService.asyncPublish(homework);
		}

		GrowthLog growthLog = growthService.grow(GrowthAction.FIRST_PUBLISH_HOLIDAY_HOMEWORK, Security.getUserId(), -1,
				Biz.HOMEWORK, homework.getId(), true);
		CoinsLog coinsLog = coinsService.earn(CoinsAction.FIRST_PUBLISH_HOLIDAY_HOMEWORK, Security.getUserId(), -1,
				Biz.HOMEWORK, homework.getId());

		// 金币及成长值信息。
		if (growthLog.getHonor() != null) {
			int upGradeCoins = 0;

			boolean isupgrade = growthLog.getHonor().isUpgrade();
			if (isupgrade) {
				upGradeCoins = growthLog.getHonor().getUpRewardCoins();
			}
			return new Value(new VUserReward(upGradeCoins, isupgrade, growthLog.getHonor().getLevel(),
					growthLog.getGrowthValue(), coinsLog.getCoinsValue()));
		}

		return new Value();
	}

	/**
	 * 教师假期作业预览增加两题,换题
	 *
	 * @param form
	 *            {@link PullQuestionForm}
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "pullQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pullQuestions(PullQuestionForm form) {
		if (form == null
				|| form.getType() == null
				|| form.getSectionCode() == null
				|| form.getSectionCode() <= 0
				|| form.getCount() == null
				|| form.getCount() <= 0
				|| form.getCount() > 2
				|| (CollectionUtils.isEmpty(form.getMetaKnowpoints()) && CollectionUtils.isEmpty(form
						.getKnowledgePoints()))) {
			return new Value(new IllegalArgException());
		}

		if (form.getType() == PullQuestionType.CHANGE_QUESTION) {// 换题
			if (form.getDifficulty() == null || CollectionUtils.isEmpty(form.getQuestionMetaKnowpoints())) {
				return new Value(new IllegalArgException());
			}
		}

		List<Long> ids = pullQuestionService.pull(form);
		if (CollectionUtils.isEmpty(ids)) {
			return new Value(Collections.EMPTY_LIST);
		} else {
			List<Question> qs = new ArrayList<Question>(ids.size());
			Map<Long, Question> questions = questionService.mget(ids);
			for (Long qid : ids) {
				qs.add(questions.get(qid));
			}
			return new Value(questionConvert.to(qs, new QuestionConvertOption(false, true, true, false, true, null)));
		}
	}

	/**
	 * 拉取题目,若教师错题本中的题目数量不够时，根据章节随机拉取补全。
	 *
	 * @param sectionQuestionCountMap
	 *            章节码对应拉取题目数量
	 * @return 章节对应拉取题列表
	 */
	private Map<Long, List<VQuestion>> pullQuestion(Map<Long, Integer> sectionQuestionCountMap) {
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		Map<Long, List<VQuestion>> pullMap = new HashMap<Long, List<VQuestion>>(sectionQuestionCountMap.size());
		Map<Long, PullQuestionForm> formMap = new HashMap<Long, PullQuestionForm>(sectionQuestionCountMap.size());
		Map<Long, Collection<Long>> questionMap = new HashMap<Long, Collection<Long>>(sectionQuestionCountMap.size());
		for (Map.Entry<Long, Integer> entry : sectionQuestionCountMap.entrySet()) {
			// 先从教师的错题本中拉取数据，按照正确率升序
			ZyTeacherFallibleQuestionQuery queryForm = new ZyTeacherFallibleQuestionQuery();
			List<Long> sectionCodes = sectionService.findSectionChildren(entry.getKey());
			sectionCodes.add(entry.getKey());
			Set<Long> codes = Sets.newHashSet(sectionCodes);
			queryForm.setSectionCodes(codes);
			queryForm.setIsDifficultyDesc(false);
			queryForm.setTeacherId(Security.getUserId());
			queryForm.setSearchInSection(true);
			queryForm.setTypes(Lists.newArrayList(Type.FILL_BLANK, Type.SINGLE_CHOICE, Type.MULTIPLE_CHOICE,
					Type.TRUE_OR_FALSE));
			Page<TeacherFallibleQuestion> p = teacherFallibleQuestionService.queryFaliableQuestion(
					teacher.getSubjectCode(), queryForm, P.index(1, entry.getValue()));
			Set<Long> questionIds = new HashSet<Long>(p.getItemSize());
			for (TeacherFallibleQuestion q : p.getItems()) {
				questionIds.add(q.getQuestionId());
			}
			questionMap.put(entry.getKey(), questionIds);

			// 若错题本中的数据不足则再随便拉取题目补全
			if (questionIds.size() < entry.getValue()) {
				PullQuestionForm pullQuestionForm = new PullQuestionForm();
				pullQuestionForm.setCount(entry.getValue() - questionIds.size());
				pullQuestionForm.setSectionCode(entry.getKey());
				pullQuestionForm.setType(PullQuestionType.PRACTISE);
				pullQuestionForm.setqIds(Lists.<Long> newArrayList(questionIds));
				pullQuestionForm.setVersion("1.3");
				formMap.put(entry.getKey(), pullQuestionForm);
			}
		}

		// 异步启动线程，取得题目
		asyncGetQuestions(questionMap, formMap, pullMap);

		return pullMap;
	}

	/**
	 * 多线程拉取题目<br/>
	 * 最大启动线程不超过5
	 *
	 * @param questionMap
	 *            章节对应的题目id
	 * @param formMap
	 *            拉取题目的form表单map
	 * @param retMap
	 *            section -> VQuestion最终返回数据
	 */
	private void asyncGetQuestions(Map<Long, Collection<Long>> questionMap, Map<Long, PullQuestionForm> formMap,
			Map<Long, List<VQuestion>> retMap) {
		int threadPoolSize = formMap.size() > 5 ? 5 : formMap.size();
		if (threadPoolSize > 0) {
			ExecutorService eService = Executors.newFixedThreadPool(threadPoolSize);
			ExecutorCompletionService<Map<Long, List<Long>>> executorService = new ExecutorCompletionService<Map<Long, List<Long>>>(
					eService);

			for (final Map.Entry<Long, PullQuestionForm> formEntry : formMap.entrySet()) {
				executorService.submit(new Callable<Map<Long, List<Long>>>() {
					@Override
					public Map<Long, List<Long>> call() throws Exception {
						List<Long> questionIds = pullQuestionService.pull(formEntry.getValue());
						Map<Long, List<Long>> eRetMap = new HashMap<Long, List<Long>>(1);
						if (CollectionUtils.isEmpty(questionIds)) {
							eRetMap.put(formEntry.getKey(), Collections.EMPTY_LIST);
						} else {
							eRetMap.put(formEntry.getKey(), questionIds);
						}

						return eRetMap;
					}
				});
			}

			try {
				int count = 0;
				while (count < formMap.size()) {
					Future<Map<Long, List<Long>>> f = executorService.poll();
					if (f != null) {
						for (Map.Entry<Long, List<Long>> entry : f.get().entrySet()) {
							questionMap.get(entry.getKey()).addAll(entry.getValue());
						}

						count++;
					}

				}

				eService.shutdown();
			} catch (InterruptedException | ExecutionException e) {
				logger.error("holiday homework pull question error {}", e);
			}
		}

		List<Long> questionIds = Lists.newArrayList();
		Set<Map.Entry<Long, Collection<Long>>> entries = questionMap.entrySet();
		for (Map.Entry<Long, Collection<Long>> qm : entries) {
			if (CollectionUtils.isNotEmpty(qm.getValue())) {
				questionIds.addAll(qm.getValue());
			}
		}
		List<Question> questions = questionService.mgetList(questionIds);
		List<VQuestion> vs = questionConvert.to(questions, new QuestionConvertOption(false, true, true, false, true,
				null));
		Map<Long, VQuestion> m = new HashMap<Long, VQuestion>(vs.size());
		for (VQuestion v : vs) {
			m.put(v.getId(), v);
		}

		for (Map.Entry<Long, Collection<Long>> qm : entries) {
			if (CollectionUtils.isEmpty(retMap.get(qm.getKey()))) {
				retMap.put(qm.getKey(), Lists.<VQuestion> newArrayList());
			}
			for (Long i : qm.getValue()) {
				retMap.get(qm.getKey()).add(m.get(i));
			}
		}

	}

}
