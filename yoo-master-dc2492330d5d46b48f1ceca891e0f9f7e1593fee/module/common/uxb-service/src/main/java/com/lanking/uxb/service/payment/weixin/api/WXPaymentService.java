package com.lanking.uxb.service.payment.weixin.api;

import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.response.OrderQueryResult;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 微信支付相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
public interface WXPaymentService {

	/**
	 * 统一下单接口（获得二维码URL）.
	 * 
	 * @param space
	 *            业务唯一标识，用于与支付结果provider处理器对应.
	 * @param productTitle
	 *            商品描述
	 * @param attach
	 *            附加数据，支付结果会原样返回，长度不得超过100位
	 * @param businessID
	 *            业务ID，本地商品或订单ID
	 * @param spbill_create_ip
	 *            客户机IP地址
	 * @param total_fee
	 *            订单总额，单位分，整数
	 * @param trade_type
	 *            JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
	 * @param open_id
	 *            微信用户的OPenID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	UnifiedPayResult unifiedPayOrder(String appid, OrderPayBusinessSpace space, String productTitle, String attach,
			String businessID, String spbill_create_ip, int total_fee, String trade_type, String open_id)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, Exception;

	/**
	 * 查询订单.
	 * 
	 * @param transactionID
	 *            微信端订单号
	 * @param outTradeNo
	 *            商户订单号
	 * @return
	 */
	OrderQueryResult orderQuery(String appid, String transactionID, String outTradeNo) throws Exception;
}
