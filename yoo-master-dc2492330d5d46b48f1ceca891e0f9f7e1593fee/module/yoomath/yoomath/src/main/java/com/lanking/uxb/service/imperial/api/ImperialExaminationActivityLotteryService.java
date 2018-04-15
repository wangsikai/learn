package com.lanking.uxb.service.imperial.api;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 科举活动抽奖接口.
 * 
 * @author peng.zhao
 *
 * @version 2017年11月9日
 */
public interface ImperialExaminationActivityLotteryService {

	/**
	 * 添加奖券
	 * 
	 * @param userId
	 *            用户id
	 * @param process
	 *            阶段
	 * @param userType
	 *            用户类型
	 * @param code
	 *            活动code
	 */
	ImperialExaminationActivityLottery addLottery(long userId, ImperialExaminationProcess process, UserType userType,
			long code);

	/**
	 * 抽奖
	 * 
	 * @param lotteryId
	 */
	ImperialExaminationActivityLottery luckDraw(long code, long lotteryId, Integer room, UserType userType);

	ImperialExaminationActivityLottery get(long id);

	/**
	 * 确认奖品
	 * 
	 * @param id
	 */
	ImperialExaminationActivityLottery confirmLottery(long id);

	/**
	 * 查询奖券信息
	 * 
	 * @param
	 */
	ImperialExaminationActivityLottery getLotteryByUser(long code, long userId, ImperialExaminationProcess process,
			Status status);

	/**
	 * 学生抽奖
	 * 
	 * @param lotteryId
	 */
	ImperialExaminationActivityLottery luckDrawStudent(long code, long lotteryId, UserType userType);
}
