package com.lanking.cloud.component.mq.common.constants;

/**
 * 搜索-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqSearchRegistryConstants {

	/**
	 * 搜索数据交换机
	 */
	String EX_SEARCH = "ex.search";
	/**
	 * 搜索数据交换机-关键字采集队列
	 */
	String QUEUE_SEARCH_WORD = "q.search.word";
	/**
	 * 搜索数据交换机-关键字采集路由
	 */
	String RK_SEARCH_WORD = "rk.search.word";

}
