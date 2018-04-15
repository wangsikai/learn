package com.lanking.uxb.service.syncOrder.api;

import java.util.Date;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.UserMember;

public interface TaskUserMemberService {

	/**
	 * 查找用户会员信息.
	 * 
	 * @param userId
	 * 
	 * @return {@link UserMember}
	 */
	UserMember findByUserId(long userId);

	/**
	 * 用户会员信息创建或续期处理.
	 * 
	 * @param userId
	 *            用户ID
	 * @param startDate
	 *            开始日期
	 * @param memberPackage
	 *            套餐
	 * @param orderID
	 *            购买会员对应的本地订单ID
	 * @param card
	 *            会员卡
	 */
	void createOrRenew(long userId, Date startDate, MemberPackage memberPackage, Long orderID, MemberPackageCard card);

}
