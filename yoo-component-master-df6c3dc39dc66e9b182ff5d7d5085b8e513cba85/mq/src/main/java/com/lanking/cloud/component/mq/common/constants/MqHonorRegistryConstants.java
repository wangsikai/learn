package com.lanking.cloud.component.mq.common.constants;

/**
 * 荣誉-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqHonorRegistryConstants {

	/**
	 * 金币数据交换机
	 */
	String EX_COINS = "ex.coins";
	/**
	 * 金币数据交换机-金币日志队列
	 */
	String QUEUE_COINS_LOG = "q.coins.log";
	/**
	 * 金币数据交换机-金币日志路由
	 */
	String RK_COINS_LOG = "rk.coins.log";
	/**
	 * 用户任务交换机
	 */
	String EX_TASK = "ex.task";
	/**
	 * 用户任务交换机-任务日志队列
	 */
	String QUEUE_TASK_LOG = "q.task.log";
	/**
	 * 用户任务交换机-任务日志路由
	 */
	String RK_TASK_LOG = "rk.task.log";

	/**
	 * 用户任务交换机-签到任务日志队列
	 */
	String QUEUE_TASK_LOG_101010001 = "q.task.log.101010001";
	/**
	 * 用户任务交换机-签到任务日志路由
	 */
	String RK_TASK_LOG_101010001 = "rk.task.log.101010001";
}
