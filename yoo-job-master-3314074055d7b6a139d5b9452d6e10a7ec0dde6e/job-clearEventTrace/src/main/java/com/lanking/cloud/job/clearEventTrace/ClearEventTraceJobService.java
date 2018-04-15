package com.lanking.cloud.job.clearEventTrace;

public interface ClearEventTraceJobService {

	String getDeleteDate(int day);

	/**
	 * 删除job_execution_log表数据
	 */
	void clearJobExecutionLog(String deleteDate);

	/**
	 * 删除job_status_trace_log表数据
	 */
	void clearJobStatusTraceLog(String deleteDate);
}
