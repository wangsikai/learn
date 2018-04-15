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
import com.lanking.cloud.domain.base.message.SmsTemplate;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.operation.messageTemplate.api.OpSmsTemplateService;

/**
 * 短信模板管理
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年6月7日
 */
@RestController
@RequestMapping(value = "op/messageTemplate/sms")
public class OpSmsTemplateController {

	@Autowired
	private OpSmsTemplateService smsTemplateService;
	@Autowired
	private MqSender mqSender;

	@RequestMapping(value = "listAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value listAll() {
		List<SmsTemplate> items = smsTemplateService.findAll();
		ValueMap vm = ValueMap.value("items", items);
		return new Value(vm);
	}

	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(String body, String note) {
		SmsTemplate template = smsTemplateService.create(body, note);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "update", method = { RequestMethod.GET, RequestMethod.POST })
	public Value update(int code, String body, String note) {
		SmsTemplate template = smsTemplateService.update(code, body, note);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "mock", method = { RequestMethod.GET, RequestMethod.POST })
	public Value mock(int code) {
		SmsTemplate template = smsTemplateService.mock(code);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "unmock", method = { RequestMethod.GET, RequestMethod.POST })
	public Value unmock(int code) {
		SmsTemplate template = smsTemplateService.unmock(code);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "takeEffect", method = { RequestMethod.GET, RequestMethod.POST })
	public Value takeEffect() {
		JSONObject object = new JSONObject();
		object.put("action", "clearCache");
		object.put("messageType", MessageType.SMS);
		mqSender.send(MqMessageRegistryConstants.EX_MSG, MqMessageRegistryConstants.RK_MSG_TEMPLATE,
				MQ.builder().data(object).build());
		return new Value();
	}
}
