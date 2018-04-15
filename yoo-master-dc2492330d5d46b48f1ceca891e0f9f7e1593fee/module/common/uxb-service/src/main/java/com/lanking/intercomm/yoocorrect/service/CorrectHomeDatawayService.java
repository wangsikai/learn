package com.lanking.intercomm.yoocorrect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.client.CorrectHomeDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.CorrectConfigData;
import com.lanking.intercomm.yoocorrect.dto.CorrectHomePageData;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 首页数据.
 * 
 * @author wanlong.che
 *
 */
@Component
public class CorrectHomeDatawayService {

	@Autowired
	private CorrectHomeDatawayClient correctHomeDatawayClient;

	public CorrectHomePageData getHomeData(long userId) {
		CorrectHomePageData data = null;
		Value value = correctHomeDatawayClient.getHomeData(userId);
		if (value.getRet_code() == 0 && value.getRet() != null) {
			data = JSON.parseObject(value.getRet().toString(), CorrectHomePageData.class);
		} else {
			throw new ZuoyeException(value.getRet_code());
		}
		return data;
	}

	/**
	 * 获取配置数据.
	 * 
	 * @return
	 */
	public CorrectConfigData getConfig() {
		CorrectConfigData configData = null;
		Value value = correctHomeDatawayClient.getConfig();
		if (value.getRet_code() == 0 && value.getRet() != null) {
			configData = JSON.parseObject(value.getRet().toString(), CorrectConfigData.class);
		} else {
			throw new ZuoyeException(value.getRet_code());
		}

		return configData;
	}
}
