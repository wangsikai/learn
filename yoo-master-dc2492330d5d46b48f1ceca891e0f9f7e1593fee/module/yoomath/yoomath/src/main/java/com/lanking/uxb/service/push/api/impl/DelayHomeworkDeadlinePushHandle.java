package com.lanking.uxb.service.push.api.impl;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.HomeworkStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
@Component
public class DelayHomeworkDeadlinePushHandle extends AbstractPushHandle {
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private DeviceService deviceService;

	@Override
	public boolean accept(PushHandleAction action) {
		return action == PushHandleAction.DELAY_HOMEWORK_DEADLINE;
	}

	@Override
	public void push(PushHandleForm form) {
		Homework homework = homeworkService.get(form.getHomeworkId());
		List<String> tokens = deviceService
		        .findTokenByUserIds(Lists.newArrayList(homework.getCreateId()), Product.YOOMATH);
		if (CollectionUtils.isNotEmpty(tokens)) {
			messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_TEACHER, tokens,
					12000022, ValueMap.value("p", OpenPath.HOMEWORK_HOME.getName()),
			        YmPushUrls.url(YooApp.MATH_TEACHER, OpenPath.HOMEWORK_DETAIL,
			                ValueMap.value("id", homework.getId())),
			        ValueMap.value("homeworkName", homework.getName())));
		}
	}
}
