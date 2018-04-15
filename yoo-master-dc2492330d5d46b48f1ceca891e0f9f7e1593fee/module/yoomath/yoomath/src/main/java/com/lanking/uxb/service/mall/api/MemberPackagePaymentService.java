package com.lanking.uxb.service.mall.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 订单支付相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月27日
 */
public interface MemberPackagePaymentService {

	/**
	 * 创建微信订单并生成二维码.
	 * 
	 * @param memberPackageID
	 *            会员套餐ID
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	UnifiedPayResult getWXQRCodeImage(String appid, long memberPackageID, long orderID, OrderPayBusinessSpace space,
			String trade_typ, String open_id, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 跳转支付宝支付页面.
	 * 
	 * @param memberPackageID
	 *            会员套餐ID
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	void jumpToAlipay(long memberPackageID, long orderID, OrderPayBusinessSpace space, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 获得跳转支付宝支付页面的表单.
	 * 
	 * @param memberPackageID
	 *            会员套餐ID
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param return_url
	 *            即时跳转页面地址
	 * @param request
	 * @param response
	 * @return
	 */
	void jumpToAlipayWap(long memberPackageID, long orderID, OrderPayBusinessSpace space, String return_url,
			HttpServletRequest request, HttpServletResponse response);
}
