package com.lanking.uxb.service.payment.weixin.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.ex.core.IllegalArgFormatException;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.client.WXPaymentClient;
import com.lanking.uxb.service.payment.weixin.response.OrderQueryResult;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 微信支付相关接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
@Service
public class WXPaymentServiceImpl implements WXPaymentService {
	@Autowired
	private WXPaymentClient wxPaymentClient;

	@Override
	public UnifiedPayResult unifiedPayOrder(String appid, OrderPayBusinessSpace space, String productTitle,
			String attach, String businessID, String spbill_create_ip, int total_fee, String trade_type, String open_id)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, Exception {
		if (StringUtils.isNotBlank(attach) && attach.length() > 100) {
			throw new IllegalArgFormatException("attach");
		} else {
			attach = "";
		}
		JSONObject json = new JSONObject();
		json.put("b", space.getValue());
		json.put("a", attach);
		// String att = "<![CDATA[" + json.toJSONString() + "]]>";
		String att = json.toJSONString();
		return wxPaymentClient.unifiedPayOrder(wxPaymentClient.getConfigure(appid), productTitle, att, businessID,
				spbill_create_ip, total_fee, trade_type, open_id);
	}

	@Override
	public OrderQueryResult orderQuery(String appid, String transactionID, String outTradeNo) throws Exception {
		return wxPaymentClient.orderQuery(wxPaymentClient.getConfigure(appid), transactionID, outTradeNo);
	}
}
