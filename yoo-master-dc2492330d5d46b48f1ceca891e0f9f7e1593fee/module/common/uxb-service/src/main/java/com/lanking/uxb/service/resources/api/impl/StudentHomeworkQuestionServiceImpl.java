package com.lanking.uxb.service.resources.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;

@Transactional(readOnly = true)
@Service
public class StudentHomeworkQuestionServiceImpl implements StudentHomeworkQuestionService {

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	Repo<StudentHomeworkQuestion, Long> studentHomeworkQuestionRepo;

	@Transactional(readOnly = true)
	@Override
	public StudentHomeworkQuestion get(long id) {
		return studentHomeworkQuestionRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomeworkQuestion> mgetList(Collection<Long> ids) {
		return studentHomeworkQuestionRepo.mgetList(ids);
	}

	@Transactional
	@Override
	public StudentHomeworkQuestion create(long studentHomeworkId, long questionId, boolean sub, boolean correct,
			Type type) {
		StudentHomeworkQuestion p = new StudentHomeworkQuestion();
		p.setQuestionId(questionId);
		p.setResult(HomeworkAnswerResult.INIT);
		p.setStudentHomeworkId(studentHomeworkId);
		p.setCorrect(correct);
		p.setSubFlag(sub);
		p.setType(type);
		return studentHomeworkQuestionRepo.save(p);
	}

	@Transactional(readOnly = true)
	@Override
	public StudentHomeworkQuestion find(long studentHomeworkId, long questionId) {
		return studentHomeworkQuestionRepo.find("$findByStudentHomeworkIdAndQuestionId",
				Params.param("studentHomeworkId", studentHomeworkId).put("questionId", questionId)).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomeworkQuestion> queryStuQuestions(long studentHomeworkId, long questionId,
			Collection<Long> questionIds, boolean newCorrect) {
		Params param = Params.param("studentHomeworkId", studentHomeworkId);
		if (newCorrect) {
			param.put("new_correct", newCorrect);
		}
		if (questionId > 0) {
			param.put("questionId", questionId);
		}
		if (null != questionIds && questionIds.size() > 0) {
			param.put("questionIds", questionIds);
		}
		return studentHomeworkQuestionRepo.find("$queryStuQuestions", param).list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomeworkQuestion> find(long studentHomeworkId, Collection<Long> questionIds) {
		return studentHomeworkQuestionRepo.find("$findByStudentHomeworkIdAndQuestionId",
				Params.param("studentHomeworkId", studentHomeworkId).put("questionIds", questionIds)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomeworkQuestion> find(long studentHomeworkId) {
		return studentHomeworkQuestionRepo
				.find("$findByStudentHomeworkIdAndQuestionId", Params.param("studentHomeworkId", studentHomeworkId))
				.list();
	}

	@Override
	public Integer getNeedCorrectQuestionCount(long studentHomeworkId) {
		return studentHomeworkQuestionRepo
				.find("$findNeedCorrectQuestionCount", Params.param("studentHomeworkId", studentHomeworkId))
				.get(Integer.class);
	}

	@Override
	public Integer getCorrectQuestionCount(long studentHomeworkId) {
		return studentHomeworkQuestionRepo
				.find("$findCorrectQuestionCount", Params.param("studentHomeworkId", studentHomeworkId))
				.get(Integer.class);
	}

	@Override
	public Integer getCorrectedQuestionCount(long studentHomeworkId) {
		return studentHomeworkQuestionRepo
				.find("$findCorrectedQuestionCount", Params.param("studentHomeworkId", studentHomeworkId))
				.get(Integer.class);
	}

	@Override
	public boolean getCorrectAnswerCorrectStatus(long studentHomeworkId) {
		Integer noCorrectQuestionNum = studentHomeworkQuestionRepo
				.find("$findNoCorrectQuestionCount", Params.param("studentHomeworkId", studentHomeworkId))
				.get(Integer.class);

		return noCorrectQuestionNum > 0;
	}

	@Override
	@Transactional
	public void updateReviseStatus(long id) {
		Params params = Params.param();
		params.put("id", id);

		studentHomeworkQuestionRepo.execute("$updateReviseStatus", params);
	}

	@Override
	public List<StudentHomeworkQuestion> findByNewCorrect(List<Long> studentHomeworkIds, Collection<Long> questionIds,
			Boolean newCorrect) {
		Params params = Params.param();
		params.put("studentHomeworkIds", studentHomeworkIds);
		if (questionIds != null && !questionIds.isEmpty()) {
			params.put("questionIds", questionIds);
		}
		if (newCorrect != null) {
			params.put("newCorrect", newCorrect);
		}

		return studentHomeworkQuestionRepo.find("$findByNewCorrect", params).list();
	}

	@Override
	public long getCorrectAnswerTeacherCorrectCount(long studentHomeworkId) {
		return studentHomeworkQuestionRepo
				.find("$queryCorrectAnswerTeacherCorrectCount", Params.param("studentHomeworkId", studentHomeworkId))
				.count();
	}

	@Transactional
	@Override
	public void setStudentHomeworkQuestionConfirmStatus(long studentHomeworkQuestionId,
			HomeworkConfirmStatus confirmStatus) {
		studentHomeworkQuestionRepo.execute("$setHomeworkQuestionConfirmStatus",
				Params.param("studentHomeworkQuestionId", studentHomeworkQuestionId).put("confirmStatus",
						confirmStatus.getValue()));

	}
}
