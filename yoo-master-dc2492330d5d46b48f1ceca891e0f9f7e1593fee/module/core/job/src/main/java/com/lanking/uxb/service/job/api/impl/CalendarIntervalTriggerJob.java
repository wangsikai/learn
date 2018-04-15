package com.lanking.uxb.service.job.api.impl;

import java.util.Date;

import org.quartz.DateBuilder.IntervalUnit;

import com.lanking.cloud.sdk.util.StringUtils;

public abstract class CalendarIntervalTriggerJob extends AbstractJob {

	private final String DEFAULT_GROUP = "CALENDARINTERVALTRIGGER_DEFAULT_GROUP";

	public String jobGroup() {
		return DEFAULT_GROUP;
	}

	public boolean jobRequestsRecovery() {
		return false;
	}

	public String triggerGroup() {
		return DEFAULT_GROUP;
	}

	public abstract String triggerName();

	public String triggerDescription() {
		return StringUtils.EMPTY;
	}

	public abstract Date startTime();

	public abstract Date endTime();

	public abstract IntervalUnit intervalUnit();

	public abstract int repeatInterval();

	public boolean legal() {
		return StringUtils.isNotBlank(jobGroup()) && StringUtils.isNotBlank(jobName())
				&& StringUtils.isNotBlank(jobDescription()) && StringUtils.isNotBlank(triggerGroup())
				&& StringUtils.isNotBlank(triggerName()) && startTime() != null && endTime() != null
				&& startTime().getTime() < endTime().getTime() && intervalUnit() != null && repeatInterval() > 0;
	}

}
