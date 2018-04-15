package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;

/**
 * 用户荣誉相关接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface UserHonorService {

	/**
	 * 初始化用户荣誉记录
	 * 
	 * @since yoomath V1.8
	 * @param userId
	 *            用户ID
	 */
	void init(long userId);

	/**
	 * 获取用户荣誉信息
	 * 
	 * @param userId
	 * @return
	 */
	UserHonor getUserHonor(long userId);

	/**
	 * 保存
	 * 
	 * @param userHonor
	 *            userHonor
	 * @return UserHonor
	 */
	UserHonor save(UserHonor userHonor);
	
	/**
	 * 保存或更新用户的荣誉信息
	 * 
	 * @param userId
	 *            用户id
	 * @return UserHonor
	 */
	void saveOrUpdate(Long userId);

	/**
	 * 更新用户荣誉
	 * 
	 * @param userId
	 *            用户ID
	 * @param isupGrade
	 *            升级是否提示
	 */
	void uptUserHonor(long userId, boolean isupGrade);

	/**
	 * 异步方法 调用growth coins 同步请求
	 * 
	 * @param growthAction
	 *            action
	 * @param coinsAction
	 *            action
	 * @param userId
	 *            用户ID
	 * @param biz
	 *            biz
	 * @param bizId
	 *            bizID
	 */
	void asyncUptUserHonor(GrowthAction growthAction, CoinsAction coinsAction, Long userId, Biz biz, long bizId);
}
