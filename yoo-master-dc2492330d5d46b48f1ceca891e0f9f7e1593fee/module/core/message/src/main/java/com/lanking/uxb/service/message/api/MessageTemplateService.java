package com.lanking.uxb.service.message.api;

import com.lanking.cloud.domain.base.message.EmailTemplate;
import com.lanking.cloud.domain.base.message.NoticeTemplate;
import com.lanking.cloud.domain.base.message.PushTemplate;
import com.lanking.cloud.domain.base.message.SmsTemplate;

/**
 * 消息模板service
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月29日
 */
public interface MessageTemplateService {

	SmsTemplate getSmsTemplate(int code);

	EmailTemplate getEmailTemplate(int code);

	PushTemplate getPushTemplate(int code);

	NoticeTemplate getNoticeTemplate(int code);
}
