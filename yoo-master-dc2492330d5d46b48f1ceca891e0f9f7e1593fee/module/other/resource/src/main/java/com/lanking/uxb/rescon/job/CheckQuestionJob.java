package com.lanking.uxb.rescon.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.rescon.account.cache.ResconAccountCacheService;

/**
 * 校验题目相关定时任务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月1日
 */
public class CheckQuestionJob implements SimpleJob {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResconAccountCacheService accountCacheService;

	@Override
	public void execute(ShardingContext shardingContext) {
		logger.info("启动校验题目缓存清空任务...");

		try {
			accountCacheService.invalidAllUserCheck();
			logger.info("清空校验题目缓存完成");
		} catch (Exception e) {
			logger.info("启动校验题目缓存清空任务出错...", e);
		}
	}
}
