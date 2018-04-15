package com.lanking.uxb.service.ranking.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqJobServiceRegistryConstants;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.push.type.OpenPath;
import com.lanking.uxb.service.push.util.YmPushUrls;
import com.lanking.uxb.service.session.api.DeviceService;

/**
 * 排行榜 Listener
 *
 * @author peng.zhao
 * @since 1.4.7
 */
@Component
@Exchange(name = MqJobServiceRegistryConstants.EX_JOBS)
public class RankingMqListener {

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private DeviceService deviceService;

	/**
	 * 给首次进入排行榜的用户发推送
	 * 
	 * @param obj
	 */
	@Listener(queue = MqJobServiceRegistryConstants.QUEUE_JOBS_RANKING_PUSH, routingKey = MqJobServiceRegistryConstants.RK_JOBS_RANKING_PUSH)
	public void sendPush(JSONObject obj) {
		String[] userIdStrs = obj.getString("userIds").split(",");
		Long classId = obj.getLong("classId");

		List<Long> userIds = Lists.newArrayList();
		for (int i = 0; i < userIdStrs.length; i++) {
			userIds.add(Long.valueOf(userIdStrs[i]));
		}

		List<String> tokens = deviceService.findTokenByUserIds(userIds, Product.YOOMATH);
		if (CollectionUtils.isNotEmpty(tokens)) {
			messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_STUDENT, tokens, 12000000,
					ValueMap.value("p", OpenPath.RANKING_CLASS2.getName()).put("classId", String.valueOf(classId)),
					YmPushUrls.url(YooApp.MATH_STUDENT, OpenPath.RANKING_CLASS2,
							ValueMap.value("classId", String.valueOf(classId)))));
		}
	}
}
