package com.lanking.uxb.service.imperialExamination.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.homework.HomeworkKnowledge;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.counter.api.impl.ExerciseCounterProvider;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityExerciseQuestionService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityExerciseService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityHomeworkService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityQuestionService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityStudentClazzService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.form.HomeworkForm;

@Transactional(readOnly = true)
@Service
public class TaskActivityHomeworkServiceImpl implements TaskActivityHomeworkService {

	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;
	
	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	Repo<HomeworkQuestion, Long> homeworkQuestionRepo;

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("HomeworkKnowledgeRepo")
	Repo<HomeworkKnowledge, Long> homeworkKnowledgeRepo;
	@Autowired
	private TaskActivityExerciseService exerciseService;
	@Autowired
	private TaskActivityQuestionService questionService;
	@Autowired
	private ExerciseCounterProvider exerciseCounterProvider;
	@Autowired
	private TaskActivityExerciseQuestionService exerciseQuestionService;
	@Autowired
	private TaskActivityStudentClazzService hkStuClazzService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Transactional
	@Override
	public Homework publish(long createId, long clazzId, long startTime, long endTime, List<Long> questionIds,
			String homeWorkName) {

		// 创建练习
		Exercise exercise = new Exercise();
		Homework homework = null;
		exercise.setCreateAt(new Date());
		exercise.setCreateId(createId);
		exercise.setName(homeWorkName);
		exercise.setStatus(Status.ENABLED);
		exercise.setUpdateAt(exercise.getCreateAt());
		exercise = exerciseService.createExercise(exercise, questionIds);
		boolean hasQuestionAnswering = questionService.hasQuestionAnswering(questionIds);
		Date createAt = new Date();
		HomeworkForm hkForm = new HomeworkForm();
		hkForm.setCreateAt(createAt);
		hkForm.setHomeworkClassId(clazzId);
		hkForm.setCreateId(createId);
		hkForm.setDeadline(endTime);
		hkForm.setExerciseId(exercise.getId());
		hkForm.setStartTime(startTime);
		List<QuestionKnowledge> qks = questionKnowledgeService.findByQuestions(questionIds);
		Set<Long> knowledgesSet = new HashSet<Long>();
		for (QuestionKnowledge qk : qks) {
			knowledgesSet.add(qk.getKnowledgeCode());
		}
		List<Long> knowledgePoints = Lists.newArrayList(knowledgesSet);
		hkForm.setKnowledgePoints(knowledgePoints); // 新知识点
		hkForm.setDifficulty(mgetList(questionIds));
		hkForm.setHasQuestionAnswering(hasQuestionAnswering);
		hkForm.setTextbookCode(exercise.getTextCode());
		hkForm.setCorrectingType(HomeworkCorrectingType.TEACHER);
		hkForm.setAutoIssue(true); // 自动下发设置
		// 创建
		Homework hk = create(hkForm);
		homework = setTime(hk.getId(), startTime, endTime);
		// 分发
		publishHomework(homework.getId());
		saveKnowlgePoints(homework.getId(), knowledgePoints);
		return homework;
	}

	public void saveKnowlgePoints(Long homeworkId, List<Long> knowledgePoints) {
		if (homeworkId > 0 && CollectionUtils.isNotEmpty(knowledgePoints)) {
			for (Long knowledgeCode : knowledgePoints) {
				HomeworkKnowledge hkd = new HomeworkKnowledge();
				hkd.setHomeworkId(homeworkId);
				hkd.setKnowledgeCode(knowledgeCode);
				homeworkKnowledgeRepo.save(hkd);
			}
		}
	}

	public BigDecimal mgetList(Collection<Long> ids) {
		List<Question> qList = questionRepo.mgetList(ids);
		double sum = 0.0;
		for (Question q : qList) {
			sum += q.getDifficulty();
		}
		return new BigDecimal(sum / qList.size());
	}

	public void publishHomework(long homeworkId) throws HomeworkException {
		final Homework p = homeworkRepo.get(homeworkId);
		List<Long> studentIds = hkStuClazzService.listClassStudents(p.getHomeworkClassId());
		if (CollectionUtils.isNotEmpty(studentIds)) {
			p.setDistributeCount((long) studentIds.size());
			homeworkRepo.save(p);

			Set<Long> stuIds = Sets.newHashSet(studentIds);
			Date now = new Date();
			studentHomeworkService.publishHomeworkWithDelStatus(p, stuIds, now,Status.DELETED);
		}
		updateStatus(homeworkId, HomeworkStatus.PUBLISH);
	}

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
			}
			return homeworkRepo.save(homework);
		} else {
			throw new HomeworkException(HomeworkException.HOMEWORK_HAS_PUBLISH);
		}
	}

	public Homework create(HomeworkForm form) throws HomeworkException {
		Homework p = new Homework();
		p.setCourseId(form.getCourseId());
		p.setHomeworkClassId(form.getHomeworkClassId());
		p.setExerciseId(form.getExerciseId());
		p.setName(exerciseService.get(form.getExerciseId()).getName());
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
		p.setDelStatus(Status.DELETED);
		p.setTextbookCode(form.getTextbookCode());
		p.setCorrectingType(form.getCorrectingType());
		p.setMetaKnowpoints(form.getMetaKnowpoints());
		// 新知识点 yoomath v2.1.2 wanlong.che 2016-11-23
		p.setKnowledgePoints(form.getKnowledgePoints());
		p.setLastIssued(form.getLastIssued());
		p.setHasQuestionAnswering(form.isHasQuestionAnswering());
		long questionCount = exerciseCounterProvider.getQuestionCount(form.getExerciseId());
		p.setQuestionCount(questionCount);
		// 添加自动下发标记 yoomath v2.3.0 wanlong.che 2016-12-13
		p.setAutoIssue(form.isAutoIssue());
		Homework p0 = homeworkRepo.save(p);
		createByExercise(p.getId(), p0.getExerciseId());
		// homeworkCounterProvider.incrQuestionCount(p0.getId(), (int)
		// questionCount);
		return p0;
	}

	public Homework updateStatus(long homeworkId, HomeworkStatus status) throws HomeworkException {
		Homework homework = homeworkRepo.get(homeworkId);
		if (homework.getStatus() != status) {
			homework.setStatus(status);
			if (status == HomeworkStatus.ISSUED) {
				homework.setIssueAt(new Date());
			}
			return homeworkRepo.save(homework);
		} else {
			return homework;
		}
	}

	public void createByExercise(long homeworkId, long exerciseId) throws HomeworkException {
		List<Long> qids = exerciseQuestionService.getQuestion(exerciseId);
		int i = 1;
		for (Long qid : qids) {
			HomeworkQuestion p = new HomeworkQuestion();
			p.setHomeworkId(homeworkId);
			p.setQuestionId(qid);
			p.setSequence(i);
			p.setStatus(Status.ENABLED);
			homeworkQuestionRepo.save(p);
			i++;
		}
	}

	@Override
	public List<Long> getIssueHomeworkUserId(Long code, Date startTime, Date endTime,
			ImperialExaminationProcess process) {
		Params params = Params.param();
		params.put("code", code);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("process", process.ordinal());

		return homeworkRepo.find("$getIssueHomeworkUserId", params).list(Long.class);
	}

	@Override
	public List<Long> getIssuedHomeworkStudentId(Long code, Date startTime, Date endTime) {
		Params params = Params.param();
		params.put("code", code);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		return homeworkRepo.find("$getCommitHomeworkStudentId", params).list(Long.class);
	}

	@Transactional
	@Override
	public void modifyImperialHomeworkDelstatus(Long code) {
		Params params = Params.param();
		params.put("code", code);
		
		homeworkRepo.execute("$modifyHomeworkDelstatus", params);
		homeworkRepo.execute("$modifyStudentHomeworkDelstatus", params);
	}

}
