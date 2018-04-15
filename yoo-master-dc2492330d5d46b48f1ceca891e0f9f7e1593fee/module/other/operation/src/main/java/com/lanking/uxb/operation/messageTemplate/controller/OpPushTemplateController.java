package com.lanking.uxb.operation.messageTemplate.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqMessageRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.PushTemplate;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.operation.messageTemplate.api.OpPushTemplateService;

/**
 * 推送模板管理
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年6月7日
 */
@RestController
@RequestMapping(value = "op/messageTemplate/push")
public class OpPushTemplateController {

	@Autowired
	private OpPushTemplateService pushTemplateService;
	@Autowired
	private MqSender mqSender;

	@RequestMapping(value = "listAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value listAll() {
		List<PushTemplate> items = pushTemplateService.findAll();
		ValueMap vm = ValueMap.value("items", items);
		return new Value(vm);
	}

	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(String title, String body, String note) {
		PushTemplate template = pushTemplateService.create(title, body, note);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "update", method = { RequestMethod.GET, RequestMethod.POST })
	public Value update(int code, String title, String body, String note) {
		PushTemplate template = pushTemplateService.update(code, title, body, note);
		ValueMap vm = ValueMap.value("template", template);
		return new Value(vm);
	}

	@RequestMapping(value = "takeEffect", method = { RequestMethod.GET, RequestMethod.POST })
	public Value takeEffect() {
		JSONObject object = new JSONObject();
		object.put("action", "clearCache");
		object.put("messageType", MessageType.PUSH);
		mqSender.send(MqMessageRegistryConstants.EX_MSG, MqMessageRegistryConstants.RK_MSG_TEMPLATE,
				MQ.builder().data(object).build());
		return new Value();
	}

	@RequestMapping(value = "pushByTokens", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pushByTokens(Product product, YooApp app, String[] tokens, String title, String body, String url) {
		List<String> targets = Arrays.asList(tokens);
		PushPacket packet = new PushPacket();
		packet.setProduct(product);
		packet.setApp(app);
		packet.setTargets(targets);
		packet.setTitle(title);
		packet.setBody(body);
		packet.setParams(ValueMap.value("url", url));
		mqSender.send(MqMessageRegistryConstants.EX_MSG, MqMessageRegistryConstants.RK_MSG_PUSH,
				MQ.builder().data(packet).build());
		return new Value();
	}

}
