package com.lanking.uxb.operation.messageTemplate.api;

import java.util.List;

import com.lanking.cloud.domain.base.message.PushTemplate;

public interface OpPushTemplateService {

	List<PushTemplate> findAll();

	PushTemplate get(int code);

	PushTemplate update(int code, String title, String body, String note);

	PushTemplate create(String title, String body, String note);
}
