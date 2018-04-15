package com.lanking.cloud.job.nationalDayActivity.service;

/**
 * 统计老师排行榜接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月23日
 */
public interface TaskNationalDayActivity01TeaAwardService {

	boolean needAward();

	/**
	 * 统计老师排行榜
	 * 
	 * @param count
	 *            统计前x名
	 */
	void statTeaAward(int count);
}
