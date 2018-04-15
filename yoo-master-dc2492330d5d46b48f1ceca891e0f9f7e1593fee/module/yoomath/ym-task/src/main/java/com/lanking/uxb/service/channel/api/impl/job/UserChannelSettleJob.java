package com.lanking.uxb.service.channel.api.impl.job;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.channel.api.UserChannelSettleService;

public class UserChannelSettleJob implements SimpleJob {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserChannelSettleService userChannelSettleService;

	@Override
	public void execute(ShardingContext shardingContext) {
		// 统计上一个月的数据
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		logger.info("[TASK] 统计上个月渠道学生会员销售数据");
		userChannelSettleService.staticChannelSettle(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
	}

}
