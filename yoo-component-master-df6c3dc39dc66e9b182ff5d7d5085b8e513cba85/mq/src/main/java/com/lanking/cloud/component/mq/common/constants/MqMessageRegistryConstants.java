package com.lanking.cloud.component.mq.common.constants;

/**
 * 消息-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqMessageRegistryConstants {

	/**
	 * 消息服务交换机
	 */
	String EX_MSG = "ex.msg";
	/**
	 * 消息服务交换机-邮件队列
	 */
	String QUEUE_MSG_EMAIL = "q.msg.email";
	/**
	 * 消息服务交换机-邮件路由
	 */
	String RK_MSG_EMAIL = "rk.msg.email";
	/**
	 * 消息服务交换机-短信队列
	 */
	String QUEUE_MSG_SMS = "q.msg.sms";
	/**
	 * 消息服务交换机-短信路由
	 */
	String RK_MSG_SMS = "rk.msg.sms";
	/**
	 * 消息服务交换机-提醒队列
	 */
	String QUEUE_MSG_NOTICE = "q.msg.notice";
	/**
	 * 消息服务交换机-提醒路由
	 */
	String RK_MSG_NOTICE = "rk.msg.notice";
	/**
	 * 消息服务交换机-推送队列
	 */
	String QUEUE_MSG_PUSH = "q.msg.push";
	/**
	 * 消息服务交换机-推送路由
	 */
	String RK_MSG_PUSH = "rk.msg.push";
	/**
	 * 消息服务交换机-消息保存队列
	 */
	String QUEUE_MSG_SAVE = "q.msg.save";
	/**
	 * 消息服务交换机-消息保存路由
	 */
	String RK_MSG_SAVE = "rk.msg.save";
	/**
	 * 消息服务交换机-模板队列
	 */
	String QUEUE_MSG_TEMPLATE = "q.msg.template";
	/**
	 * 消息服务交换机-模板路由
	 */
	String RK_MSG_TEMPLATE = "rk.msg.template";

}
