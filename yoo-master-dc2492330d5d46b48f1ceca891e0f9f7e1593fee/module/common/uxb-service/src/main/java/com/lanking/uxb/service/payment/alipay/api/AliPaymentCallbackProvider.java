package com.lanking.uxb.service.payment.alipay.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.response.PayCallbackResult;

/**
 * 支付宝支付结果通知.<br>
 * 不同业务处理结果必须实现该接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
public interface AliPaymentCallbackProvider {

	/**
	 * 获得业务唯一标识.
	 * 
	 * @return
	 */
	OrderPayBusinessSpace getBusinessSpaceName();

	/**
	 * 验证当前的唯一业务码，用于区分业务，实现回调.
	 * 
	 * @param space
	 * @return
	 */
	boolean accept(OrderPayBusinessSpace space);

	/**
	 * 处理支付结果通知.
	 * 
	 * @param isAsync
	 *            是否为异步通知
	 * @param result
	 *            结果数据
	 * @return RespondData 反馈数据
	 */
	void handlePayCallbackResult(boolean isAsync, PayCallbackResult result, HttpServletRequest request,
			HttpServletResponse response);
}
