package com.lanking.uxb.security.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 处理作业相关操作安全
 * 
 * @since yoomath V1.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月9日
 */
@Service
public final class ApiAllowedCacheService extends AbstractCacheService {

	private ValueOperations<String, String> spOpt;
	private ListOperations<String, JSONObject> jsonObjectListOpt;

	private String getKey(String uri) {
		return assemblyKey(Security.getToken(), uri);
	}

	public void set(String uri, long s) {
		spOpt.set(getKey(uri), "1", s, TimeUnit.SECONDS);
	}

	public String get(String uri) {
		return spOpt.get(getKey(uri));
	}

	@SuppressWarnings("unchecked")
	public void expire(String uri, long ms) {
		getRedisTemplate().expire(getKey(uri), ms, TimeUnit.MILLISECONDS);
	}

	private String getApiMonitorKey() {
		return "monitorlist";
	}

	public void add(JSONObject jsonObject) {
		jsonObjectListOpt.leftPush(getApiMonitorKey(), jsonObject);
	}

	public List<JSONObject> rangeApiMonitors() {
		long size = jsonObjectListOpt.size(getApiMonitorKey());
		if (size >= 50) {
			List<JSONObject> jos = new ArrayList<JSONObject>(50);
			for (int i = 0; i < 50; i++) {
				jos.add(jsonObjectListOpt.rightPop(getApiMonitorKey()));
			}
			return jos;
		}
		return null;
	}

	@Override
	public String getNs() {
		return "api";
	}

	@Override
	public String getNsCn() {
		return "api控制";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		spOpt = getRedisTemplate().opsForValue();
		jsonObjectListOpt = getRedisTemplate().opsForList();
	}

}
