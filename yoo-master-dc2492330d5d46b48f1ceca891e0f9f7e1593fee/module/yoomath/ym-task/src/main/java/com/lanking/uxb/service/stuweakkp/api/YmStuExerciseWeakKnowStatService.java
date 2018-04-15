package com.lanking.uxb.service.stuweakkp.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 学生练习薄弱知识点处理
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
public interface YmStuExerciseWeakKnowStatService {

	/**
	 * 保存薄弱指点统计
	 *
	 * @param weakList
	 */
	@SuppressWarnings("rawtypes")
	void saveWeakStat(List<Map> weakList);

	/**
	 * 游标查询学生知识点信息
	 * 
	 * @param pageable
	 * @param minRate
	 *            正确率小于minRate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryStuWeak(CursorPageable<Long> pageable, Double minRate, int version);

	/**
	 * 清空
	 * 
	 */
	void clearWeakStat();
}
