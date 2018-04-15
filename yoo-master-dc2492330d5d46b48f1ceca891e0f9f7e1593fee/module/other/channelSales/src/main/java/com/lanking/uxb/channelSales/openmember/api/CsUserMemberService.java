package com.lanking.uxb.channelSales.openmember.api;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.uxb.channelSales.openmember.form.UserMemberCreateForm;

/**
 * 用户会员相关Service接口
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public interface CsUserMemberService {
	/**
	 * 根据用户id查找会员状态
	 *
	 * @param userId
	 *            用户id
	 * @return {@link UserMember}
	 */
	UserMember findByUser(long userId);

	/**
	 * 查找用户id对应的会员状态
	 *
	 * @param userIds
	 *            用户id列表
	 * @return {@link Map}
	 */
	Map<Long, UserMember> findByUsers(Collection<Long> userIds);

	/**
	 * 创建会员
	 *
	 * @param form
	 *            {@link UserMemberCreateForm}
	 */
	void create(UserMemberCreateForm form);

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
	 * 根据班级id列表查询这些班级下面所有的会员人数
	 *
	 * @param classIds
	 *            班级id列表
	 * @return {@link Map}
	 */
	Map<Long, Integer> countMemberCountByClasses(Collection<Long> classIds);

	/**
	 * 关闭会员.
	 * 
	 * @param userIds
	 *            会员用户ID集合
	 */
	void closeMember(Long[] userIds);
}
