package com.lanking.uxb.service.lottery.api.impl.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.lottery.api.TaskLotterySeasonService;

public class CoinsLotterySeasonJob implements SimpleJob {
	@Autowired
	private TaskLotterySeasonService lotterySeasonService;

	@Override
	public void execute(ShardingContext shardingContext) {
		List<CoinsLotterySeason> list = lotterySeasonService.findNewList();
		for (CoinsLotterySeason season : list) {
			// 表明上一期已经结束，需要生成下一期，并将些期改为结束状态
			if (season != null && season.getEveryWeek() && season.getEndTime().getTime() <= System.currentTimeMillis()
					&& season.getStatus() == Status.ENABLED) {
				lotterySeasonService.save(season);
			}
		}

	}

}
