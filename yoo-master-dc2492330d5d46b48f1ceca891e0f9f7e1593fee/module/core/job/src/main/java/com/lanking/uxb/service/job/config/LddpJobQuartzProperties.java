package com.lanking.uxb.service.job.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component(value = "lddpJobQuartzProperties")
@ConditionalOnExpression("${lddp.job.quartz.enable}")
public class LddpJobQuartzProperties {

	@Value("${lddp.job.quartz.org.quartz.scheduler.instanceName}")
	private String schedulerInstanceName;

	@Value("${lddp.job.quartz.org.quartz.scheduler.instanceId}")
	private String schedulerInstanceId;

	@Value("${lddp.job.quartz.org.quartz.jobStore.class}")
	private String jobStoreClass;

	@Value("${lddp.job.quartz.org.quartz.jobStore.driverDelegateClass}")
	private String jobStoreDriverDelegateClass;

	@Value("${lddp.job.quartz.org.quartz.jobStore.tablePrefix}")
	private String jobStoreTablePrefix;

	@Value("${lddp.job.quartz.org.quartz.jobStore.isClustered}")
	private String jobStoreIsClustered;

	@Value("${lddp.job.quartz.org.quartz.jobStore.clusterCheckinInterval}")
	private String jobStoreClusterCheckinInterval;

	@Value("${lddp.job.quartz.org.quartz.threadPool.class}")
	private String threadPoolClass;

	@Value("${lddp.job.quartz.org.quartz.threadPool.threadCount}")
	private String threadPoolThreadCount;

	@Value("${lddp.job.quartz.org.quartz.threadPool.threadPriority}")
	private String threadPoolThreadPriority;

	@Value("${lddp.job.quartz.org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread}")
	private String threadPoolThreadsInheritContextClassLoaderOfInitializingThread;

	public String getSchedulerInstanceName() {
		return schedulerInstanceName;
	}

	public void setSchedulerInstanceName(String schedulerInstanceName) {
		this.schedulerInstanceName = schedulerInstanceName;
	}

	public String getSchedulerInstanceId() {
		return schedulerInstanceId;
	}

	public void setSchedulerInstanceId(String schedulerInstanceId) {
		this.schedulerInstanceId = schedulerInstanceId;
	}

	public String getJobStoreClass() {
		return jobStoreClass;
	}

	public void setJobStoreClass(String jobStoreClass) {
		this.jobStoreClass = jobStoreClass;
	}

	public String getJobStoreDriverDelegateClass() {
		return jobStoreDriverDelegateClass;
	}

	public void setJobStoreDriverDelegateClass(String jobStoreDriverDelegateClass) {
		this.jobStoreDriverDelegateClass = jobStoreDriverDelegateClass;
	}

	public String getJobStoreTablePrefix() {
		return jobStoreTablePrefix;
	}

	public void setJobStoreTablePrefix(String jobStoreTablePrefix) {
		this.jobStoreTablePrefix = jobStoreTablePrefix;
	}

	public String getJobStoreIsClustered() {
		return jobStoreIsClustered;
	}

	public void setJobStoreIsClustered(String jobStoreIsClustered) {
		this.jobStoreIsClustered = jobStoreIsClustered;
	}

	public String getJobStoreClusterCheckinInterval() {
		return jobStoreClusterCheckinInterval;
	}

	public void setJobStoreClusterCheckinInterval(String jobStoreClusterCheckinInterval) {
		this.jobStoreClusterCheckinInterval = jobStoreClusterCheckinInterval;
	}

	public String getThreadPoolClass() {
		return threadPoolClass;
	}

	public void setThreadPoolClass(String threadPoolClass) {
		this.threadPoolClass = threadPoolClass;
	}

	public String getThreadPoolThreadCount() {
		return threadPoolThreadCount;
	}

	public void setThreadPoolThreadCount(String threadPoolThreadCount) {
		this.threadPoolThreadCount = threadPoolThreadCount;
	}

	public String getThreadPoolThreadPriority() {
		return threadPoolThreadPriority;
	}

	public void setThreadPoolThreadPriority(String threadPoolThreadPriority) {
		this.threadPoolThreadPriority = threadPoolThreadPriority;
	}

	public String getThreadPoolThreadsInheritContextClassLoaderOfInitializingThread() {
		return threadPoolThreadsInheritContextClassLoaderOfInitializingThread;
	}

	public void setThreadPoolThreadsInheritContextClassLoaderOfInitializingThread(
			String threadPoolThreadsInheritContextClassLoaderOfInitializingThread) {
		this.threadPoolThreadsInheritContextClassLoaderOfInitializingThread = threadPoolThreadsInheritContextClassLoaderOfInitializingThread;
	}

	Properties createQuartzProperties() {
		Properties properties = new Properties();
		properties.put("org.quartz.scheduler.instanceName", schedulerInstanceName);
		properties.put("org.quartz.scheduler.instanceId", schedulerInstanceId);

		properties.put("org.quartz.jobStore.class", jobStoreClass);
		properties.put("org.quartz.jobStore.driverDelegateClass", jobStoreDriverDelegateClass);
		properties.put("org.quartz.jobStore.tablePrefix", jobStoreTablePrefix);
		properties.put("org.quartz.jobStore.isClustered", jobStoreIsClustered);
		properties.put("org.quartz.jobStore.clusterCheckinInterval", jobStoreClusterCheckinInterval);

		properties.put("org.quartz.threadPool.class", threadPoolClass);
		properties.put("org.quartz.threadPool.threadCount", threadPoolThreadCount);
		properties.put("org.quartz.threadPool.threadPriority", threadPoolThreadPriority);
		properties.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread",
				threadPoolThreadsInheritContextClassLoaderOfInitializingThread);

		return properties;
	}
}
