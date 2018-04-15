package com.lanking.uxb.service.adminSecurity.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.support.common.auth.ConsoleMenuEntity;
import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemService;
import com.lanking.uxb.service.adminSecurity.value.VConsoleMenuEntity;
import com.lanking.uxb.service.adminSecurity.value.VConsoleSystem;

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
public class ConsoleMenuEntityConvert extends Converter<VConsoleMenuEntity, ConsoleMenuEntity, Long> {

	@Autowired
	private ConsoleSystemService consoleSystemService;
	@Autowired
	private ConsoleSystemConvert consoleSystemConvert;

	@Override
	protected Long getId(ConsoleMenuEntity consoleMenuEntity) {
		return consoleMenuEntity.getId();
	}

	@Override
	protected VConsoleMenuEntity convert(ConsoleMenuEntity consoleMenuEntity) {
		VConsoleMenuEntity v = new VConsoleMenuEntity();
		v.setName(consoleMenuEntity.getName());
		v.setCreateAt(consoleMenuEntity.getCreateAt());
		v.setUrl(consoleMenuEntity.getUrl());
		v.setId(consoleMenuEntity.getId());
		v.setpId(consoleMenuEntity.getpId());
		v.setStatus(consoleMenuEntity.getStatus());
		return v;
	}

	public List<VConsoleMenuEntity> assembleMenuTree(List<VConsoleMenuEntity> src) {
		List<VConsoleMenuEntity> dest = Lists.newArrayList();
		for (VConsoleMenuEntity v : src) {
			internalAssembleMenuTree(dest, v);
		}

		return dest;
	}

	private void internalAssembleMenuTree(List<VConsoleMenuEntity> dest, VConsoleMenuEntity v) {
		if (v.getpId() == -1) {
			dest.add(v);
		} else {
			for (VConsoleMenuEntity pc : dest) {
				if (pc.getId().equals(v.getpId())) {
					pc.getChildren().add(v);
				} else {
					this.internalAssembleMenuTree(pc.getChildren(), v);
				}
			}
		}
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 　返回Menu对应的System对象，目前暂时用不到
		assemblers.add(new ConverterAssembler<VConsoleMenuEntity, ConsoleMenuEntity, Long, VConsoleSystem>() {

			@Override
			public boolean accept(ConsoleMenuEntity consoleMenuEntity) {
				return false;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return false;
			}

			@Override
			public Long getKey(ConsoleMenuEntity consoleMenuEntity, VConsoleMenuEntity vConsoleMenuEntity) {
				return consoleMenuEntity.getSystemId();
			}

			@Override
			public void setValue(ConsoleMenuEntity consoleMenuEntity, VConsoleMenuEntity vConsoleMenuEntity,
					VConsoleSystem value) {
				if (value != null) {
					vConsoleMenuEntity.setSystem(value);
				}
			}

			@Override
			public VConsoleSystem getValue(Long key) {
				if (key == null) {
					return null;
				}
				ConsoleSystem consoleSystem = consoleSystemService.get(key);
				return consoleSystemConvert.to(consoleSystem);
			}

			@Override
			public Map<Long, VConsoleSystem> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();
				List<ConsoleSystem> list = consoleSystemService.mgetList(keys);
				List<VConsoleSystem> vList = consoleSystemConvert.to(list);
				Map<Long, VConsoleSystem> vMap = Maps.newHashMap();
				for (VConsoleSystem v : vList) {
					vMap.put(v.getId(), v);
				}
				return vMap;
			}
		});
	}
}
