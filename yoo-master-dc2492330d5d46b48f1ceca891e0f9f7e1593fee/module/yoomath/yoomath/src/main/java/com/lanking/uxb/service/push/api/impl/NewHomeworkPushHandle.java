package com.lanking.uxb.service.push.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

@Component
public class NewHomeworkPushHandle extends AbstractPushHandle {

	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkClassGroupStudentService classGroupStudentService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private MessageSender messageSender;

	@Override
	public boolean accept(PushHandleAction action) {
		return action == PushHandleAction.NEW_HOMEWORK;
	}

	@Override
	public void push(PushHandleForm form) {
		Homework homework = homeworkService.get(form.getHomeworkId());
		if (homework.getStatus() == HomeworkStatus.PUBLISH) {
			List<Long> studentIds = new ArrayList<Long>();
			// 2017.11.13新增分组布置作业，查询组内的学生
			if (homework.getHomeworkClassGroupId() != null && homework.getHomeworkClassGroupId() != 0) {
				studentIds = classGroupStudentService.findGroupStudents(homework.getHomeworkClassId(),
						homework.getHomeworkClassGroupId());
			} else {
				studentIds = hkStuClazzService.listClassStudents(homework.getHomeworkClassId());
			}
			if (CollectionUtils.isNotEmpty(studentIds)) {
				List<String> tokens = deviceService.findTokenByUserIds(studentIds, Product.YOOMATH);
				if (CollectionUtils.isNotEmpty(tokens)) {
					messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_STUDENT, tokens, 12000001, ValueMap
							.value("p", OpenPath.HOMEWORK_HOME.getName()), YmPushUrls.url(YooApp.MATH_STUDENT,
							OpenPath.HOMEWORK_HOME, null), ValueMap.value("homeworkName", homework.getName())));
				}
			}
		}
	}
}
