package com.lanking.uxb.operation.job.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.base.job.JobMonitor;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface OpJobService {

	List<Map<String, Object>> findAllJobDetails();

	List<Map<String, Object>> findAllTriggers();

	Page<JobMonitor> query(Pageable pageable);

	void updateCronExpression(String triggerName, String cronExpression);

	/**
	 * 判断triggerName在qrtz_fired_triggers是否存在
	 * 
	 * @param triggerName
	 * @return
	 */
	int countInQrtzFiredTriggers(String triggerName);

}
