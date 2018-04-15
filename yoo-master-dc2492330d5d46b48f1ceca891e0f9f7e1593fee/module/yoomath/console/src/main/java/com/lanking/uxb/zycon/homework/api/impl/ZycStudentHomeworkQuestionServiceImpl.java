package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkQuestionService;
import com.lanking.uxb.zycon.homework.cache.ZycStudentHomeworkQuestionCacheService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Service
@Transactional(readOnly = true)
public class ZycStudentHomeworkQuestionServiceImpl implements ZycStudentHomeworkQuestionService {

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	private Repo<StudentHomeworkQuestion, Long> hqRepo;

	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	private Repo<StudentHomeworkAnswer, Long> shaRepo;

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	private Repo<StudentHomework, Long> stuHkRepo;

	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;
	@Autowired
	private ZycStudentHomeworkQuestionCacheService zycStudentHomeworkQuestionCacheService;

	@Override
	public StudentHomeworkQuestion get(Long id) {
		return hqRepo.get(id);
	}

	@Override
	public Map<Long, StudentHomeworkQuestion> mget(Collection<Long> ids) {
		return hqRepo.mget(ids);
	}

	@Override
	public void removePushedId(Long homeworkId, List<Long> ids) {
		zycStudentHomeworkQuestionCacheService.remove(homeworkId, ids);
	}

	@Transactional(readOnly = true)
	@Override
	public StudentHomeworkQuestion find(long studentHomeworkId, long questionId) {
		return hqRepo.find("$zycFindByStudentHomeworkIdAndQuestionId",
				Params.param("studentHomeworkId", studentHomeworkId).put("questionId", questionId)).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomeworkQuestion> find(long studentHomeworkId, Collection<Long> questionIds) {
		return hqRepo.find("$zycFindByStudentHomeworkIdAndQuestionId",
				Params.param("studentHomeworkId", studentHomeworkId).put("questionIds", questionIds)).list();
	}

	@Override
	public Long countNotCorrected(long studentHomeworkId) {
		return hqRepo.find("$zycCountStudentHomeworkNotCorrected", Params.param("stuHkId", studentHomeworkId)).count();
	}

	@Override
	public List<StudentHomeworkQuestion> find(long studentHomeworkId) {
		return hqRepo
				.find("$zycFindByStudentHomeworkIdAndQuestionId", Params.param("studentHomeworkId", studentHomeworkId))
				.list();
	}

	@Override
	public List<Long> getCorrectQuestions(long stuHomeworkId) {
		return hqRepo.find("$zycGetCorrectQuestions", Params.param("stuHkId", stuHomeworkId)).list(Long.class);
	}

	@Override
	public Map<Long, Question> findByStuHk(long stuHkId) {
		List<Question> questions = questionRepo.find("$zycFindByStuHk", Params.param("stuHkId", stuHkId)).list();
		Map<Long, Question> questionMap = new HashMap<Long, Question>(questions.size());
		for (Question q : questions) {
			questionMap.put(q.getId(), q);
		}
		return questionMap;
	}

	@Override
	public List<StudentHomeworkQuestion> queryStuQuestions(long studentHomeworkId, long questionId,
			Collection<Long> questionIds, boolean newCorrect) {
		Params param = Params.param("studentHomeworkId", studentHomeworkId);
		if (newCorrect) {
			param.put("newCorrect", 1);
		}
		if (questionId > 0) {
			param.put("questionId", questionId);
		}
		if (null != questionIds && questionIds.size() > 0) {
			param.put("questionIds", questionIds);
		}
		return hqRepo.find("$zycQueryStuQuestions", param).list();
	}

	@Override
	@Transactional
	public void updateConfirmStatus(long stuHkQId, HomeworkConfirmStatus status) {
		Params param = Params.param("id", stuHkQId);
		param.put("confirmStatus", status.getValue());
		hqRepo.execute("$zycUpdateConfirmStatus",param);
	}
	
	@Override
	@Transactional
	public void confirm(List<Long> ids) {
		Params params = Params.param("ids", ids).put("status", HomeworkConfirmStatus.HAD_CONFIRM.getValue());
		hqRepo.execute("$zycUpdateStatus", params);
	}
}
