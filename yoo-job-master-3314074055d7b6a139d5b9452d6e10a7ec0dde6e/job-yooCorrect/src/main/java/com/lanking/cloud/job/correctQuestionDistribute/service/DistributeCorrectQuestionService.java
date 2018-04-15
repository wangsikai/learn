package com.lanking.cloud.job.correctQuestionDistribute.service;

/**
 * <p>
 * Title:题目派送接口
 * </p>
 * <p>
 * Description:题目派送相关接口
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月7日
 * @since 小优秀快批
 */
public interface DistributeCorrectQuestionService {
	

	/**
	 * <p>
	 * Description:派送批改题目给批改人员
	 * <p>
	 * 
	 * @date: 2018年3月8日
	 * @author: pengcheng.yu
	 * @param questionId
	 * @param userId
	 * @param type
	 */
	void distributeCorrectQuestions();
	
	void distributeCorrectQuestions2();
	/**
	 * 检查已分配给批改者，但是批改者超时未处理的批改题相关信息
	 * <p>Description:<p>
	 * @date: 2018年3月10日
	 * @author: pengcheng.yu
	 */
	void checkTimeoutCorrectQuestionInfos();
	
	/**
	 * 清除待批改习题池中异常数据
	 */
	void clearAbnormalCorrectQuestions();
}
