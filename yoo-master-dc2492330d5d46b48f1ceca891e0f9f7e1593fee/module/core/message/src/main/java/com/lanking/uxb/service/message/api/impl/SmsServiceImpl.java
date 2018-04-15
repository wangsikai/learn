package com.lanking.uxb.service.message.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.Sms;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.message.api.SmsService;

@Transactional(readOnly = true)
@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	@Qualifier("SmsRepo")
	Repo<Sms, Long> smsRepo;

	@Transactional(readOnly = false)
	@Override
	public void save(SmsPacket packet) {
		Date now = new Date();
		if (CollectionUtils.isNotEmpty(packet.getTargets())) {
			for (String target : packet.getTargets()) {
				Sms sms = new Sms();
				sms.setTarget(target);
				sms.setTemplateCode(packet.getMessageTemplateCode());
				sms.setBody(packet.getBody());
				sms.setCreateAt(packet.getCreateAt());
				sms.setRet(packet.getRet());
				sms.setCallRet(packet.getCallRet());
				sms.setSendAt(packet.getSendAt());
				sms.setSaveAt(now);
				smsRepo.save(sms);
			}
		} else {
			Sms sms = new Sms();
			sms.setTarget(packet.getTarget());
			sms.setTemplateCode(packet.getMessageTemplateCode());
			sms.setBody(packet.getBody());
			sms.setCreateAt(packet.getCreateAt());
			sms.setRet(packet.getRet());
			sms.setCallRet(packet.getCallRet());
			sms.setSendAt(packet.getSendAt());
			sms.setSaveAt(now);
			smsRepo.save(sms);
		}

	}

}
