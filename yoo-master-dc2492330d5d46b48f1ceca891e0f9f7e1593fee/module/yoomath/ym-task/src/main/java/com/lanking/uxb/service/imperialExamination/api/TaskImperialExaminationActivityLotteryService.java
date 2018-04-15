package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;

public interface TaskImperialExaminationActivityLotteryService {

	/**
	 * 添加奖券
	 * 
	 * @param userId
	 *            用户id
	 * @param process
	 *            阶段
	 * @param code
	 *            活动code
	 */
	ImperialExaminationActivityLottery addLottery(long userId, ImperialExaminationProcess process, long code);

	/**
	 * 添加奖券
	 * 
	 * @param userId
	 *            用户id
	 * @param process
	 *            阶段
	 * @param code
	 *            活动code
	 */
	void addLotterys(List<ImperialExaminationActivityLottery> lotterys);
}
