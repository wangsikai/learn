package com.lanking.uxb.service.mall.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.uxb.service.mall.api.ResourcesGoodsOrderService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.api.AliPaymentCallbackProvider;
import com.lanking.uxb.service.payment.alipay.client.AlipayConfig;
import com.lanking.uxb.service.payment.alipay.response.PayCallbackResult;

/**
 * 精品组卷支付宝支付回调处理接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月4日
 */
@Service
public class ExampaperAliPaymentCallbackProvider implements AliPaymentCallbackProvider {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResourcesGoodsOrderService resourcesGoodsOrderService;

	@Override
	public OrderPayBusinessSpace getBusinessSpaceName() {
		return OrderPayBusinessSpace.EXAM_PAPER;
	}

	@Override
	public boolean accept(OrderPayBusinessSpace space) {
		return space == this.getBusinessSpaceName();
	}

	@Override
	public void handlePayCallbackResult(boolean isAsync, PayCallbackResult result, HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String out_trade_no = result.getOut_trade_no(); // 本地订单ID
		String gmt_payment = result.getGmt_payment(); // 交易付款时间
		String trade_status = result.getTrade_status(); // 交易状态

		if ("TRADE_SUCCESS".equalsIgnoreCase(trade_status) || "TRADE_PENDING".equalsIgnoreCase(trade_status)
				|| "TRADE_FINISHED".equalsIgnoreCase(trade_status)) {
			// 买家付款成功

			Date payTime = null;
			if (StringUtils.isNotBlank(gmt_payment)) {
				try {
					payTime = formate.parse(gmt_payment);
				} catch (ParseException e) {
					payTime = new Date();
					logger.error(e.getMessage(), e);
				}
			} else {
				payTime = new Date();
			}

			// 支付订单
			resourcesGoodsOrderService.updatePaymentCallback(Long.parseLong(out_trade_no), null, result.getTrade_no(),
					payTime);

			// 完成订单
			resourcesGoodsOrderService.updateOrderStatus(Long.parseLong(out_trade_no), null, GoodsOrderStatus.COMPLETE);
		} else if ("TRADE_CLOSED".equalsIgnoreCase(trade_status)) {
			// 交易关闭未支付成功
			resourcesGoodsOrderService.updateOrderStatus(Long.parseLong(out_trade_no), null, GoodsOrderStatus.FAIL);
		}

		// 非异步通知，需跳转页面
		if (!isAsync) {
			try {
				response.sendRedirect(AlipayConfig.return_server
						+ "/page/teacherIndex.html#/tea/exampaper/buyComplete?o=" + out_trade_no);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
