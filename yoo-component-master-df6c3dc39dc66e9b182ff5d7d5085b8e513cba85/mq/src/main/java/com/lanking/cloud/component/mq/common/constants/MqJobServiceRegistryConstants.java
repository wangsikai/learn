package com.lanking.cloud.component.mq.common.constants;

/**
 * job-相关MQ定义
 * 
 * @since 1.4.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月20日
 */
public interface MqJobServiceRegistryConstants {

	/**
	 * 调度服务交换机
	 */
	String EX_JOBS = "ex.jobs";

	/**
	 * 排行榜相关
	 */
	String QUEUE_JOBS_RANKING_PUSH = "q.jobs.ranking.push";
	String RK_JOBS_RANKING_PUSH = "rk.jobs.ranking.push";

}
