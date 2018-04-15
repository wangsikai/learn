package com.lanking.uxb.service.adminSecurity.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.support.common.auth.ConsoleMenuEntity;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.adminSecurity.api.ConsoleMenuService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleMenuEntityConvert;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleUserConvert;
import com.lanking.uxb.service.adminSecurity.form.ConsoleMenuForm;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.adminSecurity.value.VConsoleMenuEntity;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "con/menu")
public class ConsoleMenuController {

	@Autowired
	private ConsoleMenuService consoleMenuService;
	@Autowired
	private ConsoleMenuEntityConvert consoleMenuEntityConvert;
	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private ConsoleUserConvert consoleUserConvert;

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(@RequestParam(value = "form") String form) {
		if (StringUtils.isBlank(form)) {
			return new Value(new IllegalArgException());
		}
		ConsoleMenuForm consoleMenuForm = JSONObject.parseObject(form, ConsoleMenuForm.class);
		ConsoleMenuEntity menu = consoleMenuService.save(consoleMenuForm);
		return new Value(consoleMenuEntityConvert.to(menu));
	}

	/**
	 * 根据权限查询用户下的菜单
	 *
	 * @param systemId
	 *            系统id
	 * @return Value
	 */
	@RequestMapping(value = "get_user_menu", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getUserMenu(@RequestParam(value = "systemId") Long systemId,
			@RequestParam(value = "assembleTree", defaultValue = "false") Boolean assembleTree) {
		ConsoleUser consoleUser = consoleUserService.get(Security.getUserId());
		if (consoleUser.getSystemId().equals(0L)) {
			List<ConsoleMenuEntity> entities = consoleMenuService.getBySystem(systemId, Status.ENABLED);
			List<VConsoleMenuEntity> vConsoleMenuEntities = consoleMenuEntityConvert.to(entities);
			if (assembleTree) {
				vConsoleMenuEntities = consoleMenuEntityConvert.assembleMenuTree(vConsoleMenuEntities);
			}
			Map<String, Object> rtnMap = Maps.newHashMap();
			rtnMap.put("menus", vConsoleMenuEntities);
			rtnMap.put("user", consoleUserConvert.to(consoleUser));
			return new Value(rtnMap);
		}

		if (!consoleUser.getSystemId().equals(systemId)) {
			return new Value(new NoPermissionException());
		}

		List<ConsoleMenuEntity> consoleMenuEntityList = consoleMenuService.findByUser(systemId, consoleUser.getId());
		List<VConsoleMenuEntity> vConsoleMenuEntities = consoleMenuEntityConvert.to(consoleMenuEntityList);
		if (assembleTree) {
			vConsoleMenuEntities = consoleMenuEntityConvert.assembleMenuTree(vConsoleMenuEntities);
		}
		Map<String, Object> rtnMap = Maps.newHashMap();
		rtnMap.put("menus", vConsoleMenuEntities);
		rtnMap.put("user", consoleUserConvert.to(consoleUser));
		return new Value(rtnMap);
	}

	/**
	 * 超级管理员才可以调此接口
	 *
	 * @param systemId
	 *            系统id
	 * @return Value
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "get_by_system", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getBySystem(@RequestParam(value = "systemId") Long systemId,
			@RequestParam(value = "assembleTree", defaultValue = "true") Boolean assembleTree) {
		List<ConsoleMenuEntity> consoleMenuEntities = consoleMenuService.getBySystem(systemId, null);
		List<VConsoleMenuEntity> vConsoleMenuEntities = consoleMenuEntityConvert.to(consoleMenuEntities);
		if (assembleTree) {
			vConsoleMenuEntities = consoleMenuEntityConvert.assembleMenuTree(vConsoleMenuEntities);
		}
		return new Value(vConsoleMenuEntities);
	}

	/**
	 * 更新菜单状态
	 *
	 * @param ids
	 *            菜单id
	 * @param status
	 *            {@link Status}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "updateStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateStatus(@RequestParam(value = "ids") List<Long> ids, Status status) {
		consoleMenuService.updateStatus(ids, status);

		return new Value();
	}
}
