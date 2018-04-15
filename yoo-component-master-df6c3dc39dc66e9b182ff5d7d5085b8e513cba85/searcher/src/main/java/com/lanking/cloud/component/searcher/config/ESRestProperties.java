package com.lanking.cloud.component.searcher.config;

import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class ESRestProperties extends ESProperties {

	private String protocol = "http";

	public RestClient initRestClient() throws NumberFormatException, UnknownHostException {
		List<ESNode> esNodes = getESNode();
		HttpHost[] httpHosts = new HttpHost[esNodes.size()];
		int idx = 0;
		for (ESNode node : esNodes) {
			httpHosts[idx] = new HttpHost(node.getHost(), node.getPort(), protocol);
			idx++;
		}
		RestClientBuilder builder = RestClient.builder(httpHosts);
		return builder.build();
	}
}
