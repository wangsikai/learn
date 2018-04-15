package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkKnowledge;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMetaKnow;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.resources.api.ExerciseService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.form.HomeworkForm;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyExerciseService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.api.ZyTextbookExerciseService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.PublishHomeworkForm;

@Transactional(readOnly = true)
@Service
public class ZyHomeworkServiceImpl implements ZyHomeworkService {

	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;
	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> studentHomeworkRepo;
	@Autowired
	@Qualifier("HomeworkMetaKnowRepo")
	Repo<HomeworkMetaKnow, Long> homeworkMetaRepo;
	@Autowired
	@Qualifier("HomeworkKnowledgeRepo")
	Repo<HomeworkKnowledge, Long> homeworkKnowledgeRepo;

	@Autowired
	private ZyExerciseService zyExerciseService;
	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private ZyTextbookExerciseService tbeService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private ZyHomeworkStatService hkStatService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private ZyStudentHomeworkStatService stuHkStatService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyBookService zyBookService;
	@Autowired
	private ZyQuestionService zyQuestionService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassGroupStudentService zyHomeworkClassGroupStudentService;

	@Override
	public List<Homework> findProcessHomeworks(long teacherId, int limit, boolean isCourse) {
		Set<Integer> statusVals = Sets.newHashSet(HomeworkStatus.PUBLISH.getValue(),
				HomeworkStatus.NOT_ISSUE.getValue());
		Params params = Params.param("createId", teacherId).put("limit", limit).put("status", statusVals);
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(teacherId);
		Set<Long> clazzIds = Sets.newHashSet(-1L);
		for (HomeworkClazz clazz : clazzs) {
			clazzIds.add(clazz.getId());
		}
		params.put("homeworkClassIds", clazzIds);
		return homeworkRepo.find("$zyFindTodoHomeworks", params).list();
	}

	@Override
	public List<Homework> findProcessHomeworks2(long teacherId, int limit, boolean isCourse) {
		Set<Integer> statusVals = Sets.newHashSet(HomeworkStatus.NOT_ISSUE.getValue());
		Params params = Params.param("createId", teacherId).put("limit", limit).put("status", statusVals);
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(teacherId);
		Set<Long> clazzIds = Sets.newHashSet(-1L);
		for (HomeworkClazz clazz : clazzs) {
			clazzIds.add(clazz.getId());
		}
		params.put("homeworkClassIds", clazzIds);
		return homeworkRepo.find("$zyFindTodoHomeworks", params).list();
	}

	@Override
	public Page<Homework> query(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param();
		if (!query.isClassManage()) {
			Params.param("createId", query.getTeacherId());
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		return homeworkRepo.find("$zyQuery", params).fetch(pageable);
	}

	@Override
	public Page<Homework> queryForMobile(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param();
		if (!query.isClassManage()) {
			params.put("createId", query.getTeacherId());
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		if (query.getIssueKey() != null) {
			params.put("issueKey", 1);
		}
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		if (query.getEndTime() != null) {
			params.put("endTime", query.getEndTime());
		}
		return homeworkRepo.find("$zyQueryForMobile", params).fetch(pageable);
	}

	@Override
	public String getHomeworkName(long teacherId, long textbookExerciseId, Long sectionCode, boolean isBook) {
		Long sc = sectionCode;
		if (!isBook) {
			if (textbookExerciseId > 0) {
				TextbookExercise tbe = tbeService.get(textbookExerciseId);
				sc = tbe.getSectionCode();
			}
		}
		long count = zyExerciseService.countTodayExercise(teacherId, textbookExerciseId, sectionCode);
		String sectionName = StringUtils.EMPTY;
		if (isBook) {
			sectionName = zyBookService.getBookCatalog(sc).getName();
		} else {
			sectionName = sectionService.get(sc).getName();
		}
		String name = new java.text.SimpleDateFormat("MMdd").format(new Date()) + "-" + (count + 1) + " " + sectionName;
		return name;
	}

	@Override
	public String getHomeworkName(long teacherId, TextbookExercise textbookExercise) {
		long count = zyExerciseService.countTodayExercise(teacherId, textbookExercise.getId(), null);
		String sectionName = sectionService.get(textbookExercise.getSectionCode()).getName();
		String name = new java.text.SimpleDateFormat("MMdd").format(new Date()) + "-" + (count + 1) + " " + sectionName;
		return name;
	}

	@Transactional
	@Override
	public long publish(PublishHomeworkForm form) {
		// 创建练习
		TextbookExercise textbookExercise = null;
		if (form.getTextbookExerciseId() != null && form.getTextbookExerciseId() > 0) {
			textbookExercise = tbeService.get(form.getTextbookExerciseId());
		}
		Exercise exercise = new Exercise();
		exercise.setCreateAt(new Date());
		exercise.setCreateId(form.getCreateId());
		if (textbookExercise == null) {
			exercise.setName(form.getName().trim());
			exercise.setSectionCode(form.getSectionCode());
			// 当作业从书本中布置,sectionCode为书本的章节ID
			// 如果不是书本里面的作业且sectionCode有值,则需要设置版本教材
			if (form.getBookId() == null && form.getSectionCode() != null) {
				exercise.setTextCode(sectionService.get(form.getSectionCode()).getTextbookCode());
				exercise.setTextbookCategoryCode(tbService.get(exercise.getTextCode()).getCategoryCode());
			}
			exercise.setBookId(form.getBookId());
		} else {
			exercise.setName(getHomeworkName(form.getCreateId(), textbookExercise));
			exercise.setSectionCode(textbookExercise.getSectionCode());
			exercise.setTextbookCategoryCode(tbService.get(textbookExercise.getTextbookCode()).getCategoryCode());
			exercise.setTextCode(textbookExercise.getTextbookCode());
		}
		if (StringUtils.isNotBlank(form.getName())) {
			exercise.setName(form.getName().trim());
		}
		exercise.setStatus(Status.ENABLED);
		exercise.setTextbookExerciseId(form.getTextbookExerciseId());
		exercise.setUpdateAt(exercise.getCreateAt());
		exercise = zyExerciseService.createExercise(exercise, form.getqIds());
		long homeworkId = 0;
		// 创建作业(只支持班级布置作业方式)
		boolean hasQuestionAnswering = zyQuestionService.hasQuestionAnswering(form.getqIds());
		Date createAt = new Date();
		for (Long homeworkClassId : form.getHomeworkClassIds()) {
			HomeworkForm hkForm = new HomeworkForm();
			hkForm.setCreateAt(createAt);
			hkForm.setHomeworkClassId(homeworkClassId);
			hkForm.setCreateId(form.getCreateId());
			hkForm.setDeadline(form.getDeadline());
			hkForm.setExerciseId(exercise.getId());
			hkForm.setStartTime(form.getStartTime());
			hkForm.setDifficulty(form.getDifficulty());
			hkForm.setHasQuestionAnswering(hasQuestionAnswering);
			if (textbookExercise != null) {
				hkForm.setTextbookCode(textbookExercise.getTextbookCode());
			} else {
				hkForm.setTextbookCode(exercise.getTextCode());
			}
			hkForm.setCorrectingType(form.getCorrectingType());
			hkForm.setMetaKnowpoints(form.getMetaKnowpoints());
			if (form.isCorrectLastHomework()) {
				hkForm.setLastIssued(findLastNotCorrect(homeworkClassId));
			}
			Homework hk = hkService.create(hkForm);
			homeworkId = hk.getId();
			if (hk.getLastIssued() != null) {
				updateCorrectedHomework(hk.getLastIssued());// 更新被订正的作业
			}
			Homework homework = hkService.setTime(hk.getId(), form.getStartTime(), form.getDeadline());
			if (homework.getStatus() == HomeworkStatus.PUBLISH) {
				List<Long> studentIds = zyHkStuClazzService.listClassStudents(homeworkClassId);
				for (Long studentId : studentIds) {
					stuHkStatService.updateAfterPublishHomework(studentId, homeworkClassId);
				}
			}
			// 更新作业统计
			hkStatService.updateAfterPublishHomework(form.getCreateId(), homeworkClassId, null, homework.getStatus());
		}
		// 保存作业知识点表
		if (homeworkId > 0 && CollectionUtils.isNotEmpty(form.getMetaKnowpoints())) {
			for (Long metaCode : form.getMetaKnowpoints()) {
				HomeworkMetaKnow hm = new HomeworkMetaKnow();
				hm.setHomeworkId(homeworkId);
				hm.setMetaCode(metaCode);
				homeworkMetaRepo.save(hm);
			}
		}
		return homeworkId;
	}

	@Transactional
	@Override
	@Deprecated
	public void issue(Homework homework, boolean inAdvance) {
		if (!inAdvance) {
			if (homework.getStatus() == HomeworkStatus.PUBLISH
					&& homework.getCommitCount() < homework.getDistributeCount()) {// 如果有学生没有提交则不能下发
				throw new NoPermissionException();
			}
			if (homework.getStatus() == HomeworkStatus.PUBLISH) {
				hkService.updateStatus(homework.getId(), HomeworkStatus.NOT_ISSUE);
			}
		}
		// 所有提交的作业必须批改完成
		long notCorrectedCount = studentHomeworkRepo
				.find("$zyStatSubmitNotCorrected", Params.param("hkId", homework.getId())).count();
		if (notCorrectedCount > 0) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_NOT_ALLCORRECTED);
		}
		hkService.issue(homework.getId());
		// 更新统计
		hkStatService.updateAfterIssueHomework(homework.getCreateId(), homework.getHomeworkClassId());
	}

	@Transactional
	@Override
	public int commitHomework(long homeworkId) {
		int ret = stuHkService.commitHomework(homeworkId);

		// 作业已截止 @since 小悠快批，2018-2-27
		hkService.updateStatus(homeworkId, HomeworkStatus.NOT_ISSUE);
		hkService.get(homeworkId);
		return ret;
	}

	@Override
	public List<Homework> findByExercise(long teacherId, Collection<Long> exerciseIds) {
		return homeworkRepo
				.find("$zyFindByExercise", Params.param("exerciseIds", exerciseIds).put("teacherId", teacherId)).list();
	}

	@Override
	public List<Homework> getLatestIssuedHomeWorks(long classId, int limit) {
		List<Homework> list = homeworkRepo.find("$zyGetLastHks", Params.param("classId", classId).put("limit", limit))
				.list();
		if (CollectionUtils.isNotEmpty(list)) {
			Collections.reverse(list);
		}
		return list;
	}

	@Override
	public Map<Long, List<Homework>> getLatestIssuedHomeWorks(Collection<Long> classIds, int limit) {
		List<Homework> list = homeworkRepo
				.find("$zyMGetLastHks", Params.param("classIds", classIds).put("limit", limit)).list();
		if (CollectionUtils.isNotEmpty(list)) {
			Collections.reverse(list);
		}
		Map<Long, List<Homework>> map = new HashMap<Long, List<Homework>>();
		for (Homework homework : list) {
			List<Homework> homeworks = map.get(homework.getHomeworkClassId());
			if (homeworks == null) {
				homeworks = new ArrayList<Homework>();
				map.put(homework.getHomeworkClassId(), homeworks);
			}
			homeworks.add(homework);
		}
		return map;
	}

	@Override
	public Homework getLatextHomeworkByExerciseId(long teacherId, long exerciseId) {
		return homeworkRepo
				.find("$zyFindByExercise", Params.param("exerciseId", exerciseId).put("teacherId", teacherId)).get();
	}

	@Transactional
	@Override
	public int delete(long teacherId, long homeworkId) {
		// 更新作业表
		int count = homeworkRepo.execute("$updateHomework",
				Params.param("homeworkId", homeworkId).put("teacherId", teacherId));
		// 若数据不存在，则不更新学生作业表
		if (count > 0) {
			// 更新学生作业表
			studentHomeworkRepo.execute("$updateStuHomework", Params.param("homeworkId", homeworkId));
		}
		return count;
	}

	@Override
	public Long findLastNotCorrect(long classId) {
		Homework hk = homeworkRepo.find("$zyFindLastNotCorrect", Params.param("classId", classId)).get();
		if (hk != null && hk.getStatus() == HomeworkStatus.ISSUED && !hk.isCorrected()) {
			return hk.getId();
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public void updateCorrectedHomework(long id) {
		homeworkRepo.execute("zyUpdateCorrectedHomework", Params.param("id", id));
	}

	@Transactional
	@Override
	public void updateLastCommitAt(long id, long autoEffectiveCount) {
		homeworkRepo.execute("$zyUpdateLastCommitAt",
				Params.param("id", id).put("autoEffectiveCount", autoEffectiveCount).put("nowDate", new Date()));
	}

	@Override
	public Page<Homework> queryAfterLastCommitStat(Pageable pageable) {
		return homeworkRepo.find("$zyQueryAfterLastCommitStat").fetch(pageable);
	}

	@Transactional
	@Override
	public void udpateAfterLastCommitStat(long id) {
		homeworkRepo.execute("$zyUdpateAfterLastCommitStat", Params.param("id", id));
	}

	@Override
	public Page<Homework> queryPublishHomework(Pageable pageable) {
		return homeworkRepo.find("$zyQueryPublishHomework").fetch(pageable);
	}

	@Override
	public Page<Homework> queryHistoryHomework(long userId, Long sectionCode, String sectionNameKey, Date beginTime,
			Date endTime, Long textBookCode, Long textBookCategoryCode, Long classId, boolean isDesc, Pageable p) {
		Params params = Params.param();
		params.put("userId", userId);
		if (sectionCode != null) {
			params.put("sectionCode", sectionCode);
		}
		if (StringUtils.isNoneEmpty(sectionNameKey)) {
			params.put("sectionName", "%" + sectionNameKey + "%");
			if (sectionNameKey.equals("在线题库")) {
				params.put("isSearchOnlineQuestions", true);
			}
		}
		if (beginTime != null) {
			params.put("beginTime", beginTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		if (textBookCode != null) {
			params.put("textBookCode", textBookCode);
		}
		if (textBookCategoryCode != null) {
			params.put("textBookCategoryCode", textBookCategoryCode);
		}
		if (classId != null) {
			params.put("classId", classId);
		}
		params.put("isDesc", isDesc);

		return homeworkRepo.find("$queryHomeworkHistory", params).fetch(p);
	}

	@Transactional
	@Override
	public void setStartTimeNow(long hkId, long teacherId) {
		homeworkRepo.execute("$zySetStartTimeNow",
				Params.param("hkId", hkId).put("teacherId", teacherId).put("nowDate", new Date()));
	}

	@Override
	public Page<Map> queryHomeworkTracking(long userId, Pageable p) {
		Params params = Params.param();
		params.put("createId", userId);
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(userId);
		Set<Long> clazzIds = Sets.newHashSet(-1L);
		for (HomeworkClazz clazz : clazzs) {
			clazzIds.add(clazz.getId());
		}
		params.put("homeworkClassIds", clazzIds);
		return homeworkRepo.find("$zyQueryHomeworkTracking", params).fetch(p, Map.class);
	}

	@Override
	public List<Map> queryHomeworkTrackingCount(long userId) {
		Params params = Params.param();
		params.put("createId", userId);
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(userId);
		Set<Long> clazzIds = Sets.newHashSet(-1L);
		for (HomeworkClazz clazz : clazzs) {
			clazzIds.add(clazz.getId());
		}
		params.put("homeworkClassIds", clazzIds);
		return homeworkRepo.find("$zyQueryHomeworkTrackingCount", params).list(Map.class);
	}

	@Override
	public Page<Map> queryUnionHolidayHomework(ZyHomeworkQuery query, Pageable p) {
		Params params = Params.param("createId", query.getTeacherId());
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		if (CollectionUtils.isNotEmpty(query.getHolidayHkstatus())) {
			Set<Integer> statusVals2 = new HashSet<Integer>(query.getHolidayHkstatus().size());
			for (HomeworkStatus s : query.getHolidayHkstatus()) {
				statusVals2.add(s.getValue());
			}
			params.put("holidayHkStatus", statusVals2);
		}
		if (query.getType() != null && query.getType() == 1) {
			params.put("type", 1);
		} else if (query.getType() != null && query.getType() == 2) {
			params.put("type", 2);
		} else {
			params.put("type", 0);
		}
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		return homeworkRepo.find("$zyQueryUnionHolidayHomework", params).fetch(p, Map.class);
	}

	@Override
	public List<Homework> listByTime(long userId, long clazzId, int year, int month) {
		// 获取当月第一天时间和最后一天
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 下个月第一天减去一天就是这个月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = cal.getTime();
		return homeworkRepo.find("$listByTime", Params.param("beginTime", beginTime).put("endTime", endTime)
				.put("createId", userId).put("clazzId", clazzId)).list();
	}

	@Override
	public Homework getFirstIssuedHomeworkInMonth(int year, int month, long classId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 下个月第一天减去一天就是这个月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = cal.getTime();
		List<Homework> hkList = homeworkRepo.find("$getFirstIssuedHomeworkInMonth",
				Params.param("beginTime", beginTime).put("endTime", endTime).put("classId", classId)).list();
		if (CollectionUtils.isEmpty(hkList)) {
			return null;
		}
		return hkList.get(0);
	}

	@Override
	@Transactional
	public List<Homework> publish2(PublishHomeworkForm form) {
		// 创建练习
		TextbookExercise textbookExercise = null;
		if (form.getTextbookExerciseId() != null && form.getTextbookExerciseId() > 0) {
			textbookExercise = tbeService.get(form.getTextbookExerciseId());
		}
		Exercise exercise = new Exercise();
		Homework homework = null;
		exercise.setCreateAt(new Date());
		exercise.setCreateId(form.getCreateId());
		if (textbookExercise == null) {
			exercise.setName(form.getName().trim());
			exercise.setSectionCode(form.getSectionCode());
			// 当作业从书本中布置,sectionCode为书本的章节ID
			// 如果不是书本里面的作业且sectionCode有值,则需要设置版本教材
			if (form.getBookId() == null && form.getSectionCode() != null) {
				exercise.setTextCode(sectionService.get(form.getSectionCode()).getTextbookCode());
				exercise.setTextbookCategoryCode(tbService.get(exercise.getTextCode()).getCategoryCode());
			} else if (form.getBookId() != null && form.getBookId() > 0) {
				BookVersion bookVersion = zyBookService.getBookVersion(form.getBookId());
				if (bookVersion != null) {
					exercise.setTextCode(bookVersion.getTextbookCode());
					exercise.setTextbookCategoryCode(bookVersion.getTextbookCategoryCode());
				}
			}
			exercise.setBookId(form.getBookId());
		} else {
			exercise.setName(getHomeworkName(form.getCreateId(), textbookExercise));
			exercise.setSectionCode(textbookExercise.getSectionCode());
			exercise.setTextbookCategoryCode(tbService.get(textbookExercise.getTextbookCode()).getCategoryCode());
			exercise.setTextCode(textbookExercise.getTextbookCode());
		}
		if (exercise.getTextbookCategoryCode() != null && exercise.getTextCode() != null) {// 更新老师版本教材
			teacherService.updateCategory(form.getCreateId(), exercise.getTextbookCategoryCode(),
					exercise.getTextCode());
		}
		if (StringUtils.isNotBlank(form.getName())) {
			exercise.setName(form.getName().trim());
		}
		exercise.setStatus(Status.ENABLED);
		exercise.setTextbookExerciseId(form.getTextbookExerciseId());
		exercise.setUpdateAt(exercise.getCreateAt());
		exercise = zyExerciseService.createExercise(exercise, form.getqIds());
		long homeworkId = 0;
		// 创建作业(只支持班级布置作业方式)
		boolean hasQuestionAnswering = zyQuestionService.hasQuestionAnswering(form.getqIds());
		Date createAt = new Date();
		List<Homework> homeworks = new ArrayList<Homework>(form.getHomeworkClassIds().size());
		for (Long homeworkClassId : form.getHomeworkClassIds()) {
			HomeworkForm hkForm = new HomeworkForm();
			hkForm.setCreateAt(createAt);
			hkForm.setHomeworkClassId(homeworkClassId);
			hkForm.setCreateId(form.getCreateId());
			hkForm.setDeadline(form.getDeadline());
			hkForm.setExerciseId(exercise.getId());
			hkForm.setStartTime(form.getStartTime());
			hkForm.setDifficulty(form.getDifficulty());
			hkForm.setHasQuestionAnswering(hasQuestionAnswering);
			// 添加解答题是否需要小优快批自动批改标记 pengcheng.yu 2018-02-09 @since 小优快批
			hkForm.setAnswerQuestionAutoCorrect(form.isAnswerQuestionAutoCorrect());
			if (textbookExercise != null) {
				hkForm.setTextbookCode(textbookExercise.getTextbookCode());
			} else {
				hkForm.setTextbookCode(exercise.getTextCode());
			}
			hkForm.setCorrectingType(form.getCorrectingType());
			hkForm.setKnowledgePoints(form.getKnowledgePoints()); // 新知识点
			if (form.isCorrectLastHomework()) {
				hkForm.setLastIssued(findLastNotCorrect(homeworkClassId));
			}
			// 不再需要自动下发标记 pengcheng.yu 2018-02-09 @since 小优快批
			// hkForm.setAutoIssue(form.getAutoIssue()); // 自动下发设置
			Homework hk = hkService.create(hkForm);
			homeworkId = hk.getId();
			if (hk.getLastIssued() != null) {
				updateCorrectedHomework(hk.getLastIssued());// 更新被订正的作业
			}
			homework = setTime2(hk.getId(), form.getStartTime(), form.getDeadline());
			if (homework.getStatus() == HomeworkStatus.PUBLISH) {
				List<Long> studentIds = zyHkStuClazzService.listClassStudents(homeworkClassId);
				for (Long studentId : studentIds) {
					stuHkStatService.updateAfterPublishHomework(studentId, homeworkClassId);
				}
			}
			// 更新作业统计
			hkStatService.updateAfterPublishHomework(form.getCreateId(), homeworkClassId, null, homework.getStatus());
			homeworks.add(homework);
		}
		// 保存作业知识点表
		if (homeworkId > 0 && CollectionUtils.isNotEmpty(form.getMetaKnowpoints())) {
			for (Long metaCode : form.getMetaKnowpoints()) {
				HomeworkMetaKnow hm = new HomeworkMetaKnow();
				hm.setHomeworkId(homeworkId);
				hm.setMetaCode(metaCode);
				homeworkMetaRepo.save(hm);
			}
		}

		// 保存作业新知识点表
		if (homeworkId > 0 && CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
			for (Long knowledgeCode : form.getKnowledgePoints()) {
				HomeworkKnowledge hkd = new HomeworkKnowledge();
				hkd.setHomeworkId(homeworkId);
				hkd.setKnowledgeCode(knowledgeCode);
				homeworkKnowledgeRepo.save(hkd);
			}
		}

		// since 2.0.3 若为教辅布置作业则更新班级的教辅进度
		if (form.getBookId() != null) {
			Long nextCataId = zyBookService.getNextCatalog(form.getBookId(), form.getSectionCode());
			Map<Long, HomeworkClazz> homeworkClazzs = zyHkClassService.mget(form.getHomeworkClassIds());
			List<Long> needSetBookClassIds = Lists.newArrayList();
			for (long hkClassId : form.getHomeworkClassIds()) {
				HomeworkClazz tmpHkClass = homeworkClazzs.get(hkClassId);
				if (tmpHkClass.getBookVersionId() == null) {
					needSetBookClassIds.add(hkClassId);
				} else if (tmpHkClass.getBookVersionId().equals(form.getBookId())) {
					needSetBookClassIds.add(hkClassId);
				}
			}
			if (CollectionUtils.isNotEmpty(needSetBookClassIds)) {
				zyHkClassService.setBook(needSetBookClassIds, form.getBookId(), nextCataId, form.getCreateId());
			}
		}
		return homeworks;
	}

	@Override
	@Transactional
	public Homework setTime2(long homeworkId, long startTime, long deadline) throws HomeworkException {
		Homework homework = homeworkRepo.get(homeworkId);
		if (homework.getStatus() == HomeworkStatus.INIT
				|| (homework.getStartTime().getTime() > System.currentTimeMillis())) {
			if ((startTime == 0 && deadline != 0) || (startTime != 0 && deadline == 0) || (startTime > deadline)) {
				throw new HomeworkException(HomeworkException.HOMEWORK_TIME_ILLEGAL);
			}
			homework.setStartTime(new Date(startTime));
			homework.setDeadline(new Date(deadline));
			if (startTime <= System.currentTimeMillis()) {
				// 立即发布
				homework.setStatus(HomeworkStatus.PUBLISH);
			}
			return homeworkRepo.save(homework);
		} else {
			throw new HomeworkException(HomeworkException.HOMEWORK_HAS_PUBLISH);
		}
	}

	@Override
	@Transactional
	public void publishHomework2(long homeworkId) throws HomeworkException {
		Homework p = homeworkRepo.get(homeworkId);
		List<Long> studentIds = new ArrayList<Long>();
		if (p.getHomeworkClassGroupId() != null && p.getHomeworkClassGroupId() != 0) {
			studentIds = zyHomeworkClassGroupStudentService.findGroupStudents(p.getHomeworkClassId(),
					p.getHomeworkClassGroupId());
		} else {
			studentIds = zyHkStuClazzService.listClassStudents(p.getHomeworkClassId());
		}
		if (CollectionUtils.isNotEmpty(studentIds)) {
			p.setDistributeCount((long) studentIds.size());
			homeworkRepo.save(p);

			Set<Long> stuIds = Sets.newHashSet(studentIds);
			Date now = new Date();
			studentHomeworkService.publishHomework(p, stuIds, now);
		}
		hkService.updateStatus(homeworkId, HomeworkStatus.PUBLISH);
	}

	/**
	 * 新作业查询.
	 */
	@Override
	public Page<Homework> queryHomeworkWeb2(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("createId", query.getTeacherId());
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}

			params.put("status", statusVals);
		}
		if (query.getIssueKey() != null) {
			params.put("issueKey", 1);
		}
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("keys", "%" + query.getKey().replace(" ", "") + "%");
		}
		if (query.getBeginTime() != null && query.getEndTime() != null) {
			params.put("bt", query.getBeginTime());
			params.put("et", query.getEndTime());
		}
		if (StringUtils.isNotBlank(query.getSectionName())) {
			params.put("secname", "%" + query.getSectionName() + "%");
		}
		params.put("line", query.isLineQuestion());
		return homeworkRepo.find("$queryHomeworkWeb3", params).fetch(pageable);
	}

	/**
	 * 新作业查询
	 * 
	 * @since 小优快批
	 */
	@Override
	public Page<Homework> queryHomeworkWeb3(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("createId", query.getTeacherId());
		Integer hkStatus = query.getHomeworkStatus();
		if (null != hkStatus) {
			if (hkStatus == 1) {// 作业中（答题开始时间--答题截止时间内）
				params.put("current", new Date());
			} else if (hkStatus == 2) {// 已截至，所有学生都已提交作业
				params.put("isOver", 1);

			} else if (hkStatus == 3) {// 待批改 有需要批改的题目即为待批改。
				params.put("tobeCorrected", 1);
			}
		}
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("keys", "%" + query.getKey().replace(" ", "") + "%");
		}
		if (query.getBeginTime() != null && query.getEndTime() != null) {
			params.put("bt", query.getBeginTime());
			params.put("et", query.getEndTime());
		}
		if (StringUtils.isNotBlank(query.getSectionName())) {
			params.put("secname", "%" + query.getSectionName() + "%");
		}
		params.put("line", query.isLineQuestion());
		return homeworkRepo.find("$queryHomeworkWeb3", params).fetch(pageable);
	}

	@Override
	public List<Homework> getIssuedHomework(long userId, int limt) {
		Params params = Params.param("createId", userId);
		params.put("limt", limt);
		return homeworkRepo.find("$getIssuedHomework", params).list();
	}

	@Override
	public List<Long> queryWeakByCodes(long hkId, List<Long> knowledges) {
		Params params = Params.param("hkId", hkId);
		if (CollectionUtils.isNotEmpty(knowledges)) {
			params.put("knowledges", knowledges);
		}
		return homeworkRepo.find("$queryWeakByCodes", params).list(Long.class);
	}

	@Override
	public List<Integer> queryWeakByOldCodes(long hkId, List<Long> codes) {
		Params params = Params.param("hkId", hkId);
		if (CollectionUtils.isNotEmpty(codes)) {
			params.put("knowledges", codes);
		}
		return homeworkRepo.find("$queryWeakByOldCodes", params).list(Integer.class);
	}

	@Override
	public long allCountByOriginalCreateId(long originalCreateId) {
		return homeworkRepo.find("$countByOriginalCreateId", Params.param("originalCreateId", originalCreateId))
				.count();
	}

	@Override
	public Page<Homework> queryForMobile2(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("createId", query.getTeacherId());
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		if (query.getEndTime() != null) {
			params.put("endTime", query.getEndTime());
		}
		return homeworkRepo.find("$zyQueryForMobile2", params).fetch(pageable);
	}

	@Override
	public Homework queryLastestByTeacher(ZyHomeworkQuery query) {
		Params params = Params.param("createId", query.getTeacherId());
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		return homeworkRepo.find("$queryLastestByTeacher", params).get();
	}

	@Override
	@Transactional
	public List<Homework> publish3(PublishHomeworkForm form) {
		// 创建练习
		Exercise exercise = new Exercise();
		Homework homework = null;
		exercise.setCreateAt(new Date());
		exercise.setCreateId(form.getCreateId());
		exercise.setSectionCode(form.getSectionCode());
		if (StringUtils.isNotBlank(form.getName())) {
			exercise.setName(form.getName().trim());
		}
		exercise.setStatus(Status.ENABLED);
		exercise.setTextbookExerciseId(form.getTextbookExerciseId());
		exercise.setUpdateAt(exercise.getCreateAt());
		exercise = zyExerciseService.createExercise(exercise, form.getqIds());
		long homeworkId = 0;
		// 创建作业(只支持班级布置作业方式)
		boolean hasQuestionAnswering = zyQuestionService.hasQuestionAnswering(form.getqIds());
		Date createAt = new Date();
		List<Homework> homeworks = new ArrayList<Homework>(form.getHomeworkClassIds().size());
		for (Long homeworkClassId : form.getHomeworkClassIds()) {
			HomeworkForm hkForm = new HomeworkForm();
			hkForm.setCreateAt(createAt);
			hkForm.setHomeworkClassId(homeworkClassId);
			hkForm.setCreateId(form.getCreateId());
			hkForm.setDeadline(form.getDeadline());
			hkForm.setExerciseId(exercise.getId());
			hkForm.setStartTime(form.getStartTime());
			hkForm.setDifficulty(form.getDifficulty());
			hkForm.setHasQuestionAnswering(hasQuestionAnswering);
			hkForm.setCorrectingType(form.getCorrectingType());
			// hkForm.setMetaKnowpoints(form.getMetaKnowpoints());
			hkForm.setKnowledgePoints(form.getKnowledgePoints());
			// 通过题目查询到的新知识点 教师端v1.3.0
			hkForm.setQuestionknowledgePoints(form.getQuestionknowledgePoints());
			if (form.isCorrectLastHomework()) {
				hkForm.setLastIssued(findLastNotCorrect(homeworkClassId));
			}
			// 自动下发设置
			// if (hasQuestionAnswering) {
			// // 含有解答题时，自动下发默认关闭
			// hkForm.setAutoIssue(false);
			// } else {
			// hkForm.setAutoIssue(true);
			// }
			hkForm.setAutoIssue(form.getAutoIssue());
			// 作业限时时间
			hkForm.setTimeLimit(form.getTimeLimit());
			// @since 2018-3-1 解答题自动批改标记
			hkForm.setAnswerQuestionAutoCorrect(form.isAnswerQuestionAutoCorrect());
			Homework hk = hkService.create(hkForm);
			homeworkId = hk.getId();
			if (hk.getLastIssued() != null) {
				updateCorrectedHomework(hk.getLastIssued());// 更新被订正的作业
			}
			homework = setTime2(hk.getId(), form.getStartTime(), form.getDeadline());
			if (homework.getStatus() == HomeworkStatus.PUBLISH) {
				List<Long> studentIds = zyHkStuClazzService.listClassStudents(homeworkClassId);
				for (Long studentId : studentIds) {
					stuHkStatService.updateAfterPublishHomework(studentId, homeworkClassId);
				}
			}
			// 更新作业统计
			hkStatService.updateAfterPublishHomework(form.getCreateId(), homeworkClassId, null, homework.getStatus());
			homeworks.add(homework);
		}

		// 保存作业新知识点表 取form的通过题目查询到的新知识点
		if (homeworkId > 0 && CollectionUtils.isNotEmpty(form.getQuestionknowledgePoints())) {
			for (Long knowledgeCode : form.getQuestionknowledgePoints()) {
				HomeworkKnowledge hkd = new HomeworkKnowledge();
				hkd.setHomeworkId(homeworkId);
				hkd.setKnowledgeCode(knowledgeCode);
				homeworkKnowledgeRepo.save(hkd);
			}
		}

		return homeworks;
	}

	@Transactional
	@Override
	public List<Homework> publishByGroup(PublishHomeworkForm form) {

		// 创建练习
		Exercise exercise = new Exercise();
		Homework homework = null;
		exercise.setCreateAt(new Date());
		exercise.setCreateId(form.getCreateId());
		exercise.setSectionCode(form.getSectionCode());
		if (StringUtils.isNotBlank(form.getName())) {
			exercise.setName(form.getName().trim());
		}
		exercise.setStatus(Status.ENABLED);
		exercise.setTextbookExerciseId(form.getTextbookExerciseId());
		exercise.setUpdateAt(exercise.getCreateAt());
		exercise = zyExerciseService.createExercise(exercise, form.getqIds());
		long homeworkId = 0;
		// 创建作业(只支持班级布置作业方式)
		boolean hasQuestionAnswering = zyQuestionService.hasQuestionAnswering(form.getqIds());
		Date createAt = new Date();
		List<Homework> homeworks = new ArrayList<Homework>();
		List<Map> groupList = JSON.parseObject(form.getClassGroupList(), List.class);
		for (Map map : groupList) {
			Long homeworkClassId = Long.parseLong(map.get("classId").toString());
			Long groupId = map.get("groupId") == null ? null : Long.parseLong(map.get("groupId").toString());
			HomeworkForm hkForm = new HomeworkForm();
			hkForm.setCreateAt(createAt);
			hkForm.setHomeworkClassId(homeworkClassId);
			if (groupId != null) {
				hkForm.setGroupId(groupId);
			}
			hkForm.setCreateId(form.getCreateId());
			hkForm.setDeadline(form.getDeadline());
			hkForm.setExerciseId(exercise.getId());
			hkForm.setStartTime(form.getStartTime());
			hkForm.setDifficulty(form.getDifficulty());
			hkForm.setHasQuestionAnswering(hasQuestionAnswering);
			// @since 2018-3-1 解答题自动批改标记
			hkForm.setAnswerQuestionAutoCorrect(form.isAnswerQuestionAutoCorrect());
			hkForm.setCorrectingType(form.getCorrectingType());
			// hkForm.setMetaKnowpoints(form.getMetaKnowpoints());
			hkForm.setKnowledgePoints(form.getKnowledgePoints());
			// 通过题目查询到的新知识点 教师端v1.3.0
			hkForm.setQuestionknowledgePoints(form.getQuestionknowledgePoints());
			if (form.isCorrectLastHomework()) {
				hkForm.setLastIssued(findLastNotCorrect(homeworkClassId));
			}
			hkForm.setAutoIssue(form.getAutoIssue());
			// 作业限时时间
			hkForm.setTimeLimit(form.getTimeLimit());
			Homework hk = hkService.create(hkForm);
			homeworkId = hk.getId();
			if (hk.getLastIssued() != null) {
				updateCorrectedHomework(hk.getLastIssued());// 更新被订正的作业
			}
			homework = setTime2(hk.getId(), form.getStartTime(), form.getDeadline());
			if (homework.getStatus() == HomeworkStatus.PUBLISH) {
				List<Long> studentIds = zyHkStuClazzService.listClassStudents(homeworkClassId);
				for (Long studentId : studentIds) {
					stuHkStatService.updateAfterPublishHomework(studentId, homeworkClassId);
				}
			}
			// 更新作业统计
			// to do group-stat
			hkStatService.updateAfterPublishHomework(form.getCreateId(), homeworkClassId, null, homework.getStatus());
			homeworks.add(homework);
		}

		// 保存作业新知识点表 取form的通过题目查询到的新知识点
		if (homeworkId > 0 && CollectionUtils.isNotEmpty(form.getQuestionknowledgePoints())) {
			for (Long knowledgeCode : form.getQuestionknowledgePoints()) {
				HomeworkKnowledge hkd = new HomeworkKnowledge();
				hkd.setHomeworkId(homeworkId);
				hkd.setKnowledgeCode(knowledgeCode);
				homeworkKnowledgeRepo.save(hkd);
			}
		}
		return homeworks;

	}

	@Override
	public Long countNotIssue(long classId) {
		return homeworkRepo.find("$countNotIssue", Params.param("classId", classId)).get(Long.class);
	}

	@Override
	public Page<Homework> queryForMobile3(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param();
		params.put("createId", query.getTeacherId());
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		if (query.getHomeworkStatus() != null) {
			params.put("homeworkStatus", query.getHomeworkStatus());
		}
		params.put("nowTime", new Date());

		return homeworkRepo.find("$zyQueryForMobile3", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Set<Long> queryStudentCorrect(Long userId, List<Long> homeworkIds) {
		Params params = Params.param();
		params.put("createId", userId);
		params.put("homeworkIds", homeworkIds);
		List<Map> maps = homeworkRepo.find("$queryStudentCorrectCount", params).list(Map.class);
		Set<Long> data = Sets.newHashSet();
		for (Map value : maps) {
			String idStr = value.get("id").toString();
			data.add(Long.valueOf(idStr));
		}

		return data;
	}

	@Override
	public Page<Homework> queryForMobileIndex(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param();
		params.put("createId", query.getTeacherId());
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		if (query.getHomeworkStatus() != null) {
			params.put("homeworkStatus", query.getHomeworkStatus());
		}
		params.put("nowTime", new Date());

		return homeworkRepo.find("$zyQueryForMobileIndex", params).fetch(pageable);
	}

}
