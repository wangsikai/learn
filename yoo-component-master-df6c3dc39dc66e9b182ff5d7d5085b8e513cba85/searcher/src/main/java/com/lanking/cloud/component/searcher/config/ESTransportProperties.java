package com.lanking.cloud.component.searcher.config;

import java.net.UnknownHostException;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class ESTransportProperties extends ESProperties {

	/**
	 * 是否自动嗅探
	 */
	private boolean sniff = true;

	public Client initTransportClient() throws NumberFormatException, UnknownHostException {
		Settings.Builder builder = Settings.builder();
		builder.put("client.transport.sniff", sniff);
		if (getCluster() != null) {
			builder.put("cluster.name", getCluster());
		}
		TransportClient client = new PreBuiltTransportClient(builder.build());
		List<ESNode> esNodes = getESNode();
		for (ESNode node : esNodes) {
			client.addTransportAddress(new InetSocketTransportAddress(node.getAddress(), node.getPort()));
		}
		return client;
	}
}
