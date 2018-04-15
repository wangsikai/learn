package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityHomeworkQuestionService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityQuestionService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityStudentHomeworkAnswerService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityStudentHomeworkQuestionService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityStudentHomeworkService;
import com.lanking.uxb.service.resources.ex.HomeworkException;

@Transactional(readOnly = true)
@Service
public class TaskActivityStudentHomeworkServiceImpl implements TaskActivityStudentHomeworkService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> studentHomeworkRepo;

	@Autowired
	private TaskActivityHomeworkQuestionService hqService;
	@Autowired
	private TaskActivityQuestionService questionService;
	@Autowired
	private TaskActivityStudentHomeworkAnswerService shaService;
	@Autowired
	private TaskActivityStudentHomeworkQuestionService shqService;

	@Transactional
	@Override
	public void publishHomework(Homework homework, Set<Long> studentIds, Date createAt) throws HomeworkException {
		if (CollectionUtils.isNotEmpty(studentIds)) {
			List<Long> questionIds = hqService.getQuestion(homework.getId());
			List<Question> qs = questionService.mgetList(questionIds);
			Map<Long, List<Question>> subQuestions = Maps.newHashMap();
			for (Long qid : questionIds) {
				List<Question> subs = questionService.getSubQuestions(qid);
				if (CollectionUtils.isNotEmpty(subs)) {
					subQuestions.put(qid, subs);
				}
			}

			for (Long studentId : studentIds) {
				StudentHomework p = new StudentHomework();
				p.setCreateAt(new Date());
				p.setHomeworkId(homework.getId());
				p.setStatus(StudentHomeworkStatus.NOT_SUBMIT);
				p.setStudentId(studentId);
				p.setStudentCorrected(homework.getCorrectingType() == HomeworkCorrectingType.TEACHER);
				studentHomeworkRepo.save(p);
				boolean autoManualAllCorrected = true;
				for (Question question : qs) {
					StudentHomeworkQuestion shq = shqService.create(p.getId(), question.getId(), false, false,
							question.getType());
					if (question.getAnswerNumber() != null && question.getAnswerNumber() > 0) {
						for (int i = 1; i <= question.getAnswerNumber(); i++) {
							shaService.create(shq.getId(), i);
						}
					}
					if (autoManualAllCorrected && question.getType() != Type.QUESTION_ANSWERING) {
						autoManualAllCorrected = false;
					}
					List<Question> subqs = subQuestions.get(question.getId());
					if (CollectionUtils.isNotEmpty(subqs)) {
						for (Question q : subqs) {
							StudentHomeworkQuestion subShq = shqService.create(p.getId(), q.getId(), true, false,
									q.getType());
							if (q.getAnswerNumber() != null && q.getAnswerNumber() > 0) {
								for (int i = 1; i <= q.getAnswerNumber(); i++) {
									shaService.create(subShq.getId(), i);
								}
							}
							if (autoManualAllCorrected && q.getType() != Type.QUESTION_ANSWERING) {
								autoManualAllCorrected = false;
							}
						}
					}
				}
				if (autoManualAllCorrected) {// 一个学生的一份作业全部为简答题的话,将auto_manual_all_corrected设置为true
					p.setAutoManualAllCorrected(autoManualAllCorrected);
					studentHomeworkRepo.save(p);
				}
			}
		}
	}

}
