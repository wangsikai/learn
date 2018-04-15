package com.lanking.uxb.service.examactivity001.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001Answer;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001Question;

/**
 * 期末活动真题相关接口.
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 *
 * @version 2017年12月27日
 */
public interface ExamActivity01ExamService {
	
	/**
	 * 获取真题列表
	 * 
	 * @param code
	 */
	List<ExamActivity001Question> getPastExams(Integer category, Integer grade, Integer type);
	
	/**
	 * 获取真题试卷
	 * 
	 * @param code
	 */
	ExamActivity001Question getPastExam(Long examCode);
	
	/**
	 * 获取真题题目id
	 * 
	 * @param code
	 */
	List<Long> getPastExamQuestionIds(Long examCode);
	
	/**
	 * 获取真题对应的答案记录
	 * 
	 * @param code
	 */
	List<ExamActivity001Answer> getPastExamAnswers(Long userId,List<Long> codes,Long activityCode);
	
	/**
	 * 获取真题对应的答案记录
	 * 
	 * @param code
	 */
	ExamActivity001Answer getPastExamAnswer(Long userId,Long code,Long activityCode);
	
	/**
	 * 保存答案记录
	 * 
	 * @param code
	 */
	 ExamActivity001Answer save(ExamActivity001Answer answer);
}
