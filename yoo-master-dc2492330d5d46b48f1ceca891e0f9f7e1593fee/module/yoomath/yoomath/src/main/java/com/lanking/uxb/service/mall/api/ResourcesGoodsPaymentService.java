package com.lanking.uxb.service.mall.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 资源商品支付接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
public interface ResourcesGoodsPaymentService {

	/**
	 * 创建微信订单并生成二维码.
	 * 
	 * @param resourcesGoodsID
	 *            资源商品ID
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	UnifiedPayResult getWXQRCodeImage(String appid, long resourcesGoodsID, long orderID, OrderPayBusinessSpace space,
			HttpServletRequest request, HttpServletResponse response);

	/**
	 * 跳转支付宝支付页面.
	 * 
	 * @param resourcesGoodsID
	 *            资源商品ID
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	void jumpToAlipay(long resourcesGoodsID, long orderID, OrderPayBusinessSpace space, HttpServletRequest request,
			HttpServletResponse response);
}
