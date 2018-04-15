package com.lanking.uxb.service.adminSecurity.convert;

import com.lanking.cloud.domain.support.common.auth.ConsoleRole;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.adminSecurity.value.VConsoleRole;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ConsoleRoleConvert extends Converter<VConsoleRole, ConsoleRole, Long> {

	@Override
	protected Long getId(ConsoleRole consoleRole) {
		return consoleRole.getId();
	}

	@Override
	protected VConsoleRole convert(ConsoleRole consoleRole) {
		VConsoleRole v = new VConsoleRole();
		v.setName(consoleRole.getName());
		v.setId(consoleRole.getId());
		v.setCreateAt(consoleRole.getCreateAt());
		v.setCode(consoleRole.getCode());
		return v;
	}
}
