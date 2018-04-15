package com.lanking.uxb.service.message.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.Notice;
import com.lanking.uxb.service.message.api.NoticeService;

@Service
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	@Qualifier("NoticeRepo")
	Repo<Notice, Long> noticeRepo;

	@Transactional
	@Override
	public Notice create(Notice notice) {
		notice.setCreateAt(new Date());
		return noticeRepo.save(notice);
	}

}
