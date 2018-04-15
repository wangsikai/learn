package com.lanking.uxb.service.message.api.impl.provider;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.message.Notice;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.base.message.api.NoticePacket;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.message.api.NoticeService;
import com.lanking.uxb.service.message.ex.MessageException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月18日
 *
 */
@Component
public class NoticeSendProvider extends AbstractSendProvider {

	private Logger logger = LoggerFactory.getLogger(NoticeSendProvider.class);

	@Autowired
	private NoticeService noticeService;

	@Override
	public boolean accept(MessageType messageType) {
		return messageType == getType();
	}

	@Override
	public MessageType getType() {
		return MessageType.NOTICE;
	}

	@Override
	String getTitleTemplate(int code) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getBodyTemplate(int code) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean isMock(int code) throws Exception {
		throw new Exception();
	}

	@Override
	public void send(MessagePacket packet) throws MessageException {
		try {
			NoticePacket noticePacket = (NoticePacket) packet;
			logger.info("receive mq packet:{}", noticePacket.toString());
			if (CollectionUtils.isNotEmpty(noticePacket.getTos())) {
				for (Long to : noticePacket.getTos()) {
					Notice notice = new Notice();
					notice.setBiz(noticePacket.getBiz());
					notice.setBizId(noticePacket.getBizId());
					notice.setBody(noticePacket.getBody());
					notice.setCatgory(noticePacket.getCatgory());
					notice.setCreateAt(new Date());
					notice.setNoticeId(noticePacket.getFrom());
					notice.setStatus(Status.ENABLED);
					notice.setType(noticePacket.getType());
					notice.setUid(to);
					noticeService.create(notice);
				}
			} else {
				Notice notice = new Notice();
				notice.setBiz(noticePacket.getBiz());
				notice.setBizId(noticePacket.getBizId());
				notice.setBody(noticePacket.getBody());
				notice.setCatgory(noticePacket.getCatgory());
				notice.setCreateAt(new Date());
				notice.setNoticeId(noticePacket.getFrom());
				notice.setStatus(Status.ENABLED);
				notice.setType(noticePacket.getType());
				notice.setUid(noticePacket.getTo());
				noticeService.create(notice);
			}
		} catch (Exception e) {
			logger.error("send notice fail:", e);
		}
	}
}
