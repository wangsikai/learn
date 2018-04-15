package com.lanking.uxb.service.holiday.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItemQuestion;

/**
 * 假日作业项-题目接口
 *
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
public interface HolidayHomeworkItemQuestionService {

	/**
	 * 查询作业项题目ID集合
	 *
	 * @param holidayHomeworkItemId
	 *            假日作业项ID
	 * @return
	 */
	List<Long> queryQuestions(long holidayHomeworkItemId);

	/**
	 * 批量保存专项练习下的题目
	 *
	 * @param questionIds
	 *            题目id列表
	 * @param homeworkItemId
	 *            专项练习id
	 */
	void create(List<Long> questionIds, long homeworkItemId);

	/**
	 * 查询homeworkItem ID 获取 该作业专项所有的题目列表
	 * 
	 * @param holidayHomeworkItemId
	 *            作业专项ID
	 * @return
	 */
	List<HolidayHomeworkItemQuestion> getHomeworkQuestion(Long holidayHomeworkItemId);

	/**
	 * 根据题目ID，itemID获取 作业专项习题
	 * 
	 * @param questionId
	 * @param holidayHomeworkItemId
	 * @return 作业专项习题
	 */
	HolidayHomeworkItemQuestion find(Long questionId, Long holidayHomeworkItemId);

	/**
	 * 根据题目IDs，itemID获取 作业专项习题
	 * 
	 * @param qIds
	 *            题目ID集合
	 * @param holidayHomeworkItemId
	 * @return 作业专项习题
	 */
	List<HolidayHomeworkItemQuestion> find(Collection<Long> qIds, Long holidayHomeworkItemId);

	/**
	 * 一次作业某题做错的学生列表
	 * 
	 * @param homeworkItemId
	 * @param questionId
	 * @return
	 */
	List<Map> listWrongStu(long homeworkItemId, long questionId);

	/**
	 * 更新假期作业专项题目数据
	 *
	 * @param questionId
	 *            题目id
	 * @param itemId
	 *            专项id
	 * @param incrRightCount
	 *            增加的正确数量
	 * @param incrWrongCount
	 *            增加的错误数量
	 */
	void updateStatistics(long questionId, long itemId, int incrRightCount, int incrWrongCount);

	/**
	 * 获取该专项班级正确率
	 * 
	 * @param holidayHomeworkItemId
	 *            假期班级专项ID
	 * @return
	 */
	List<Double> getRateStat(long holidayHomeworkItemId);

	/**
	 * 批量获得item -> questionIds
	 *
	 * @param itemIds
	 *            假期作业专项id列表
	 * @return {@link Map}
	 */
	Map<Long, List<Question>> mgetByItemIds(Collection<Long> itemIds);
}
