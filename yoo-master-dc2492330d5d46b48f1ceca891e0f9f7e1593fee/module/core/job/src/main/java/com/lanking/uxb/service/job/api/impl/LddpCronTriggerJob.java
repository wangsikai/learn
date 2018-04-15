package com.lanking.uxb.service.job.api.impl;

import org.quartz.Scheduler;

import com.lanking.cloud.sdk.util.StringUtils;

public abstract class LddpCronTriggerJob extends AbstractJob {

	public String jobGroup() {
		return Scheduler.DEFAULT_GROUP;
	}

	public boolean jobRequestsRecovery() {
		return false;
	}

	public String triggerGroup() {
		return Scheduler.DEFAULT_GROUP;
	}

	public abstract String triggerName();

	public String triggerDescription() {
		return StringUtils.EMPTY;
	}

	public abstract String cron();

	public boolean legal() {
		return StringUtils.isNotBlank(jobGroup()) && StringUtils.isNotBlank(jobName())
				&& StringUtils.isNotBlank(jobDescription()) && StringUtils.isNotBlank(triggerGroup())
				&& StringUtils.isNotBlank(triggerName()) && StringUtils.isNotBlank(cron());
	}

}
