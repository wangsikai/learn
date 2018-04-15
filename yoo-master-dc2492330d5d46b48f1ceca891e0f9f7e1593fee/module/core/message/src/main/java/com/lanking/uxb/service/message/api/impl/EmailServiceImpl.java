package com.lanking.uxb.service.message.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.Email;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.message.api.EmailService;

@Transactional(readOnly = true)
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	@Qualifier("EmailRepo")
	private Repo<Email, Long> emailRepo;

	@Transactional(readOnly = false)
	@Override
	public void save(EmailPacket packet) {
		Date now = new Date();
		if (CollectionUtils.isNotEmpty(packet.getTargets())) {
			for (String target : packet.getTargets()) {
				Email email = new Email();
				email.setTarget(target);
				email.setTemplateCode(packet.getMessageTemplateCode());
				email.setTitle(packet.getTitle());
				email.setBody(packet.getBody());
				email.setCreateAt(packet.getCreateAt());
				email.setRet(packet.getRet());
				email.setSendAt(packet.getSendAt());
				email.setSaveAt(now);
				emailRepo.save(email);
			}
		} else {
			Email email = new Email();
			email.setTarget(packet.getTarget());
			email.setTemplateCode(packet.getMessageTemplateCode());
			email.setTitle(packet.getTitle());
			email.setBody(packet.getBody());
			email.setCreateAt(packet.getCreateAt());
			email.setRet(packet.getRet());
			email.setSendAt(packet.getSendAt());
			email.setSaveAt(now);
			emailRepo.save(email);
		}

	}

}
