package com.lanking.uxb.service.mall.api;

import com.lanking.cloud.domain.yoo.member.MemberPackageCard;

/**
 * 会员卡相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che </a>
 *
 * @version 2016年11月15日
 */
public interface MemberPackageCardService {

	/**
	 * 获取会员卡.
	 * 
	 * @param code
	 *            卡号
	 * @return
	 */
	MemberPackageCard get(String code);

	/**
	 * 会员卡使用.
	 * 
	 * @param code
	 *            卡号
	 * @param userId
	 *            用户ID
	 * @param memberPackageOrderId
	 *            会员订单ID
	 */
	void used(String code, long userId, long memberPackageOrderId);
}