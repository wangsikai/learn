package com.lanking.uxb.service.mall.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 套餐接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月27日
 */
public interface MemberPackageService {

	/**
	 * 获取某用户类型的会员套餐列表.
	 * 
	 * @param userType
	 *            用户类型
	 * @param memberType
	 *            套餐类型
	 * @return
	 */
	List<MemberPackage> queryMemberPackage(UserType userType, MemberType memberType);

	/**
	 * 获取套餐.
	 * 
	 * @param id
	 *            套餐ID.
	 * @return
	 */
	MemberPackage get(long id);

	/**
	 * 获取某用户类型的会员套餐列表--跟组相关的<br>
	 * 
	 * 如果指定套餐组为具体的学校后，则前台用户页面显示的就是这个套餐组内的套餐（取颗粒度最小的套餐组）<br>
	 * 一个学校只能同时存在一个会员组套餐
	 * 
	 * @param userType
	 * @param memberType
	 * @param schoolId
	 * @param channelCode
	 * @return
	 */
	List<MemberPackage> queryMemberPackageByGroup(UserType userType, MemberType memberType, Long schoolId,
			Integer channelCode);

	/**
	 * 自主注册用户获取会员套餐列表
	 * 
	 * @param userType
	 * @param memberType
	 * @return
	 */
	List<MemberPackage> queryMemberPackageByAutoRegister(UserType userType, MemberType memberType);
}
