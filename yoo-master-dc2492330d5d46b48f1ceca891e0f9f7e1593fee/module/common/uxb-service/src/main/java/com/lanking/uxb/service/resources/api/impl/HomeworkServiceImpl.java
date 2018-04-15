package com.lanking.uxb.service.resources.api.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.counter.api.impl.ExerciseCounterProvider;
import com.lanking.uxb.service.counter.api.impl.HomeworkCounterProvider;
import com.lanking.uxb.service.resources.api.ExerciseService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.form.HomeworkForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

@Transactional(readOnly = true)
@Service
public class HomeworkServiceImpl implements HomeworkService {

	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;

	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private ExerciseCounterProvider exerciseCounterProvider;
	@Autowired
	private HomeworkCounterProvider homeworkCounterProvider;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkQuestionService hkQuestionService;
	@Autowired
	private ZyHomeworkClassGroupStudentService zyHomeworkClassGroupStudentService;

	@Transactional(readOnly = true)
	@Override
	public Homework get(long id) {
		return homeworkRepo.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Homework> mget(Collection<Long> ids) {
		return homeworkRepo.mget(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public Homework load(long id) throws HomeworkException {
		Homework homework = get(id);
		if (homework == null) {
			throw new HomeworkException(HomeworkException.HOMEWORK_NOT_EXIST);
		} else {
			return homework;
		}
	}

	@Transactional
	@Override
	public Homework create(HomeworkForm form) throws HomeworkException {
		Homework p = new Homework();
		p.setCourseId(form.getCourseId());
		p.setHomeworkClassId(form.getHomeworkClassId());
		if (form.getGroupId() != null) {
			p.setHomeworkClassGroupId(form.getGroupId());
		}
		p.setExerciseId(form.getExerciseId());
		if (form.getName() == null) {
			p.setName(exerciseService.get(form.getExerciseId()).getName());
		} else {
			p.setName(form.getName());
		}
		p.setDifficulty(form.getDifficulty());
		p.setCreateId(form.getCreateId());
		p.setOriginalCreateId(p.getCreateId());
		p.setCreateAt(form.getCreateAt() == null ? new Date() : form.getCreateAt());
		if (form.getDeadline() != 0 && form.getStartTime() != 0) {
			p.setDeadline(new Date(form.getDeadline()));
			p.setStartTime(new Date(form.getStartTime()));
		} else if ((form.getDeadline() == 0 && form.getStartTime() != 0)
				|| (form.getDeadline() != 0 && form.getStartTime() == 0)
				|| (form.getDeadline() < form.getStartTime())) {
			throw new HomeworkException(HomeworkException.HOMEWORK_TIME_ILLEGAL);
		}
		p.setStatus(HomeworkStatus.INIT);
		p.setTextbookCode(form.getTextbookCode());
		p.setCorrectingType(form.getCorrectingType());
		p.setMetaKnowpoints(form.getMetaKnowpoints());
		// 新知识点 yoomath v2.1.2 wanlong.che 2016-11-23
		p.setKnowledgePoints(form.getKnowledgePoints());
		p.setLastIssued(form.getLastIssued());
		p.setHasQuestionAnswering(form.isHasQuestionAnswering());
		p.setAnswerQuestionAutoCorrect(form.isAnswerQuestionAutoCorrect());
		long questionCount = exerciseCounterProvider.getQuestionCount(form.getExerciseId());
		p.setQuestionCount(questionCount);
		// 不再需要自动下发标记 pengcheng.yu 2018-02-09 @since 小优快批
		// p.setAutoIssue(form.isAutoIssue());
		// 通过题目查询到的新知识点 教师端v1.3.0
		p.setQuestionknowledgePoints(form.getQuestionknowledgePoints());
		// 作业限时时间
		p.setTimeLimit(form.getTimeLimit());

		Homework p0 = homeworkRepo.save(p);
		hqService.createByExercise(p.getId(), p0.getExerciseId());
		// homeworkCounterProvider.incrQuestionCount(p0.getId(), (int)
		// questionCount);
		return p0;
	}

	@Transactional
	@Override
	public Homework setTime(long homeworkId, long startTime, long deadline) throws HomeworkException {
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
				// 发布作业
				publishHomework(homeworkId);
			}
			return homeworkRepo.save(homework);
		} else {
			throw new HomeworkException(HomeworkException.HOMEWORK_HAS_PUBLISH);
		}
	}

	@Transactional
	@Override
	public void updateStatus(long homeworkId, HomeworkStatus status) throws HomeworkException {
		homeworkRepo.execute("$updateStatus", Params.param("homeworkId", homeworkId).put("status", status.getValue()));
	}

	@Transactional(readOnly = true)
	@Override
	public CursorPage<Long, Homework> queryShouldPublish(Date now, CursorPageable<Long> cpr) {
		return homeworkRepo.find("$findAllNotPublish", Params.param("nowtime", now)).fetch(cpr);
	}

	@Transactional(readOnly = true)
	@Override
	public CursorPage<Long, Homework> queryShouldCommit(Date now, CursorPageable<Long> cpr) {
		return homeworkRepo.find("$findAllNotCommit", Params.param("nowtime", now)).fetch(cpr);
	}

	@Override
	public CursorPage<Long, Homework> queryShouldIssue(Date now, int issueHour, CursorPageable<Long> cpr) {
		// 全选择题的作业在截止时间就下发，其他的需要issueHour小时
		// 超过issueHour小时的全部过滤掉
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.HOUR_OF_DAY, -issueHour);
		cal.add(Calendar.HOUR_OF_DAY, -1); // 为防止系统异常未处理，延期取一小时内的作业
		Date issuetime = cal.getTime();
		return homeworkRepo.find("$findAllNotIssue", Params.param("issuetime", issuetime).put("nowtime", now))
				.fetch(cpr);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Homework> findByCourseId(long courseId, HomeworkStatus status) throws HomeworkException {
		Params params = Params.param("courseId", courseId);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return homeworkRepo.find("$findByCourse", params).list();
	}

	@Transactional
	@Override
	@Deprecated
	public void issue(long homeworkId) throws HomeworkException {
		updateStatus(homeworkId, HomeworkStatus.ISSUED);
		studentHomeworkService.issue(homeworkId);
	}

	@Transactional(readOnly = true)
	@Override
	public CursorPage<Long, Homework> findByCourseId(long courseId, String keyword, CursorPageable<Long> cpr) {
		Params params = Params.param("courseId", courseId).put("next", cpr.getCursor());
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", "%" + keyword + "%");
		}
		return homeworkRepo.find("$findByCourse", params).fetch(cpr);
	}

	@Transactional
	@Override
	public void publishHomework(long homeworkId) throws HomeworkException {
		Homework p = homeworkRepo.get(homeworkId);
		List<Long> studentIds = zyHkStuClazzService.listClassStudents(p.getHomeworkClassId());
		if (p.getHomeworkClassGroupId() != null && p.getHomeworkClassGroupId() != 0) {
			studentIds = zyHomeworkClassGroupStudentService.findGroupStudents(p.getHomeworkClassId(),
					p.getHomeworkClassGroupId());
		} else {
			studentIds = zyHkStuClazzService.listClassStudents(p.getHomeworkClassId());
		}
		if (CollectionUtils.isNotEmpty(studentIds)) {
			p.setDistributeCount((long) studentIds.size());// 设置下发数量
			homeworkRepo.save(p);
			studentHomeworkService.publishHomework(p, Sets.newHashSet(studentIds), new Date());
		}
		updateStatus(homeworkId, HomeworkStatus.PUBLISH);
	}

	@Transactional
	@Override
	public void saveRightRate(long id, double rightRate) {
		Homework p = homeworkRepo.get(id);
		p.setRightRate(BigDecimal.valueOf(rightRate));
		homeworkRepo.save(p);
	}

	@Transactional
	@Override
	public Homework updateName(long id, String name) {
		Homework p = homeworkRepo.get(id);
		if (p.getName().equals(name)) {
			return p;
		} else {
			p.setName(name);
			return homeworkRepo.save(p);
		}
	}

	@Transactional
	@Override
	public void delete(long id) throws HomeworkException {
		Homework p = get(id);
		if (p.getStatus() == HomeworkStatus.INIT) {
			homeworkRepo.deleteById(id);
			hqService.deleteByHomework(id);
		} else {
			throw new NoPermissionException();
		}
	}

	@Transactional
	@Override
	public void incrCommitCount(long id) {
		homeworkRepo.execute("$incrCommitCount", Params.param("id", id));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Date getFirstCreateAt(Long teacherId) {
		Params params = Params.param("teacherId", teacherId);
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(teacherId);
		Set<Long> clazzIds = Sets.newHashSet();
		for (HomeworkClazz clazz : clazzs) {
			clazzIds.add(clazz.getId());
		}
		if (clazzIds.size() > 0) {
			params.put("homeworkClassIds", clazzIds);
		}
		List<Map> list = homeworkRepo.find("$getFirstCreateAt", params).list(Map.class);
		return list.size() == 0 ? null : (Date) list.get(0).get("ct");
	}

	@Transactional
	@Override
	public void closeAutoIssued(long hkId) {
		Homework p = get(hkId);
		if (p.isAutoIssue()) {
			p.setAutoIssue(false);
			homeworkRepo.save(p);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Date getAutoIssuedTime(long hkId, int issueHour) {
		Homework p = get(hkId);
		List<Map> map = homeworkRepo.find("$QueryFill", Params.param("hkId", hkId)).list(Map.class);
		int fillCount = 0;
		int questionAnswer = 0;
		for (Map m : map) {
			Integer count = Integer.parseInt(String.valueOf(m.get("count")));
			Type type = Type.findByValue(Integer.parseInt(String.valueOf(m.get("type"))));
			if (type == Type.FILL_BLANK) {
				fillCount = count;
			}
			if (type == Type.QUESTION_ANSWERING) {
				questionAnswer = count;
			}
		}
		if (fillCount == 0 && questionAnswer == 0) {
			return null;
		} else {
			if (questionAnswer > 0) {
				return null;
			} else {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(p.getDeadline());
				calendar.add(Calendar.HOUR_OF_DAY, issueHour);// 把日期往后增加一天
				return calendar.getTime();
			}
		}
	}

	@Override
	@Transactional
	public void setDeadline(long hkId, Date deadline) {
		if (hkId <= 0 || deadline == null || deadline.getTime() <= System.currentTimeMillis()) {
			throw new IllegalArgException();
		}
		Homework homework = homeworkRepo.get(hkId);
		if (null == homework) {
			throw new IllegalArgException();
		}
		if (homework.getDelStatus() != Status.ENABLED) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_HOMEWORK_UPDATEDEADLINE_DELETED);
		}

		if (deadline.getTime() <= homework.getDeadline().getTime()) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_HOMEWORK_UPDATEDEADLINE_EXPIRE);
		}

		if (homework.getStatus() == HomeworkStatus.ISSUED) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_HOMEWORK_UPDATEDEADLINE_ISSUED);
		}

		homework.setDeadline(deadline);
		homeworkRepo.save(homework);
	}

	@Override
	@Transactional
	public void setAutoIssued(long hkId, boolean autoIssue) {
		Homework homework = homeworkRepo.get(hkId);
		homework.setAutoIssue(autoIssue);
		homeworkRepo.save(homework);
	}

	@Override
	public CursorPage<Long, Homework> queryShouldDelay(Date now, CursorPageable<Long> cursor) {
		Params params = Params.param();
		params.put("nowtime", DateUtils.addMinutes(now, 30));
		return homeworkRepo.find("$findShouldDelay", params).fetch(cursor);
	}

	@Override
	public List<Homework> mgetList(Collection<Long> ids) {
		return homeworkRepo.mgetList(ids);
	}

	@Override
	public CursorPage<Long, Homework> queryShouldCalRightRate(CursorPageable<Long> cpr) {
		// 作业截止时间超过24小时的不再处理
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, -24);
		return homeworkRepo.find("$findAllNeedCalRightRate", Params.param("lastTime", cal.getTime())).fetch(cpr);
	}

	@Transactional
	@Override
	public Homework setTimeAndTimelimit(long homeworkId, long startTime, long deadline, Integer timeLimit)
			throws HomeworkException {
		Homework homework = homeworkRepo.get(homeworkId);
		if (homework.getStatus() == HomeworkStatus.INIT
				|| (homework.getStartTime().getTime() > System.currentTimeMillis())) {
			if ((startTime == 0 && deadline != 0) || (startTime != 0 && deadline == 0) || (startTime > deadline)) {
				throw new HomeworkException(HomeworkException.HOMEWORK_TIME_ILLEGAL);
			}
			homework.setStartTime(new Date(startTime));
			homework.setDeadline(new Date(deadline));
			homework.setTimeLimit(timeLimit);
			if (startTime <= System.currentTimeMillis()) {
				// 立即发布
				homework.setStatus(HomeworkStatus.PUBLISH);
				// 发布作业
				publishHomework(homeworkId);
			}
			return homeworkRepo.save(homework);
		} else {
			throw new HomeworkException(HomeworkException.HOMEWORK_HAS_PUBLISH);
		}
	}
}
