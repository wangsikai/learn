package com.lanking.uxb.service.job.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.job.JobMonitor;
import com.lanking.cloud.domain.base.job.JobMonitor.JobExeState;
import com.lanking.uxb.service.job.api.JobMonitorService;

@Transactional(readOnly = true)
@Service
public class JobMonitorServiceImpl implements JobMonitorService {

	@Autowired
	@Qualifier("JobMonitorRepo")
	private Repo<JobMonitor, Long> jobMonitorRepo;

	@Transactional
	@Override
	public JobMonitor create(long id, String jobGroup, String jobName, String jobDescription, String triggerGroup,
			String triggerName, String triggerDescription, String triggerCronExpression, Date startAt,
			JobExeState state) {
		JobMonitor jobMonitor = new JobMonitor();
		jobMonitor.setId(id);
		jobMonitor.setJobGroup(jobGroup);
		jobMonitor.setJobName(jobName);
		jobMonitor.setJobDescription(jobDescription);
		jobMonitor.setTriggerGroup(triggerGroup);
		jobMonitor.setTriggerName(triggerName);
		jobMonitor.setTriggerDescription(triggerDescription);
		jobMonitor.setTriggerCronExpression(triggerCronExpression);
		jobMonitor.setStartAt(startAt);
		jobMonitor.setState(state);
		jobMonitor.setCreateAt(new Date());
		return jobMonitorRepo.save(jobMonitor);
	}

	@Transactional
	@Override
	public JobMonitor update(long id, Date endAt, JobExeState state) {
		JobMonitor jobMonitor = jobMonitorRepo.get(id);
		if (jobMonitor != null) {
			jobMonitor.setEndAt(endAt);
			jobMonitor.setState(state);
			jobMonitor.setUpdateAt(new Date());
			return jobMonitorRepo.save(jobMonitor);
		} else {
			return null;
		}
	}

}
