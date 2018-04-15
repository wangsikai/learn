package com.lanking.cloud.component.mq.common.constants;

/**
 * 悠数学-错题-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqYoomathFallibleRegistryConstants {

	/**
	 * 错题相关(task模块使用)交换机
	 */
	String EX_YM_FALLIBLE_TASK = "ex.ym.fallible.task";
	/**
	 * 错题相关(ocr-search后触发,task模块使用)-队列
	 */
	String QUEUE_YM_FALLIBLE_TASK_OCRSEARCH = "q.ym.fallible.task.ocrsearch";
	/**
	 * 错题相关(ocr-search后触发,task模块使用)-路由
	 */
	String RK_YM_FALLIBLE_TASK_OCRSEARCH = "rk.ym.fallible.task.ocrsearch";
	/**
	 * 错题相关(教师错题触发,task模块使用)-队列
	 */
	String QUEUE_YM_FALLIBLE_TASK_TEACHER = "q.ym.fallible.task.teacher";
	/**
	 * 错题相关(教师错题触发,task模块使用)-路由
	 */
	String RK_YM_FALLIBLE_TASK_TEACHER = "rk.ym.fallible.task.teacher";
	/**
	 * 错题相关(学生错题触发,task模块使用)-队列
	 */
	String QUEUE_YM_FALLIBLE_TASK_STUDENT = "q.ym.fallible.task.student";
	/**
	 * 错题相关(学生错题触发,task模块使用)-路由
	 */
	String RK_YM_FALLIBLE_TASK_STUDENT = "rk.ym.fallible.task.student";

}
