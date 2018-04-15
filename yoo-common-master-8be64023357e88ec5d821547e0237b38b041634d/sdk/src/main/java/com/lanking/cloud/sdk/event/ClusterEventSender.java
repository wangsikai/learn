package com.lanking.cloud.sdk.event;

public interface ClusterEventSender {

	@SuppressWarnings("rawtypes")
	void send(ClusterEvent event);
}
