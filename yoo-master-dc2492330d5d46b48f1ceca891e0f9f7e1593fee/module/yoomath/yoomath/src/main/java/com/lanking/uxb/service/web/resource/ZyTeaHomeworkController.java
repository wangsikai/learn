package com.lanking.uxb.service.web.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.PageNotFoundException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassKnowpointConvert;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.ExerciseQuestionService;
import com.lanking.uxb.service.resources.api.ExerciseService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.HomeworkQuestionConvert;
import com.lanking.uxb.service.resources.convert.HomeworkQuestionConvertOption;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkQuestionConvert;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VHomeworkQuestion;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.web.util.VQuestionComparator;
import com.lanking.uxb.service.wx.api.ZyWXMessageService;
import com.lanking.uxb.service.zuoye.api.Paper;
import com.lanking.uxb.service.zuoye.api.PaperBuilder;
import com.lanking.uxb.service.zuoye.api.PaperParam;
import com.lanking.uxb.service.zuoye.api.ZyCorrectUserService;
import com.lanking.uxb.service.zuoye.api.ZyExerciseService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyTextbookExerciseService;
import com.lanking.uxb.service.zuoye.convert.ZyExerciseSectionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyTextbookExerciseConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.GenerateHomeworkForm;
import com.lanking.uxb.service.zuoye.form.PublishHomeworkForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;
import com.lanking.uxb.service.zuoye.value.VExerciseSection;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkPage;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;
import com.lanking.uxb.service.zuoye.value.VTextbookExercise;

/**
 * 悠作业老师布置作业接口
 * 
 * @since yoomath V1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/hk")
public class ZyTeaHomeworkController {

	private Logger logger = LoggerFactory.getLogger(ZyTeaHomeworkBookController.class);
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyTextbookExerciseService tbeService;
	@Autowired
	private ZyTextbookExerciseConvert tbeConvert;
	@Autowired
	private ZyExerciseSectionConvert esConvert;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZyExerciseService zyExerciseService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private HomeworkConvert hkConvert;
	@Autowired
	private StudentHomeworkService shService;
	@Autowired
	private StudentHomeworkConvert shConvert;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private HomeworkQuestionConvert hqConvert;
	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private ZyStudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	private ZyHomeworkStatisticService hkStatisticService;
	@Autowired
	private MetaKnowSectionService metaKnowpointSectionService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private ExerciseQuestionService exerciseQuestionService;
	@Autowired
	private ZyHomeworkQuestionService hkQuestionService;
	@Autowired
	private PaperBuilder paperBuilder;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ZyHomeworkClazzConvert hkClassConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ZyQuestionCarService zyQCarService;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert hkStuClazzConvert;
	@Autowired
	private ZyCorrectUserService zyCorrectUserService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private ZyWXMessageService zyWXMessageService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private QuestionKnowledgeService qkService;
	@Autowired
	private DiagnosticStudentClassKnowpointService diagStuKpService;
	@Autowired
	private DiagnosticStudentClassKnowpointConvert diagStuKpConvert;
	@Autowired
	@Qualifier("executor")
	private Executor executor;
	@Autowired
	private ZyHomeworkClassGroupStudentService zyHomeworkClassGroupStudentService;
	@Autowired
	private StudentHomeworkAnswerService studentHomeworkAnswerService;
	@Autowired
	private CorrectProcessor correctProcessor;
	@Autowired
	private StudentHomeworkQuestionConvert stuhQuestionConvert;

	/**
	 * 通过教材代码获取左侧列表<br>
	 * 
	 * <pre>
	 * textbookCategories：版本列表
	 * textbooks：教材列表
	 * textbookCategoryCode:初始化选择的版本
	 * textbookCode:初始化选择的教材
	 * sections：章节树结构
	 * textbookExerciseId：用户上次编辑的习题（可能没有此字段）
	 * </pre>
	 * 
	 * @since 2.1
	 * @param textbookCode
	 *            教材代码,此字段为空时,表示第一次获取,会返回版本教材过滤条件数据
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>(6);
		TextbookExercise textbookExercise = null;
		// 查找此教材下次教师最后一次使用的预置练习
		Exercise exercise = zyExerciseService.findLatestOne(Security.getUserId(), textbookCode);
		if (exercise != null) {
			textbookExercise = tbeService.get(exercise.getTextbookExerciseId());
			data.put("textbookExerciseId", exercise.getTextbookExerciseId());
		}
		if (textbookCode == null) {
			Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
			// 设置返回版本列表
			List<VTextbookCategory> categories = null;
			if (teacher.getTextbookCategoryCode() == null) {
				categories = tbcConvert.to(tbcService.find(Product.YOOMATH, teacher.getPhaseCode()));
			} else {
				categories = Lists.newArrayList(tbcConvert.to(tbcService.get(teacher.getTextbookCategoryCode())));
			}
			data.put("textbookCategories", categories);
			// 设置返回教材列表
			List<Integer> categoryCodes = new ArrayList<Integer>(categories.size());
			for (VTextbookCategory v : categories) {
				categoryCodes.add(v.getCode());
			}
			List<VTextbook> textbooks = tbConvert.to(
					tbService.find(Product.YOOMATH, teacher.getPhaseCode(), teacher.getSubjectCode(), categoryCodes));
			data.put("textbooks", textbooks);

			// 设置当前选中的版本和教材
			if (textbookExercise == null) {
				if (teacher.getTextbookCode() == null) {// 如果用户没有设置教材
					for (VTextbook v : textbooks) {
						if (v.getCategoryCode() == categories.get(0).getCode()) {
							textbookCode = v.getCode();
							break;
						}
					}
					if (!data.containsKey("textbookCategoryCode") || !data.containsKey("textbookCode")) {
						data.put("textbookCategoryCode", categories.get(0).getCode());// 初始化选择的版本
						for (VTextbook v : textbooks) {
							if (v.getCategoryCode() == categories.get(0).getCode()) {
								textbookCode = v.getCode();
								data.put("textbookCode", v.getCode());// 初始化选择的教材
								break;
							}
						}
					}
				} else {
					// 默认取用户设置的版本教材
					data.put("textbookCategoryCode", teacher.getTextbookCategoryCode());// 初始化选择的版本
					data.put("textbookCode", teacher.getTextbookCode());// 初始化选择的教材
					// 用户没有布置过作业，获取的目录树为teacher本身的TextbookCode
					textbookCode = teacher.getTextbookCode();
				}
			} else {
				textbookCode = textbookExercise.getTextbookCode();
				for (VTextbook v : textbooks) {
					if (v.getCode() == textbookCode) {
						if (teacher.getTextbookCode() == null) {
							data.put("textbookCategoryCode", v.getCategoryCode());
						} else {
							if (teacher.getTextbookCategoryCode() == v.getCategoryCode()) {
								data.put("textbookCategoryCode", v.getCategoryCode());
							} else {
								data.put("textbookCategoryCode", teacher.getTextbookCategoryCode());
								textbookCode = teacher.getTextbookCode();
								data.remove("textbookExerciseId");
							}
						}
						break;
					}
				}
				if (!data.containsKey("textbookCategoryCode")) {
					data.put("textbookCategoryCode", teacher.getTextbookCategoryCode());
					textbookCode = teacher.getTextbookCode();
					data.remove("textbookExerciseId");
				}
				data.put("textbookCode", textbookCode);// 初始化选择的教材
			}
		}
		if (textbookCode == null) {
			data.put("sections", new ArrayList<VExerciseSection>(0));
		} else {
			List<VTextbookExercise> vexercises = tbeConvert.to(tbeService.findByTextbook(textbookCode));
			List<VExerciseSection> vs = esConvert
					.to(sectionConvert.to(sectionService.findByTextbookCode(textbookCode)));
			Long textbookExerciseId = null;
			if (data.containsKey("textbookExerciseId")) {
				textbookExerciseId = (Long) data.get("textbookExerciseId");
			}
			esConvert.assemblyExercise(vs, vexercises, textbookExerciseId);
			data.put("sections", esConvert.assemblySectionTree(vs));
		}
		return new Value(data);
	}

	/**
	 * 通过预置习题ID拉取题目列表以及其他信息
	 * 
	 * @since 2.1
	 * @param textbookExerciseId
	 *            预置习题ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "listQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listQuestions(long textbookExerciseId,
			@RequestParam(value = "version", defaultValue = "1.2") String version) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		// 返回题目
		Exercise exercise = zyExerciseService.findLatestOne(Security.getUserId(), textbookExerciseId, null, null);
		List<Long> qIds = zyExerciseService.listQuestions(Security.getUserId(), textbookExerciseId,
				exercise == null ? null : exercise.getId());
		if (qIds.size() == 0) {
			data.put("questions", Collections.EMPTY_LIST);
		} else {
			Map<Long, Question> questions = questionService.mget(qIds);
			Map<Long, VQuestion> vquestions = questionConvert.to(questions,
					new QuestionConvertOption(false, true, true, false, true, null));
			List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
			for (Long qId : qIds) {
				VQuestion vq = vquestions.get(qId);
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
			Homework h = zyHomeworkService.getLatextHomeworkByExerciseId(Security.getUserId(), exercise.getId());
			if (h != null && CollectionUtils.isNotEmpty(h.getMetaKnowpoints())) {
				List<Integer> codes = new ArrayList<Integer>(h.getMetaKnowpoints().size());
				for (Long code : h.getMetaKnowpoints()) {
					codes.add(code.intValue());
				}
				List<MetaKnowpoint> knowpoints = metaKnowpointService.mgetList(codes);
				if (CollectionUtils.isNotEmpty(knowpoints)) {
					data.put("pMetaKnowpoints", metaKnowpointConvert.to(knowpoints));
				}
			}
		}
		// 返回此章节下面的知识点
		TextbookExercise tbe = tbeService.get(textbookExerciseId);
		List<Integer> codes = null;
		Section section = sectionService.get(tbe.getSectionCode());
		if ("本章综合与测试".equals(section.getName())) {
			codes = metaKnowpointSectionService.getKnowCodesByCode(section.getPcode());
		} else {
			codes = metaKnowpointSectionService.findBySectionCode(tbe.getSectionCode());
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
		return new Value(data);
	}

	/**
	 * 拉取题目
	 * 
	 * @since 2.1
	 * @param form
	 *            {@link PullQuestionForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "pullQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value pullQuestions(PullQuestionForm form) {
		if (form == null || (form.getSectionCode() == null && form.getTextbookExerciseId() == null)) {
			return new Value(new IllegalArgException());
		}
		List<Question> qs = zyExerciseService.pullQuestions(form.getTextbookExerciseId(), form.getSectionCode(),
				Sets.newHashSet(Type.FILL_BLANK), form.getCount(), form.getqIds(), form.getMinDifficulty(),
				form.getMaxDifficulty());
		return new Value(questionConvert.to(qs, new QuestionConvertOption(false, true, true, false, true, null)));
	}

	/**
	 * 拉取题目(调整难度、换题、增加两题)
	 * 
	 * @since 2.1
	 * @param form
	 *            {@link PullQuestionForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "2/pullQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value pullQuestions2(PullQuestionForm form) {
		if (form == null || form.getType() == null || form.getTextbookExerciseId() == null
				|| form.getSectionCode() == null || form.getSectionCode() <= 0 || form.getCount() == null
				|| form.getCount() <= 0 || form.getCount() > 2) {
			return new Value(new IllegalArgException());
		}
		// 章节没有对应知识点的情况,提示为：没有更多题目了
		if (CollectionUtils.isEmpty(form.getMetaKnowpoints()) && CollectionUtils.isEmpty(form.getKnowledgePoints())) {
			return new Value(Collections.EMPTY_LIST);
		}
		if (form.getType() == PullQuestionType.CHANGE_DIFFICULTY) {// 调整难度
			if ((CollectionUtils.isEmpty(form.getQuestionMetaKnowpoints())
					&& CollectionUtils.isEmpty(form.getQuestionKnowledgePoints()))
					|| (form.getMaxDifficulty() == null && form.getMinDifficulty() == null)) {
				return new Value(new IllegalArgException());
			}
		} else if (form.getType() == PullQuestionType.CHANGE_QUESTION) {// 换题
			if (form.getDifficulty() == null || (CollectionUtils.isEmpty(form.getQuestionMetaKnowpoints())
					&& CollectionUtils.isEmpty(form.getQuestionKnowledgePoints()))) {
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
			// @since 教师端v1.3.0
			QuestionConvertOption option = new QuestionConvertOption(false, true, true, false, true, null);
			option.setInitPublishCount(true);
			option.setInitQuestionSimilarCount(true);
			option.setInitExamination(true);
			return new Value(questionConvert.to(qs, option));
		}
	}

	/**
	 * 布置作业前获取相关数据
	 * 
	 * @since yoomathV1.2
	 * @param textbookExerciseId
	 *            预置练习ID
	 * @param isBook
	 *            是否是书本章节(true|false)
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/publishData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishData2(long textbookExerciseId, Long sectionCode,
			@RequestParam(value = "isBook", defaultValue = "false") boolean isBook) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		if (textbookExerciseId > 0 || sectionCode != null) {
			data.put("name",
					zyHomeworkService.getHomeworkName(Security.getUserId(), textbookExerciseId, sectionCode, isBook));
		}
		// 返回班级列表
		List<HomeworkClazz> clazzs = hkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			data.put("clazzs", Collections.EMPTY_LIST);
		} else {
			data.put("clazzs", hkClassConvert.to(clazzs));
		}
		int issueHour = Env.getDynamicInt("homework.issued.time"); // 自动下发小时数
		data.put("issueHour", issueHour);
		return new Value(data);
	}

	/**
	 * 布置作业
	 * 
	 * @since 2.1
	 * @param form
	 *            {@link PublishHomeworkForm}
	 * @return {@link Value}
	 */
	@Deprecated
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "publish", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publish(PublishHomeworkForm form) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		if (form == null || CollectionUtils.isEmpty(form.getHomeworkClassIds())
				|| CollectionUtils.isEmpty(form.getqIds()) || form.getDeadline() == null
				|| form.getStartTime() >= form.getDeadline() || form.getDeadline() < System.currentTimeMillis() + 600000
				|| form.getDifficulty() == null || form.getCorrectingType() == null
				|| form.getCorrectingType() == null) {
			return new Value(new IllegalArgException());
		}
		if ((form.getTextbookExerciseId() == null || form.getTextbookExerciseId() <= 0)
				&& StringUtils.isBlank(form.getName()) && (form.getBookId() == null || form.getBookId() <= 0)) {
			// textbookExerciseId和name不能同时为空
			return new Value(new IllegalArgException());
		}
		form.setCreateId(Security.getUserId());
		if (form.getMetaKnowpoints() == null) {
			form.setMetaKnowpoints(new ArrayList<Long>(0));
		} else {
			form.setMetaKnowpoints(new ArrayList<Long>(Sets.newHashSet(form.getMetaKnowpoints())));
		}
		try {
			form.setCorrectLastHomework(Env.getBoolean("homework.correct"));
			form.setCorrectingType(HomeworkCorrectingType.TEACHER);
			long homeworkId = zyHomeworkService.publish(form);
			// 通知
			if (homeworkId != 0) {
				zyCorrectUserService.asyncNoticeUser(homeworkId);
				mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
						MQ.builder().data(new PushHandleForm(PushHandleAction.NEW_HOMEWORK, homeworkId)).build());
				JSONObject jo = new JSONObject();
				jo.put("teacherId", Security.getUserId());
				jo.put("homeworkId", homeworkId);
				mqSender.send(MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK,
						MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_PUBLISH, MQ.builder().data(jo).build());
				zyWXMessageService.sendPublishHomeworkMessage(form.getHomeworkClassIds(), homeworkId, null);
			}
			if (form.isFromCar()) {
				zyQCarService.removeAll(Security.getUserId());
			}
			GrowthLog growthLogFirst = growthService.grow(GrowthAction.FIRST_PUBLISH_HOMEWORK, Security.getUserId(), -1,
					Biz.HOMEWORK, homeworkId, true);
			CoinsLog coinsLogFirst = coinsService.earn(CoinsAction.FIRST_PUBLISH_HOMEWORK, Security.getUserId(), -1,
					Biz.HOMEWORK, homeworkId);
			GrowthLog growthLog = growthService.grow(GrowthAction.PUBLISH_HOMEWORK, Security.getUserId(), -1,
					Biz.HOMEWORK, homeworkId, true);
			CoinsLog coinsLog = coinsService.earn(CoinsAction.PUBLISH_HOMEWORK, Security.getUserId(), -1, Biz.HOMEWORK,
					homeworkId);
			// 防止初次布置作业 升级，后面在给与常规奖励时返回页面的isUpgrade不正确
			if (growthLog.getHonor() != null) {
				boolean isupgrade = false;
				int upGradeCoins = 0;
				if (growthLogFirst.getHonor() != null) {
					if (growthLogFirst.getHonor().isUpgrade() && growthLogFirst.getHonor().getUpRewardCoins() != null) {
						isupgrade = true;
						upGradeCoins = growthLogFirst.getHonor().getUpRewardCoins();
					}
				} else {
					isupgrade = growthLog.getHonor().isUpgrade();
					if (isupgrade && growthLog.getHonor().getUpRewardCoins() != null) {
						upGradeCoins = growthLog.getHonor().getUpRewardCoins();
					}
				}
				data.put("userReward",
						new VUserReward(upGradeCoins, isupgrade, growthLog.getHonor().getLevel(),
								growthLog.getGrowthValue()
										+ (growthLogFirst.getGrowthValue() == -1 ? 0 : growthLogFirst.getGrowthValue()),
								coinsLog.getCoinsValue()
										+ (coinsLogFirst.getCoinsValue() == -1 ? 0 : coinsLogFirst.getCoinsValue())));
			}
			return new Value(data);
		} catch (Exception e) {
			logger.error("publish homework error:", e);
			return new Value(new ServerException());
		}
	}

	/**
	 * 新版布置作业接口为了解决原先布置学生过多时,等待时间过长的情况
	 *
	 * @since yoomath V1.9.2
	 * @since yoomath v2.1.2 添加新知识点 wanlong.che 2016-11-24
	 * @since yoomath v2.3.0 添加自动下发设置
	 * @param form
	 *            {@link PublishHomeworkForm}
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "2/publish", method = { RequestMethod.GET, RequestMethod.POST })
	public Value publish2(PublishHomeworkForm form) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		if (form == null || CollectionUtils.isEmpty(form.getHomeworkClassIds())
				|| CollectionUtils.isEmpty(form.getqIds()) || form.getDeadline() == null
				|| form.getStartTime() >= form.getDeadline() || form.getDeadline() < System.currentTimeMillis() + 600000
				|| form.getDifficulty() == null || form.getCorrectingType() == null
				|| form.getCorrectingType() == null) {
			return new Value(new IllegalArgException());
		}
		if ((form.getTextbookExerciseId() == null || form.getTextbookExerciseId() <= 0)
				&& StringUtils.isBlank(form.getName()) && (form.getBookId() == null || form.getBookId() <= 0)) {
			// textbookExerciseId和name不能同时为空
			return new Value(new IllegalArgException());
		}

		form.setCreateId(Security.getUserId());

		// 新知识点，优先处理新知识点，当新知识点没有时再处理旧知识点
		if (CollectionUtils.isEmpty(form.getKnowledgePoints())) {
			form.setKnowledgePoints(new ArrayList<Long>(0));
			if (form.getMetaKnowpoints() == null) {
				form.setMetaKnowpoints(new ArrayList<Long>(0));
			} else {
				form.setMetaKnowpoints(new ArrayList<Long>(Sets.newHashSet(form.getMetaKnowpoints())));
			}
		} else {
			form.setMetaKnowpoints(new ArrayList<Long>(0));
			form.setKnowledgePoints(new ArrayList<Long>(Sets.newHashSet(form.getKnowledgePoints())));
		}
		try {
			form.setCorrectLastHomework(Env.getBoolean("homework.correct"));
			form.setCorrectingType(HomeworkCorrectingType.TEACHER);
			final List<Homework> homeworks = zyHomeworkService.publish2(form);

			Homework homework = homeworks.get(0);
			executor.execute(new Runnable() {
				@Override
				public void run() {
					for (Homework hk : homeworks) {
						if (hk.getStatus() == HomeworkStatus.PUBLISH) {
							zyHomeworkService.publishHomework2(hk.getId());
							JSONObject jo = new JSONObject();
							jo.put("teacherId", hk.getCreateId());
							jo.put("homeworkId", hk.getId());
							mqSender.send(MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK,
									MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_PUBLISH,
									MQ.builder().data(jo).build());
						}
					}
				}
			});

			// 通知
			if (homework.getId() != 0) {
				zyCorrectUserService.asyncNoticeUser(homework.getId());
				mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
						MQ.builder().data(new PushHandleForm(PushHandleAction.NEW_HOMEWORK, homework.getId())).build());
				zyWXMessageService.sendPublishHomeworkMessage(form.getHomeworkClassIds(), homework.getId(), null);
			}
			if (form.isFromCar()) {
				zyQCarService.removeAll(Security.getUserId());
			}
			GrowthLog growthLogFirst = growthService.grow(GrowthAction.FIRST_PUBLISH_HOMEWORK, Security.getUserId(), -1,
					Biz.HOMEWORK, homework.getId(), true);
			CoinsLog coinsLogFirst = coinsService.earn(CoinsAction.FIRST_PUBLISH_HOMEWORK, Security.getUserId(), -1,
					Biz.HOMEWORK, homework.getId());
			GrowthLog growthLog = growthService.grow(GrowthAction.PUBLISH_HOMEWORK, Security.getUserId(), -1,
					Biz.HOMEWORK, homework.getId(), true);
			CoinsLog coinsLog = coinsService.earn(CoinsAction.PUBLISH_HOMEWORK, Security.getUserId(), -1, Biz.HOMEWORK,
					homework.getId());
			// 防止初次布置作业 升级，后面在给与常规奖励时返回页面的isUpgrade不正确
			if (growthLog.getHonor() != null) {
				boolean isupgrade = false;
				int upGradeCoins = 0;
				if (growthLogFirst.getHonor() != null) {
					if (growthLogFirst.getHonor().isUpgrade() && growthLogFirst.getHonor().getUpRewardCoins() != null) {
						isupgrade = true;
						upGradeCoins = growthLogFirst.getHonor().getUpRewardCoins();
					}
				} else {
					isupgrade = growthLog.getHonor().isUpgrade();
					if (isupgrade && growthLog.getHonor().getUpRewardCoins() != null) {
						upGradeCoins = growthLog.getHonor().getUpRewardCoins();
					}
				}
				data.put("userReward",
						new VUserReward(upGradeCoins, isupgrade, growthLog.getHonor().getLevel(),
								growthLog.getGrowthValue()
										+ (growthLogFirst.getGrowthValue() == -1 ? 0 : growthLogFirst.getGrowthValue()),
								coinsLog.getCoinsValue()
										+ (coinsLogFirst.getCoinsValue() == -1 ? 0 : coinsLogFirst.getCoinsValue())));
			}
			return new Value(data);
		} catch (Exception e) {
			logger.error("publish homework error:", e);
			return new Value(new ServerException());
		}
	}

	/**
	 * 作业分发给的学生作业列表(用于老师管理作业详情里面的左侧列表)
	 * 
	 * @since 小优快批 2018-02-11
	 * @param hkIdR
	 *            作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "stuhks", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentHomeworks2(long hkId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		Homework homework = hkService.get(hkId);
		if (homework == null) {
			return new Value(new EntityNotFoundException());
		}
		VHomework vHomework = hkConvert.to(homework, new HomeworkConvertOption(false));
		vHomework.setHomeworkClazz(zyHkClassConvert.to(zyHkClassService.get(homework.getHomeworkClassId())));
		data.put("homework", vHomework);
		if (vHomework.getNewHomeworkStatus() == "0") {
			// 待分发,查询出当前班级的学生
			List<Long> studentList = new ArrayList<Long>();
			if (homework.getHomeworkClassGroupId() != null && homework.getHomeworkClassGroupId() != 0) {
				studentList = zyHomeworkClassGroupStudentService.findGroupStudents(homework.getHomeworkClassId(),
						homework.getHomeworkClassGroupId());
			} else {
				studentList = hkStuClazzService.listClassStudents(homework.getHomeworkClassId());
			}
			List<VUser> userList = userConvert.mgetList(studentList);
			List<VStudentHomework> vshs = new ArrayList<VStudentHomework>();
			for (VUser user : userList) {
				VStudentHomework v = new VStudentHomework();
				v.setUser(user);
				vshs.add(v);
			}
			data.put("studentHomeworks", vshs);
		} else {
			List<StudentHomework> shs = shService.listByHomeworkOrderByJoinAt(hkId, homework.getHomeworkClassId());
			StudentHomeworkConvertOption option = new StudentHomeworkConvertOption();
			option.setInitStuHomeworkWrongAndCorrect(true);
			option.setInitStuHomeworkCorrectedAndCorrecting(true);
			option.setInitUser(true);
			List<VStudentHomework> vshs = shConvert.to(shs, option);
			if (homework.getHomeworkClassId() != null || homework.getHomeworkClassId() > 0) {
				List<VHomeworkStudentClazz> vhkStuClazzs = hkStuClazzConvert
						.to(hkStuClazzService.list(homework.getHomeworkClassId()));
				Map<Long, VHomeworkStudentClazz> vhkStuClazzMap = new HashMap<Long, VHomeworkStudentClazz>(
						vhkStuClazzs.size());
				for (VStudentHomework v : vshs) {
					v.setStudentClazz(vhkStuClazzMap.get(v.getStudentId()));
				}
			}
			data.put("studentHomeworks", vshs);
		}
		return new Value(data);
	}

	/**
	 * 作业分发给的学生作业列表(用于老师管理作业详情里面的左侧列表)
	 * 
	 * @since 2.1
	 * @param hkId
	 *            作业ID
	 * @return {@link Value}
	 */
	@Deprecated
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/stuhks", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentHomeworks(long hkId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		Homework homework = hkService.get(hkId);
		if (homework == null) {
			return new Value(new EntityNotFoundException());
		}
		VHomework vHomework = hkConvert.to(homework, new HomeworkConvertOption(false));
		vHomework.setHomeworkClazz(zyHkClassConvert.to(zyHkClassService.get(homework.getHomeworkClassId())));
		data.put("homework", vHomework);
		if (vHomework.getStatus() == HomeworkStatus.INIT) {
			// 待分发,查询出当前班级的学生
			List<Long> studentList = new ArrayList<Long>();
			if (homework.getHomeworkClassGroupId() != null && homework.getHomeworkClassGroupId() != 0) {
				studentList = zyHomeworkClassGroupStudentService.findGroupStudents(homework.getHomeworkClassId(),
						homework.getHomeworkClassGroupId());
			} else {
				studentList = hkStuClazzService.listClassStudents(homework.getHomeworkClassId());
			}
			List<VUser> userList = userConvert.mgetList(studentList);
			List<VStudentHomework> vshs = new ArrayList<VStudentHomework>();
			for (VUser user : userList) {
				VStudentHomework v = new VStudentHomework();
				v.setUser(user);
				vshs.add(v);
			}
			data.put("studentHomeworks", vshs);
		} else {
			List<StudentHomework> shs = shService.listByHomeworkOrderByJoinAt(hkId, homework.getHomeworkClassId());
			List<VStudentHomework> vshs = shConvert.to(shs, false, false, true, false);
			if (homework.getHomeworkClassId() != null || homework.getHomeworkClassId() > 0) {
				List<VHomeworkStudentClazz> vhkStuClazzs = hkStuClazzConvert
						.to(hkStuClazzService.list(homework.getHomeworkClassId()));
				Map<Long, VHomeworkStudentClazz> vhkStuClazzMap = new HashMap<Long, VHomeworkStudentClazz>(
						vhkStuClazzs.size());
				for (VStudentHomework v : vshs) {
					v.setStudentClazz(vhkStuClazzMap.get(v.getStudentId()));
				}
			}
			data.put("studentHomeworks", vshs);
		}
		// 如果是自动下发,获取下发时间
		if (homework.isAutoIssue()) {
			int issueHour = Env.getDynamicInt("homework.issued.time"); // 自动下发小时数
			Date time = hkService.getAutoIssuedTime(hkId, issueHour);
			// 等于空说明有解答题或者有没回答但有解题过程的填空题,不给页面传值
			// 并且下发时间要大于当前时间才显示
			if (time != null && time.getTime() > new Date().getTime()) {
				data.put("autoIssuedTime", time);
			}
		}
		return new Value(data);
	}

	/**
	 * 统计一次布置的作业结果
	 * 
	 * @since yoomath V1.2
	 * @param exerciseId
	 *            习题ID
	 * @return {@link Value}
	 */
	@Deprecated
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "statistic", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statistic(long exerciseId) {
		List<Homework> homeworks = zyHomeworkService.findByExercise(Security.getUserId(),
				Lists.newArrayList(exerciseId));
		List<Homework> ps = new ArrayList<Homework>(homeworks.size());
		for (Homework homework : homeworks) {
			if (homework.getStatus() == HomeworkStatus.ISSUED) {
				ps.add(homework);
			}
		}
		List<VHomework> vs = hkConvert.to(ps, new HomeworkConvertOption(false));
		if (CollectionUtils.isNotEmpty(vs)) {
			List<Long> hkIds = new ArrayList<Long>(vs.size());
			List<Long> classIds = new ArrayList<Long>(vs.size());
			for (VHomework v : vs) {
				hkIds.add(v.getId());
				classIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = hkClassConvert.to(hkClassService.mget(classIds));
			for (VHomework v : vs) {
				v.setHomeworkClazz(vmap.get(v.getHomeworkClazzId()));
			}
			Map<Long, List<VHomeworkQuestion>> map = new HashMap<Long, List<VHomeworkQuestion>>(hkIds.size());
			for (Long hkId : hkIds) {
				map.put(hkId, new ArrayList<VHomeworkQuestion>());
			}
			List<VHomeworkQuestion> homeworkQuestions = hqConvert.to(hkQuestionService.findByHomework(hkIds));
			for (VHomeworkQuestion v : homeworkQuestions) {
				map.get(v.getHomeworkId()).add(v);
			}
			for (VHomework v : vs) {
				v.setHomeworkQuestions(map.get(v.getId()));
			}
		}
		return new Value(vs);
	}

	/**
	 * 作业总体的结果列表
	 * 
	 * @since 2.1
	 * @param hkId
	 *            作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "hk_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value homeworkQuestions(long hkId) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		Homework homework = hkService.get(hkId);
		map.put("homework", hkConvert.to(homework));
		List<HomeworkQuestion> hqs = hqService.getHomeworkQuestion(hkId);
		List<VHomeworkQuestion> vhqs = hqConvert.to(hqs);
		map.put("homeworkQuestions", vhqs);
		return new Value(map);
	}

	/**
	 * 学生作业题目列表(批改、查看结果时使用)<br>
	 * yoomath V1.4返回订正作业的题目(题目ID查询需要优化)
	 * 
	 * @since 2.1
	 * @param stuHomeworkId
	 * @param codes
	 *            知识点集合 学生作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "stuhk_questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentHomeworkquestions(long stuHomeworkId,
			@RequestParam(value = "codes", required = false) List<Long> codes,
			@RequestParam(value = "isNewKp", required = false) Boolean isNewKp) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		StudentHomework studentHomework = shService.get(stuHomeworkId);
		StudentHomeworkConvertOption option = new StudentHomeworkConvertOption();
		option.setInitHomework(true);
		option.setStatisticTobeCorrected(true);
		option.setInitStuHomeworkCorrectedAndCorrecting(true);
		VStudentHomework v = shConvert.to(studentHomework, option);
		data.put("studentHomework", v);
		List<Long> qid = hqService.getQuestion(studentHomework.getHomeworkId());
		List<Long> correctQIds = stuHkQuestionService.getNewCorrectQuestions(stuHomeworkId);
		qid.addAll(correctQIds);
		List<Question> qs = new ArrayList<Question>(qid.size());
		Map<Long, Question> qsMap = questionService.mget(qid);
		for (Long id : qid) {
			if (correctQIds.contains(id)) {
				qsMap.get(id).setCorrectQuestion(true);
			}
			qs.add(qsMap.get(id));
		}
		List<VQuestion> vqs = questionConvert.to(qs,
				new QuestionConvertOption(false, true, true, false, true, stuHomeworkId));
		data.put("questions", vqs);
		if (CollectionUtils.isNotEmpty(codes)) {
			List<Map> kpList = new ArrayList<Map>();
			// 新知识点
			if (isNewKp) {
				List<DiagnosticStudentClassKnowpoint> list = diagStuKpService.queryHistoryWeakListByCodes(
						studentHomework.getStudentId(), v.getHomework().getHomeworkClazzId(), codes);
				data.put("weakList", diagStuKpConvert.to(list));
			} else {
				// 旧知识点
				kpList = stuHkQuestionService.findStuQuestionMapByOldCodes(stuHomeworkId, codes);
				// 知识点总数对应的map
				Map<Long, Integer> totalMap = new HashMap<Long, Integer>();
				Map<Long, Integer> rightMap = new HashMap<Long, Integer>();
				Map<Long, Integer> rateMap = new HashMap<Long, Integer>();
				for (Map m : kpList) {
					Long code = Long.parseLong(String.valueOf(m.get("code")));
					Integer result = Integer.parseInt(String.valueOf(m.get("result")));
					Integer count = Integer.parseInt(String.valueOf(m.get("count")));
					if (CollectionUtils.isEmpty(totalMap)) {
						totalMap.put(code, count);
					} else {
						totalMap.put(code, totalMap.get(code) != null ? totalMap.get(code) + count : count);
					}
					if (result == HomeworkAnswerResult.RIGHT.getValue()) {
						if (CollectionUtils.isEmpty(rightMap)) {
							rightMap.put(code, count);
						} else {
							rightMap.put(code, rightMap.get(code) != null ? rightMap.get(code) + count : count);
						}
					}
				}
				// 正确率取整
				for (Long code : totalMap.keySet()) {
					rateMap.put(code, (rightMap.get(code) == null ? 1 : (rightMap.get(code) + 1)) * 100
							/ (totalMap.get(code) + 2));
				}
				List<Long> kpCodes = new ArrayList<Long>();
				List<Integer> kpCodes2 = new ArrayList<Integer>();
				for (Long code : rateMap.keySet()) {
					if (rateMap.get(code) <= 50) {
						kpCodes.add(code);
						kpCodes2.add(code.intValue());
					}
				}
				data.put("weakList", metaKnowpointConvert.to(metaKnowpointService.mgetList(kpCodes2)));
			}

		}
		return new Value(data);
	}

	/**
	 * 批改题目
	 * 
	 * @since 1.9.1 (支持简单题的批改)
	 * @since 小优快批，2018-2-12
	 * 
	 * @param stuHkId
	 *            学生作业ID
	 * @param stuHkQId
	 *            学生作业题目ID
	 * @param result
	 *            批改结果
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/correct", method = { RequestMethod.POST, RequestMethod.GET })
	public Value correct2(long stuHkId, long stuHkQId, HomeworkAnswerResult result, BigDecimal rightRate, Type type,
			Integer questionRate, @RequestParam(required = false) List<HomeworkAnswerResult> itemResults,
			@RequestParam(required = false) Long questionId) {

		StudentHomework stuHk = shService.get(stuHkId);
		if (stuHk == null || stuHk.getStatus() == StudentHomeworkStatus.NOT_SUBMIT
				|| stuHk.getStatus() == StudentHomeworkStatus.ISSUED) {
			// 没有提交或已经下发的作业不能批改
			return new Value(new NoPermissionException());
		}

		// 批改对象
		// 注意此处不再使用从前台传输整份作业的正确率
		QuestionCorrectObject questionCorrectObject = new QuestionCorrectObject();
		questionCorrectObject.setStudentHomeworkId(stuHkId);
		questionCorrectObject.setStuHomeworkQuestionId(stuHkQId);
		if (type == null) {
			StudentHomeworkQuestion studentHomeworkQuestion = stuHkQuestionService.get(stuHkQId);
			type = studentHomeworkQuestion.getType();
		}
		questionCorrectObject.setQuestionType(type);
		questionCorrectObject.setQuestionResult(result);
		questionCorrectObject.setQuestionRightRate(questionRate == null ? null : questionRate.intValue());

		if (type == Type.FILL_BLANK) {
			// 填空题
			List<StudentHomeworkAnswer> list = studentHomeworkAnswerService.find(stuHkQId);
			if (itemResults == null || list.size() != itemResults.size()) {
				return new Value(new MissingArgumentException());
			}
			Map<Long, HomeworkAnswerResult> answerResultMap = new HashMap<Long, HomeworkAnswerResult>(
					itemResults.size());
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					answerResultMap.put(list.get(i).getId(), itemResults.get(i));
				}
			}
			questionCorrectObject.setAnswerResultMap(answerResultMap);
		}

		// 调用批改处理
		correctProcessor.correctStudentHomeworkQuestion(Security.getUserId(), CorrectorType.TEACHER,
				questionCorrectObject);

		// 多空题批改返回批改后的question对象，便于更新多空对错
		if (type == Type.FILL_BLANK) {
			VQuestion q = questionConvert.to(questionService.get(questionId),
					new QuestionConvertOption(false, true, true, false, true, stuHkId));
			return new Value(q);
		}
		return new Value();
	}

	/**
	 * 下发作业（废弃）
	 * 
	 * @since 2.1
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 * 
	 * @param hkId
	 *            作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "issue", method = { RequestMethod.POST, RequestMethod.GET })
	@Deprecated
	public Value issue(long hkId) {
		try {
			Homework homework = hkService.get(hkId);
			if (homework.getCreateId() != Security.getUserId()) {
				return new Value(new PageNotFoundException());
			}
			if (homework.getStatus() == HomeworkStatus.INIT) {
				return new Value(new PageNotFoundException());
			} else if (homework.getStatus() == HomeworkStatus.ISSUED) {
				return new Value();
			}
			zyHomeworkService.issue(homework, false);
			// 处理相关统计
			hkStatisticService.staticAfterIssue(hkId);
			hkStatisticService.asyncStaticHomework(hkId);
			// 发送推送消息
			mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
					MQ.builder().data(new PushHandleForm(PushHandleAction.ISSUED_HOMEWORK, hkId)).build());
			// 微信提醒
			zyWXMessageService.sendIssuedHomeworkMessage(hkId);
			return new Value();
		} catch (HomeworkException e) {
			return new Value(e);
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 查询历史作业
	 * 
	 * @since 2.1
	 * @param sectionCode
	 *            章节代码
	 * @param pageNo
	 *            页码
	 * @param size
	 *            数量
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query_history", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHistory(long sectionCode, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "40") int size) {
		size = Math.min(40, size);
		Page<Exercise> exercisePage = zyExerciseService.query(Security.getUserId(), sectionCode, P.index(pageNo, size));
		List<VHomework> vhomeworks = Lists.newArrayList();
		VHomeworkPage vpage = new VHomeworkPage();
		if (exercisePage.isNotEmpty()) {
			List<Long> exerciseIds = Lists.newArrayList();
			for (Exercise exercise : exercisePage) {
				exerciseIds.add(exercise.getId());
			}
			List<Homework> homeworks = zyHomeworkService.findByExercise(Security.getUserId(), exerciseIds);
			List<VHomework> vs = hkConvert.to(homeworks, new HomeworkConvertOption(true));
			long exerciseId = -1L;
			int exerciseHomeworkCount = 0;
			for (VHomework v : vs) {
				int vsize = vhomeworks.size();
				VHomework vh = null;
				if (vsize > 0) {
					vh = vhomeworks.get(vsize - 1);
				}
				if (v.getExerciseId() == exerciseId) {
					vh.setDistributeCount(vh.getDistributeCount() + v.getDistributeCount());
					if (v.getStatus() == HomeworkStatus.ISSUED) {
						vh.setWrongCount(vh.getWrongCount() + v.getWrongCount());
						vh.setRightCount(vh.getRightCount() + v.getRightCount());
						vh.setRightRate(BigDecimal
								.valueOf((vh.getRightCount() * 100f) / (vh.getRightCount() + vh.getWrongCount()))
								.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					vh.setHomeworkTime(vh.getHomeworkTime() + v.getHomeworkTime());
					// 设置状态,将作业状态设置为status最大的
					if (vh.getStatus().getValue() > v.getStatus().getValue()) {
						vh.setStatus(v.getStatus());
					}
					vhomeworks.set(vsize - 1, vh);

					exerciseHomeworkCount++;

				} else {
					if (exerciseHomeworkCount > 1) {
						vh.setHomeworkTime(vh.getHomeworkTime() / exerciseHomeworkCount);
						vhomeworks.set(vsize - 1, vh);
					}
					exerciseHomeworkCount = 1;
					vhomeworks.add(v);
				}
				exerciseId = v.getExerciseId();
			}
		}
		vpage.setCurrentPage(pageNo);
		vpage.setItems(vhomeworks);
		vpage.setPageSize(size);
		vpage.setTotal(exercisePage.getTotalCount());
		vpage.setTotalPage(exercisePage.getPageCount());
		return new Value(vpage);
	}

	/**
	 * 查看历史作业详情
	 * 
	 * @since 2.1
	 * @param exerciseId
	 *            作业里面的exerciseId
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query_history_detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value homeworkHistoryDetail(long exerciseId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		// put homework
		List<Homework> homeworks = zyHomeworkService.findByExercise(Security.getUserId(),
				Lists.newArrayList(exerciseId));
		List<VHomework> vs = hkConvert.to(homeworks, new HomeworkConvertOption(true));
		List<Long> homeworkIds = new ArrayList<Long>(homeworks.size());
		int index = 0;
		VHomework vhomework = null;
		for (VHomework v : vs) {
			homeworkIds.add(v.getId());
			if (index == 0) {
				vhomework = v;
			} else {
				vhomework.setDistributeCount(vhomework.getDistributeCount() + v.getDistributeCount());
				if (v.getStatus() == HomeworkStatus.ISSUED) {
					vhomework.setWrongCount(vhomework.getWrongCount() + v.getWrongCount());
					vhomework.setRightCount(vhomework.getRightCount() + v.getRightCount());
					vhomework.setRightRate(BigDecimal
							.valueOf((vhomework.getRightCount() * 100f)
									/ (vhomework.getRightCount() + vhomework.getWrongCount()))
							.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				vhomework.setHomeworkTime(vhomework.getHomeworkTime() + v.getHomeworkTime());
				// 设置状态,将作业状态设置为status最大的
				if (vhomework.getStatus().getValue() < v.getStatus().getValue()) {
					vhomework.setStatus(v.getStatus());
				}
			}
			index++;
		}
		vhomework.setHomeworkTime(vhomework.getHomeworkTime() / index);
		data.put("homework", vhomework);
		// put question list
		List<HomeworkQuestion> homeworkQuestions = hkQuestionService.findByHomework(homeworkIds);
		List<VHomeworkQuestion> vhks = Lists.newArrayList();
		List<VHomeworkQuestion> vhkQuestions = hqConvert.to(homeworkQuestions,
				new HomeworkQuestionConvertOption(false, false));
		long questionId = -1L;
		for (VHomeworkQuestion v : vhkQuestions) {
			int vsize = vhks.size();
			VHomeworkQuestion vh = null;
			if (vsize > 0) {
				vh = vhks.get(vsize - 1);
			}
			if (v.getQuestionId() == questionId) {
				if (vhomework.getStatus() == HomeworkStatus.ISSUED) {
					vh.setWrongCount(vh.getWrongCount() + v.getWrongCount());
					vh.setRightCount(vh.getRightCount() + v.getRightCount());
					vh.setRightRate(
							BigDecimal.valueOf((vh.getRightCount() * 100f) / (vh.getRightCount() + vh.getWrongCount()))
									.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				vhks.set(vsize - 1, vh);
			} else {
				vhks.add(v);
			}
			questionId = v.getQuestionId();
		}
		data.put("homeworkQuestions", vhks);
		return new Value(data);
	}

	/**
	 * 根据作业参数生成作业
	 * 
	 * @since 2.1
	 * @param form
	 *            生成作业参数
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "generate_by_param", method = { RequestMethod.POST, RequestMethod.GET })
	public Value generateHomeworkByParam(GenerateHomeworkForm form) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		int count = form.getImprove() + form.getSimple() + form.getDifficult();
		if (form.getSectionCode() == null || count == 0 || count > 25
				|| (CollectionUtils.isEmpty(form.getMetaKnowpoints())
						&& CollectionUtils.isEmpty(form.getKnowledgePoints()))) {
			return new Value(new IllegalArgException());
		}
		PaperParam param = new PaperParam();
		param.setCount(count);
		param.setDifficult(form.getDifficult());
		param.setImprove(form.getImprove());
		param.setSimple(form.getSimple());
		param.setMetaKnowpoints(form.getMetaKnowpoints());
		param.setKnowledgePoints(form.getKnowledgePoints());
		param.setVersion(form.getVersion());
		Paper paper = paperBuilder.generate(param);
		List<Question> qs = new ArrayList<Question>(count);
		Map<Long, Question> questions = questionService.mget(paper.getIds());
		for (Long qid : paper.getIds()) {
			qs.add(questions.get(qid));
		}
		List<VQuestion> vqs = questionConvert.to(qs, new QuestionConvertOption(false, true, true, false, true, null));
		Collections.sort(vqs, new VQuestionComparator());
		data.put("questions", vqs);
		// 此章节下面的元知识点
		List<Integer> codes = metaKnowpointSectionService.findBySectionCode(form.getSectionCode());
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
		List<Long> knowledgeCodes = knowledgeSectionService.findBySectionCode(form.getSectionCode());
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

		return new Value(data);
	}

	/**
	 * 根据历史作业ID生成新的作业
	 * 
	 * @since 2.1
	 * @since yoomath V2.1.2 新旧知识点更换 wanlong.che 2016-11-23
	 * @param exerciseId
	 *            作业里面的exerciseId
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "generate_by_history", method = { RequestMethod.POST, RequestMethod.GET })
	public Value generateHomeworkByHistory(long exerciseId) {
		Exercise exercise = exerciseService.get(exerciseId);
		Map<String, Object> data = new HashMap<String, Object>(2);
		// 返回题目
		List<Long> qIds = exerciseQuestionService.getQuestion(exerciseId);
		if (qIds.size() == 0) {
			data.put("questions", Collections.EMPTY_LIST);
		} else {
			Map<Long, Question> questions = questionService.mget(qIds);
			Map<Long, VQuestion> vquestions = questionConvert.to(questions,
					new QuestionConvertOption(false, true, true, false, true, null));
			List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
			for (Long qId : qIds) {
				qs.add(vquestions.get(qId));
			}
			data.put("questions", qs);
		}
		// 返回作业参数
		if (exercise != null) {
			Homework h = zyHomeworkService.getLatextHomeworkByExerciseId(Security.getUserId(), exercise.getId());
			if (h != null && CollectionUtils.isNotEmpty(h.getMetaKnowpoints())) {
				List<Integer> codes = new ArrayList<Integer>(h.getMetaKnowpoints().size());
				for (Long code : h.getMetaKnowpoints()) {
					codes.add(code.intValue());
				}
				List<MetaKnowpoint> knowpoints = metaKnowpointService.mgetList(codes);
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
			}
		}
		// 返回此章节下面的知识点
		TextbookExercise tbe = tbeService.get(exercise.getTextbookExerciseId());
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
	 * 再次布置获得数据接口
	 *
	 * @param homeworkId
	 *            作业id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "publishAgainData", method = { RequestMethod.GET, RequestMethod.POST })
	public Value publishAgain(long homeworkId) {
		Homework homework = hkService.get(homeworkId);
		List<HomeworkQuestion> questions = hqService.getHomeworkQuestion(homeworkId);
		Map<String, Object> retMap = new HashMap<String, Object>();

		List<Long> questionIds = new ArrayList<Long>(questions.size());
		for (HomeworkQuestion q : questions) {
			questionIds.add(q.getQuestionId());
		}

		retMap.put("questionIds", questionIds);
		retMap.put("questions", questionService.mgetList(questionIds));

		List<Long> knowledgePoints = homework.getKnowledgePoints();
		if (CollectionUtils.isEmpty(knowledgePoints)) {
			List<QuestionKnowledge> questionKnowledges = qkService.findByQuestions(questionIds);

			knowledgePoints = new ArrayList<Long>(questionKnowledges.size());
			for (QuestionKnowledge qk : questionKnowledges) {
				knowledgePoints.add(qk.getKnowledgeCode());
			}
		}

		List<KnowledgePoint> kps = knowledgePointService.mgetList(knowledgePoints);
		List<VKnowledgePoint> vkps = knowledgePointConvert.to(kps);

		retMap.put("kps", vkps);

		List<HomeworkClazz> clazzs = hkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			retMap.put("clazzs", Collections.EMPTY_LIST);
		} else {
			retMap.put("clazzs", hkClassConvert.to(clazzs));
		}
		int issueHour = Env.getDynamicInt("homework.issued.time"); // 自动下发小时数
		retMap.put("issueHour", issueHour);

		return new Value(retMap);
	}

	/**
	 * 关闭自动下发
	 * 
	 * @param hkId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "closeAutoIssued", method = { RequestMethod.GET, RequestMethod.POST })
	public Value closeAutoIssued(long hkId) {
		hkService.closeAutoIssued(hkId);
		return new Value();
	}

	/**
	 * 进入解答题批改页面
	 * 
	 * @since 小优快批 2018-02-13
	 * @param hkId
	 *            homewrokId(班级作业id)
	 * @param questionId
	 *            题目id
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "preCorrect", method = { RequestMethod.POST, RequestMethod.GET })
	public Value preCorrect(long hkId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		Homework homework = hkService.get(hkId);
		if (homework == null) {
			return new Value(new EntityNotFoundException());
		}
		// 已提交的学生作业
		List<StudentHomework> stuHomeworks = shService.findSubmitedStuHomeworks(hkId, homework.getHomeworkClassId());
		StudentHomeworkConvertOption option = new StudentHomeworkConvertOption();
		option.setInitUser(true);
		List<VStudentHomework> vstuHomeworks = shConvert.to(stuHomeworks, option);
		// 获取该班级作业下的所有解答题
		List<Long> questionIds = hqService.findHomeworkQuestionsByType(hkId, 5);
		data.put("studentHomeworks", vstuHomeworks);
		data.put("questionIds", questionIds);
		return new Value(data);
	}

	/**
	 * 批改解答题，下一题
	 * 
	 * @since 小优快批 2018-02-13
	 * @param stuhkId
	 * @param questionId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "gotoNext", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getNext(long stuhkId, long questionId) {
		List<StudentHomeworkQuestion> stuhQuestions = stuHkQuestionService.listByStuHomeworkIdAndQuestionId(stuhkId,
				questionId);
		List<VStudentHomeworkQuestion> list = stuhQuestionConvert.to(stuhQuestions);
		VStudentHomeworkQuestion vSthquestion = null;
		if (null != list && list.size() > 0) {
			Question question = questionService.get(questionId);
			VQuestion vq = questionConvert.to(question, new QuestionConvertOption(true, true, true, null));
			vSthquestion = list.get(0);
//			vSthquestion.setQuestion(vq);
		}
		return new Value(vSthquestion);
	}

}
