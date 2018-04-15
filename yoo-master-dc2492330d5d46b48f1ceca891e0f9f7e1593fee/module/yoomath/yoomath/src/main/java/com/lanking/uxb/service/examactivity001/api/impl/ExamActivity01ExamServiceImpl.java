package com.lanking.uxb.service.examactivity001.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001Answer;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001Question;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01ExamService;

import httl.util.CollectionUtils;

/**
 * 期末活动真题相关接口.
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 *
 * @version 2017年12月27日
 */
@Service
@Transactional(readOnly = true)
public class ExamActivity01ExamServiceImpl implements ExamActivity01ExamService {
	
	@Autowired
	@Qualifier("ExamActivity001QuestionRepo")
	private Repo<ExamActivity001Question, Long> questionRepo;
	
	@Autowired
	@Qualifier("ExamActivity001AnswerRepo")
	private Repo<ExamActivity001Answer, Long> answerRepo;

	@Override
	public List<ExamActivity001Question> getPastExams(Integer category, Integer grade, Integer type) {
		Params params = Params.param("category", category);
		
		params.put("grade", grade);
		params.put("type", type);
		
		return questionRepo.find("$ymFindExams", params).list();
	}

	@Override
	public List<Long> getPastExamQuestionIds(Long examCode) {
		ExamActivity001Question exam = questionRepo.get(examCode);
		
		if(exam != null){
			return exam.getQuestionList();
		}
		
		return null;
	}

	@Override
	public List<ExamActivity001Answer> getPastExamAnswers(Long userId,List<Long> codes,Long activityCode) {
		Params params = Params.param("codes", codes);
		params.put("userId", userId);
		params.put("activityCode", activityCode);
		
		return answerRepo.find("$ymFindExamAnswers", params).list();
	}

	@Override
	public ExamActivity001Answer getPastExamAnswer(Long userId,Long code,Long activityCode) {
		Params params = Params.param("code", code);
		params.put("userId", userId);
		params.put("activityCode", activityCode);
		
		List<ExamActivity001Answer> answers = answerRepo.find("$ymFindExamAnswer", params).list(ExamActivity001Answer.class);
		if(CollectionUtils.isNotEmpty(answers)){
			return answers.get(0);
		} else {
			return null;
		}
	}

	@Override
	public ExamActivity001Question getPastExam(Long examCode) {
		return questionRepo.get(examCode);
	}

	@Override
	@Transactional
	public ExamActivity001Answer save(ExamActivity001Answer answer) {
		Params params = Params.param("code", answer.getExamQuestioncode());
		params.put("userId", answer.getUserId());
		
		return answerRepo.save(answer);
	}

}
