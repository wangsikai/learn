package com.lanking.uxb.service.adminSecurity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.support.common.auth.ConsoleGroup;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.adminSecurity.api.ConsoleGroupService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleGroupConvert;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleUserConvert;
import com.lanking.uxb.service.adminSecurity.form.ConsoleGroupForm;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.adminSecurity.value.VConsoleGroup;
import com.lanking.uxb.service.adminSecurity.value.VConsoleUser;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "con/grp")
public class ConsoleGroupController {

	@Autowired
	private ConsoleGroupService consoleGroupService;
	@Autowired
	private ConsoleGroupConvert consoleGroupConvert;
	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private ConsoleUserConvert consoleUserConvert;

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(@RequestParam(value = "name") String name, @RequestParam(value = "systemId") Long systemId) {
		ConsoleGroupForm consoleGroupForm = new ConsoleGroupForm();
		consoleGroupForm.setName(name);
		consoleGroupForm.setSystemId(systemId);

		consoleGroupService.save(consoleGroupForm);

		return new Value();
	}

	/**
	 * 更新用户组相关数据
	 *
	 * @param userIds
	 *            用户列表
	 * @param groupId
	 *            用户组id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "updateUserGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateUserGroup(@RequestParam(value = "userIds") List<Long> userIds,
			@RequestParam(value = "groupId") long groupId) {
		// 全量进行传递，若删除，会自行判断
		consoleGroupService.updateGroupUsers(groupId, userIds);
		return new Value();
	}

	/**
	 * 查询用户组相关数据
	 *
	 * @param systemId
	 *            系统id
	 * @param name
	 *            名称
	 * @param size
	 *            分页大小
	 * @param page
	 *            当前页
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(Long systemId, String name, @RequestParam(value = "size", defaultValue = "50") int size,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		Pageable pageable = P.index(page, size);
		Page<ConsoleGroup> groups = consoleGroupService.query(systemId, name, pageable);
		VPage<VConsoleGroup> vpage = new VPage<VConsoleGroup>();
		List<VConsoleGroup> vs = consoleGroupConvert.to(groups.getItems());
		vpage.setCurrentPage(page);
		vpage.setItems(vs);
		vpage.setPageSize(size);
		vpage.setTotal(groups.getTotalCount());
		vpage.setTotalPage(groups.getPageCount());

		return new Value(vpage);
	}

	/**
	 * 查找某个用户组下的用户数据
	 *
	 * @param id
	 *            用户组id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "findUser", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findUsers(long id) {
		List<ConsoleUser> consoleUsers = consoleUserService.getUsersByGroup(id);

		List<VConsoleUser> vs = consoleUserConvert.to(consoleUsers);

		return new Value(vs);
	}

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "findUsersBySystem", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findUsersBySystem(long systemId, long groupId) {

		Map<String, Object> retMap = new HashMap<String, Object>(2);

		List<ConsoleUser> consoleUsers = consoleUserService.findBySystem(systemId);

		retMap.put("systemUsers", consoleUserConvert.to(consoleUsers));
		consoleUsers = consoleUserService.getUsersByGroup(groupId);

		retMap.put("groupUsers", consoleUserConvert.to(consoleUsers));

		return new Value(retMap);
	}
}
