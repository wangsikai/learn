package com.lanking.uxb.service.holidayActivity01.resource;

import java.math.BigDecimal;
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

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Class;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ClassUser;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseQuestion;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseType;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Grade;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Homework;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01UserCategoryGrade;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.activity.api.HolidayActivity01ClassService;
import com.lanking.uxb.service.activity.api.HolidayActivity01ClassUserService;
import com.lanking.uxb.service.activity.api.HolidayActivity01ExerciseQuestionService;
import com.lanking.uxb.service.activity.api.HolidayActivity01ExerciseService;
import com.lanking.uxb.service.activity.api.HolidayActivity01HomeworkService;
import com.lanking.uxb.service.activity.api.HolidayActivity01Service;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserCategoryGradeService;
import com.lanking.uxb.service.activity.cache.HolidayActivity01GradeCacheService;
import com.lanking.uxb.service.activity.convert.HolidayActivity01ExerciseConvert;
import com.lanking.uxb.service.activity.form.HolidayActivity01ExerciseForm;
import com.lanking.uxb.service.activity.value.VHolidayActivity01Exercise;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

@RestController
@RequestMapping("zy/m/activity/holiday01/exercise")
public class ZyMHoldiayActivity01ExerciseController {
	@Autowired
	private HolidayActivity01ExerciseService holidayActivity01ExerciseService;
	@Autowired
	private HolidayActivity01ExerciseConvert holidayActivity01ExerciseConvert;
	@Autowired
	private HolidayActivity01ExerciseQuestionService holidayActivity01ExerciseQuestionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private HolidayActivity01HomeworkService homeworkService;
	@Autowired
	private UserService userService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;
	@Autowired
	private HolidayActivity01ClassService holidayActivity01ClassService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHomeworkStudentClazzService;
	@Autowired
	private HolidayActivity01ClassUserService holidayActivity01ClassUserService;
	@Autowired
	private HolidayActivity01GradeCacheService gradeCacheService;
	@Autowired
	private HolidayActivity01Service holidayActivity01Service;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private HolidayActivity01UserCategoryGradeService holidayActivity01UserCategoryGradeService;
	
	/**
	 * 练习列表
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(Long activityCode, Integer categoryCode, HolidayActivity01Grade grade,
			HolidayActivity01ExerciseType type, @RequestParam(defaultValue = "0") Integer cursor,
			@RequestParam(defaultValue = "200") Integer size) {
		if (type == null) {
			return new Value(new MissingArgumentException());
		}
		
		Integer currentCategory = null;
		Integer currentGrade = null;
		
		HolidayActivity01UserCategoryGrade userCategoryGrade = holidayActivity01UserCategoryGradeService.get(activityCode, Security.getUserId());
		
		if (type == HolidayActivity01ExerciseType.PRESET && grade == null) {
			
			//首先获取上次保存的教材和年级
			if(userCategoryGrade != null){
				currentGrade = userCategoryGrade.getGrade();
			} else {
				currentGrade = gradeCacheService.getGrade(Security.getUserId(), activityCode);
			}
			
			for (HolidayActivity01Grade grade2 : HolidayActivity01Grade.values()) {
				if (grade2.getValue() == currentGrade) {
					grade = grade2;
					break;
				}
			}
			
			if(grade == null){
				grade = HolidayActivity01Grade.PHASE_2_1;
			}
		}

		// 限制
		size = Math.min(size, 200);
		if (type == HolidayActivity01ExerciseType.PRESET && categoryCode == null) {
			
			//首先获取上次保存的教材和年级
			if(userCategoryGrade != null){
				currentCategory = userCategoryGrade.getTextbookCategoryCode();
			} else {
				Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
				currentCategory = teacher.getTextbookCategoryCode();
			}
			
			boolean hasCategoryCode = false;
			if (currentCategory != null) {
				HolidayActivity01 hActive = holidayActivity01Service.get(activityCode);
				List<Integer> textbookCategoryCodeList = null;
				textbookCategoryCodeList = hActive.getCfg().getTextbookCategoryCodes2();
				for (Integer code : textbookCategoryCodeList) {
					if (currentCategory.intValue() == code.intValue()) {
						hasCategoryCode = true;
						break;
					}
				}
			}
			
			if (currentCategory == null || !hasCategoryCode) {
				// 默认教材版本
				currentCategory = 15;
			}
			
			if(categoryCode == null){
				categoryCode = currentCategory;
			}
		}
		
		Long presetUserId = null;
		if (type != HolidayActivity01ExerciseType.PRESET) {
			presetUserId = Security.getUserId();
		}
		CursorPageable<Integer> cursorPageable = CP.cursor(cursor, size);
		CursorPage<Integer, HolidayActivity01Exercise> page = holidayActivity01ExerciseService.list(activityCode,
				categoryCode, presetUserId, type, grade, cursorPageable);

		List<VHolidayActivity01Exercise> vs = holidayActivity01ExerciseConvert.to(page.getItems());
		VCursorPage<VHolidayActivity01Exercise> vpage = new VCursorPage<VHolidayActivity01Exercise>();
		vpage.setItems(vs);
		vpage.setCursor(page.getNextCursor() == null ? 0 : page.getNextCursor());
		return new Value(vpage);
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "question", method = { RequestMethod.POST, RequestMethod.GET })
	public Value question(long activityCode, Long exerciseId) {
		if (exerciseId == null) {
			return new Value(new MissingArgumentException());
		}
		List<HolidayActivity01ExerciseQuestion> questions = holidayActivity01ExerciseQuestionService
				.queryQuestionList(activityCode, exerciseId);
		List<Long> qids = Lists.newArrayList();
		for (HolidayActivity01ExerciseQuestion question : questions) {
			qids.add(question.getQuestionId());
		}
		QuestionConvertOption option = new QuestionConvertOption();
		option.setInitSub(false);
		option.setInitTextbookCategory(false);
		option.setInitPhase(false);
		option.setInitSubject(false);
		option.setAnalysis(true);
		option.setAnswer(true);
		option.setInitKnowledgePoint(false);
		option.setInitQuestionType(false);
		option.setInitStudentQuestionCount(false);
		List<VQuestion> vlist = questionConvert.to(questionService.mgetList(qids), option);
		return new Value(vlist);
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "publish", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publish(HolidayActivity01ExerciseForm form) {
		if (CollectionUtils.isEmpty(form.getExerciseIds()) || CollectionUtils.isEmpty(form.getHomeworkClassIds())) {
			return new Value(new MissingArgumentException());
		}
		HolidayActivity01 holidayActivity01 = holidayActivity01Service.get(form.getActivityCode());
		if (holidayActivity01 == null) {
			return new Value(new EntityNotFoundException());
		}

		Date now = new Date();
		List<HolidayActivity01Homework> homeworks = Lists.newArrayList();
		List<HolidayActivity01Class> clazzs = Lists.newArrayList();
		List<HolidayActivity01Class> hasClazzs = holidayActivity01ClassService.getClassByUserId(Security.getUserId(),
				form.getActivityCode());
		// 防止班级重复
		Set<Long> clazzsSet = new HashSet<Long>();
		for (Long classId : form.getHomeworkClassIds()) {
			clazzsSet.add(classId);
		}
		for (Long classId : clazzsSet) {
			// 活动练习
			for (Long exerciseId : form.getExerciseIds()) {
				HolidayActivity01Homework holidayActivity01Homework = new HolidayActivity01Homework();
				holidayActivity01Homework.setActivityCode(form.getActivityCode());
				holidayActivity01Homework.setClassId(classId);
				holidayActivity01Homework.setValid(false);
				holidayActivity01Homework.setDeadline(new Date(form.getDeadline()));
				holidayActivity01Homework.setStartTime(new Date(form.getStartTime()));
				holidayActivity01Homework.setHolidayActivity01ExerciseId(exerciseId);
				holidayActivity01Homework.setLuckyDraw(0);
				holidayActivity01Homework.setSubmitRate(BigDecimal.valueOf(0L));
				holidayActivity01Homework.setUserId(Security.getUserId());
				holidayActivity01Homework.setCreateAt(now);
				homeworks.add(holidayActivity01Homework);
			}
			boolean exist = false;
			for (HolidayActivity01Class hasclazz : hasClazzs) {
				if (hasclazz.getClassId() == classId.longValue()) {
					exist = true;
					break;
				}
			}

			List<Long> studentIds = zyHomeworkStudentClazzService.listClassStudents(classId);
			if (studentIds.size() >= holidayActivity01.getCfg().getMinClassStudents()) {
				for (HolidayActivity01Homework holidayActivity01Homework : homeworks) {
					holidayActivity01Homework.setValid(true);
				}
			}

			if (!exist) {
				HolidayActivity01Class clazz = new HolidayActivity01Class();
				clazz.setActivityCode(form.getActivityCode());
				clazz.setClassId(classId);
				clazz.setCreateAt(now);
				clazz.setSubmitRate(BigDecimal.valueOf(0L));
				clazz.setUserId(Security.getUserId());
				clazzs.add(clazz);
				// 通过班级查询学生
				List<HolidayActivity01ClassUser> classUsers = Lists.newArrayList();

				for (Long studentId : studentIds) {
					HolidayActivity01ClassUser classUser = new HolidayActivity01ClassUser();
					classUser.setActivityCode(form.getActivityCode());
					classUser.setClassId(classId);
					classUser.setCreateAt(now);
					classUser.setSubmitRate(BigDecimal.valueOf(0L));
					classUser.setUserId(studentId);
					classUsers.add(classUser);
				}
				holidayActivity01ClassUserService.create(classUsers);
			}

		}

		// 处理holidayActivity01Homework
		homeworkService.create(homeworks, holidayActivity01, Security.getUserId());

		// 处理HolidayActivity01Class
		holidayActivity01ClassService.create(clazzs);
		
		// 处理本次选择的教材和年级的记录
		Integer category = form.getCategoryCode();
		HolidayActivity01Grade grade = form.getGrade();
		if(category != null && grade != null){
			HolidayActivity01UserCategoryGrade userCategoryGrade = holidayActivity01UserCategoryGradeService.get(form.getActivityCode(), Security.getUserId());
			//没有就保存
			if(userCategoryGrade == null){
				userCategoryGrade = new HolidayActivity01UserCategoryGrade();
				userCategoryGrade.setActivityCode(form.getActivityCode());
				userCategoryGrade.setUserId(Security.getUserId());
				userCategoryGrade.setTextbookCategoryCode(category);
				userCategoryGrade.setGrade(grade.getValue());
				holidayActivity01UserCategoryGradeService.create(userCategoryGrade);
			} else {
				//有的话看新的与原来的是不是一样，不一样要更新
				if(category != userCategoryGrade.getTextbookCategoryCode()
						|| grade.getValue() != userCategoryGrade.getGrade()){
					userCategoryGrade.setGrade(grade.getValue());
					userCategoryGrade.setTextbookCategoryCode(category);
					holidayActivity01UserCategoryGradeService.update(userCategoryGrade);
				}
			}
		}

		return new Value();
	}

	/**
	 * 查询班级列表
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "clazzs", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryClazzs() {
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			retMap.put("clazzs", Collections.EMPTY_LIST);
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
			retMap.put("clazzs", notEmptyClazz);
		}
		return new Value(retMap);
	}

}
