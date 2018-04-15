package com.lanking.uxb.operation.messageTemplate.api;

import java.util.List;

import com.lanking.cloud.domain.base.message.NoticeTemplate;

public interface OpNoticeTemplateService {

	List<NoticeTemplate> findAll();

	NoticeTemplate get(int code);

	NoticeTemplate update(int code, String title, String body, String note);

	NoticeTemplate create(String title, String body, String note);
}
