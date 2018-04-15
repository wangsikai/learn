package com.lanking.uxb.service.message.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.Push;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.message.api.PushService;

@Transactional(readOnly = true)
@Service
public class PushServiceImpl implements PushService {

	@Autowired
	@Qualifier("PushRepo")
	Repo<Push, Long> pushRepo;

	@Transactional(readOnly = false)
	@Override
	public void save(PushPacket packet) {
		Date now = new Date();
		if (CollectionUtils.isNotEmpty(packet.getTargets())) {
			for (String target : packet.getTargets()) {
				Push push = new Push();
				push.setTarget(target);
				push.setTemplateCode(packet.getMessageTemplateCode());
				push.setTitle(packet.getTitle());
				push.setBody(packet.getBody());
				push.setCreateAt(packet.getCreateAt());
				push.setRet(packet.getRet());
				push.setRetMsg(packet.getRetMsgs().get(target));
				push.setSendAt(packet.getSendAt());
				push.setSaveAt(now);
				pushRepo.save(push);
			}
		} else {
			Push push = new Push();
			push.setTarget(packet.getTarget());
			push.setTemplateCode(packet.getMessageTemplateCode());
			push.setTitle(packet.getTitle());
			push.setBody(packet.getBody());
			push.setCreateAt(packet.getCreateAt());
			push.setRet(packet.getRet());
			push.setRetMsg(packet.getRetMsg());
			push.setSendAt(packet.getSendAt());
			push.setSaveAt(now);
			pushRepo.save(push);
		}

	}

}
