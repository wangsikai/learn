package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.zuoye.form.DailyPractiseSaveForm;

import java.util.List;
import java.util.Map;

/**
 * 每日一练service
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public interface ZyDailyPractiseService {

	/**
	 * 查询用户的每日一练列表
	 *
	 * @param userId
	 *            用户id
	 * @param textbookCode
	 *            当前教材代码
	 * @param cursorPageable
	 *            分页条件
	 * @return {@link CursorPage}
	 */
	CursorPage<Long, DailyPractise> query(long userId, int textbookCode, CursorPageable<Long> cursorPageable);

	/**
	 * 分页查询数据
	 *
	 * @param userId
	 *            用户id
	 * @param textbookCode
	 *            当前教材代码
	 * @param pageable
	 *            {@link Pageable}
	 * @return {@link Page}
	 */
	Page<DailyPractise> query(long userId, int textbookCode, Pageable pageable);

	/**
	 * 获得最新的每日一练
	 *
	 * @param userId
	 *            用户id
	 * @param textbookCode
	 *            用户教材code
	 * @return {@link DailyPractise}
	 */
	DailyPractise getLatest(long userId, int textbookCode);

	/**
	 * 保存每日一练
	 *
	 * @param form
	 *            {@link DailyPractiseSaveForm}
	 * @return {@link DailyPractise}
	 */
	DailyPractise save(DailyPractiseSaveForm form);

	/**
	 * 此教材下已经练习多少次
	 *
	 * @param userId
	 *            用户id
	 * @param textbookCode
	 *            教材code
	 * @param isFindTotal
	 *            是否查找所有的练习
	 * @return 练习次数
	 */
	long getTotalPractiseDays(long userId, int textbookCode, boolean isFindTotal);

	/**
	 * 根据id获得每日练习
	 *
	 * @param id
	 *            每日练习id
	 * @return {@link DailyPractise}
	 */
	DailyPractise get(long id);

	/**
	 * 复制一份每日练习
	 *
	 * @param from
	 *            原每日练习记录
	 * @return {@link DailyPractise}
	 */
	DailyPractise copy(DailyPractise from);

	/**
	 * 更新每日练习
	 *
	 * @param dailyPractise
	 *            每日练习数据
	 * @return {@link DailyPractise}
	 */
	DailyPractise update(DailyPractise dailyPractise);

	/**
	 * 保存每日练草稿
	 *
	 * @param answerList
	 *            答案列表
	 * @param questionIds
	 *            题目id列表
	 * @param id
	 *            每日练id
	 * @param homeworkTime
	 *            花费时间
	 * @return {@link DailyPractise}
	 */
	DailyPractise draft(List<Map<Long, List<String>>> answerList, List<Long> questionIds, long id, int homeworkTime);

	/**
	 * copy并更新历史记录
	 *
	 * @param d
	 *            需要拷贝的每日练纪录
	 */
	void copyAndUpdateHistory(DailyPractise d);

	/**
	 * 做一题
	 *
	 * @param answers
	 *            答案
	 * @param dailyQuestionId
	 *            每日练题目id
	 * @param id
	 *            每日练id
	 * @param homeworkTime
	 *            作业时间
	 * @return {@link DailyPractise}
	 */
	DailyPractise doOne(Map<Long, List<String>> answers, long dailyQuestionId, long id, int homeworkTime);
}
