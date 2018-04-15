package com.lanking.cloud.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqActivityRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.session.api.SessionConstants;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.job.nationalDayActivity.service.ActivityHomeworkService;
import com.lanking.cloud.job.nationalDayActivity.service.PVUVService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Exchange(name = MqActivityRegistryConstants.EX_NDA01)
public class NationalDayActivity01Listener {

	@Autowired
	private MqSender mqSender;
	@Autowired
	@Qualifier("nda01PVUVService")
	private PVUVService pvuvService;
	@Autowired
	@Qualifier("nda01ActivityHomeworkService")
	private ActivityHomeworkService activityHomeworkService;

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_PVUV, routingKey = MqActivityRegistryConstants.RK_NDA01_PVUV)
	public void pvuv(JSONObject jo) {
		try {
			Long userId = jo.getLong("userId");
			NationalDayActivity01H5 h5 = jo.getObject("h5", NationalDayActivity01H5.class);
			Date viewAt = jo.getDate("viewAt");
			pvuvService.viewPage(h5, userId == null ? SessionConstants.GUEST_ID : userId, viewAt);
		} catch (Exception e) {
			log.error("pvuv error:", e);
		}
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ)
	public void dq(JSONObject jo) {
		try {
			long studentId = jo.getLongValue("studentId");
			long mode = studentId % 11;
			mqSender.send(MqActivityRegistryConstants.EX_NDA01, MqActivityRegistryConstants.RK_NDA01_DQ + "." + mode,
					MQ.builder().data(jo).build());
		} catch (Exception e) {
			log.error("distribute do question right count error:", e);
		}
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_HK_PUBDEL, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_PUBDEL)
	public void publishORDeleteHomework(JSONObject jo) {
		try {
			String action = jo.getString("action");
			if ("publish".equals(action)) {
				activityHomeworkService.publishHomework(jo.getLongValue("teacherId"), jo.getLongValue("homeworkId"));
			} else if ("delete".equals(action)) {
				activityHomeworkService.deleteHomework(jo.getLongValue("homeworkId"));
			}
		} catch (Exception e) {
			log.error("publish|delete homework error:", e);
		}
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_HK_ISSUE, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_ISSUE)
	public void issueHomework(JSONObject jo) {
		try {
			long homeworkId = jo.getLongValue("homeworkId");
			long mode = homeworkId % 3;
			mqSender.send(MqActivityRegistryConstants.EX_NDA01,
					MqActivityRegistryConstants.RK_NDA01_DQ_ISSUE + "." + mode, MQ.builder().data(jo).build());
		} catch (Exception e) {
			log.error("distribute issue homework error:", e);
		}

	}
}
