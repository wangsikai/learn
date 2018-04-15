package com.lanking.uxb.security.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.frame.monitor.ApiMonitor;

public interface ApiMonitorService {

	ApiMonitor findOneByApi(String api);

	ApiMonitor register(String api);

	List<ApiMonitor> getAll();

	void update(String api, int requestTime, int failTime, int costTime, Date updateAt);

	void updateAvgCostTime();
}
