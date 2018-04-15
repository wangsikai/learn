package com.lanking.cloud.component.mq.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqDataRegistryConstants;
import com.lanking.cloud.sdk.event.ClusterEvent;

/**
 * 集群数据同步监听
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@Component
@Exchange(name = MqDataRegistryConstants.CLUSTER_DATA_EXCHANGE, type = ExchangeTypes.FANOUT)
public class MqClusterDataListener implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(MqClusterDataListener.class);

	private ApplicationContext appCtx;

	@SuppressWarnings("rawtypes")
	@Listener(queue = "${cluster.data}", queueExclusive = true)
	public void clusterData(ClusterEvent clusterEvent) {
		try {
			logger.info("cluster data mq:{}", clusterEvent.toString());
			appCtx.publishEvent(clusterEvent);
		} catch (Exception e) {
			logger.error("publish event fail:", e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;
	}
}
