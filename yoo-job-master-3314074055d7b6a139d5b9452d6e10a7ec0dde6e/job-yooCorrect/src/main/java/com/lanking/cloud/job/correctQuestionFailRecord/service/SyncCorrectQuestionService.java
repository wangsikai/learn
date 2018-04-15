package com.lanking.cloud.job.correctQuestionFailRecord.service;

/**
 * <p>Description:待批改题目同步服务<p>
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优秀快批
 */
public interface SyncCorrectQuestionService {
	/**
	 * <p>Description:将传题记录表（'correct_question_fail_record'）中的数据同步到小优快批微服务的数据库表（'correct_question'）中去<p>
	 * @date: 2018年3月19日
	 * @author: pengcheng.yu
	 */
	void sync();
}
