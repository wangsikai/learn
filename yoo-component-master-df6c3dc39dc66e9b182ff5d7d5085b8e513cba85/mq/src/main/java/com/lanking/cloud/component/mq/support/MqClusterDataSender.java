package com.lanking.cloud.component.mq.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.mq.common.constants.MqDataRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;

@Component("clusterDataSender")
public class MqClusterDataSender implements ClusterEventSender {

	@Autowired
	private MqSender mqSender;

	@SuppressWarnings("rawtypes")
	@Override
	public void send(ClusterEvent event) {
		mqSender.send(MqDataRegistryConstants.CLUSTER_DATA_EXCHANGE, MQ.builder().data(event).build());
	}

}
