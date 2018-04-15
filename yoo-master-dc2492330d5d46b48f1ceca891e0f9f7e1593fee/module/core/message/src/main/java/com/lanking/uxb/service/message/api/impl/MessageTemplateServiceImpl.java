package com.lanking.uxb.service.message.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.EmailTemplate;
import com.lanking.cloud.domain.base.message.NoticeTemplate;
import com.lanking.cloud.domain.base.message.PushTemplate;
import com.lanking.cloud.domain.base.message.SmsTemplate;
import com.lanking.uxb.service.message.api.MessageTemplateService;

@Transactional(readOnly = true)
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

	@Autowired
	@Qualifier("SmsTemplateRepo")
	Repo<SmsTemplate, Integer> smsTemplateRepo;
	@Autowired
	@Qualifier("EmailTemplateRepo")
	Repo<EmailTemplate, Integer> emailTemplateRepo;
	@Autowired
	@Qualifier("PushTemplateRepo")
	Repo<PushTemplate, Integer> pushTemplateRepo;
	@Autowired
	@Qualifier("NoticeTemplateRepo")
	Repo<NoticeTemplate, Integer> noticeTemplateRepo;

	@Override
	public SmsTemplate getSmsTemplate(int code) {
		return smsTemplateRepo.get(code);
	}

	@Override
	public EmailTemplate getEmailTemplate(int code) {
		return emailTemplateRepo.get(code);
	}

	@Override
	public PushTemplate getPushTemplate(int code) {
		return pushTemplateRepo.get(code);
	}

	@Override
	public NoticeTemplate getNoticeTemplate(int code) {
		return noticeTemplateRepo.get(code);
	}

}
