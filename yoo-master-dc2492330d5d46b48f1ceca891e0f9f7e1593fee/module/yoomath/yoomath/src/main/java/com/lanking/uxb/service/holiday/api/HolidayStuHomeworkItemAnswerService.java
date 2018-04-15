package com.lanking.uxb.service.holiday.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.uxb.service.holiday.form.StuItemAnswerForm;

/**
 * 学生假日作业项-答案相关接口
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
public interface HolidayStuHomeworkItemAnswerService {
	/**
	 * 保存答案入口<br>
	 * 复合题时候会出现多个学生专项题目ID
	 * 
	 * @param stuItemAnswerForm
	 */
	void saveAnswer(StuItemAnswerForm stuItemAnswerForm);

	/**
	 * 保存
	 * 
	 * @param holidystuHkItemQuestionId
	 *            假期学生专项题目ID
	 * @param answers
	 * @param answerAsciis
	 * @param solvingImg
	 *            解题截图
	 * @param studentId
	 *            学生ID
	 * @param type
	 *            题目类型
	 */
	void saveAnswer(long holidystuHkItemQuestionId, List<String> answers, List<String> answerAsciis,
			List<Long> solvingImgs, long studentId, Type type);

	/**
	 * 学生假期作业题目答案保存
	 *
	 * @param holidayStuHkItemQuestionIds
	 *            学生假期作业专项题目id列表
	 * @param answers
	 *            latex答案 HolidayStudentHomeworkItemQuestion.id -> 答案
	 * @param asciiAnswers
	 *            ascii答案 HolidayStudentHomeworkItemQuestion.id -> 答案
	 * @param solvingImgs
	 *            HolidayStudentHomeworkItemQuestion.id -> 解题过程图片
	 * @param studentId
	 *            学生id
	 * @param types
	 *            HolidayStudentHomeworkItemQuestion.id -> 题目类型
	 * @param holidayHomeworkId
	 *            假期作业id
	 * @param holidayHomeworkItemId
	 *            假期作业专项id
	 * @param holidayStuHkId
	 *            学生假期作业id
	 * @param holidayStuHkItemId
	 *            学生假期作业专项id
	 */
	void saveAnswer(List<Long> holidayStuHkItemQuestionIds, Map<Long, List<String>> answers,
			Map<Long, List<String>> asciiAnswers, Map<Long, List<Long>> solvingImgs, long studentId,
			Map<Long, Type> types, long holidayHomeworkId, long holidayHomeworkItemId, long holidayStuHkId,
			long holidayStuHkItemId);

	/**
	 * 获取当前假期作业专项题目对应的答案
	 * 
	 * @param holidystuHkItemQuestionId
	 *            假期学生专项题目ID
	 * @return
	 */
	List<HolidayStuHomeworkItemAnswer> queryItemAnswers(long holidystuHkItemQuestionId);

	/**
	 * 批量获取学生答案对象
	 * 
	 * @param holidystuHkItemQuestionIds
	 * @return
	 */
	Map<Long, List<HolidayStuHomeworkItemAnswer>> queryItemAnswers(Collection<Long> holidystuHkItemQuestionIds);

	/**
	 * 获取学生答案对象列表
	 * 
	 * @param holidystuHkItemQuestionIds
	 * @return
	 */
	List<HolidayStuHomeworkItemAnswer> queryItemAnswerList(Collection<Long> holidystuHkItemQuestionIds);

	/**
	 * 获取假期专项题目
	 * 
	 * @param id
	 * @return
	 */
	HolidayStuHomeworkItemAnswer get(long id);

}
