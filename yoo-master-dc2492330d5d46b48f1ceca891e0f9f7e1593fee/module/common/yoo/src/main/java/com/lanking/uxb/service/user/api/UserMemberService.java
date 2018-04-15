package com.lanking.uxb.service.user.api;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.UserMember;

public interface UserMemberService {

	/**
	 * 查找用户会员信息.
	 * 
	 * @param userId
	 * 
	 * @return {@link UserMember}
	 */
	UserMember findByUserId(long userId);

	/**
	 * 查找用户会员信息（返回结果不可能为空）.
	 * 
	 * @param userId
	 *            用户ID
	 * @return {@link UserMember}
	 */
	UserMember findSafeByUserId(long userId);

	/**
	 * 查找用户会员信息（根据会员体系是否启用做相关处理,会员体系全部启用后废弃此方法）<br>
	 * session环境中使用,异步线程不可以使用
	 * 
	 * @param userId
	 *            用户ID
	 * @return {@link UserMember}
	 */
	UserMember $findByUserId(long userId);

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

	/**
	 * 根据用户id列表mget数据
	 *
	 * @param ids
	 *            用户id列表
	 * @return {@link Map}
	 */
	Map<Long, UserMember> mgetByUserIds(Collection<Long> ids);

}
