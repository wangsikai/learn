package com.lanking.uxb.security.api;

import java.util.Date;
import java.util.List;

public interface ApiMonitorLogService {

	void save(String api, int costTime, String ex, long userId, String params, String token, String hostName,
			Date createAt);

	void save(List<String> apis, List<Integer> costTimes, List<String> exs, List<Long> userIds, List<String> params,
			List<String> tokens, List<String> hostNames, List<Date> createAts);
}
