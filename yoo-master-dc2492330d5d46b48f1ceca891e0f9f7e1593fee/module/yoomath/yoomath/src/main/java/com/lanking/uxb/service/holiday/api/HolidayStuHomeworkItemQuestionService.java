package com.lanking.uxb.service.holiday.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;

/**
 * 学生专项练习题目接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public interface HolidayStuHomeworkItemQuestionService {
	/**
	 * 保存学生假期作业专项练习题目数据
	 *
	 * @param questions
	 *            题目列表
	 * @param holidayHomeworkId
	 *            假期作业id
	 * @param holidayHomeworkItemId
	 *            假期专项id
	 * @param holidayStuHomeworkId
	 *            学生假期作业id
	 * @param holidayStuHomeworkItemId
	 *            学生假期作业专项id
	 * @param studentId
	 *            学生id
	 * @param type
	 *            假期作业类型
	 */
	void create(List<Question> questions, long holidayHomeworkId, long holidayHomeworkItemId,
			long holidayStuHomeworkId, long holidayStuHomeworkItemId, long studentId, HolidayHomeworkType type);

	/**
	 * 保存学生解题截图过程
	 * 
	 * @param holidayStuHomeworkItemId
	 *            学生假期作业专项ID
	 * @param solvingImg
	 *            解题截图过程
	 */
	void saveSolvingImg(long holidayStuHomeworkItemId, Long solvingImg);

	/**
	 * 保存假期答题过程图片
	 *
	 * @param holidayStuHomeworkItemId
	 *            学生专项题目id
	 * @param answerImg
	 *            答题过程图片
	 */
	void saveAnswerImg(long holidayStuHomeworkItemId, Long answerImg);


	/**
	 * 通过题目ID和学生假期作业专项ID获取对象
	 * 
	 * @param questionId
	 *            题目ID
	 * @param holidayStuHomeworkItemId
	 *            学生假期作业专项ID
	 * @return
	 */
	HolidayStuHomeworkItemQuestion find(Long questionId, Long holidayStuHomeworkItemId);

	/**
	 * 批量获取专项题目集合
	 * 
	 * @param questionIds
	 *            题目集合
	 * @param holidayStuHomeworkItemId
	 *            学生假期作业专项ID
	 * @return
	 */
	List<HolidayStuHomeworkItemQuestion> find(Collection<Long> questionIds, Long holidayStuHomeworkItemId);

	/**
	 * 批量获取学生假期作业习题
	 * 
	 * @param stuHKQIds
	 *            学生作业QuestionIds
	 * @return
	 */
	Map<Long, HolidayStuHomeworkItemQuestion> mget(List<Long> stuHKQIds);

	/**
	 * 获取假期学生专项里的题目
	 * 
	 * @param holidayStuHomeworkItemId
	 * @return
	 */
	List<HolidayStuHomeworkItemQuestion> queryQuestionList(Long holidayStuHomeworkItemId);
}
