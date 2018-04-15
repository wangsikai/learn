package com.lanking.uxb.operation.job.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.job.JobMonitor;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.operation.job.api.OpJobService;

@Transactional(value = "bizDataSourceTransactionManager", readOnly = true)
@Service
public class OpJobServiceImpl implements OpJobService {

	@Autowired
	@Qualifier("bizJdbcTemplate")
	private JdbcTemplate bizJdbcTemplate;
	@Autowired
	@Qualifier("JobMonitorRepo")
	private Repo<JobMonitor, Integer> jobMonitorRepo;

	@Override
	public List<Map<String, Object>> findAllJobDetails() {
		return bizJdbcTemplate.queryForList("select * from qrtz_job_details");
	}

	@Override
	public List<Map<String, Object>> findAllTriggers() {
		return bizJdbcTemplate.queryForList(
				"select t4.*,t3.state from (SELECT t1.*, t2.cron_expression,t2.time_zone_id FROM qrtz_triggers t1 INNER JOIN qrtz_cron_triggers t2 WHERE t1.sched_name = t2.sched_name AND t1.trigger_group = t2.trigger_group AND t1.trigger_name = t2.trigger_name) t4 LEFT JOIN qrtz_fired_triggers t3 ON t4.trigger_group = t3.trigger_group AND t4.trigger_name = t3.trigger_name AND t4.job_group = t3.job_group AND t4.job_name = t3.job_name");
	}

	@Override
	public Page<JobMonitor> query(Pageable pageable) {
		return jobMonitorRepo.find("$opQuery").fetch(pageable);
	}

	@Transactional(value = "bizDataSourceTransactionManager")
	@Override
	public void updateCronExpression(String triggerName, String cronExpression) {
		String sql = "SELECT PREV_FIRE_TIME FROM QRTZ_TRIGGERS WHERE trigger_name = ?";
		Long prevTime = (Long) bizJdbcTemplate.queryForObject(sql, new Object[] { triggerName }, Long.class);
		Long prevTime1 = prevTime == -1 ? System.currentTimeMillis() : prevTime;
		CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronExpression);
		Date nextTimePoint = cronSequenceGenerator.next(new Date(prevTime1));
		// 如果算出来的时间在当前时间之前 则再算下次时间 一直算到在当前时间之后的时间
		while (nextTimePoint.getTime() < new Date().getTime()) {
			nextTimePoint = cronSequenceGenerator.next(nextTimePoint);
		}
		bizJdbcTemplate.update("update qrtz_cron_triggers set cron_expression = ? where trigger_name = ?",
				new Object[] { cronExpression, triggerName });
		bizJdbcTemplate.update("update qrtz_triggers set next_fire_time = ? where trigger_name = ?",
				new Object[] { nextTimePoint.getTime(), triggerName });
	}

	@Override
	public int countInQrtzFiredTriggers(String triggerName) {
		String sql = "SELECT count(1) FROM qrtz_fired_triggers WHERE trigger_name = ?";
		Integer count = (Integer) bizJdbcTemplate.queryForObject(sql, new Object[] { triggerName }, Integer.class);
		return count;
	}

}
