package com.lanking.uxb.security.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.monitor.ApiMonitor;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.security.api.ApiMonitorService;

@Transactional(readOnly = true)
@Service
public class ApiMonitorServiceImpl implements ApiMonitorService, ApplicationListener<ApplicationReadyEvent> {
	@Autowired
	@Qualifier("ApiMonitorRepo")
	private Repo<ApiMonitor, Long> apiMonitorRepo;

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping mapping;

	@Override
	public ApiMonitor findOneByApi(String api) {
		return apiMonitorRepo.find("$findByApi", Params.param("api", api)).get();
	}

	@Transactional
	@Override
	public ApiMonitor register(String api) {
		ApiMonitor apiMonitor = new ApiMonitor();
		apiMonitor.setApi(api);
		apiMonitor.setRequestTime(0);
		apiMonitor.setFailTime(0);
		apiMonitor.setCostTime(0);
		apiMonitor.setCreateAt(new Date());
		apiMonitor.setUpdateAt(apiMonitor.getCreateAt());
		return apiMonitorRepo.save(apiMonitor);
	}

	@Override
	public List<ApiMonitor> getAll() {
		return apiMonitorRepo.find("$getAll").list();
	}

	@Transactional
	@Override
	public void update(String api, int requestTime, int failTime, int costTime, Date updateAt) {
		apiMonitorRepo.execute("$update", Params.param("api", api).put("requestTime", requestTime)
				.put("failTime", failTime).put("costTime", costTime).put("updateAt", updateAt));
	}

	@Transactional
	@Override
	public void updateAvgCostTime() {
		apiMonitorRepo.execute("$updateAvgCostTime");
	}

	@Transactional
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Map<RequestMappingInfo, HandlerMethod> infos = mapping.getHandlerMethods();
		List<String> apis = new ArrayList<String>(infos.size());
		List<ApiMonitor> apiMonitors = getAll();
		for (ApiMonitor apiMonitor : apiMonitors) {
			apis.add(apiMonitor.getApi());
		}
		for (RequestMappingInfo info : infos.keySet()) {
			Set<String> patterns = info.getPatternsCondition().getPatterns();
			for (String pattern : patterns) {
				if (!apis.contains(pattern)) {
					register(pattern);
				}
			}
		}
	}

}
