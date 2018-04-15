package com.lanking.uxb.service.message.api.impl.sms;

import java.util.Collection;
import java.util.Map;

public interface SmsSender {

	int send(String mobile, String content);

	int send(Collection<String> mobiles, String content);

	String send(String mobile, String signName, String smsTemplateCode, Map<String, Object> params);

	String send(Collection<String> mobiles, String signName, String smsTemplateCode, Map<String, Object> params);
}
