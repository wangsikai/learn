package com.lanking.uxb.rescon.account.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface ResconVendorUserManage {

	/**
	 * 获取供应商用户
	 * 
	 * @param id
	 *            供应商ID
	 */
	VendorUser getVendorUser(long id);

	/**
	 * 批量获取供应商用户.
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, VendorUser> mgetVendorUser(Collection<Long> ids);

	/**
	 * 创建供应商用户
	 * 
	 * @param user
	 *            用户信息(外部new)
	 * @return 创建好的供应商用户
	 */
	VendorUser create(VendorUser user);

	long getVendorUserCount(long vendorId);

	/**
	 * 
	 * @param vendorId
	 * @return
	 */
	long getVendorUserCountByUserType(long vendorId, UserType usertype);

	/**
	 * 通过用户类型,获取供应商用户信息
	 * 
	 * @param vendorId
	 * @param usertype
	 * @return
	 */
	List<VendorUser> getVendorUserListByUserType(long vendorId, UserType usertype);

	/**
	 * 
	 * @param vendorId
	 * @param status
	 * @return
	 */
	long getVendorUserCountByStatus(long vendorId, VendorUserStatus status);

	/**
	 * 获取供应商管理员
	 * 
	 * @param getType
	 *            匹配类型
	 * @param value
	 *            匹配值
	 * @return 供应商管理员
	 */
	VendorUser findOne(GetType getType, String value);

	/**
	 * 获取供应商用户
	 * 
	 * @param vendorId
	 *            供应商id
	 * @param status
	 *            用户状态
	 * @param userTypes
	 *            用户类型
	 * @return
	 */
	Page<VendorUser> getVendorUserList(Pageable p, long vendorId, Collection<VendorUserStatus> statuss,
			UserType userType);

	/**
	 * 查询一个供应商的所有账号
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @return 账号列表
	 */
	List<VendorUser> listVendorUsers(long vendorId);

	/**
	 * 查询一个供应商指定类型的所有账号
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @return 账号列表
	 */
	List<VendorUser> listVendorUsers(long vendorId, Collection<UserType> userTypes);
}
