package com.lanking.uxb.service.message.api.impl.provider;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.lanking.cloud.component.mq.common.constants.MqMessageRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.uxb.service.message.api.MessageTemplateService;
import com.lanking.uxb.service.message.api.SendProvider;
import com.lanking.uxb.service.message.cache.MessageTemplateCacheService;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
public abstract class AbstractSendProvider implements SendProvider {

	@Autowired
	MessageTemplateService messageTemplateService;
	@Autowired
	MessageTemplateCacheService messageTemplateCacheService;
	@Autowired
	private MqSender mqSender;

	private Logger logger = LoggerFactory.getLogger(AbstractSendProvider.class);
	private Configuration templateCfg = null;
	private StringTemplateLoader stringLoader = null;

	{
		templateCfg = new Configuration();
		templateCfg.setDefaultEncoding("UTF-8");
		stringLoader = new StringTemplateLoader();
		templateCfg.setTemplateLoader(stringLoader);
	}

	@Override
	public void refreshTemplate() {
		messageTemplateCacheService.clearCache(getType());
		templateCfg = new Configuration();
		templateCfg.setDefaultEncoding("UTF-8");
		stringLoader = new StringTemplateLoader();
		templateCfg.setTemplateLoader(stringLoader);
	}

	abstract String getTitleTemplate(int code) throws Exception;

	abstract String getBodyTemplate(int code) throws Exception;

	abstract boolean isMock(int code) throws Exception;

	private String getTemplateName(TemplateType type, int code) {
		return new StringBuilder(type.name()).append(code).toString();
	}

	void afterSend(MessagePacket packet) {
		packet.setSendAt(new Date());
		mqSender.send(MqMessageRegistryConstants.EX_MSG, MqMessageRegistryConstants.RK_MSG_SAVE,
				MQ.builder().data(packet).build());
	}

	String getTitle(int code, Map<String, Object> model) {
		Template template = null;
		String name = getTemplateName(TemplateType.TITLE, code);
		try {
			template = templateCfg.getTemplate(name);
		} catch (IOException e) {
			logger.info("template [{}] is not exist!", name);
		}
		try {
			if (template == null) {
				stringLoader.putTemplate(name, getTitleTemplate(code));
			}
			templateCfg.clearTemplateCache();
			template = templateCfg.getTemplate(name);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch (Exception e) {
			logger.info("template [{}] is not exist!", name);
			return null;
		}
	}

	String getBody(int code, Map<String, Object> model) {
		Template template = null;
		String name = getTemplateName(TemplateType.BODY, code);
		try {
			template = templateCfg.getTemplate(name);
		} catch (IOException e) {
			logger.info("template [{}] is not exist!", name);
		}
		try {
			if (template == null) {
				stringLoader.putTemplate(name, getBodyTemplate(code));
			}
			templateCfg.clearTemplateCache();
			template = templateCfg.getTemplate(name);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch (Exception e) {
			logger.info("template [{}] is not exist!", name);
			return null;
		}
	}

	enum TemplateType {
		TITLE, BODY;
	}
}
