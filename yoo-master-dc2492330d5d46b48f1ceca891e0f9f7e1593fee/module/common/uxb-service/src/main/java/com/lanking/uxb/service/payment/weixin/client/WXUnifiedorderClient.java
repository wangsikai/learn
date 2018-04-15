package com.lanking.uxb.service.payment.weixin.client;

import com.lanking.uxb.service.payment.weixin.request.UnifiedPayData;
import com.tencent.common.Configure;
import com.tencent.service.BaseService;

/**
 * 统一下单服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
public class WXUnifiedorderClient extends BaseService {

	public WXUnifiedorderClient(Configure configure, String api) throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		super(api, configure);
	}

	/**
	 * 发送请求.
	 * 
	 * @param unifiedPayData
	 *            提交数据
	 * @return
	 * @throws Exception
	 */
	public String request(Configure configure, UnifiedPayData unifiedPayData) throws Exception {
		String responseString = sendPost(unifiedPayData, configure);
		return responseString;
	}
}
