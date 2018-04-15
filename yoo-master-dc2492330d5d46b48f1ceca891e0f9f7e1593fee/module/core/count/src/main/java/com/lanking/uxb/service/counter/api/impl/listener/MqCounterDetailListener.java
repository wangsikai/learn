package com.lanking.uxb.service.counter.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathCounterDetailRegistryConstants;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.uxb.service.counter.api.impl.QuestionUserCouterProvider;

@Component
@Exchange(name = MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL)
public class MqCounterDetailListener {

	private Logger logger = LoggerFactory.getLogger(MqCounterDetailListener.class);

	@Autowired
	private QuestionUserCouterProvider questionUserCouterProvider;

	@Listener(queue = MqYoomathCounterDetailRegistryConstants.QUEUE_YM_COUNTERDETAIL_QUESTIONUSER, routingKey = MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER)
	public void questionUserDetail(JSONObject json) {
		try {
			long bizId = json.getLongValue("bizId");
			long otherBizId = json.getLongValue("otherBizId");
			Count count = json.getObject("count", Count.class);
			int delta = json.getIntValue("delta");
			questionUserCouterProvider.counterDetail(bizId, otherBizId, count, delta);
		} catch (Exception e) {
			logger.error("question user detail error:", e);
		}
	}
}