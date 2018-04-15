package com.lanking.cloud.component.mq.common.constants;

/**
 * 会话-相关MQ定义
 * 
 * @since 4.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月11日
 */
public interface MqSessionRegistryConstants {

	/**
	 * 会话服务交换机
	 */
	String EX_SESSION = "ex.session";
	/**
	 * 会话服务交换机-下线队列
	 */
	String QUEUE_SESSION_OFFLINE = "q.session.offline";
	/**
	 * 会话服务交换机-下线路由
	 */
	String RK_SESSION_OFFLINE = "rk.session.offline";

}
