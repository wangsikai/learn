package com.lanking.uxb.service.holiday.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.service.question.api.QuestionService;

/**
 * @see HolidayStuHomeworkItemQuestionService
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional(readOnly = true)
public class HolidayStuHomeworkItemQuestionServiceImpl implements HolidayStuHomeworkItemQuestionService {
	@Autowired
	@Qualifier("HolidayStuHomeworkItemQuestionRepo")
	private Repo<HolidayStuHomeworkItemQuestion, Long> repo;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemAnswerRepo")
	private Repo<HolidayStuHomeworkItemAnswer, Long> answerRepo;
	@Autowired
	private QuestionService questionService;

	@Override
	@Transactional
	public void create(List<Question> questions, long holidayHomeworkId, long holidayHomeworkItemId,
			long holidayStuHomeworkId, long holidayStuHomeworkItemId, long studentId, HolidayHomeworkType type) {
		for (Question questionVal : questions) {
			List<Question> subQuestions = questionService.getSubQuestions(questionVal.getId());
			HolidayStuHomeworkItemQuestion question = new HolidayStuHomeworkItemQuestion();
			question.setAutoCorrect(false);
			question.setCreateAt(new Date());
			question.setHolidayHomeworkId(holidayHomeworkId);
			question.setHolidayHomeworkItemId(holidayHomeworkItemId);
			question.setHolidayStuHomeworkId(holidayStuHomeworkId);
			question.setHolidayStuHomeworkItemId(holidayStuHomeworkItemId);
			question.setManualCorrect(false);
			question.setQuestionId(questionVal.getId());
			question.setStudentId(studentId);
			question.setType(type);
			question.setSubFlag(false);

			repo.save(question);

			if (Question.Type.SINGLE_CHOICE == questionVal.getType()
					|| Question.Type.MULTIPLE_CHOICE == questionVal.getType()
					|| Question.Type.TRUE_OR_FALSE == questionVal.getType()) {
				HolidayStuHomeworkItemAnswer answer;
				answer = new HolidayStuHomeworkItemAnswer();
				answer.setHolidayHomeworkId(holidayHomeworkId);
				answer.setHolidayHomeworkItemId(holidayHomeworkItemId);
				answer.setHolidayStuHomeworkItemId(holidayStuHomeworkItemId);
				answer.setHolidayStuHomeworkItemQuestionId(question.getId());
				answer.setAnswerId(question.getStudentId());
				answer.setContent("");
				answer.setContentAscii("");
				answer.setType(type);
				answer.setSequence(1);
				answerRepo.save(answer);
			} else {
				for (int i = 1; i <= questionVal.getAnswerNumber(); i++) {
					HolidayStuHomeworkItemAnswer answer;
					answer = new HolidayStuHomeworkItemAnswer();
					answer.setHolidayHomeworkId(holidayHomeworkId);
					answer.setHolidayHomeworkItemId(holidayHomeworkItemId);
					answer.setHolidayStuHomeworkItemId(holidayStuHomeworkItemId);
					answer.setHolidayStuHomeworkItemQuestionId(question.getId());
					answer.setContent("");
					answer.setContentAscii("");
					answer.setAnswerId(question.getStudentId());
					answer.setType(type);
					answer.setSequence(i);
					answerRepo.save(answer);
				}
			}

			for (Question q : subQuestions) {
				question = new HolidayStuHomeworkItemQuestion();
				question.setAutoCorrect(false);
				question.setCreateAt(new Date());
				question.setHolidayHomeworkId(holidayHomeworkId);
				question.setHolidayHomeworkItemId(holidayHomeworkItemId);
				question.setHolidayStuHomeworkId(holidayStuHomeworkId);
				question.setHolidayStuHomeworkItemId(holidayStuHomeworkItemId);
				question.setManualCorrect(false);
				question.setQuestionId(q.getId());
				question.setStudentId(studentId);
				question.setType(type);
				question.setSubFlag(true);

				repo.save(question);

				if (q.getType() == Question.Type.SINGLE_CHOICE || q.getType() == Question.Type.MULTIPLE_CHOICE
						|| q.getType() == Question.Type.TRUE_OR_FALSE) {
					HolidayStuHomeworkItemAnswer subAnswer = new HolidayStuHomeworkItemAnswer();
					subAnswer.setHolidayHomeworkId(holidayHomeworkId);
					subAnswer.setHolidayHomeworkItemId(holidayHomeworkItemId);
					subAnswer.setHolidayStuHomeworkItemId(holidayStuHomeworkItemId);
					subAnswer.setHolidayStuHomeworkItemQuestionId(question.getId());
					subAnswer.setAnswerId(question.getStudentId());
					subAnswer.setContent("");
					subAnswer.setContentAscii("");
					subAnswer.setType(type);
					subAnswer.setSequence(1);
					answerRepo.save(subAnswer);
				} else {
					for (int i = 1; i <= q.getAnswerNumber(); i++) {
						HolidayStuHomeworkItemAnswer subAnswer = new HolidayStuHomeworkItemAnswer();
						subAnswer.setHolidayHomeworkId(holidayHomeworkId);
						subAnswer.setHolidayHomeworkItemId(holidayHomeworkItemId);
						subAnswer.setHolidayStuHomeworkItemId(holidayStuHomeworkItemId);
						subAnswer.setHolidayStuHomeworkItemQuestionId(question.getId());
						subAnswer.setAnswerId(question.getStudentId());
						subAnswer.setContent("");
						subAnswer.setContentAscii("");
						subAnswer.setType(type);
						subAnswer.setSequence(i);
						answerRepo.save(subAnswer);
					}
				}
			}
		}
	}

	@Transactional
	@Override
	public void saveSolvingImg(long holidayStuHomeworkItemId, Long solvingImg) {
		repo.execute("$saveHolidaySolvingImg",
				Params.param("stuItemId", holidayStuHomeworkItemId).put("solvingImg", solvingImg));

	}

	@Override
	@Transactional
	public void saveAnswerImg(long holidayStuHomeworkItemId, Long answerImg) {
		repo.execute("$saveHolidayAnswerImg",
				Params.param("stuItemId", holidayStuHomeworkItemId).put("answerImg", answerImg));
	}

	@Override
	public HolidayStuHomeworkItemQuestion find(Long questionId, Long holidayStuHomeworkItemId) {
		return repo.find("$findStuItemQuestion",
				Params.param("questionId", questionId).put("stuItemId", holidayStuHomeworkItemId)).get();
	}

	@Override
	public List<HolidayStuHomeworkItemQuestion> find(Collection<Long> questionIds, Long holidayStuHomeworkItemId) {
		return repo.find("$findStuItemQuestion",
				Params.param("questionIds", questionIds).put("stuItemId", holidayStuHomeworkItemId)).list();
	}

	@Override
	public Map<Long, HolidayStuHomeworkItemQuestion> mget(List<Long> stuHKQIds) {
		return repo.mget(stuHKQIds);
	}

	@Override
	public List<HolidayStuHomeworkItemQuestion> queryQuestionList(Long holidayStuHomeworkItemId) {
		return repo.find("$findStuItemQuestion", Params.param("stuItemId", holidayStuHomeworkItemId)).list();
	}

}
