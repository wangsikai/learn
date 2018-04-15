package com.lanking.uxb.operation.messageTemplate.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.PushTemplate;
import com.lanking.uxb.operation.messageTemplate.api.OpPushTemplateService;

@Transactional(readOnly = true)
@Service
public class OpPushTemplateServiceImpl implements OpPushTemplateService {

	@Autowired
	@Qualifier("PushTemplateRepo")
	private Repo<PushTemplate, Integer> pushTemplateRepo;
	private int start_code = 12000000;

	@Override
	public List<PushTemplate> findAll() {
		return pushTemplateRepo.getAll();
	}

	@Override
	public PushTemplate get(int code) {
		return pushTemplateRepo.get(code);
	}

	@Transactional
	@Override
	public PushTemplate update(int code, String title, String body, String note) {
		PushTemplate pushTemplate = pushTemplateRepo.get(code);
		pushTemplate.setTitle(title);
		pushTemplate.setBody(body);
		pushTemplate.setNote(note);
		return pushTemplateRepo.save(pushTemplate);
	}

	@Transactional
	@Override
	public PushTemplate create(String title, String body, String note) {
		PushTemplate pushTemplate = new PushTemplate();
		PushTemplate max = pushTemplateRepo.find("$opGetMaxCode").get();
		if (max == null) {
			pushTemplate.setCode(start_code);
		} else {
			pushTemplate.setCode(max.getCode() + 1);
		}
		pushTemplate.setTitle(title);
		pushTemplate.setBody(body);
		pushTemplate.setNote(note);
		return pushTemplateRepo.save(pushTemplate);
	}

}
