package com.lanking.uxb.service.fallible.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 错题代打印服务接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月8日
 */
public interface ZyStuFalliblePrintService {

	/**
	 * 创建打印订单.
	 * 
	 * @param userID
	 *            订单用户
	 * @param payMod
	 *            支付方式
	 * @param paymentPlatformCode
	 *            支付平台
	 * @param totalPrice
	 *            总价
	 * @param attachData
	 *            附加数据
	 * @param contactName
	 *            收货联系人
	 * @param contactPhone
	 *            收货电话
	 * @param districtCode
	 *            收货地区
	 * @param contactAddress
	 *            收货详细地址
	 * @return
	 */
	FallibleQuestionPrintOrder createOrder(long userID, PayMode payMod, Integer paymentPlatformCode,
			BigDecimal totalPrice, String attachData, String contactName, String contactPhone, long districtCode,
			String contactAddress);

	/**
	 * 创建待打印订单快照.
	 * 
	 * @param order
	 *            订单
	 * @param date
	 *            日期
	 * @param userID
	 *            订单用户
	 * @return
	 */
	FallibleQuestionPrintOrderSnapshot createOrderSnapshot(FallibleQuestionPrintOrder order, Date date, long userID);

	/**
	 * 创建微信订单并生成二维码.
	 * 
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	UnifiedPayResult getWXQRCodeImage(String appid, long orderID, OrderPayBusinessSpace space,
			HttpServletRequest request, HttpServletResponse response);

	/**
	 * 跳转支付宝支付页面.
	 * 
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	void jumpToAlipay(long orderID, OrderPayBusinessSpace space, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 订单支付返回更新订单.
	 * 
	 * @param orderID
	 *            订单ID
	 * @param paymentPlatformOrderCode
	 *            支付平台订单号
	 * @param paymentCode
	 *            支付流水号
	 * @param payTime
	 *            支付时间
	 * @return
	 */
	FallibleQuestionPrintOrder updatePaymentCallback(long orderID, String paymentPlatformOrderCode, String paymentCode,
			Date payTime);

	/**
	 * 更新订单状态.
	 * 
	 * @param orderID
	 *            订单ID
	 * @param updateID
	 *            更新人
	 * @param status
	 * @return
	 */
	FallibleQuestionPrintOrder updateOrderStatus(long orderID, Long updateID, FallibleQuestionPrintOrderStatus status);

	/**
	 * 获得代打印订单.
	 * 
	 * @param orderID
	 *            订单ID.
	 * @return
	 */
	FallibleQuestionPrintOrder get(long orderID);

	/**
	 * 获得最近一次代打印订单.
	 * 
	 * @param userID
	 *            用户ID
	 * @return
	 */
	FallibleQuestionPrintOrder getLast(long userID);

	/**
	 * 批量获取订单.
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, FallibleQuestionPrintOrder> mget(Collection<Long> ids);
}
