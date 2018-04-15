package com.lanking.uxb.service.examPaper.api;

import java.util.Collection;
import java.util.Map;

/**
 * 试卷点击统计管理
 *
 * @author zemin.song
 * @since 2.3.1
 */
public interface ExamPaperCountService {

	int updateCount(long examPaperId, int dayOfN, int nDay);

	/**
	 * 增加试卷点击次数
	 * 
	 * @param examPaperId
	 *            试卷id
	 * @param dayOfN
	 *            周期中某天
	 * @param nDay
	 *            周期
	 */
	void addOneClick(long examPaperId, int dayOfN, int nDay);

	/**
	 * 获取点击次数
	 * 
	 * @param examPaperId
	 *            试卷id
	 * @param dayOfN
	 *            周期中某天
	 * @param nDay
	 *            周期
	 */
	Map<Long, Long> get(long examPaperId, Integer dayOfN, int nDay);

	/**
	 * 获取批量点击次数
	 * 
	 * @param examPaperId
	 *            试卷id
	 * @param nDay
	 *            周期
	 */
	Map<Long, Long> mget(Collection<Long> examPaperIds, int nDay);

}
