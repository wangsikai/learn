package com.lanking.uxb.service.code.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataService;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * 负责集群下基础数据的更新
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@SuppressWarnings("rawtypes")
@Component
public class BaseDataUpdateListener implements ApplicationListener<LocalCacheEvent> {

	private Logger logger = LoggerFactory.getLogger(BaseDataUpdateListener.class);

	@Autowired
	private BaseDataService baseDataService;

	@Override
	public void onApplicationEvent(LocalCacheEvent event) {
		logger.info(JSON.toJSONString(event.getSource()));
		BaseDataAction action = BaseDataAction.findByName(event.getAction());
		if (action == BaseDataAction.RELOAD) {
			baseDataService.reload(BaseDataType.findByName((String) event.getSource()));
		}
	}
}
