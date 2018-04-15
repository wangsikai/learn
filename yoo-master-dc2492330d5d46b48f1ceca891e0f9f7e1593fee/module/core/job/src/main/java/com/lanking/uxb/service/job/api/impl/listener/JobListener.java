package com.lanking.uxb.service.job.api.impl.listener;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqJobRegistryConstants;
import com.lanking.cloud.domain.base.job.JobMonitor;
import com.lanking.cloud.domain.base.job.JobMonitor.JobExeState;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.job.api.JobMonitorService;
import com.lanking.uxb.service.message.api.MessageSender;

@Component
@Exchange(name = MqJobRegistryConstants.EX_JOB)
public class JobListener {

	private Logger logger = LoggerFactory.getLogger(JobListener.class);

	@Autowired
	private JobMonitorService jobMonitorService;
	@Autowired
	private MessageSender messageSender;
	@Autowired(required = false)
	private Scheduler scheduler;

	@Listener(queue = MqJobRegistryConstants.QUEUE_JOB_MONITOR, routingKey = MqJobRegistryConstants.RK_JOB_MONITOR)
	public void monitor(JSONObject jsonObject) {
		logger.info("monitor:{}", jsonObject.toString());
		try {
			String action = jsonObject.getString("action");
			if ("create".equals(action)) {
				long id = jsonObject.getLongValue("id");
				String jobGroup = jsonObject.getString("jobGroup");
				String jobName = jsonObject.getString("jobName");
				String jobDescription = jsonObject.getString("jobDescription");
				String triggerGroup = jsonObject.getString("triggerGroup");
				String triggerName = jsonObject.getString("triggerName");
				String triggerDescription = jsonObject.getString("triggerDescription");
				String triggerCronExpression = jsonObject.getString("triggerCronExpression");
				Date startAt = jsonObject.getDate("startAt");
				JobExeState state = jsonObject.getObject("state", JobExeState.class);
				jobMonitorService.create(id, jobGroup, jobName, jobDescription, triggerGroup, triggerName,
						triggerDescription, triggerCronExpression, startAt, state);
				// send email
				if (Env.getBoolean("job.monitor.email.enable")) {
					messageSender.send(new EmailPacket(Env.getDynamicString("job.monitor.email"), 11000016,
							ValueMap.value("job", jobName + "【" + jobDescription + "】").put("state", state.name())));
				}
			} else if ("update".equals(action)) {
				long id = jsonObject.getLongValue("id");
				Date endAt = jsonObject.getDate("endAt");
				JobExeState state = jsonObject.getObject("state", JobExeState.class);
				JobMonitor jobMonitor = jobMonitorService.update(id, endAt, state);
				// send email
				if (jobMonitor != null && Env.getBoolean("job.monitor.email.enable")) {
					messageSender
							.send(new EmailPacket(Env.getDynamicString("job.monitor.email"),
									11000017, ValueMap
											.value("job",
													jobMonitor.getJobName() + "【" + jobMonitor.getJobDescription()
															+ "】")
											.put("state", jobMonitor.getState().name()).put("seconds",
													(jobMonitor.getStartAt().getTime() - endAt.getTime()) / 1000)));
				}
			} else if ("schedule".equals(action)) {
				JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
				jobDetailFactoryBean.setGroup(jsonObject.getString("jobGroup"));
				jobDetailFactoryBean.setName("TMP_" + jsonObject.getString("jobName"));
				jobDetailFactoryBean.setDescription(jsonObject.getString("jobDescription"));
				jobDetailFactoryBean.setJobClass(Class.forName(jsonObject.getString("jobClassName")));
				jobDetailFactoryBean.setDurability(false);
				jobDetailFactoryBean.setRequestsRecovery(false);
				jobDetailFactoryBean.afterPropertiesSet();
				JobDetail jobDetail = jobDetailFactoryBean.getObject();

				SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
				simpleTriggerFactoryBean.setName("TMP_SIMPLETRIGGER");
				simpleTriggerFactoryBean.setStartTime(new Date());
				simpleTriggerFactoryBean.setRepeatCount(0);
				simpleTriggerFactoryBean.setRepeatInterval(1000);
				simpleTriggerFactoryBean.setJobDetail(jobDetail);
				simpleTriggerFactoryBean.afterPropertiesSet();
				SimpleTrigger simpleTrigger = simpleTriggerFactoryBean.getObject();
				scheduler.scheduleJob(jobDetail, simpleTrigger);
			} else if ("pause".equals(action) || "waiting".equals(action)) {
				TriggerKey key = new TriggerKey(jsonObject.getString("triggerName"),
						jsonObject.getString("triggerGroup"));
				if ("pause".equals(action)) {
					scheduler.pauseTrigger(key);
				} else {
					scheduler.resumeTrigger(key);
				}
			}
		} catch (Exception e) {
			logger.error("process template error:", e);
		}
	}
}
