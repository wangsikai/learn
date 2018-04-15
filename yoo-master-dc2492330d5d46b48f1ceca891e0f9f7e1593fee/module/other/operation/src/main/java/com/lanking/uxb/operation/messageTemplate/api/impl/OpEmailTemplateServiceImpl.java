package com.lanking.uxb.operation.messageTemplate.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.EmailTemplate;
import com.lanking.uxb.operation.messageTemplate.api.OpEmailTemplateService;

@Transactional(readOnly = true)
@Service
public class OpEmailTemplateServiceImpl implements OpEmailTemplateService {

	@Autowired
	@Qualifier("EmailTemplateRepo")
	private Repo<EmailTemplate, Integer> emailTemplateRepo;
	private int start_code = 11000000;

	@Override
	public List<EmailTemplate> findAll() {
		return emailTemplateRepo.getAll();
	}

	@Override
	public EmailTemplate get(int code) {
		return emailTemplateRepo.get(code);
	}

	@Transactional
	@Override
	public EmailTemplate update(int code, String title, String body, String note) {
		EmailTemplate emailTemplate = emailTemplateRepo.get(code);
		emailTemplate.setTitle(title);
		emailTemplate.setBody(body);
		emailTemplate.setNote(note);
		return emailTemplateRepo.save(emailTemplate);
	}

	@Transactional
	@Override
	public EmailTemplate create(String title, String body, String note) {
		EmailTemplate emailTemplate = new EmailTemplate();
		EmailTemplate max = emailTemplateRepo.find("$opGetMaxCode").get();
		if (max == null) {
			emailTemplate.setCode(start_code);
		} else {
			emailTemplate.setCode(max.getCode() + 1);
		}
		emailTemplate.setTitle(title);
		emailTemplate.setBody(body);
		emailTemplate.setNote(note);
		return emailTemplateRepo.save(emailTemplate);
	}
}
