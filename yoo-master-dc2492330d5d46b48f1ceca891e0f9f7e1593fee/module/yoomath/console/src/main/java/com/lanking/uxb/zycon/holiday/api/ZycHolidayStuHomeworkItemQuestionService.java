package com.lanking.uxb.zycon.holiday.api;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 假期作业学生题目
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public interface ZycHolidayStuHomeworkItemQuestionService {

	/**
	 * 查找待批改的
	 *
	 * @param size
	 *            待批改题目查询条数
	 * @return {@link HolidayStuHomeworkItemQuestion}
	 */
	List<HolidayStuHomeworkItemQuestion> findCorrectQuestions(int size);

	/**
	 * 查询未批改的假期作业题目数
	 *
	 * @return 未批改的题目数量
	 */
	long countNotCorrectQuestions();

	/**
	 * 批改学生假期作业
	 *
	 * @deprecated 自3.9.0后，此接口正式废除
	 * @param stuItemQuestionId
	 *            假期作业学生专项题目id
	 * @param result
	 *            批改结果
	 * @param correctUserId
	 *            批改人id
	 */
	@Deprecated
	void correct(long stuItemQuestionId, HomeworkAnswerResult result, long correctUserId);

	/**
	 * 根据id获得数据
	 *
	 * @param id
	 *            id
	 * @return {@link HolidayStuHomeworkItemQuestion}
	 */
	HolidayStuHomeworkItemQuestion get(long id);

	/**
	 * 确认批改假期作业
	 *
	 * @param ids
	 *            id列表
	 */
	void confirm(List<Long> ids);

	/**
	 * 查询待确认的假期作业
	 *
	 * @param pageable
	 *            {@link Pageable}
	 * @return {@link Page}
	 */
	Page<HolidayStuHomeworkItemQuestion> findConfirmQuestions(Pageable pageable);

	/**
	 * 批改假期作业，支持单空批改
	 *
	 * @since 3.9.0
	 * @param stuItemQuestionId
	 *            假期item id
	 * @param results
	 *            结果 HolidayStuHomeworkItemAnswer.id -> result
	 * @param correctUserId
	 *            批改人id
	 */
	void correct(long stuItemQuestionId, Map<Long, HomeworkAnswerResult> results, long correctUserId);
	
	/**
	 * 通过题目编号查询待确认的假期作业
	 *
	 * @param pageable
	 * @param questionCode
	 * @return
	 */
	Page<HolidayStuHomeworkItemQuestion> findConfirmQuestionsByCode(Pageable pageable, String questionCode);
}
