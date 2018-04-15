package com.lanking.cloud.component.mq.common.constants;

/**
 * 悠数学-作业-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqYoomathHomeworkRegistryConstants {
	/**
	 * 作业交换机
	 */
	String EX_YM_HOMEWORK = "ex.ym.homework";
	/**
	 * 布置队列
	 */
	String QUEUE_YM_HOMEWORK_PUBLISH = "q.ym.homework.publish";
	/**
	 * 布置路由
	 */
	String RK_YM_HOMEWORK_PUBLISH = "rk.ym.homework.publish";
	/**
	 * 提交队列
	 */
	String QUEUE_YM_HOMEWORK_COMMIT = "q.ym.homework.commit";
	/**
	 * 提交路由
	 */
	String RK_YM_HOMEWORK_COMMIT = "rk.ym.homework.commit";
}
