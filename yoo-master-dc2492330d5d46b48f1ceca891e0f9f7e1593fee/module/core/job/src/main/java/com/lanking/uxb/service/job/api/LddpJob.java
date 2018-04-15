package com.lanking.uxb.service.job.api;

import org.quartz.Job;

public interface LddpJob extends Job {

	boolean isCluster();

	String jobGroup();

	String jobName();

	String jobDescription();

	boolean jobRequestsRecovery();
}
