package com.lanking.cloud.job.nationalDayActivity.service;

/**
 * 统计老师综合得分接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月23日
 */
public interface TaskNationalDayActivity01TeaScoreService {

	/**
	 * 删除tea表和homework表无对应数据的数据
	 */
	void deleteTeaData();

	/**
	 * 统计教师综合得分&作业提交率
	 */
	void statTeaScore();
}
