package com.lanking.uxb.service.message.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.base.message.EmailTemplate;
import com.lanking.cloud.domain.base.message.NoticeTemplate;
import com.lanking.cloud.domain.base.message.PushTemplate;
import com.lanking.cloud.domain.base.message.SmsTemplate;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 消息模板缓存
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月7日
 */
@Service
public class MessageTemplateCacheService extends AbstractCacheService {

	private HashOperations<String, Integer, SmsTemplate> smsTemplateHashOp;
	private HashOperations<String, Integer, EmailTemplate> emailTemplateHashOp;
	private HashOperations<String, Integer, PushTemplate> pushTemplateHashOp;
	private HashOperations<String, Integer, NoticeTemplate> noticeTemplateHashOp;

	private String SMS_TEMPLATE_KEY = "sms";
	private String EMAIL_TEMPLATE_KEY = "email";
	private String PUSH_TEMPLATE_KEY = "push";
	private String NOTICE_TEMPLATE_KEY = "notice";

	public void putSmsTemplate(SmsTemplate smsTemplate) {
		smsTemplateHashOp.put(assemblyKey(SMS_TEMPLATE_KEY), smsTemplate.getCode(), smsTemplate);
	}

	public SmsTemplate getSmsTemplate(int code) {
		return smsTemplateHashOp.get(assemblyKey(SMS_TEMPLATE_KEY), code);
	}

	public void putEmailTemplate(EmailTemplate emailTemplate) {
		emailTemplateHashOp.put(assemblyKey(EMAIL_TEMPLATE_KEY), emailTemplate.getCode(), emailTemplate);
	}

	public EmailTemplate getEmailTemplate(int code) {
		return emailTemplateHashOp.get(assemblyKey(EMAIL_TEMPLATE_KEY), code);
	}

	public void putPushTemplate(PushTemplate pushTemplate) {
		pushTemplateHashOp.put(assemblyKey(PUSH_TEMPLATE_KEY), pushTemplate.getCode(), pushTemplate);
	}

	public PushTemplate getPushTemplate(int code) {
		return pushTemplateHashOp.get(assemblyKey(PUSH_TEMPLATE_KEY), code);
	}

	public void putNoticeTemplate(NoticeTemplate noticeTemplate) {
		noticeTemplateHashOp.put(assemblyKey(NOTICE_TEMPLATE_KEY), noticeTemplate.getCode(), noticeTemplate);
	}

	public NoticeTemplate getNoticeTemplate(int code) {
		return noticeTemplateHashOp.get(assemblyKey(NOTICE_TEMPLATE_KEY), code);
	}

	@SuppressWarnings("unchecked")
	public void clearCache(MessageType messageType) {
		if (messageType == MessageType.EMAIL) {
			getRedisTemplate().expire(assemblyKey(EMAIL_TEMPLATE_KEY), 3, TimeUnit.SECONDS);
		} else if (messageType == MessageType.PUSH) {
			getRedisTemplate().expire(assemblyKey(PUSH_TEMPLATE_KEY), 3, TimeUnit.SECONDS);
		} else if (messageType == MessageType.NOTICE) {
			getRedisTemplate().expire(assemblyKey(NOTICE_TEMPLATE_KEY), 3, TimeUnit.SECONDS);
		} else if (messageType == MessageType.SMS) {
			getRedisTemplate().expire(assemblyKey(SMS_TEMPLATE_KEY), 3, TimeUnit.SECONDS);
		}
	}

	@Override
	public String getNs() {
		return "lddp-msg-t";
	}

	@Override
	public String getNsCn() {
		return "平台-消息模板缓存";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		smsTemplateHashOp = getRedisTemplate().opsForHash();
		emailTemplateHashOp = getRedisTemplate().opsForHash();
		pushTemplateHashOp = getRedisTemplate().opsForHash();
		noticeTemplateHashOp = getRedisTemplate().opsForHash();
	}

}
