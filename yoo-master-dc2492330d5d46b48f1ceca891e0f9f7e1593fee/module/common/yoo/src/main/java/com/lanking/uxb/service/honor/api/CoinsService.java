package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;

/**
 * 金币相关接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface CoinsService {

	/**
	 * 操作金币
	 * 
	 * @param action
	 *            行为
	 * @param userId
	 *            用户ID
	 * @param coinsValue
	 *            金币 不需要外部计算传-1
	 * @param biz
	 *            biz
	 * @param bizId
	 *            bizId
	 * @return
	 */
	CoinsLog earn(CoinsAction action, Long userId, int coinsValue, Biz biz, long bizId);

	CoinsLog earn(CoinsAction action, Long userId);

}
