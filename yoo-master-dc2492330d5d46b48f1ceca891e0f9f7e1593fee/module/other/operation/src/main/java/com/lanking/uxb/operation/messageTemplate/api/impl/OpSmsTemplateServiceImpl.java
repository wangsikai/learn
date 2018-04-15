package com.lanking.uxb.operation.messageTemplate.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.SmsTemplate;
import com.lanking.uxb.operation.messageTemplate.api.OpSmsTemplateService;

@Transactional(readOnly = true)
@Service
public class OpSmsTemplateServiceImpl implements OpSmsTemplateService {

	@Autowired
	@Qualifier("SmsTemplateRepo")
	private Repo<SmsTemplate, Integer> smsTemplateRepo;
	private int start_code = 10000000;

	@Override
	public List<SmsTemplate> findAll() {
		return smsTemplateRepo.getAll();
	}

	@Override
	public SmsTemplate get(int code) {
		return smsTemplateRepo.get(code);
	}

	@Transactional
	@Override
	public SmsTemplate update(int code, String body, String note) {
		SmsTemplate smsTemplate = smsTemplateRepo.get(code);
		smsTemplate.setBody(body);
		smsTemplate.setNote(note);
		return smsTemplateRepo.save(smsTemplate);
	}

	@Transactional
	@Override
	public SmsTemplate create(String body, String note) {
		SmsTemplate smsTemplate = new SmsTemplate();
		SmsTemplate max = smsTemplateRepo.find("$opGetMaxCode").get();
		if (max == null) {
			smsTemplate.setCode(start_code);
		} else {
			smsTemplate.setCode(max.getCode() + 1);
		}
		smsTemplate.setBody(body);
		smsTemplate.setNote(note);
		return smsTemplateRepo.save(smsTemplate);
	}

	@Transactional
	@Override
	public SmsTemplate mock(int code) {
		SmsTemplate smsTemplate = smsTemplateRepo.get(code);
		smsTemplate.setMock(true);
		return smsTemplateRepo.save(smsTemplate);
	}

	@Transactional
	@Override
	public SmsTemplate unmock(int code) {
		SmsTemplate smsTemplate = smsTemplateRepo.get(code);
		smsTemplate.setMock(false);
		return smsTemplateRepo.save(smsTemplate);
	}

}
