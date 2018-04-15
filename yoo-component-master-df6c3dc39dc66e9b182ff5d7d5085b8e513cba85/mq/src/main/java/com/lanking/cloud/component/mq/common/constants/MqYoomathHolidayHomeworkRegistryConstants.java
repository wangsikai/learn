package com.lanking.cloud.component.mq.common.constants;

/**
 * 悠数学-假期作业-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqYoomathHolidayHomeworkRegistryConstants {
	/**
	 * 假期作业交换机
	 */
	String EX_YM_HOLIDAYHOMEWORK = "ex.ym.holidayhomework";

	/**
	 * 布置队列
	 */
	String QUEUE_YM_HOLIDAYHOMEWORK_PUBLISH = "q.ym.holidayhomework.publish";
	/**
	 * 布置路由
	 */
	String RK_YM_HOLIDAYHOMEWORK_PUBLISH = "rk.ym.holidayhomework.publish";
	/**
	 * 提交队列
	 */
	String QUEUE_YM_HOLIDAYHOMEWORK_COMMIT = "q.ym.holidayhomework.commit";
	/**
	 * 提交路由
	 */
	String RK_YM_HOLIDAYHOMEWORK_COMMIT = "rk.ym.holidayhomework.commit";
}
