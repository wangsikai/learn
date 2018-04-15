package com.lanking.cloud.sdk.httpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

public class HttpClientFactoryBean implements FactoryBean<HttpClient>, InitializingBean {

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 20;
	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 30 * 1000;

	private int maxTotalConnections = DEFAULT_MAX_TOTAL_CONNECTIONS * 2;
	private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE * 5;
	private int soTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;
	private int connectionTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;
	private boolean disableCookie = true;
	private Map<String, String> defaultHeaders;

	private HttpClient httpClient;

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
		this.maxConnectionsPerRoute = maxConnectionsPerRoute;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setTimeout(int timeout) {
		setSoTimeout(timeout);
		setConnectionTimeout(timeout);
	}

	public void setDisableCookie(boolean disableCookie) {
		this.disableCookie = disableCookie;
	}

	public void setDefaultHeaders(Map<String, String> defaultParams) {
		this.defaultHeaders = defaultParams;
	}

	@Override
	public HttpClient getObject() throws Exception {
		return httpClient;
	}

	@Override
	public Class<?> getObjectType() {
		return HttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setMaxConnTotal(maxTotalConnections);
		builder.setMaxConnPerRoute(maxConnectionsPerRoute);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout)
				.setConnectTimeout(connectionTimeout).build();
		builder.setDefaultRequestConfig(requestConfig);

		if (disableCookie) {
			builder.disableCookieManagement();
		}

		if (!CollectionUtils.isEmpty(defaultHeaders)) {
			List<Header> headers = new ArrayList<>();
			for (Map.Entry<String, String> entry : this.defaultHeaders.entrySet()) {
				BasicHeader header = new BasicHeader(entry.getKey(), entry.getValue());
				headers.add(header);
			}
			builder.setDefaultHeaders(headers);
		}
		httpClient = builder.build();
	}
}
