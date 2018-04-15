package com.lanking.uxb.service.adminSecurity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.convert.ConsoleSystemConvert;
import com.lanking.uxb.service.adminSecurity.form.ConsoleSystemForm;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.adminSecurity.value.VConsoleSystem;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "con/system")
public class ConsoleSystemController {

	@Autowired
	private ConsoleSystemService consoleSystemService;
	@Autowired
	private ConsoleSystemConvert consoleSystemConvert;
	@Autowired
	private ConsoleUserService consoleUserService;

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(@RequestParam(value = "name") String name) {
		ConsoleSystemForm form = new ConsoleSystemForm();
		form.setName(name);
		consoleSystemService.save(form);
		return new Value();
	}

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "find_all", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll() {
		List<ConsoleSystem> systemList = consoleSystemService.findAll();
		List<VConsoleSystem> vConsoleSystems = consoleSystemConvert.to(systemList);

		return new Value(vConsoleSystems);
	}

	@ConsoleRolesAllowed(systemAdmin = true)
	@RequestMapping(value = "updateStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateStatus(long systemId, Status status) {
		if (systemId == 0) {
			return new Value(new IllegalArgException("systemId"));
		}

		consoleSystemService.updateStatus(systemId, status);

		return new Value();
	}
}
