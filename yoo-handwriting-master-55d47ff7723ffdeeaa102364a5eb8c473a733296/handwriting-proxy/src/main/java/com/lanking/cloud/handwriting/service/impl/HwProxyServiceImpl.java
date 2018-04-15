package com.lanking.cloud.handwriting.service.impl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.handwriting.DAO.HandwritingRecordDAO;
import com.lanking.cloud.handwriting.config.HwProxyProperties;
import com.lanking.cloud.handwriting.form.HwRequest;
import com.lanking.cloud.handwriting.form.HwResponse;
import com.lanking.cloud.handwriting.service.HwProxyService;

@Service
@Configuration
public class HwProxyServiceImpl implements HwProxyService {

	@Autowired
	@Qualifier("hwProxyHandwritingRecordDAO")
	private HandwritingRecordDAO hwrDAO;
	@Autowired
	private HwProxyProperties hwProxyProperties;
	@Autowired
	private HttpClient httpClient;

	@Override
	public HwResponse proxy(HwRequest hwRequest) throws ClientProtocolException, IOException {
		HwResponse hwResponse = null;
		StringEntity entity = new StringEntity(JSONObject.toJSONString(hwRequest), "UTF-8");
		HttpPost httpPost = new HttpPost(hwProxyProperties.getProxy());
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.setEntity(entity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int status = httpResponse.getStatusLine().getStatusCode();
		String response = EntityUtils.toString(httpResponse.getEntity());
		if (status == 200) {
			hwResponse = JSONObject.parseObject(response, HwResponse.class);
		}
		return hwResponse;

	}

	@Transactional
	@Override
	public void record(HwRequest hwRequest) {
		hwrDAO.create(hwRequest.getId(), hwRequest.getScg_ink());
	}

	@Transactional
	@Override
	public void response(HwResponse hwResponse) {
		hwrDAO.response(hwResponse.getId(), JSONObject.toJSONString(hwResponse));
	}
}
