package com.lanking.uxb.service.session.api.impl.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.session.cache.SessionCacheService;

/**
 * session数据同步监听
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月18日
 */
public class SessionSyncJob implements SimpleJob {

	private Logger logger = LoggerFactory.getLogger(SessionSyncJob.class);

	@Autowired
	private SessionCacheService sessionCacheService;

	@Override
	public void execute(ShardingContext shardingContext) {
		try {
			sessionCacheService.loadConfig();
			logger.info("start sync session info....");
			sessionCacheService.sync();
			logger.info("sync ok....");
			sessionCacheService.flushWebSession();
			logger.info("flush web session ok....");
			sessionCacheService.flushMobileSession();
			logger.info("flush mobile session ok....");
			logger.info("complete sync session info....");
		} catch (Exception e) {
			logger.error("sync session info error:", e);
		}
	}
}
