package com.lanking.uxb.service.adminSecurity.convert;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemService;
import com.lanking.uxb.service.adminSecurity.value.VConsoleSystem;
import com.lanking.uxb.service.adminSecurity.value.VConsoleUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ConsoleUserConvert extends Converter<VConsoleUser, ConsoleUser, Long> {
	@Autowired
	private ConsoleSystemService consoleSystemService;
	@Autowired
	private ConsoleSystemConvert consoleSystemConvert;

	@Override
	protected Long getId(ConsoleUser consoleUser) {
		return consoleUser.getId();
	}

	@Override
	protected VConsoleUser convert(ConsoleUser consoleUser) {
		VConsoleUser v = new VConsoleUser();
		v.setCreateAt(consoleUser.getCreateAt());
		v.setId(consoleUser.getId());
		v.setName(consoleUser.getName());
		v.setRealName(consoleUser.getRealName());
		v.setStatus(consoleUser.getStatus());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VConsoleUser, ConsoleUser, Long, VConsoleSystem>() {

			@Override
			public boolean accept(ConsoleUser consoleUser) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ConsoleUser consoleUser, VConsoleUser vConsoleUser) {
				return consoleUser.getSystemId();
			}

			@Override
			public void setValue(ConsoleUser consoleUser, VConsoleUser vConsoleUser, VConsoleSystem value) {
				if (value != null) {
					vConsoleUser.setSystem(value);
				}
			}

			@Override
			public VConsoleSystem getValue(Long key) {
				if (key == null)
					return null;

				ConsoleSystem consoleSystem = consoleSystemService.get(key);
				return consoleSystemConvert.to(consoleSystem);
			}

			@Override
			public Map<Long, VConsoleSystem> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();
				List<ConsoleSystem> consoleSystems = consoleSystemService.mgetList(keys);
				List<VConsoleSystem> vConsoleSystems = consoleSystemConvert.to(consoleSystems);
				Map<Long, VConsoleSystem> vMap = Maps.newHashMap();
				for (VConsoleSystem v : vConsoleSystems) {
					vMap.put(v.getId(), v);
				}
				return vMap;
			}
		});
	}
}
