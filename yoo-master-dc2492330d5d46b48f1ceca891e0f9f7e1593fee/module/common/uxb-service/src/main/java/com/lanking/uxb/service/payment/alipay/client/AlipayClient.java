package com.lanking.uxb.service.payment.alipay.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.util.XmlBeanMarshall;
import com.lanking.uxb.service.payment.alipay.request.ToAccountTransferData;
import com.lanking.uxb.service.payment.alipay.response.OrderQueryResult;
import com.lanking.uxb.service.payment.alipay.response.ToAccountTransferResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝客户端.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */

@Slf4j
@Component
public class AlipayClient {

	@Autowired
	private HttpClient httpClient;

	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public String buildRequestMysign(Map<String, String> sPara) {
		String prestr = AlipayCore.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = "";
		if (AlipayConfig.sign_type.equals("RSA")) {
			mysign = AlipayRSA.sign(prestr, AlipayConfig.private_key, "UTF-8");
		}
		return mysign;
	}

	/**
	 * 生成要请求给支付宝的参数数组
	 * 
	 * @param sParaTemp
	 *            请求前的参数数组
	 * @return 要请求的参数数组
	 */
	public Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = buildRequestMysign(sPara);

		// 签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", AlipayConfig.sign_type);

		return sPara;
	}

	/**
	 * 订单查询接口.
	 * 
	 * @param partner
	 *            商户PID
	 * @param out_trade_no
	 *            本地订单ID
	 * @param trade_no
	 *            支付宝交易号
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public OrderQueryResult orderQuery(String partner, String out_trade_no, String trade_no) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("service", "single_trade_query");
		paramMap.put("partner", partner);
		paramMap.put("_input_charset", "utf-8");
		if (StringUtils.isNotBlank(out_trade_no)) {
			paramMap.put("out_trade_no", out_trade_no);
		}
		if (StringUtils.isNotBlank(trade_no)) {
			paramMap.put("trade_no", trade_no);
		}

		AlipayCore.paraFilter(paramMap);
		String sign = this.buildRequestMysign(paramMap);
		paramMap.put("sign_type", "RSA");
		paramMap.put("sign", sign);
		String encodeStr = AlipayCore.createLinkEncodeString(paramMap);

		HttpGet httpGet = new HttpGet(AlipayConfig.gateway + encodeStr);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("Content-type", "text/html");

		HttpResponse response = httpClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				OrderQueryResult orderQueryResult = (OrderQueryResult) XmlBeanMarshall.xml2Bean(OrderQueryResult.class,
						result);
				return orderQueryResult;
			}
		}

		return null;
	}

	public ToAccountTransferResponse toTransfer(ToAccountTransferData data) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 构造请求参数
		Map<String, String> paramMap = new HashMap<String, String>();

		// 处理附加数据
		paramMap.put("biz_content", JSON.toJSONString(data));

		paramMap.put("app_id", AlipayConfig.sandboxPid);
		paramMap.put("method", "alipay.fund.trans.toaccount.transfer");
		paramMap.put("charset", "utf-8");
		paramMap.put("timestamp", format.format(new Date()));
		paramMap.put("version", "1.0");

		AlipayCore.paraFilter(paramMap);
		String sign = this.buildRequestMysign(paramMap);
		paramMap.put("sign_type", "RSA");
		paramMap.put("sign", sign);
		String encodeStr = AlipayCore.createLinkEncodeString(paramMap);

		HttpGet httpGet = new HttpGet(AlipayConfig.gatewayDev + encodeStr);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("Content-type", "text/html");

		ToAccountTransferResponse responseData = new ToAccountTransferResponse();

		HttpResponse response = httpClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				JSONObject jo = JSONObject.parseObject(result);
				JSONObject res = jo.getJSONObject("alipay_fund_trans_toaccount_transfer_response");
				String resSign = jo.getString("sign");
				System.out.println(result);
			}
		} else {
			log.error("[-alipay error-] request Status = {}", response.getStatusLine().getStatusCode());
		}

		return null;
	}
}
