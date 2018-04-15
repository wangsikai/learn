package com.lanking.uxb.service.youngyedu.syncdata.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathYoungyeduRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.user.api.UserChannelService;
import com.lanking.uxb.service.youngyedu.syncdata.api.YoungyeduHttpClient;
import com.lanking.uxb.service.youngyedu.syncdata.api.YoungyeduSyncDataService;

/**
 * 融捷提醒更新调用接口
 *
 * @author xinyu.zhou
 * @since 3.0.3
 */
@RestController
@RequestMapping(value = "router/youngyedu/ym/sync")
public class ZyMYoungyeduSyncController {

	@Autowired
	private YoungyeduSyncDataService dataService;
	@Autowired
	private YoungyeduHttpClient client;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private UserChannelService ucService;

	/**
	 * 提醒进行同步操作
	 *
	 * @param id
	 *            同步id
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "noticeSync", method = { RequestMethod.GET, RequestMethod.POST })
	public Value noticeSync(String id) {
		UserChannel uc = ucService.findByName(UserChannel.YOUNGEDU);
		Integer channelCode = uc == null ? null : uc.getCode();

		if (channelCode == null) {
			client.updateVersion(id, false);
			return new Value(new IllegalArgException());
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("classId", id);
		jsonObject.put("channelCode", channelCode);

		mqSender.asynSend(MqYoomathYoungyeduRegistryConstants.EX_YM_YOUNGEDU,
				MqYoomathYoungyeduRegistryConstants.RK_YM_YOUNGEDU_SYNC, MQ.builder().data(jsonObject).build());

		client.updateVersion(id, true);

		return new Value();
	}

}
