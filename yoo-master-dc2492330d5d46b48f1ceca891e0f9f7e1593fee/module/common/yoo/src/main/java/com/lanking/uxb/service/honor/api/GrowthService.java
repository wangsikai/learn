package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;

/**
 * 成长值相关接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface GrowthService {

	/**
	 * 操作成长值
	 * 
	 * @since yoomath V1.8
	 * @param action
	 *            操作
	 * @param userId
	 *            用户ID
	 * @param growthValue
	 *            值
	 * @param biz
	 *            业务对象类型
	 * @param bizId
	 *            业务对象ID
	 * @param isUpgrade
	 *            是否要获知是否升级
	 * @return {@link GrowthLog}
	 */
	GrowthLog grow(GrowthAction action, long userId, int growthValue, Biz biz, long bizId, boolean isUpgrade);

	/**
	 * 操作成长值
	 * 
	 * @since yoomath V1.8
	 * @param GrowthAction
	 *            动作
	 * @param userId
	 *            用户ID
	 * @param isUpgrade
	 *            是否要获知是否升级
	 * @return {@link GrowthLog}
	 */
	GrowthLog grow(GrowthAction action, long userId, boolean isUpgrade);

}
