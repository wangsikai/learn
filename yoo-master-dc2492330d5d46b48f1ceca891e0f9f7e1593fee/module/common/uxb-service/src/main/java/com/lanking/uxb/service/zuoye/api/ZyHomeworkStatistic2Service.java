package com.lanking.uxb.service.zuoye.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;

/**
 * 作业统计接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月21日
 */
public interface ZyHomeworkStatistic2Service {

	/**
	 * 下发作业后调用
	 * 
	 * @since yoomath V1.4.2
	 * @since 小悠快批，2018-2-26，方法名称暂不改，但新流程中已不作为“下发”调用
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	void staticAfterIssue(long homeworkId);

	/**
	 * 统计一次作业用时、总体正确率
	 * 
	 * @since yoomath V1.4.2
	 * @param homeworkId
	 *            作业ID
	 */
	void staticHomeworkAfterIssue(long homeworkId);

	/**
	 * 统计一次作业每道题正确率
	 * 
	 * @since yoomath V1.4.2
	 * @param homeworkId
	 *            作业ID
	 */
	void staticHomeworkQuestionAfterIssue(long homeworkId);

	/**
	 * 统计错题
	 * 
	 * @since yoomath V1.4.2
	 * @param homeworkId
	 *            作业ID
	 * @return 老师错题记录ID
	 */
	List<Long> staticFall(long homeworkId);

	/**
	 * 记录历史答案
	 * 
	 * @param studentId
	 * @param studentHomeworkQuestion
	 */
	void recordAnswer(long studentId, StudentHomeworkQuestion studentHomeworkQuestion);
}
