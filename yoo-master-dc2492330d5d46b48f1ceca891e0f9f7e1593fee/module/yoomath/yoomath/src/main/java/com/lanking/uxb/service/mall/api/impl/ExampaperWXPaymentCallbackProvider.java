package com.lanking.uxb.service.mall.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.uxb.service.mall.api.ResourcesGoodsOrderService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentCallbackProvider;
import com.lanking.uxb.service.payment.weixin.response.PayCallbackResult;

/**
 * 精品组卷微信支付回调处理接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月4日
 */
@Service
public class ExampaperWXPaymentCallbackProvider implements WXPaymentCallbackProvider {
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
	public void handleUnifiedPayResult(PayCallbackResult result) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		String outTradeNo = result.getOutTradeNo(); // 本地订单ID
		String timeEnd = result.getTimeEnd(); // 交易付款时间
		String transactionId = result.getTransactionId(); // 微信支付订单号
		String resultCode = result.getResultCode(); // 交易状态

		if ("SUCCESS".equalsIgnoreCase(resultCode)) {
			// 交易成功

			Date payTime = null;
			try {
				payTime = formate.parse(timeEnd);
			} catch (ParseException e) {
				payTime = new Date();
				logger.error(e.getMessage(), e);
			}

			// 支付订单
			resourcesGoodsOrderService.updatePaymentCallback(Long.parseLong(outTradeNo), transactionId, null, payTime);

			// 完成订单
			resourcesGoodsOrderService.updateOrderStatus(Long.parseLong(outTradeNo), null, GoodsOrderStatus.COMPLETE);
		} else {
			// 交易失败
			resourcesGoodsOrderService.updateOrderStatus(Long.parseLong(outTradeNo), null, GoodsOrderStatus.FAIL);
		}
	}
}
