package com.lanking.uxb.service.payment.alipay.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.request.DirectPayData;
import com.lanking.uxb.service.payment.alipay.request.ToAccountTransferData;
import com.lanking.uxb.service.payment.alipay.response.ToAccountTransferResponse;

/**
 * 支付宝相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
public interface AlipayService {

	/**
	 * 跳转至支付宝支付页面.<br>
	 * 注意该方法调用必须是GET方式.
	 * 
	 * @param DirectPayData
	 *            本地商品订单参数
	 */
	void jumpToAlipay(OrderPayBusinessSpace space, DirectPayData directPayData, HttpServletRequest request,
			HttpServletResponse response) throws IOException;

	/**
	 * 跳转至支付宝支付页面（手机支付使用）.<br>
	 * 注意该方法调用必须是GET方式.
	 * 
	 * @param DirectPayData
	 *            本地商品订单参数
	 */
	void jumpToAlipayWap(OrderPayBusinessSpace space, DirectPayData directPayData, String return_url,
			HttpServletRequest request, HttpServletResponse response) throws IOException;

	/**
	 * 转账.
	 * 
	 * @param data
	 *            转账数据
	 * @return
	 */
	ToAccountTransferResponse toTransfer(ToAccountTransferData data) throws Exception;
}
