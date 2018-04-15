package com.tencent.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import com.tencent.common.Configure;
import com.tencent.common.HttpsRequest;

/**
 * User: rizenguo Date: 2014/12/10 Time: 15:44 服务的基类
 */
public class BaseService {

	// API的地址
	private String apiURL;

	// 发请求的HTTPS请求器
	private HttpsRequest serviceRequest;

	public BaseService(String api, Configure configure) throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		apiURL = api;
		try {
			serviceRequest = new HttpsRequest(configure);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String sendPost(Object xmlObj, Configure configure) throws UnrecoverableKeyException, IOException,
			NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		return serviceRequest.sendPost(apiURL, xmlObj, configure);
	}
}
