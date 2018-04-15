package com.lanking.uxb.service.payment.alipay.resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.api.AliPaymentCallbackProvider;
import com.lanking.uxb.service.payment.alipay.client.AlipayNotify;
import com.lanking.uxb.service.payment.alipay.response.PayCallbackResult;

/**
 * 支付宝支付结果处理.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
@RestController
@RequestMapping("alipay")
public class AlipayController implements ApplicationContextAware {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	/**
	 * 业务结果处理器集合.
	 */
	private List<AliPaymentCallbackProvider> providers;

	/**
	 * 异步回调通知.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("notifyCall")
	@RolesAllowed(anyone = true)
	public void notifyCall(HttpServletRequest request, HttpServletResponse response) {
		this.handleResult(true, request, response);
	}

	/**
	 * 同步回调通知.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("returnCall")
	@RolesAllowed(anyone = true)
	public void returnCall(HttpServletRequest request, HttpServletResponse response) {
		this.handleResult(false, request, response);
	}

	/**
	 * 处理返回数据.
	 * 
	 * @param isAsync
	 *            是否为异步通知.
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	private void handleResult(final boolean isAsync, final HttpServletRequest request,
			final HttpServletResponse response) {
		// String notify_id = request.getParameter("notify_id"); // 通知校验ID.
		// String sign = request.getParameter("sign"); // 签名

		String business_scene = request.getParameter("business_scene");
		String buyer_email = request.getParameter("buyer_email");
		String buyer_id = request.getParameter("buyer_id");
		String discount = request.getParameter("discount");
		String extra_common_param = request.getParameter("extra_common_param");
		String gmt_close = request.getParameter("gmt_close");
		String gmt_create = request.getParameter("gmt_create");
		String gmt_payment = request.getParameter("gmt_payment");
		String is_total_fee_adjust = request.getParameter("is_total_fee_adjust");
		String notify_time = request.getParameter("notify_time");
		String out_trade_no = request.getParameter("out_trade_no");
		String subject = request.getParameter("subject");
		String total_fee = request.getParameter("total_fee");
		String trade_no = request.getParameter("trade_no");
		String trade_status = request.getParameter("trade_status");

		// 准备进行签名验证
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// if (name.equals("extra_common_param") &&
			// StringUtils.isBlank(valueStr)) {
			// continue;
			// }
			params.put(name, valueStr);
		}
		boolean verify_result = AlipayNotify.verify(params); // 计算得出通知验证结果
		if (!verify_result) {
			logger.warn("[Alipay] 支付宝通知验证失败，有可能是伪造通知！params = " + params);
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.write("fail");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
			return;
		}

		final PayCallbackResult result = new PayCallbackResult();
		result.setBusiness_scene(business_scene);
		result.setBuyer_email(buyer_email);
		result.setBuyer_id(buyer_id);
		result.setDiscount(discount);
		result.setExtra_common_param(extra_common_param);
		result.setGmt_close(gmt_close);
		result.setGmt_create(gmt_create);
		result.setGmt_payment(gmt_payment);
		result.setIs_total_fee_adjust(is_total_fee_adjust);
		result.setNotify_time(notify_time);
		result.setOut_trade_no(out_trade_no);
		result.setSubject(subject);
		result.setTotal_fee(total_fee);
		result.setTrade_no(trade_no);
		result.setTrade_status(trade_status);

		if (StringUtils.isNotBlank(extra_common_param)) {
			Integer spaceValue = Integer.parseInt(extra_common_param);
			if (null != spaceValue) {
				for (final AliPaymentCallbackProvider provider : providers) {
					if (provider.accept(OrderPayBusinessSpace.findByValue(spaceValue))) {
						if (isAsync) {
							fixedThreadPool.execute(new Runnable() {
								@Override
								public void run() {
									provider.handlePayCallbackResult(isAsync, result, request, response);
								}
							});

							PrintWriter writer = null;
							try {
								writer = response.getWriter();
								writer.write("success");
								writer.flush();
								writer.close();
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
							} finally {
								if (writer != null) {
									writer.close();
								}
							}
						} else {
							provider.handlePayCallbackResult(isAsync, result, request, response);
						}
						break;
					}
				}
			} else {
				logger.error("[Ali-Pay] 支付宝返回数据中缺失附加数据，匹配provider失败！extra_common_param=" + extra_common_param);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		providers = new ArrayList<AliPaymentCallbackProvider>(applicationContext.getBeansOfType(
				AliPaymentCallbackProvider.class).values());
	}
}
