package com.lanking.uxb.service.adminSecurity.api;

import com.lanking.cloud.domain.support.common.auth.ConsoleRole;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.adminSecurity.form.ConsoleRoleForm;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ConsoleRoleService {
	ConsoleRole save(ConsoleRoleForm form);

	ConsoleRole update(ConsoleRoleForm form);

	void delete(Long id);

	//void updateRoleMenu(List<Long> menuIds, Long roleId);

	/**
	 * 分页查询数据
	 *
	 * @param name
	 *            名称
	 * @param pageable
	 *            分页参数
	 * @return 查询数据
	 */
	Page<ConsoleRole> query(String name, Pageable pageable);

	/**
	 * 操作用户角色
	 *
	 * @param userId
	 *            学生id
	 * @param roleId
	 *            角色id
	 * @param isRemove
	 *            是否移除 true -> remove false -> add
	 */
	void updateUserRole(long userId, long roleId, boolean isRemove);

	/**
	 * 更新用户组角色
	 *
	 * @param groupId
	 *            用户组id
	 * @param roleId
	 *            角色id
	 * @param isRemove
	 *            是否移除 true -> remove false -> add
	 */
	void updateGroupRole(long groupId, long roleId, boolean isRemove);

	/**
	 * 获得角色授权的所有用户及用户组
	 *
	 * @param roleId
	 *            角色id
	 * @return {@link List}
	 */
	List<Long> getRoleGroupUser(long roleId);

	/**
	 * 获得一个角色所对应的系统下的菜单列表
	 *
	 * @param roleId
	 *            角色id
	 * @param systemId
	 *            系统id
	 * @return {@link List}
	 */
	List<Long> getRoleSystemMenus(long roleId, long systemId);

	/**
	 * 添加角色的菜单权限
	 *
	 * @param roleId
	 *            角色id
	 * @param menuIds
	 *            要添加的菜单列表
	 */
	void addRoleMenus(long roleId, List<Long> menuIds);

	/**
	 * 移除角色的菜单权限
	 *
	 * @param roleId
	 *            角色id
	 * @param menuIds
	 *            菜单id列表
	 * @return {@link List}
	 */
	void removeRoleMenus(long roleId, List<Long> menuIds);

	/**
	 * 获得用户所具有的所有角色
	 *
	 * @param userId
	 *            用户id
	 * @return {@link List}
	 */
	List<ConsoleRole> getUserRoles(long userId);
}
