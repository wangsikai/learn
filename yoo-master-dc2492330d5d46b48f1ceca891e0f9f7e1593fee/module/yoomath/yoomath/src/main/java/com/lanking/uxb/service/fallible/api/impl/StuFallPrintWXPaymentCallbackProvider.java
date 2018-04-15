package com.lanking.uxb.service.fallible.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.fallible.api.ZyStuFalliblePrintService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentCallbackProvider;
import com.lanking.uxb.service.payment.weixin.response.PayCallbackResult;
import com.lanking.uxb.service.web.resource.ZyStuFallibleQuestionController;

/**
 * 精品组卷微信支付回调处理接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月4日
 */
@Service
public class StuFallPrintWXPaymentCallbackProvider implements WXPaymentCallbackProvider {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

	@Autowired
	private ZyStuFallibleQuestionController stuFallibleQuestionController;
	@Autowired
	private ZyStuFalliblePrintService stuFalliblePrintService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;

	@Override
	public OrderPayBusinessSpace getBusinessSpaceName() {
		return OrderPayBusinessSpace.STU_FALLIBLE_PRINT;
	}

	@Override
	public boolean accept(OrderPayBusinessSpace space) {
		return space == this.getBusinessSpaceName();
	}

	@Override
	public void handleUnifiedPayResult(PayCallbackResult result) {
		final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		final String outTradeNo = result.getOutTradeNo(); // 本地订单ID
		final String timeEnd = result.getTimeEnd(); // 交易付款时间
		final String transactionId = result.getTransactionId(); // 微信支付订单号
		final String resultCode = result.getResultCode(); // 交易状态

		final long orderID = Long.parseLong(outTradeNo);

		FallibleQuestionPrintOrder order = stuFalliblePrintService.get(orderID);
		if ("SUCCESS".equalsIgnoreCase(resultCode)) {
			// 交易成功

			// 判断订单是否已被处理中
			if (callbackOrderCache.hasProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID)) {
				return;
			} else {
				callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID);
			}
			logger.info("[错题代印] 微信购买, orderID = " + orderID);

			if (order.getStatus() == FallibleQuestionPrintOrderStatus.NOT_PAY) {
				singleThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						Date payTime = null;
						try {
							payTime = formate.parse(timeEnd);
						} catch (ParseException e) {
							payTime = new Date();
							logger.error(e.getMessage(), e);
						}

						// 支付订单
						FallibleQuestionPrintOrder order = stuFalliblePrintService.updatePaymentCallback(orderID,
								transactionId, null, payTime);

						// 生成错题文档
						String attachData = order.getAttachData();
						JSONObject jo = JSONObject.parseObject(attachData);
						JSONArray scArray = jo.getJSONArray("sectionCodes");
						List<Long> sectionCodes = new ArrayList<Long>(scArray.size());
						for (int i = 0; i < scArray.size(); i++) {
							sectionCodes.add(scArray.getLong(i));
						}

						int timeScope = jo.getIntValue("timeScope");
						JSONArray qtArray = jo.getJSONArray("questionTypes");
						List<Question.Type> qTypes = new ArrayList<Question.Type>();
						for (int i = 0; i < qtArray.size(); i++) {
							qTypes.add(Question.Type.findByValue(qtArray.getIntValue(i)));
						}
						int errorTimes = jo.getIntValue("errorTimes");

						String host = Env.getString("alipay.return_server").replace("heep://", "");
						stuFallibleQuestionController.createExportDoc(timeScope, qTypes, errorTimes, sectionCodes,
								host, order.getId());
					}
				});
			}
		} else {
			// 交易失败
			if (order.getStatus() != FallibleQuestionPrintOrderStatus.FAIL) {
				singleThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						stuFalliblePrintService.updateOrderStatus(orderID, null, FallibleQuestionPrintOrderStatus.FAIL);
					}
				});
			}
		}
	}
}
