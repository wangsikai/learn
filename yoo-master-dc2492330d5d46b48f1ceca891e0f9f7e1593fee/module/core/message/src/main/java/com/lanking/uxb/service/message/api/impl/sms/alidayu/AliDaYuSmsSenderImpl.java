package com.lanking.uxb.service.message.api.impl.sms.alidayu;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.message.api.impl.sms.SmsSender;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

@Component
public class AliDaYuSmsSenderImpl implements SmsSender {

	private Logger logger = LoggerFactory.getLogger(AliDaYuSmsSenderImpl.class);

	private String uri;
	private String appkey;
	private String secret;

	@PostConstruct
	void init() {
		uri = Env.getString("sms.alidayu.uri");
		appkey = Env.getString("sms.alidayu.appkey");
		secret = Env.getString("sms.alidayu.secret");
	}

	@Override
	public int send(String mobile, String content) {
		return -1;
	}

	@Override
	public int send(Collection<String> mobiles, String content) {
		return -1;
	}

	@Override
	public String send(String mobile, String signName, String smsTemplateCode, Map<String, Object> params) {
		return send(Lists.newArrayList(mobile), signName, smsTemplateCode, params);
	}

	@Override
	public String send(Collection<String> mobiles, String signName, String smsTemplateCode, Map<String, Object> params) {
		TaobaoClient client = new DefaultTaobaoClient(uri, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName);
		if (params.size() > 0) {
			JSONObject jsonObject = new JSONObject();
			for (String key : params.keySet()) {
				jsonObject.put(key, params.get(key).toString());
			}
			req.setSmsParamString(jsonObject.toString());
		}
		req.setRecNum(CollectionUtils.listToString(mobiles, ","));
		req.setSmsTemplateCode(smsTemplateCode);
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			return rsp.getBody();
		} catch (ApiException e) {
			logger.error("send sms error:", e);
			return "-1";
		}
	}
}
