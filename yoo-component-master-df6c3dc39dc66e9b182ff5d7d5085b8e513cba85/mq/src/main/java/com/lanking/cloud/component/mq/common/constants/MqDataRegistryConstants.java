package com.lanking.cloud.component.mq.common.constants;

/**
 * 数据-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqDataRegistryConstants {

	/**
	 * 集群数据交换机
	 */
	String CLUSTER_DATA_EXCHANGE = "ex.cluster.data";

	/**
	 * 数据服务交换机
	 */
	String EX_DATA = "ex.data";
	/**
	 * 数据服务交换机-api日志队列
	 */
	String QUEUE_DATA_APILOG = "q.data.apilog";
	/**
	 * 数据服务交换机-api日志路由
	 */
	String RK_DATA_APILOG = "rk.data.apilog";

}
