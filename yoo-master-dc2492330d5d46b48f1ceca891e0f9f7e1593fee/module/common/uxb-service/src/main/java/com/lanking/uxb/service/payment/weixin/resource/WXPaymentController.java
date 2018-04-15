package com.lanking.uxb.service.payment.weixin.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.sdk.util.XmlBeanMarshall;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentCallbackProvider;
import com.lanking.uxb.service.payment.weixin.client.WXPaymentClient;
import com.lanking.uxb.service.payment.weixin.request.RespondData;
import com.lanking.uxb.service.payment.weixin.response.PayCallbackResult;
import com.tencent.common.Signature;

/**
 * 微信支付结果处理.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月31日
 */
@RestController
@RequestMapping("wx/pay")
public class WXPaymentController implements ApplicationContextAware {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	@Autowired
	private WXPaymentClient paymentClient;

	/**
	 * 业务结果处理器集合.
	 */
	private List<WXPaymentCallbackProvider> providers;

	/**
	 * 微信消息接收.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "call")
	@RolesAllowed(anyone = true)
	public void call(HttpServletRequest request, HttpServletResponse response) {

		String rmsg = this.dataProcess(request, response);
		PrintWriter writer = null;
		try {
			response.setHeader("content-type", "text/xml; charset=UTF-8");
			writer = response.getWriter();
			writer.write(rmsg);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 微信消息数据处理.
	 * 
	 * @param request
	 * @param response
	 */
	private String dataProcess(HttpServletRequest request, HttpServletResponse response) {
		RespondData respondData = new RespondData();
		InputStream in = null;
		String xml = "";
		try {
			in = request.getInputStream();
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			xml = out.toString(); // 获得微信返回的XML字串
			if (StringUtils.isNotBlank(xml) && xml.indexOf("xml") != -1) {
				xml = URLDecoder.decode(xml, "UTF-8");
				final PayCallbackResult result = (PayCallbackResult) XmlBeanMarshall.xml2Bean(PayCallbackResult.class,
						xml);

				// 校验签名
				boolean isSign = Signature.checkIsSignValidFromResponseString(xml,
						paymentClient.getConfigure(result.getAppid()));
				if (isSign) {
					String attach = result.getAttach();
					JSONObject obj = JSONObject.parseObject(attach);
					Integer spaceValue = obj.getInteger("b");
					if (null != spaceValue) {
						result.setAttach(obj.getString("a"));
						for (final WXPaymentCallbackProvider provider : providers) {
							if (provider.accept(OrderPayBusinessSpace.findByValue(spaceValue))) {
								fixedThreadPool.execute(new Runnable() {
									@Override
									public void run() {
										provider.handleUnifiedPayResult(result);
									}
								});
								respondData.setReturnCode("SUCCESS");
								break;
							}
						}
					} else {
						respondData.setReturnCode("FAIL");
						logger.error("[XW-Pay] 微信返回数据中缺失附加attach，匹配provider失败！");
					}
				}
			}
		} catch (Exception e) {
			respondData.setReturnCode("FAIL");
			logger.error("[XW-Pay] xml = " + xml, e);
		}
		respondData.setReturnMsg("OK");
		return XmlBeanMarshall.bean2Xml(respondData, "UTF-8", true);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		providers = new ArrayList<WXPaymentCallbackProvider>(applicationContext.getBeansOfType(
				WXPaymentCallbackProvider.class).values());
	}
}
