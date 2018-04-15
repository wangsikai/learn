package com.lanking.cloud.component.mq.common.constants;

/**
 * 悠数学-计数明细-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqYoomathCounterDetailRegistryConstants {
	/**
	 * 计数明细交换机
	 */
	String EX_YM_COUNTERDETAIL = "ex.ym.counterdetail";
	/**
	 * 计数明细交换机-questiondetail队列
	 */
	String QUEUE_YM_COUNTERDETAIL_QUESTIONUSER = "q.ym.counterdetail.questiondetail";
	/**
	 * 计数明细交换机-questiondetail路由
	 */
	String RK_YM_COUNTERDETAIL_QUESTIONUSER = "rk.ym.counterdetail.questiondetail";
}
