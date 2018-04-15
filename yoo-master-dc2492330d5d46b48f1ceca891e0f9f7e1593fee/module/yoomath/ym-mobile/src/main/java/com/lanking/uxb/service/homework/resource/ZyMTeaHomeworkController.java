package com.lanking.uxb.service.homework.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.ex.code.StatusCode;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.homework.value.VHomeworkPage;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.ExerciseService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.web.resource.ZyTeaHomework2Controller;
import com.lanking.uxb.service.web.resource.ZyTeaHomeworkController;
import com.lanking.uxb.service.web.util.VQuestionComparator;
import com.lanking.uxb.service.zuoye.api.Paper;
import com.lanking.uxb.service.zuoye.api.PaperBuilder;
import com.lanking.uxb.service.zuoye.api.PaperParam;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.form.GenerateHomeworkForm;
import com.lanking.uxb.service.zuoye.form.PublishHomeworkForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 教师端作业相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月12日
 */
@RestController
@RequestMapping("zy/m/t/hk")
public class ZyMTeaHomeworkController {

	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;
	@Autowired
	private ZyTeaHomework2Controller teaHomework2Controller;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private ZyTeaHomeworkController teaHomeworkController;
	@Autowired
	private PaperBuilder paperBuilder;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private HomeworkQuestionService hkQuestionService;
	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private UserActionService userActionService;

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(Long endTime, Integer pageNo, @RequestParam(value = "size", defaultValue = "20") int size) {
		pageNo = pageNo == null ? 1 : (pageNo.intValue() <= 0 ? 1 : pageNo);
		endTime = endTime == null ? System.currentTimeMillis() : endTime;
		VHomeworkPage vpage = new VHomeworkPage();
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setEndTime(new Date(endTime));
		Page<Homework> homeworkPage = zyHkService.queryForMobile(query, P.index(pageNo, Math.min(size, 20)));
		if (homeworkPage.isNotEmpty()) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
			List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs);
			Map<Long, VHomeworkClazz> vclazzMap = new HashMap<Long, VHomeworkClazz>(vclazzs.size());
			for (VHomeworkClazz v : vclazzs) {
				vclazzMap.put(v.getId(), v);
			}
			List<VHomework> homeworks = homeworkConvert.to(homeworkPage.getItems());
			for (VHomework v : homeworks) {
				v.setHomeworkClazz(vclazzMap.get(v.getHomeworkClazzId()));
			}
			vpage.setItems(homeworks);
		} else {
			vpage.setItems(Collections.EMPTY_LIST);
		}
		vpage.setCurrentPage(pageNo);
		vpage.setEndTime(endTime);
		vpage.setPageSize(20);
		vpage.setTotal(homeworkPage.getTotalCount());
		vpage.setTotalPage(homeworkPage.getPageCount());
		return new Value(vpage);
	}

	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(String password, long id) {
		Value value = teaHomework2Controller.delete(password, id);
		if (value.getRet_code() == AccountException.ACCOUNT_PASSWORD_WRONG) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PASSWORD_WRONG));
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(Integer textbookCode) {
		if (Security.getDeviceType() == DeviceType.IOS
				&& Integer.valueOf(Security.getVersion().replaceAll("\\.", "")) <= 117) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IOS_UNCORRECT_CLIENT_VERSION));
		}
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (teacher.getTextbookCode() == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_NEED_FILL_PROFILE));
		}
		ValueMap vm = ValueMap.value();
		// 可选教材列表
		List<Textbook> tbs = tbService.find(Product.YOOMATH, teacher.getPhaseCode(), teacher.getSubjectCode(),
				teacher.getTextbookCategoryCode());
		vm.put("textbooks", tbConvert.to(tbs));

		if (textbookCode == null) {
			textbookCode = teacher.getTextbookCode();
		}
		vm.put("textbookCode", textbookCode);
		Map<String, Object> data = ((Map<String, Object>) teaHomeworkController.sectionTree(textbookCode).getRet());
		vm.put("sections", data.get("sections"));
		return new Value(vm);
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree(int textbookCode) {
		ValueMap vm = ValueMap.value();
		Map<String, Object> data = ((Map<String, Object>) teaHomeworkController.sectionTree(textbookCode).getRet());
		vm.put("sections", data.get("sections"));
		return new Value(vm);
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions(Long textbookExerciseId, Long sectionCode) {
		textbookExerciseId = textbookExerciseId == null ? 0 : textbookExerciseId;
		Value value = teaHomework2Controller.listQuestions4(textbookExerciseId, sectionCode, false);
		Map<String, Object> data = ((Map<String, Object>) value.getRet());
		ValueMap vm = ValueMap.value();
		List<VQuestion> questions = (List<VQuestion>) data.get("questions");
		vm.put("questions", questions);
		// 章节包含知识点
		List<VMetaKnowpoint> metaKnowpoints = (List<VMetaKnowpoint>) data.get("metaKnowpoints");
		List<Integer> metaKnowpointCodeList = Lists.newArrayList();
		for (VMetaKnowpoint v : metaKnowpoints) {
			metaKnowpointCodeList.add(v.getCode());
		}
		vm.put("metaKnowpoints", metaKnowpoints);
		vm.put("metaKnowpointCodes", metaKnowpointCodeList);

		List<VMetaKnowpoint> pMetaKnowpoints = Lists.newArrayList();
		List<Integer> pMetaKnowpointCodeList = Lists.newArrayList();
		Double minDiff = Double.valueOf(0);
		Double maxDiff = Double.valueOf(1);
		BigDecimal difficulty = BigDecimal.valueOf(0);
		// 作业知识点参数
		if (data.containsKey("pMetaKnowpoints")) {
			List<VMetaKnowpoint> $pMetaKnowpoints = (List<VMetaKnowpoint>) data.get("pMetaKnowpoints");
			for (VMetaKnowpoint v : $pMetaKnowpoints) {
				if (metaKnowpointCodeList.contains(v.getCode()) && !pMetaKnowpointCodeList.contains(v.getCode())) {
					pMetaKnowpoints.add(v);
					pMetaKnowpointCodeList.add(v.getCode());
				}
			}
		} else {
			pMetaKnowpoints = metaKnowpoints;
			pMetaKnowpointCodeList = metaKnowpointCodeList;
		}
		vm.put("pMetaKnowpoints", pMetaKnowpoints);
		vm.put("pMetaKnowpointCodes", pMetaKnowpointCodeList);

		// 新知识点相关数据处理
		List<VKnowledgePoint> knowledgePoints = (List<VKnowledgePoint>) data.get("knowledgePoints");
		List<Long> knowledgePointCodeList = new ArrayList<Long>(knowledgePoints.size());
		for (VKnowledgePoint p : knowledgePoints) {
			knowledgePointCodeList.add(p.getCode());
		}
		vm.put("knowledgePoints", knowledgePoints);
		vm.put("knowledgePointCodes", knowledgePointCodeList);

		List<VKnowledgePoint> pKnowledgePoints = Lists.newArrayList();
		List<Long> pKnowledgePointCodes = Lists.newArrayList();
		if (data.containsKey("pNewMetaKnowpoints")) {
			List<VKnowledgePoint> $pKnowledgePoints = (List<VKnowledgePoint>) data.get("pNewMetaKnowpoints");
			for (VKnowledgePoint v : $pKnowledgePoints) {
				if (knowledgePointCodeList.contains(v.getCode()) && !pKnowledgePointCodes.contains(v.getCode())) {
					pKnowledgePoints.add(v);
					pKnowledgePointCodes.add(v.getCode());
				}
			}
		} else {
			pKnowledgePoints = knowledgePoints;
			pKnowledgePointCodes = knowledgePointCodeList;
		}

		vm.put("pKnowledgePoints", pKnowledgePoints);
		vm.put("pKnowledgePointCodes", pKnowledgePointCodes);

		// 其他参数
		vm.put("questionCount", questions.size());
		for (VQuestion vq : questions) {
			if (minDiff == null) {
				minDiff = vq.getDifficulty();
			} else {
				minDiff = Math.min(minDiff, vq.getDifficulty());
			}
			if (maxDiff == null) {
				maxDiff = vq.getDifficulty();
			} else {
				maxDiff = Math.max(maxDiff, vq.getDifficulty());
			}
			difficulty = difficulty.add(BigDecimal.valueOf(vq.getDifficulty()));
		}
		vm.put("minDiff", BigDecimal.valueOf(minDiff).setScale(1, BigDecimal.ROUND_HALF_UP));
		vm.put("maxDiff", BigDecimal.valueOf(maxDiff).setScale(1, BigDecimal.ROUND_HALF_UP));
		if (CollectionUtils.isEmpty(questions)) {
			vm.put("difficulty", BigDecimal.valueOf(0.0));
		} else {
			vm.put("difficulty", difficulty.divide(BigDecimal.valueOf(questions.size()), 1, BigDecimal.ROUND_HALF_UP));
		}
		return new Value(vm);
	}

	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "pullQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value pullQuestions(PullQuestionForm form) {
		if (form.getTextbookExerciseId() == null) {
			form.setTextbookExerciseId(0L);
		}
		Value value = teaHomeworkController.pullQuestions2(form);
		if (value.getRet_code() == StatusCode.SUCCEED
				&& (value.getRet() == null || ((List) value.getRet()).size() == 0)) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CAN_NOT_PULL_QUESTIONS));
		}
		return value;
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "generateHomeworkByParam", method = { RequestMethod.POST, RequestMethod.GET })
	public Value generateHomeworkByParam(GenerateHomeworkForm form) {
		if (form.getCount() == null || form.getCount() > 25 || form.getMinDifficulty() == null
				|| form.getMaxDifficulty() == null || (CollectionUtils.isEmpty(form.getMetaKnowpoints())
						&& CollectionUtils.isEmpty(form.getKnowledgePoints()))) {
			return new Value(new IllegalArgException());
		}

		PaperParam param = new PaperParam();
		param.setCount(form.getCount());
		param.setMetaKnowpoints(form.getMetaKnowpoints());
		param.setKnowledgePoints(form.getKnowledgePoints());
		param.setMinDifficulty(form.getMinDifficulty());
		param.setMaxDifficulty(form.getMaxDifficulty());
		param.setMobile(true);
		Paper paper = paperBuilder.generate(param);

		ValueMap vm = ValueMap.value();

		List<Question> qs = new ArrayList<Question>(paper.getIds().size());
		Map<Long, Question> questions = questionService.mget(paper.getIds());
		for (Long qid : paper.getIds()) {
			qs.add(questions.get(qid));
		}
		QuestionConvertOption questionConvertOption = new QuestionConvertOption(false, true, true, null);
		questionConvertOption.setInitQuestionTag(true);
		List<VQuestion> vqs = questionConvert.to(qs, questionConvertOption);
		Collections.sort(vqs, new VQuestionComparator());
		vm.put("questions", vqs);

		BigDecimal difficulty = BigDecimal.valueOf(0);
		for (VQuestion vq : vqs) {
			difficulty = difficulty.add(BigDecimal.valueOf(vq.getDifficulty()));
		}
		vm.put("difficulty", difficulty.divide(BigDecimal.valueOf(vqs.size()), 1, BigDecimal.ROUND_HALF_UP));
		vm.put("count", vqs.size());
		vm.put("recommend", paper.isRecommend());
		return new Value(vm);
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "publishData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishData(Long textbookExerciseId, Long sectionCode) {
		ValueMap vm = ValueMap.value();
		textbookExerciseId = textbookExerciseId == null ? 0 : textbookExerciseId;
		if (textbookExerciseId > 0 || sectionCode != null) {
			vm.put("name", zyHkService.getHomeworkName(Security.getUserId(), textbookExerciseId, sectionCode, false));
		}
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			vm.put("clazzs", Collections.EMPTY_LIST);
		} else {
			List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs);
			List<VHomeworkClazz> emptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			List<VHomeworkClazz> notEmptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			for (VHomeworkClazz v : vclazzs) {
				if (v.getStudentNum() <= 0) {
					emptyClazz.add(v);
				} else {
					notEmptyClazz.add(v);
				}
			}

			notEmptyClazz.addAll(emptyClazz);
			vm.put("clazzs", notEmptyClazz);
		}
		vm.put("autoIssueHour", Env.getDynamicInt("homework.issued.time"));
		return new Value(vm);
	}

	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "publish", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publish(PublishHomeworkForm form) {
		if (Security.getDeviceType() == DeviceType.IOS
				&& Integer.valueOf(Security.getVersion().replaceAll("\\.", "")) <= 117) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IOS_UNCORRECT_CLIENT_VERSION));
		}
		if (StringUtils.getJsUnicodeLength(form.getName()) > 40) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_NAME_TOO_LONG));
		}
		Value value = teaHomeworkController.publish2(form);
		// 用户动作处理
		userActionService.asyncAction(UserAction.PUBLISH_HOMEWORK, Security.getUserId(), null);
		return value;
	}

	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "publishByHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishByHistory(PublishHomeworkForm form) {
		if (form.getHomeworkId() == null || form.getHomeworkId() < 0) {
			return new Value(new IllegalArgException());
		}
		Homework hk = hkService.get(form.getHomeworkId());
		if (hk == null || hk.getCreateId().longValue() != Security.getUserId()) {
			return new Value(new IllegalArgException());
		}
		Exercise exercise = exerciseService.get(hk.getExerciseId());
		form.setTextbookExerciseId(exercise.getTextbookExerciseId());
		form.setBookId(exercise.getBookId());
		form.setSectionCode(exercise.getSectionCode());
		List<Long> qIds = hkQuestionService.getQuestion(form.getHomeworkId());
		form.setqIds(qIds);
		form.setMetaKnowpoints(hk.getMetaKnowpoints());
		form.setKnowledgePoints(hk.getKnowledgePoints());
		form.setDifficulty(hk.getDifficulty());
		Value value = teaHomeworkController.publish2(form);
		return value;
	}
}
