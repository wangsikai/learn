package com.lanking.cloud.component.mq.common.constants;

/**
 * 用户动作行为相关MQ定义.
 * 
 * @author wlche
 *
 */
public interface MqUserActionRegistryConstants {

	/**
	 * 用户动作行为交换机
	 */
	String EX_USER_ACTION = "ex.uaction";

	/**
	 * 用户动作行为交换机-任务日志队列
	 */
	String QUEUE_USER_ACTION_LOG = "q.uaction.log";

	/**
	 * 用户动作行为交换机-任务日志路由
	 */
	String RK_USER_ACTION_LOG = "rk.uaction.log";
}
