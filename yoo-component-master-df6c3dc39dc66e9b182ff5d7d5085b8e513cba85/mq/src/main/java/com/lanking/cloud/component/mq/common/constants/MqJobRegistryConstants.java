package com.lanking.cloud.component.mq.common.constants;

/**
 * 任务-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqJobRegistryConstants {

	/**
	 * 调度服务交换机
	 */
	String EX_JOB = "ex.job";
	/**
	 * 调度服务交换机-monitor队列
	 */
	String QUEUE_JOB_MONITOR = "q.job.monitor";
	/**
	 * 调度服务交换机-monitor路由
	 */
	String RK_JOB_MONITOR = "rk.job.monitor";

}
