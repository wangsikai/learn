package com.lanking.cloud.component.mq.common.constants;

/**
 * 悠数学-融捷-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqYoomathYoungyeduRegistryConstants {
	/**
	 * 融捷相关交换机
	 */
	String EX_YM_YOUNGEDU = "ex.ym.youngedu";
	/**
	 * 数据同步队列
	 */
	String QUEUE_YM_YOUNGEDU_SYNC = "q.ym.youngedu.sync";
	/**
	 * 数据同步路由
	 */
	String RK_YM_YOUNGEDU_SYNC = "rk.ym.youngedu.sync";
}
