package com.lanking.uxb.service.payment.alipay.api.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.api.AlipayService;
import com.lanking.uxb.service.payment.alipay.client.AlipayClient;
import com.lanking.uxb.service.payment.alipay.client.AlipayConfig;
import com.lanking.uxb.service.payment.alipay.request.DirectPayData;
import com.lanking.uxb.service.payment.alipay.request.ToAccountTransferData;
import com.lanking.uxb.service.payment.alipay.response.ToAccountTransferResponse;

/**
 * 支付宝接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
@Service
public class AlipayServiceImpl implements AlipayService {

	@Autowired
	private AlipayClient alipayClient;

	@Override
	public void jumpToAlipay(OrderPayBusinessSpace space, DirectPayData directPayData, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// 构造请求参数
		Map<String, String> paraTemp = directPayData.toMap();

		// 处理附加数据
		paraTemp.put("extra_common_param", String.valueOf(space.getValue()));

		paraTemp.put("service", AlipayConfig.service);
		paraTemp.put("partner", AlipayConfig.partner);
		paraTemp.put("seller_id", AlipayConfig.partner);
		paraTemp.put("payment_type", AlipayConfig.payment_type);
		paraTemp.put("notify_url", AlipayConfig.notify_url);
		paraTemp.put("return_url", AlipayConfig.return_url);
		paraTemp.put("_input_charset", "UTF-8");
		Map<String, String> sParaTemp = alipayClient.buildRequestPara(paraTemp);
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<meta charset=\"utf-8\">");
		sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		sbHtml.append("</head>");
		sbHtml.append("<body>");
		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + AlipayConfig.gateway
				+ "_input_charset=utf-8\" method=\"get\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sParaTemp.get(name);
			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");

		response.setHeader("Content-Type", "text/html; charset=utf-8");
		Writer writer = response.getWriter();
		writer.write(sbHtml.toString());
		writer.flush();
		writer.close();
	}

	@Override
	public void jumpToAlipayWap(OrderPayBusinessSpace space, DirectPayData directPayData, String return_url,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 构造请求参数
		Map<String, String> paraTemp = directPayData.toMap();

		// 处理附加数据
		paraTemp.put("extra_common_param", String.valueOf(space.getValue()));

		paraTemp.put("service", "alipay.wap.create.direct.pay.by.user");
		paraTemp.put("partner", AlipayConfig.partner);
		paraTemp.put("seller_id", AlipayConfig.partner);
		paraTemp.put("payment_type", AlipayConfig.payment_type);
		paraTemp.put("notify_url", AlipayConfig.notify_url);
		paraTemp.put("return_url", return_url);
		paraTemp.put("_input_charset", "UTF-8");
		Map<String, String> sParaTemp = alipayClient.buildRequestPara(paraTemp);
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<meta charset=\"utf-8\">");
		sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		sbHtml.append("</head>");
		sbHtml.append("<body>");
		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + AlipayConfig.gateway
				+ "_input_charset=utf-8\" method=\"get\">");
		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sParaTemp.get(name);
			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");

		response.setHeader("Content-Type", "text/html; charset=utf-8");
		Writer writer = response.getWriter();
		writer.write(sbHtml.toString());
		writer.flush();
		writer.close();
	}

	@Override
	public ToAccountTransferResponse toTransfer(ToAccountTransferData data) throws Exception {
		return alipayClient.toTransfer(data);
	}
}
