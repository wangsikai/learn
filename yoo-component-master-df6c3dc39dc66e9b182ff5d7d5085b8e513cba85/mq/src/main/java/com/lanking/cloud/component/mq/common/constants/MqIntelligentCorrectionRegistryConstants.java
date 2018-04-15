package com.lanking.cloud.component.mq.common.constants;

/**
 * 智能批改-相关MQ定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public interface MqIntelligentCorrectionRegistryConstants {
	/**
	 * 智能批改服务交换机
	 */
	String EX_INTELLIGENTCORRECTION = "ex.intelligentCorrection";
	/**
	 * 智能批改交换机-答案归档队列
	 */
	String QUEUE_INTELLIGENTCORRECTION_ARCHIVE = "q.intelligentCorrection.archive";
	/**
	 * 智能批改交换机-答案归档路由
	 */
	String RK_INTELLIGENTCORRECTION_ARCHIVE = "rk.intelligentCorrection.archive";
}
