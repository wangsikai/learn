package com.lanking.uxb.service.fallible.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.lanking.uxb.service.payment.alipay.api.AliPaymentCallbackProvider;
import com.lanking.uxb.service.payment.alipay.client.AlipayConfig;
import com.lanking.uxb.service.payment.alipay.response.PayCallbackResult;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.web.resource.ZyStuFallibleQuestionController;

/**
 * 精品组卷支付宝支付回调处理接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月4日
 */
@Service
public class StuFallPrintAliPaymentCallbackProvider implements AliPaymentCallbackProvider {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ZyStuFallibleQuestionController stuFallibleQuestionController;
	@Autowired
	private ZyStuFalliblePrintService stuFalliblePrintService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;

	private ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

	@Override
	public OrderPayBusinessSpace getBusinessSpaceName() {
		return OrderPayBusinessSpace.STU_FALLIBLE_PRINT;
	}

	@Override
	public boolean accept(OrderPayBusinessSpace space) {
		return space == this.getBusinessSpaceName();
	}

	@Override
	public void handlePayCallbackResult(boolean isAsync, PayCallbackResult result, HttpServletRequest request,
			HttpServletResponse response) {
		final SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String out_trade_no = result.getOut_trade_no(); // 本地订单ID
		final String gmt_payment = result.getGmt_payment(); // 交易付款时间
		final String trade_status = result.getTrade_status(); // 交易状态
		final String trade_no = result.getTrade_no(); // 流水号

		final long orderID = Long.parseLong(out_trade_no);

		FallibleQuestionPrintOrder order = stuFalliblePrintService.get(orderID);
		if ("TRADE_SUCCESS".equalsIgnoreCase(trade_status) || "TRADE_PENDING".equalsIgnoreCase(trade_status)
				|| "TRADE_FINISHED".equalsIgnoreCase(trade_status)) {

			// 判断订单是否已被处理中
			if (callbackOrderCache.hasProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID)) {
				return;
			} else {
				callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID);
			}
			logger.info("[错题代印] 支付宝购买, orderID = " + orderID);

			// 买家付款成功
			if (order.getStatus() == FallibleQuestionPrintOrderStatus.NOT_PAY) {
				singleThreadPool.execute(new Runnable() {
					@Override
					public void run() {
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
						FallibleQuestionPrintOrder order = stuFalliblePrintService.updatePaymentCallback(orderID, null,
								trade_no, payTime);

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
			callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID);
		} else if ("TRADE_CLOSED".equalsIgnoreCase(trade_status)) {
			// 交易关闭未支付成功
			if (order.getStatus() != FallibleQuestionPrintOrderStatus.FAIL) {
				singleThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						stuFalliblePrintService.updateOrderStatus(orderID, null, FallibleQuestionPrintOrderStatus.FAIL);
					}
				});
			}
		}

		// 非异步通知，需跳转页面
		if (!isAsync) {
			try {
				response.sendRedirect(AlipayConfig.return_server + "/page/student/fallible2/pay/complete.html?o="
						+ orderID);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
