package com.lanking.uxb.service.channel.api;

/**
 * 渠道套餐结算任务接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年2月28日
 */
public interface UserChannelSettleService {

	/**
	 * 结算统计任务.
	 */
	void staticChannelSettle(int year, int month);
}