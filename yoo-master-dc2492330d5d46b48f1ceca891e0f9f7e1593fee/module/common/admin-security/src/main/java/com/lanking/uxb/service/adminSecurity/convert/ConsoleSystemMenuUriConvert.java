package com.lanking.uxb.service.adminSecurity.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.domain.support.common.auth.ConsoleSystemMenuUri;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemService;
import com.lanking.uxb.service.adminSecurity.value.VConsoleSystemMenuUri;

@Component
public class ConsoleSystemMenuUriConvert extends Converter<VConsoleSystemMenuUri, ConsoleSystemMenuUri, Long> {

	@Autowired
	private ConsoleSystemService consoleSystemService;

	@Override
	protected Long getId(ConsoleSystemMenuUri s) {
		return s.getId();
	}

	@Override
	protected VConsoleSystemMenuUri convert(ConsoleSystemMenuUri s) {
		VConsoleSystemMenuUri v = new VConsoleSystemMenuUri();
		v.setDescription(s.getDescription());
		v.setId(s.getId());
		v.setMenuUri(s.getMenuUri());
		v.setSystemId(s.getSystemId());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VConsoleSystemMenuUri, ConsoleSystemMenuUri, Long, ConsoleSystem>() {

			@Override
			public boolean accept(ConsoleSystemMenuUri s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ConsoleSystemMenuUri s, VConsoleSystemMenuUri d) {
				return s.getSystemId();
			}

			@Override
			public void setValue(ConsoleSystemMenuUri s, VConsoleSystemMenuUri d, ConsoleSystem value) {
				d.setSystemName(value.getName());
			}

			@Override
			public ConsoleSystem getValue(Long key) {
				return consoleSystemService.get(key);
			}

			@Override
			public Map<Long, ConsoleSystem> mgetValue(Collection<Long> keys) {
				return consoleSystemService.mget(keys);
			}

		});
	}
}
