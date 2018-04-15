package com.lanking.uxb.service.adminSecurity.convert;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.support.common.auth.ConsoleGroup;
import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.value.VConsoleGroup;
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
public class ConsoleGroupConvert extends Converter<VConsoleGroup, ConsoleGroup, Long> {
	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private ConsoleUserConvert consoleUserConvert;
	@Autowired
	private ConsoleSystemService consoleSystemService;
	@Autowired
	private ConsoleSystemConvert consoleSystemConvert;

	@Override
	protected Long getId(ConsoleGroup consoleGroup) {
		return consoleGroup.getId();
	}

	@Override
	protected VConsoleGroup convert(ConsoleGroup consoleGroup) {
		VConsoleGroup v = new VConsoleGroup();
		v.setId(consoleGroup.getId());
		v.setName(consoleGroup.getName());
		List<ConsoleUser> users = consoleUserService.getUsersByGroup(consoleGroup.getId());
		List<VConsoleUser> vUsers = consoleUserConvert.to(users);
		v.setUsers(vUsers);
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VConsoleGroup, ConsoleGroup, Long, VConsoleSystem>() {

			@Override
			public boolean accept(ConsoleGroup consoleGroup) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ConsoleGroup consoleGroup, VConsoleGroup vConsoleGroup) {
				return consoleGroup.getSystemId();
			}

			@Override
			public void setValue(ConsoleGroup consoleGroup, VConsoleGroup vConsoleGroup, VConsoleSystem value) {
				if (value != null) {
					vConsoleGroup.setSystem(value);
				}
			}

			@Override
			public VConsoleSystem getValue(Long key) {
				if (null == key)
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
