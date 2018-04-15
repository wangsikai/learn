package com.lanking.uxb.service.code.api.impl;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.lanking.uxb.service.code.api.BaseDataHandle;
import com.lanking.uxb.service.code.api.BaseDataService;
import com.lanking.uxb.service.code.api.BaseDataType;

@Service
public class BaseDataServiceImpl implements BaseDataService, ApplicationContextAware {

	private ApplicationContext appContext;

	Map<BaseDataType, BaseDataHandle> baseDatahandles = Maps.newHashMap();

	@Override
	public void reload(BaseDataType baseDataType) {
		if (baseDatahandles.isEmpty()) {
			for (BaseDataHandle baseDataHandle : appContext.getBeansOfType(BaseDataHandle.class).values()) {
				baseDatahandles.put(baseDataHandle.getType(), baseDataHandle);
			}
		}
		BaseDataHandle baseDataHandle = baseDatahandles.get(baseDataType);
		if (baseDataHandle != null) {
			baseDataHandle.reload();
		}
	}

	@Override
	public void init(BaseDataType baseDataType) {
		if (baseDatahandles.isEmpty()) {
			for (BaseDataHandle baseDataHandle : appContext.getBeansOfType(BaseDataHandle.class).values()) {
				baseDatahandles.put(baseDataHandle.getType(), baseDataHandle);
			}
		}
		BaseDataHandle baseDataHandle = baseDatahandles.get(baseDataType);
		if (baseDataHandle != null) {
			baseDataHandle.init();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

}
