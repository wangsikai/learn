package com.lanking.cloud.job.clearEventTrace.Impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.job.clearEventTrace.ClearEventTraceJobService;

import lombok.extern.slf4j.Slf4j;

@Transactional(value = "eventTraceDataSourceTransactionManager", readOnly = true)
@Service
@Slf4j
public class ClearEventTraceJobJdbcImpl implements ClearEventTraceJobService {

	@Autowired
	@Qualifier("eventTraceJdbcTemplate")
	private JdbcTemplate eventTraceJdbcTemplate;

	@Transactional("eventTraceDataSourceTransactionManager")
	@Override
	public void clearJobExecutionLog(String deleteDate) {
		log.info("clearJobExecutionLog starts ...");
		Object[] batchArg = { deleteDate };
		eventTraceJdbcTemplate.update(getJobExecutionLogSql(), batchArg);
		log.info("clearJobExecutionLog ends");

	}

	@Transactional("eventTraceDataSourceTransactionManager")
	@Override
	public void clearJobStatusTraceLog(String deleteDate) {
		log.info("clearJobStatusTraceLog starts ...");
		Object[] batchArg = { deleteDate };
		eventTraceJdbcTemplate.update(getJobStatusTraceLogSql(), batchArg);
		log.info("clearJobStatusTraceLog ends");
	}

	private String getJobExecutionLogSql() {
		return "delete from job_execution_log where complete_time < ?";
	}

	private String getJobStatusTraceLogSql() {
		return "delete from job_status_trace_log where creation_time < ?";
	}

	@Override
	public String getDeleteDate(int day) {
		if (day == 0) {
			day = 1;
		}

		Date date = Date.from(LocalDateTime.now().minusDays(day - 1).withHour(0).withMinute(0).withSecond(0).withNano(0)
				.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formater.format(date);
	}

}
