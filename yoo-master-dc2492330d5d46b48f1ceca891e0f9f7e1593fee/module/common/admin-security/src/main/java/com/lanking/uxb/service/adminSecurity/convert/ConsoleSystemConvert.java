package com.lanking.uxb.service.adminSecurity.convert;

import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.adminSecurity.value.VConsoleSystem;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ConsoleSystemConvert extends Converter<VConsoleSystem, ConsoleSystem, Long> {
	@Override
	protected Long getId(ConsoleSystem consoleSystem) {
		return consoleSystem.getId();
	}

	@Override
	protected VConsoleSystem convert(ConsoleSystem consoleSystem) {
		VConsoleSystem v = new VConsoleSystem();
		v.setId(consoleSystem.getId());
		v.setName(consoleSystem.getName());
		v.setCreateAt(consoleSystem.getCreateAt());
		v.setStatus(consoleSystem.getStatus());
		return v;
	}
}
