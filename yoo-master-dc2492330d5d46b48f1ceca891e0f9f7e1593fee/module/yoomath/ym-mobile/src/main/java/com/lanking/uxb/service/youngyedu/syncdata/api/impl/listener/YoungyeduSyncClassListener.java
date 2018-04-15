package com.lanking.uxb.service.youngyedu.syncdata.api.impl.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathYoungyeduRegistryConstants;
import com.lanking.uxb.service.youngyedu.syncdata.api.YoungyeduHttpClient;
import com.lanking.uxb.service.youngyedu.syncdata.api.YoungyeduSyncDataService;
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduUser;

/**
 * 融捷同步班级数据task
 *
 * @author xinyu.zhou
 * @since 3.0.4
 */
@Component
@Exchange(name = MqYoomathYoungyeduRegistryConstants.EX_YM_YOUNGEDU)
public class YoungyeduSyncClassListener {

	private Logger logger = LoggerFactory.getLogger(YoungyeduSyncClassListener.class);
	@Autowired
	private YoungyeduHttpClient client;
	@Autowired
	private YoungyeduSyncDataService dataService;

	@Listener(queue = MqYoomathYoungyeduRegistryConstants.QUEUE_YM_YOUNGEDU_SYNC, routingKey = MqYoomathYoungyeduRegistryConstants.RK_YM_YOUNGEDU_SYNC)
	public void syncClass(JSONObject mq) {
		String classId = mq.getString("classId");
		Integer channelCode = mq.getInteger("channelCode");
		try {
			logger.info("youngedut sync data: {}", mq.toString());

			List<YoungyeduUser> users = client.querySyncUsers(classId);
			dataService.sync(users, channelCode);
		} catch (Exception e) {
			logger.error("younyedu sync class data has error:{}", e);
		}
	}
}
