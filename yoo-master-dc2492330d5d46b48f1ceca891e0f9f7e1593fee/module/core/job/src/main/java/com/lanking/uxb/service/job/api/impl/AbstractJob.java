package com.lanking.uxb.service.job.api.impl;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.cloud.component.mq.common.constants.MqJobRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.job.JobMonitor.JobExeState;
import com.lanking.uxb.service.job.api.LddpJob;

public abstract class AbstractJob implements LddpJob {

	@Autowired
	private MqSender mqSender;

	@Override
	public boolean isCluster() {
		return false;
	}

	public abstract void execute();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JSONObject startJson = new JSONObject();
		startJson.put("action", "create");
		long id = SnowflakeUUID.next();
		startJson.put("id", id);
		startJson.put("jobGroup", context.getJobDetail().getKey().getGroup());
		startJson.put("jobName", context.getJobDetail().getKey().getName());
		startJson.put("jobDescription", context.getJobDetail().getDescription());

		Trigger trigger = context.getTrigger();
		startJson.put("triggerGroup", trigger.getKey().getGroup());
		startJson.put("triggerName", trigger.getKey().getName());
		startJson.put("triggerDescription", trigger.getDescription());
		if (trigger instanceof CronTriggerImpl) {
			CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
			startJson.put("triggerCronExpression", cronTrigger.getCronExpression());
		}
		startJson.put("startAt", new Date());
		startJson.put("state", JobExeState.START);
		mqSender.send(MqJobRegistryConstants.EX_JOB, MqJobRegistryConstants.RK_JOB_MONITOR,
				MQ.builder().data(startJson).build());

		JSONObject endJson = new JSONObject();
		endJson.put("action", "update");
		endJson.put("id", id);
		JobExeState state = JobExeState.END_SUCCESS;
		try {
			execute();
		} catch (Exception e) {
			state = JobExeState.END_FAIL;
		}
		endJson.put("state", state);
		endJson.put("endAt", new Date());
		mqSender.send(MqJobRegistryConstants.EX_JOB, MqJobRegistryConstants.RK_JOB_MONITOR,
				MQ.builder().data(endJson).build());
	}

}
