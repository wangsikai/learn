package com.lanking.uxb.service.adminSecurity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.support.common.auth.ConsoleRole;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.adminSecurity.api.ConsoleRoleService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleRoleConvert;
import com.lanking.uxb.service.adminSecurity.form.ConsoleRoleForm;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.adminSecurity.value.VConsoleRole;

/**
 * 管控台角色Controller
 *
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "con/role")
public class ConsoleRoleController {

	@Autowired
	private ConsoleRoleService consoleRoleService;
	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private ConsoleRoleConvert consoleRoleConvert;

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(@RequestParam(value = "name") String name, @RequestParam(value = "code") String code) {
		ConsoleRoleForm consoleRoleForm = new ConsoleRoleForm();
		consoleRoleForm.setName(name);
		consoleRoleForm.setCode(code);
		consoleRoleService.save(consoleRoleForm);

		return new Value();
	}

	/**
	 * 查询角色
	 *
	 * @param name
	 *            查询名称
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(String name, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "50") int size) {

		Pageable pageable = P.index(page, size);
		Page<ConsoleRole> roles = consoleRoleService.query(name, pageable);

		VPage<VConsoleRole> vpage = new VPage<VConsoleRole>();
		List<VConsoleRole> vs = consoleRoleConvert.to(roles.getItems());
		vpage.setItems(vs);
		vpage.setCurrentPage(page);
		vpage.setPageSize(size);
		vpage.setTotal(roles.getTotalCount());
		vpage.setTotalPage(roles.getPageCount());
		return new Value(vpage);
	}

	/**
	 * 授权某个用户一个角色
	 *
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            角色id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "addUserRole", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addUserRole(long userId, long roleId) {

		consoleRoleService.updateUserRole(userId, roleId, false);

		return new Value();
	}

	/**
	 * 移除某个用户角色
	 *
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            角色id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "removeUserRole", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removeUserRole(long userId, long roleId) {

		consoleRoleService.updateUserRole(userId, roleId, true);
		return new Value();
	}

	/**
	 * 授权某个用户组一个角色
	 *
	 * @param groupId
	 *            用户组id
	 * @param roleId
	 *            角色id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "addGroupRole", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addGroupRole(long groupId, long roleId) {
		consoleRoleService.updateGroupRole(groupId, roleId, false);
		return new Value();
	}

	/**
	 * 删除用户组权限
	 *
	 * @param groupId
	 *            用户组id
	 * @param roleId
	 *            角色id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "removeGroupRole", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removeGroupRole(long groupId, long roleId) {
		consoleRoleService.updateGroupRole(groupId, roleId, true);
		return new Value();
	}

	/**
	 * 获得一个角色所对应的系统下的权限
	 *
	 * @param roleId
	 *            角色id
	 * @param systemId
	 *            系统id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "getRoleSystemMenus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getRoleSystemMenus(long roleId, long systemId) {
		return new Value(consoleRoleService.getRoleSystemMenus(roleId, systemId));
	}

	/**
	 * 添加角色菜单权限
	 *
	 * @param roleId
	 *            角色id
	 * @param menuIds
	 *            需要添加的菜单列表
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "addRoleMenus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addRoleMenus(long roleId, @RequestParam(value = "menuIds") List<Long> menuIds) {
		if (CollectionUtils.isEmpty(menuIds)) {
			return new Value(new IllegalArgException("menuIds"));
		}

		consoleRoleService.addRoleMenus(roleId, menuIds);
		return new Value();
	}

	/**
	 * 移除角色菜单权限
	 *
	 * @param roleId
	 *            角色id
	 * @param menuIds
	 *            需要移除的菜单列表
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "removeRoleMenus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removeRoleMenus(long roleId, @RequestParam(value = "menuIds") List<Long> menuIds) {
		if (CollectionUtils.isEmpty(menuIds)) {
			return new Value(new IllegalArgException("menuIds"));
		}

		consoleRoleService.removeRoleMenus(roleId, menuIds);

		return new Value();
	}

	/**
	 * 获得角色授权的用户及用户组所有数据
	 *
	 * @param roleId
	 *            角色id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "getRoleGroupUser", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getRoleGroupUser(long roleId) {

		List<Long> ids = consoleRoleService.getRoleGroupUser(roleId);
		return new Value(ids);
	}

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(long roleId) {

		consoleRoleService.delete(roleId);

		return new Value();
	}
}
