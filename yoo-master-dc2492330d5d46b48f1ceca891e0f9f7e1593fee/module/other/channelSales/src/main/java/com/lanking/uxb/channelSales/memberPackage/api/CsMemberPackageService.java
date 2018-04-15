package com.lanking.uxb.channelSales.memberPackage.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroup;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageGroupForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageQueryForm;
import com.lanking.uxb.channelSales.memberPackage.form.ParameterForm;

/**
 * 会员套餐
 * 
 * @author zemin.song
 * @version 2016年9月26日
 */
public interface CsMemberPackageService {
	/**
	 * 查询套餐
	 * 
	 * @param
	 * @return
	 */
	Page<MemberPackage> query(MemberPackageQueryForm query, Pageable p);

	/**
	 * 查询套餐组
	 * 
	 * @param
	 * @return
	 */
	List<MemberPackageGroup> queryGroup(MemberPackageQueryForm query);

	/**
	 * 查询套餐组byId
	 * 
	 * @param Long
	 *            id
	 * @return
	 */
	MemberPackageGroup getGroupById(Long id);

	/**
	 * 查询所有的会员套餐类型
	 * 
	 * @param mtype
	 *            会员类型
	 * @param utype
	 *            用户类型
	 * @return
	 */
	List<MemberPackage> findAll(MemberType mtype, UserType utype);

	/**
	 * 查询所有会员套餐（用户类、会员类型、学校）
	 *
	 * @param memberType
	 *            会员类型
	 * @param userType
	 *            用户类型
	 * @param schoolId
	 *            学校id
	 * @return 套餐列表
	 */
	List<MemberPackage> findAll(MemberType memberType, UserType userType, Long schoolId);

	/**
	 * 查询所有的会员套餐类型
	 *
	 * @return {@link List}
	 */
	List<MemberPackage> findAll();

	/**
	 * 根据id获得套餐数据
	 *
	 * @param id
	 *            套餐id
	 * @return {@link MemberPackage}
	 */
	MemberPackage get(long id);

	/**
	 * 添加,编辑套餐
	 * 
	 * @param
	 * @return
	 */
	void save(MemberPackageForm form);

	/**
	 * 添加,套餐组
	 * 
	 * @param
	 * @return
	 */
	void createGroup(MemberPackageGroupForm form);

	/**
	 * 删除套餐
	 * 
	 * @param id
	 *            套餐ID
	 * @return
	 */
	int delete(Long id);

	/**
	 * 删除套餐组
	 * 
	 * @param id
	 *            套餐ID
	 * @return
	 */
	void deleteGroup(Long id);

	/**
	 * 修改排序
	 * 
	 * @param
	 * @return
	 */
	void sort(List<Long> ids);

	/**
	 * 更新渠道组关联
	 * 
	 * @param form
	 */
	void updateGroup(MemberPackageGroupForm form);

	/**
	 * 修改阀值
	 * 
	 * @param form
	 * @return
	 */
	int updateParam(ParameterForm form);

	/**
	 * 查找会员套餐列表
	 *
	 * @param userType
	 *            用户类型
	 * @param memberType
	 *            会员类型
	 * @param schoolId
	 *            学校id
	 * @param channelCode
	 *            渠道商code
	 * @param groupType
	 *            用户组类型
	 * @return 套餐列表
	 */
	List<MemberPackage> findPackage(UserType userType, MemberType memberType, Long schoolId,
	        Integer channelCode, MemberPackageGroupType groupType);

}
