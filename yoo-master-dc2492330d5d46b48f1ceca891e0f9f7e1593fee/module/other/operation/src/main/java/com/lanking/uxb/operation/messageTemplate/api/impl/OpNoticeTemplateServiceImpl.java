package com.lanking.uxb.operation.messageTemplate.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.NoticeTemplate;
import com.lanking.uxb.operation.messageTemplate.api.OpNoticeTemplateService;

@Transactional(readOnly = true)
@Service
public class OpNoticeTemplateServiceImpl implements OpNoticeTemplateService {

	@Autowired
	@Qualifier("NoticeTemplateRepo")
	private Repo<NoticeTemplate, Integer> noticeTemplateRepo;
	private final int start_code = 13000000;

	@Override
	public List<NoticeTemplate> findAll() {
		return noticeTemplateRepo.getAll();
	}

	@Override
	public NoticeTemplate get(int code) {
		return noticeTemplateRepo.get(code);
	}

	@Transactional
	@Override
	public NoticeTemplate update(int code, String title, String body, String note) {
		NoticeTemplate noticeTemplate = noticeTemplateRepo.get(code);
		noticeTemplate.setTitle(title);
		noticeTemplate.setBody(body);
		noticeTemplate.setNote(note);
		return noticeTemplateRepo.save(noticeTemplate);
	}

	@Transactional
	@Override
	public NoticeTemplate create(String title, String body, String note) {
		NoticeTemplate noticeTemplate = new NoticeTemplate();
		NoticeTemplate max = noticeTemplateRepo.find("$opGetMaxCode").get();
		if (max == null) {
			noticeTemplate.setCode(start_code);
		} else {
			noticeTemplate.setCode(max.getCode() + 1);
		}
		noticeTemplate.setTitle(title);
		noticeTemplate.setBody(body);
		noticeTemplate.setNote(note);
		return noticeTemplateRepo.save(noticeTemplate);
	}

}
