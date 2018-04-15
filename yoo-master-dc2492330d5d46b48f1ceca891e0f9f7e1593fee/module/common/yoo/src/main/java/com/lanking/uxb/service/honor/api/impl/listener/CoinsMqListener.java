package com.lanking.uxb.service.honor.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.uxb.service.honor.api.CoinsService;

@Component
@Exchange(name = MqHonorRegistryConstants.EX_COINS)
public class CoinsMqListener {

	private Logger logger = LoggerFactory.getLogger(CoinsMqListener.class);

	@Autowired
	private CoinsService coinsService;

	@Listener(queue = MqHonorRegistryConstants.QUEUE_COINS_LOG, routingKey = MqHonorRegistryConstants.RK_COINS_LOG)
	public void coinsLog(JSONObject json) {
		try {
			CoinsAction action = json.getObject("action", CoinsAction.class);
			long userId = json.getLongValue("userId");
			if (json.containsKey("biz")) {
				int coinsValue = json.getIntValue("coinsValue");
				Biz biz = json.getObject("biz", Biz.class);
				long bizId = json.getLongValue("bizId");
				coinsService.earn(action, userId, coinsValue, biz, bizId);
			} else {
				coinsService.earn(action, userId);
			}
		} catch (Exception e) {
			logger.error("coins log:", e);
		}
	}
}
