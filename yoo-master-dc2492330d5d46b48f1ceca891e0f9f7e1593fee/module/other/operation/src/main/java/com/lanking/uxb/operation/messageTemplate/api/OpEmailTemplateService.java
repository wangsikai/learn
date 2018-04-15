package com.lanking.uxb.operation.messageTemplate.api;

import java.util.List;

import com.lanking.cloud.domain.base.message.EmailTemplate;

public interface OpEmailTemplateService {

	List<EmailTemplate> findAll();

	EmailTemplate get(int code);

	EmailTemplate update(int code, String title, String body, String note);

	EmailTemplate create(String title, String body, String note);
}
