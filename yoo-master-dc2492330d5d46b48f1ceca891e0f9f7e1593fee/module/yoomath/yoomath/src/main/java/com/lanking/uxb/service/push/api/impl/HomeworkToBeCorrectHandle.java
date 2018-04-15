package com.lanking.uxb.service.push.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.session.Device;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.push.type.OpenPath;
import com.lanking.uxb.service.push.util.YmPushUrls;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.session.api.DeviceService;

@Component
public class HomeworkToBeCorrectHandle extends AbstractPushHandle {

	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private MessageSender messageSender;

	@Override
	public boolean accept(PushHandleAction action) {
		return action == PushHandleAction.HOMEWORK_TO_BE_CORRECT;
	}

	@Override
	public void push(PushHandleForm form) {
		Homework homework = homeworkService.get(form.getHomeworkId());
		List<Device> devices = deviceService.findByUserId(homework.getCreateId(), Product.YOOMATH);
		if (CollectionUtils.isNotEmpty(devices)) {
			List<String> tokens = new ArrayList<String>(devices.size());
			for (Device device : devices) {
				tokens.add(device.getToken());
			}
			messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_TEACHER, tokens, 12000011, null, YmPushUrls
					.url(YooApp.MATH_TEACHER, OpenPath.HOMEWORK_DETAIL, ValueMap.value("id", form.getHomeworkId())),
					ValueMap.value("homeworkName", homework.getName())));
		}
	}

}
