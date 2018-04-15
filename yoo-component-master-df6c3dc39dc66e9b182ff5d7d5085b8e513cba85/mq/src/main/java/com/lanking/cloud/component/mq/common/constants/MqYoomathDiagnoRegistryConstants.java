package com.lanking.cloud.component.mq.common.constants;

/**
 * 悠数学-诊断-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqYoomathDiagnoRegistryConstants {

	/**
	 * 教学诊断、学习诊断(task模块使用)交换机
	 */
	String EX_YM_DIAGNO_TASK = "ex.ym.diagno.task";
	/**
	 * 教学诊断(作业下发后触发,task模块使用)-队列
	 */
	String QUEUE_YM_DIAGNO_TASK_CLASS_HOMEWORK = "q.ym.diagno.task.class.hk";
	/**
	 * 教学诊断(作业下发后触发,task模块使用)-路由
	 */
	String RK_YM_DIAGNO_TASK_CLASS_HOMEWORK = "rk.ym.diagno.task.class.hk";
	/**
	 * 教学诊断(处理班级增减量统计,task模块使用)-路由
	 */
	String RK_YM_DIAGNO_TASK_CLASS_INCR = "rk.ym.diagno.task.class.incr";
	/**
	 * 教学诊断(处理班级增减量统计,task模块使用)-队列
	 */
	String QUEUE_YM_DIAGNO_TASK_CLASS_INCR = "q.ym.diagno.task.class.incr";
	/**
	 * 学习诊断(作业下发后触发,task模块使用,针对最近30次)-队列
	 */
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK = "q.ym.diagno.task.student.hk";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_0 = "q.ym.diagno.task.student.hk.0";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_1 = "q.ym.diagno.task.student.hk.1";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_2 = "q.ym.diagno.task.student.hk.2";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_3 = "q.ym.diagno.task.student.hk.3";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_4 = "q.ym.diagno.task.student.hk.4";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_5 = "q.ym.diagno.task.student.hk.5";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_6 = "q.ym.diagno.task.student.hk.6";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_7 = "q.ym.diagno.task.student.hk.7";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_8 = "q.ym.diagno.task.student.hk.8";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_9 = "q.ym.diagno.task.student.hk.9";
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_10 = "q.ym.diagno.task.student.hk.10";
	/**
	 * 学习诊断(作业下发后触发,task模块使用,针对最近30次)-路由
	 */
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK = "rk.ym.diagno.task.student.hk";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_0 = "rk.ym.diagno.task.student.hk.0";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_1 = "rk.ym.diagno.task.student.hk.1";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_2 = "rk.ym.diagno.task.student.hk.2";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_3 = "rk.ym.diagno.task.student.hk.3";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_4 = "rk.ym.diagno.task.student.hk.4";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_5 = "rk.ym.diagno.task.student.hk.5";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_6 = "rk.ym.diagno.task.student.hk.6";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_7 = "rk.ym.diagno.task.student.hk.7";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_8 = "rk.ym.diagno.task.student.hk.8";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_9 = "rk.ym.diagno.task.student.hk.9";
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_10 = "rk.ym.diagno.task.student.hk.10";
	/**
	 * 学习诊断(作业下发后触发,task模块使用,针对所有的包括历史)-队列
	 */
	String QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_ALL = "q.ym.diagno.task.student.all";
	/**
	 * 学习诊断(作业下发后触发,task模块使用,针对所有的包括历史)-路由
	 */
	String RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_ALL = "rk.ym.diagno.task.student.all";
	/**
	 * 学习诊断(学生离开班级,task模块使用)-队列
	 */
	String QUEUE_YM_DIAGNO_TASK_STUDENT_LEAVE = "q.ym.diagno.task.student.leave";
	/**
	 * 学习诊断(学生离开班级,task模块使用)-路由
	 */
	String RK_YM_DIAGNO_TASK_STUDENT_LEAVE = "rk.ym.diagno.task.student.leave";
	/**
	 * 学习诊断(学生加入班级,task模块使用)-队列
	 */
	String QUEUE_YM_DIAGNO_TASK_STUDENT_JOIN = "q.ym.diagno.task.student.join";
	/**
	 * 学习诊断(学生加入班级,task模块使用)-路由
	 */
	String RK_YM_DIAGNO_TASK_STUDENT_JOIN = "rk.ym.diagno.task.student.join";

}
