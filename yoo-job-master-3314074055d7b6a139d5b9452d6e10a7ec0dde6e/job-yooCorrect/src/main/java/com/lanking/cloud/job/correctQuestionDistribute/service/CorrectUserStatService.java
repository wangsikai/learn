package com.lanking.cloud.job.correctQuestionDistribute.service;

import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;

/**
 * <p>
 * Description:批改用户统计服务
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优秀快批
 */
public interface CorrectUserStatService {

	/**
	 * <p>
	 * Description:CorrectUserStat中的allotQuestionCount(首次派题数（不重复习题个数）.)加1
	 * <p>
	 * 
	 * @date: 2018年3月19日
	 * @author: pengcheng.yu
	 * @param correctUserId
	 */
	void increaseAllotQuestionCount(Long correctUserId,CorrectQuestion question);
}
