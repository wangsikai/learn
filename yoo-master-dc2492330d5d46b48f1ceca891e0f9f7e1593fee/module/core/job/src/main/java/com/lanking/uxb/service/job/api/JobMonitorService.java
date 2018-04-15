package com.lanking.uxb.service.job.api;

import java.util.Date;

import com.lanking.cloud.domain.base.job.JobMonitor;
import com.lanking.cloud.domain.base.job.JobMonitor.JobExeState;

public interface JobMonitorService {

	JobMonitor create(long id, String jobGroup, String jobName, String jobDescription, String triggerGroup,
			String triggerName, String triggerDescription, String triggerCronExpression, Date startAt, JobExeState state);

	JobMonitor update(long id, Date endAt, JobExeState state);
}
