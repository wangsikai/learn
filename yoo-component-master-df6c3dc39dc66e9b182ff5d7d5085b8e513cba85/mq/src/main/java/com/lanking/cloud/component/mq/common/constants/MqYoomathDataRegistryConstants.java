package com.lanking.cloud.component.mq.common.constants;

/**
 * 悠数学-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqYoomathDataRegistryConstants {

	/**
	 * 数据服务交换机
	 */
	String EX_YM_DATA = "ex.ym.data";
	/**
	 * 数据服务交换机-学生作业在班级里面的统计数据处理队列
	 */
	String QUEUE_YM_DATA_HOMEWORKSTUDENTCLAZZSTAT = "q.ym.data.hkstuclazzstat";
	/**
	 * 数据服务交换机-学生作业在班级里面的统计数据处理路由
	 */
	String RK_YM_DATA_HOMEWORKSTUDENTCLAZZSTAT = "rk.ym.data.hkstuclazzstat";

	/**
	 * 数据服务交换机-班级作业知识点数据处理队列
	 */
	String QUEUE_YM_DATA_HKCLAZZKNOWPOINTSTAT = "q.ym.data.hkclazzknowpointstat";
	/**
	 * 数据服务交换机-班级作业知识点数据处理路由
	 */
	String RK_YM_DATA_HKCLAZZKNOWPOINTSTAT = "rk.ym.data.hkclazzknowpointstat";
	/**
	 * 数据服务交换机-班级学生作业知识点数据处理队列
	 */
	String QUEUE_YM_DATA_HKSTUCLAZZKNOWPOINTSTAT = "q.ym.data.hkstuclazzknowpointstat";
	/**
	 * 数据服务交换机-班级学生作业知识点数据处理路由
	 */
	String RK_YM_DATA_HKSTUCLAZZKNOWPOINTSTAT = "rk.ym.data.hkstuclazzknowpointstat";

	/**
	 * 数据服务交换机-学生练习(做作业、每日练、章节练习、错题练习、智能试卷)后数据处理队列
	 */
	String QUEUE_YM_DATA_STUDENTEXERCISE = "q.ym.data.stuexercise";
	/**
	 * 数据服务交换机-学生练习(做作业、每日练、章节练习、错题练习、智能试卷)后数据处理路由
	 */
	String RK_YM_DATA_STUDENTEXERCISE = "rk.ym.data.stuexercise";

	/**
	 * 数据服务交换机-学生练习(做作业、每日练、章节练习、错题练习、智能试卷)后数据处理队列
	 */
	String QUEUE_YM_DATA_DOQUESTIONGOAL = "q.ym.data.doquestiongoal";
	/**
	 * 数据服务交换机-学生练习(做作业、每日练、章节练习、错题练习、智能试卷)后数据处理路由
	 */
	String RK_YM_DATA_DOQUESTIONGOAL = "rk.ym.data.doquestiongoal";

	/**
	 * 数据服务交换机-学生错题统计处理队列
	 */
	String QUEUE_YM_DATA_STUDENTFALLIBLE = "q.ym.data.studentfallible";
	/**
	 * 数据服务交换机-学生错题统计处理路由
	 */
	String RK_YM_DATA_STUDENTFALLIBLE = "rk.ym.data.studentfallible";

}
