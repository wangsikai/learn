package com.lanking.uxb.service.data.api.impl.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqDataRegistryConstants;
import com.lanking.uxb.security.api.ApiMonitorLogService;
import com.lanking.uxb.security.cache.ApiAllowedCacheService;

/**
 * 充当着数据总线的作用
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月29日
 */
@Component
@Exchange(name = MqDataRegistryConstants.EX_DATA)
public class DataMqListener {

	private Logger logger = LoggerFactory.getLogger(DataMqListener.class);

	@Autowired
	private ApiMonitorLogService apiMonitorLogService;
	@Autowired
	private ApiAllowedCacheService apiAllowedCacheService;

	@Listener(queue = MqDataRegistryConstants.QUEUE_DATA_APILOG, routingKey = MqDataRegistryConstants.RK_DATA_APILOG)
	public void apiMonitorLog(JSONObject json) {
		try {
			apiAllowedCacheService.add(json);
			List<JSONObject> jsonObjects = apiAllowedCacheService.rangeApiMonitors();
			if (jsonObjects != null) {
				int size = jsonObjects.size();
				List<String> apis = new ArrayList<String>(size);
				List<Integer> costTimes = new ArrayList<Integer>(size);
				List<Long> userIds = new ArrayList<Long>(size);
				List<Date> dates = new ArrayList<Date>(size);
				List<String> params = new ArrayList<String>(size);
				List<String> exs = new ArrayList<String>(size);
				List<String> tokens = new ArrayList<String>(size);
				List<String> hostNames = new ArrayList<String>(size);
				for (JSONObject jo : jsonObjects) {
					apis.add(jo.getString("api"));
					costTimes.add(jo.getIntValue("costTime"));
					userIds.add(jo.getLong("userId"));
					dates.add(jo.getDate("createAt"));
					exs.add(jo.get("ex") == null ? null : jo.getString("ex"));
					params.add(jo.get("params") == null ? null : jo.getString("params"));
					tokens.add(jo.get("token") == null ? null : jo.getString("token"));
					hostNames.add(jo.get("hostName") == null ? null : jo.getString("hostName"));
				}
				apiMonitorLogService.save(apis, costTimes, exs, userIds, params, tokens, hostNames, dates);
			}
		} catch (Exception e) {
			logger.error("api monitor log:", e);
		}
	}
}
