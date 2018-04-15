package com.lanking.cloud.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqActivityRegistryConstants;
import com.lanking.cloud.job.nationalDayActivity.service.ActivityHomeworkService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Exchange(name = MqActivityRegistryConstants.EX_NDA01)
public class NationalDayActivity01IssueInternalListener {

	@Qualifier("nda01ActivityHomeworkService")
	@Autowired
	private ActivityHomeworkService activityHomeworkService;

	void internalIssueHomework(JSONObject jo) {
		try {
			activityHomeworkService.issueHomework(jo.getLongValue("homeworkId"));
		} catch (Exception e) {
			log.error("internal issue homework error:", e);
		}
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_HK_ISSUE_0, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_ISSUE_0)
	public void issueHomework0(JSONObject jo) {
		internalIssueHomework(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_HK_ISSUE_1, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_ISSUE_1)
	public void issueHomework1(JSONObject jo) {
		internalIssueHomework(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_HK_ISSUE_2, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_ISSUE_2)
	public void issueHomework2(JSONObject jo) {
		internalIssueHomework(jo);
	}
}
