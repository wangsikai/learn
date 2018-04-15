package com.lanking.uxb.operation.messageTemplate.api;

import java.util.List;

import com.lanking.cloud.domain.base.message.SmsTemplate;

public interface OpSmsTemplateService {

	List<SmsTemplate> findAll();

	SmsTemplate get(int code);

	SmsTemplate update(int code, String body, String note);

	SmsTemplate create(String body, String note);

	SmsTemplate mock(int code);

	SmsTemplate unmock(int code);
}
