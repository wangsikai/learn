package com.lanking.cloud.component.mq.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqInternalDelayConstants;

@Component
@Exchange(name = MqInternalDelayConstants.EX_INTERNAL_DELAY, delayed = true)
public class DelayQueue {

	@Autowired
	private MqSender mqSender;

	@Listener(queue = MqInternalDelayConstants.QUEUE_INTERNAL_DELAY, routingKey = MqInternalDelayConstants.RK_INTERNAL_DELAY, series = false)
	public void forward(DelayMQ mq) {
		mqSender.send(mq.getEx(), mq.getRk(), MQ.builder().data(mq.getData()).build());
	}
}