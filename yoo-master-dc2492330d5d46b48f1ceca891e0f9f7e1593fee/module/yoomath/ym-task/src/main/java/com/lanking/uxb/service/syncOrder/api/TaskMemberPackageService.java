package com.lanking.uxb.service.syncOrder.api;

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
public interface TaskMemberPackageService {

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
}
