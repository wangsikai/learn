package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.uxb.rescon.basedata.api.ResconTTSHandler;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.form.ResconTTSForm;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ResconTTSConsumer implements ApplicationContextAware, InitializingBean {
	private ApplicationContext applicationContext;

	private List<ResconTTSHandler> handlers = Lists.newArrayList();
	private Map<ResconTTSType, ResconTTSHandler> mHandlers = Maps.newHashMap();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (ResconTTSHandler handler : applicationContext.getBeansOfType(ResconTTSHandler.class).values()) {
			handlers.add(handler);
			mHandlers.put(handler.getType(), handler);
		}
	}

	private ResconTTSHandler get(ResconTTSType type) {
		return mHandlers.get(type);
	}

	public List<ResconTTSHandler> getAll() {
		return handlers;
	}

	public void save(ResconTTSForm form, ResconTTSType type) {
		ResconTTSHandler handler = get(type);
		if (handler != null) {
			handler.save(form);
			// 通知其他集群进行数据更新操作
			// handler.syncData();
		}
	}

	public VResconTTS get(Long id, ResconTTSType type) {
		ResconTTSHandler handler = get(type);
		if (handler != null) {
			return handler.get(id);
		}
		return null;
	}

	public List<VResconTTS> findAll(Map<String, Object> params, ResconTTSType type) {
		ResconTTSHandler handler = get(type);
		if (handler != null) {
			return handler.findAll(params);
		}
		return Lists.newArrayList();
	}

	public void sequence(ResconTTSForm form, ResconTTSType type) {
		ResconTTSHandler handler = get(type);
		if (handler != null) {
			handler.updateSequence(form);
		}
	}

	public void syncData(ResconTTSType type) {
		ResconTTSHandler handler = get(type);
		if (handler != null) {
			handler.syncData();
			;
		}
	}

	public void sequence(List<ResconTTSForm> forms, ResconTTSType type) {
		ResconTTSHandler handler = get(type);
		if (handler != null) {
			handler.updateSequence(forms);
		}
	}

}
