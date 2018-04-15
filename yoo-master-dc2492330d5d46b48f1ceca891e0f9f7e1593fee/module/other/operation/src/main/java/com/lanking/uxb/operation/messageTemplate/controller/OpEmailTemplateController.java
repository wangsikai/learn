package com.lanking.uxb.operation.messageTemplate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqMessageRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.EmailTemplate;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.operation.messageTemplate.api.OpEmailTemplateService;

/**
 * 邮件模板管理
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年6月7日
 */
@RestController
@RequestMapping(value = "op/messageTemplate/email")
public class OpEmailTemplateController {

	@Autowired
	private OpEmailTemplateService emailTemplateService;
	@Autowired
	private MqSender mqSender;

	@RequestMapping(value = "listAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value listAll() {
		List<EmailTemplate> items = emailTemplateService.findAll();
		ValueMap vm = ValueMap.value("items", items);
		return new Value(vm);
	}

	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(String title, String body, String note) {
		EmailTemplate template = emailTemplateService.create(title, body, note);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "update", method = { RequestMethod.GET, RequestMethod.POST })
	public Value update(int code, String title, String body, String note) {
		EmailTemplate template = emailTemplateService.update(code, title, body, note);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "takeEffect", method = { RequestMethod.GET, RequestMethod.POST })
	public Value takeEffect() {
		JSONObject object = new JSONObject();
		object.put("action", "clearCache");
		object.put("messageType", MessageType.EMAIL);
		mqSender.send(MqMessageRegistryConstants.EX_MSG, MqMessageRegistryConstants.RK_MSG_TEMPLATE,
				MQ.builder().data(object).build());
		return new Value();
	}
}
