package com.lanking.uxb.service.adminSecurity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.support.common.auth.ConsoleSystemMenuUri;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemMenuUriService;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleSystemMenuUriConvert;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;

/**
 * 支撑系统对应菜单地址代码
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping(value = "con/uri")
public class ConsoleSystemMenuUriController {

	@Autowired
	private ConsoleSystemMenuUriService menuUriService;
	@Autowired
	private ConsoleSystemMenuUriConvert menuUriConvert;

	/**
	 * 通过系统过滤菜单
	 * 
	 * @param systemId
	 * @return
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "queryBySystem", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryBySystem(long systemId) {
		List<ConsoleSystemMenuUri> list = menuUriService.queryBySystem(systemId);
		return new Value(menuUriConvert.to(list));
	}

	/**
	 * 添加新的uri
	 * 
	 * @param systemId
	 * @param menuUri
	 * @param description
	 * @return
	 */
	@RequestMapping(value = "addUri", method = { RequestMethod.GET, RequestMethod.POST })
	@ConsoleRolesAllowed(systemAdmin = true)
	public Value addUri(long systemId, String menuUri, String description) {
		menuUriService.addUri(systemId, menuUri, description);
		return new Value();
	}

	/**
	 * 更新uri
	 * 
	 * @param id
	 * @param systemId
	 * @param menuUri
	 * @param description
	 * @return
	 */
	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "updateUri", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateUri(long id, Long systemId, String menuUri, String description) {
		menuUriService.updateUri(id, systemId, menuUri, description);
		return new Value();
	}
}
