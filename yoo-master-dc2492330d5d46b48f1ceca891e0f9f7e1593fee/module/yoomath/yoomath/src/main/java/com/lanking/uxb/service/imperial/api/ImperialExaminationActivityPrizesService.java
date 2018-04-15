package com.lanking.uxb.service.imperial.api;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityPrizes;

/**
 * 科举活动抽奖奖品接口.
 * 
 * @author peng.zhao
 *
 * @version 2017年11月9日
 */
public interface ImperialExaminationActivityPrizesService {

	ImperialExaminationActivityPrizes get(Long id);

	/**
	 * 添加奖品 奖品固定
	 */
	void addPrize(ImperialExaminationActivityPrizes prize);
}
