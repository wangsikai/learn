package com.lanking.uxb.service.payment.weixin.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.lanking.cloud.sdk.util.XmlBeanMarshall;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.payment.weixin.request.UnifiedPayData;
import com.lanking.uxb.service.payment.weixin.response.OrderQueryResult;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;
import com.tencent.WXPay;
import com.tencent.common.APIConfigure;
import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;

/**
 * 微信支付客户端.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月31日
 */
@Component
public class WXPaymentClient implements InitializingBean {

	// 使用悠数学公众号作为支付商户
	// private String appid = Env.getString("weixin.pay.appid"); // 公众号ID
	// private String appsecret = Env.getString("weixin.pay.appkey"); // 公众号KEY
	// private String mchID = Env.getString("weixin.pay.mchID"); // 商户ID
	// private String notifyUrl = Env.getString("weixin.pay.notifyUrl"); // 回调地址

	// 微信支付配置
	private Map<String, Configure> configures = new HashMap<String, Configure>(3);

	/**
	 * 获得二维码链接.<br>
	 * 微信支付模式一使用 https://pay.weixin.qq.com/wiki/doc/api/native.php
	 * 
	 * @param businessID
	 *            业务ID，订单、商品等
	 * @return
	 */
	public String getQRCodeURL(Configure configure, String businessID) {
		String nonce_str = RandomStringGenerator.getRandomStringByLength(16); // 随机字串
		long time_stamp = System.currentTimeMillis();

		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("appid", configure.getAppid());
		map.put("mch_id", configure.getMchid());
		map.put("time_stamp", time_stamp);
		map.put("nonce_str", nonce_str);
		map.put("product_id", businessID);

		String sign = Signature.getSign(map, configure); // 签名
		StringBuffer url = new StringBuffer("weixin：//wxpay/bizpayurl?");
		url.append("sign=").append(sign).append("&appid=").append(configure.getAppid()).append("&mch_id=")
				.append(configure.getMchid()).append("&product_id=").append(businessID).append("&time_stamp=")
				.append(time_stamp).append("&nonce_str=").append(nonce_str);
		return url.toString();
	}

	/**
	 * 调用统一支付.
	 * 
	 * @param productTitle
	 *            商品 标题
	 * @param attach
	 *            附加数据（length <= 127）
	 * @param businessID
	 *            业务ID，商品或订单ID
	 * @param spbill_create_ip
	 *            客户机IP地址
	 * @param total_fee
	 *            订单总价，单位：分
	 * @param notify_url
	 *            回调 URL
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	public UnifiedPayResult unifiedPayOrder(Configure configure, String productTitle, String attach, String businessID,
			String spbill_create_ip, int total_fee, String trade_type, String open_id) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException, Exception {
		if (StringUtils.isBlank(productTitle)) {
			productTitle = "订单商品";
		} else {
			productTitle = productTitle.length() > 30 ? (productTitle.substring(0, 30) + "...") : productTitle;
		}
		String nonce_str = RandomStringGenerator.getRandomStringByLength(32); // 随机字串
		UnifiedPayData unifiedPayData = new UnifiedPayData();
		unifiedPayData.setAppid(configure.getAppid());
		unifiedPayData.setAttach(URLEncoder.encode(attach, "UTF-8"));
		unifiedPayData.setBody(productTitle);
		unifiedPayData.setMch_id(configure.getMchid());
		unifiedPayData.setNonce_str(nonce_str);
		unifiedPayData.setNotify_url(configure.getNotifyUrl());
		unifiedPayData.setOut_trade_no(businessID);
		unifiedPayData.setSpbill_create_ip(spbill_create_ip);
		unifiedPayData.setTotal_fee(total_fee);
		unifiedPayData.setTrade_type(trade_type);
		if (open_id != null) {
			unifiedPayData.setOpenid(open_id);
		}

		String sign = Signature.getSign(unifiedPayData.toMap(), configure); // 签名
		unifiedPayData.setSign(sign);
		String xml = new WXUnifiedorderClient(configure, APIConfigure.URL_UNIFIED_PAY).request(configure,
				unifiedPayData);

		if (StringUtils.isNotBlank(xml)) {
			UnifiedPayResult result = (UnifiedPayResult) XmlBeanMarshall.xml2Bean(UnifiedPayResult.class, xml);
			result.setNonceStrRequest(unifiedPayData.getNonce_str());
			return result;
		}
		return null;
	}

	/**
	 * 查询微信订单状态.
	 * 
	 * @param transactionID
	 *            微信端订单号.
	 * @param outTradeNo
	 *            商户订单号
	 * @throws Exception
	 */
	public OrderQueryResult orderQuery(Configure configure, String transactionID, String outTradeNo) throws Exception {
		ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData(configure, transactionID, outTradeNo);
		String resultStr = WXPay.requestScanPayQueryService(configure, scanPayQueryReqData);
		if (StringUtils.isNotBlank(resultStr)) {
			OrderQueryResult orderQueryResult = (OrderQueryResult) XmlBeanMarshall.xml2Bean(OrderQueryResult.class,
					resultStr);
			return orderQueryResult;
		}
		return null;
	}

	/**
	 * 获得微信支付配置.
	 * 
	 * @param appid
	 * @return
	 */
	public Configure getConfigure(String appid) {
		return configures.get(appid);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final File baseFileDir = new File(Env.getString("weixin.pay.cert.destPath"));
		if (!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath*:" + Env.getString("weixin.pay.cert.srcPath") + "/*.*");
		if (resources != null) {
			for (Resource resource : resources) {
				String fileName = resource.getFilename();
				File ftlFile = new File(baseFileDir, fileName);
				if (ftlFile.exists()) {
					ftlFile.delete();
				}
				ftlFile.createNewFile();

				InputStream ins = resource.getInputStream();
				OutputStream os = new FileOutputStream(ftlFile);
				int bytesRead = 0;
				byte[] buffer = new byte[4096];
				while ((bytesRead = ins.read(buffer, 0, 4096)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				ins.close();
			}
		}

		// web公众号支付配置（H5支付）
		Configure configureH5 = new Configure();
		configureH5.setAppID(Env.getString("weixin.appid.zuoye"));
		configureH5.setKey(Env.getString("weixin.appsecret.zuoye"));
		configureH5.setMchID(Env.getString("weixin.pay.mchID"));
		configureH5.setNotifyUrl(Env.getString("weixin.pay.notifyUrl"));
		configureH5.setCertLocalPath(Env.getString("weixin.pay.cert.file"));
		configureH5.setCertPassword(configureH5.getMchid());

		// web公众号支付配置（网页扫码）
		Configure configureWeb = new Configure();
		configureWeb.setAppID(Env.getString("weixin.pay.appid"));
		configureWeb.setKey(Env.getString("weixin.pay.appkey"));
		configureWeb.setMchID(Env.getString("weixin.pay.mchID"));
		configureWeb.setNotifyUrl(Env.getString("weixin.pay.notifyUrl"));
		configureWeb.setCertLocalPath(Env.getString("weixin.pay.cert.file"));
		configureWeb.setCertPassword(configureWeb.getMchid());

		// 教师端支付配置
		Configure configureMobileTea = new Configure();
		configureMobileTea.setAppID(Env.getString("weixin.pay.mobile.teacher.appid"));
		configureMobileTea.setKey(Env.getString("weixin.pay.mobile.teacher.appkey"));
		configureMobileTea.setMchID(Env.getString("weixin.pay.mobile.teacher.mchID"));
		configureMobileTea.setNotifyUrl(Env.getString("weixin.pay.mobile.teacher.notifyUrl"));
		configureMobileTea.setCertLocalPath(Env.getString("weixin.pay.mobile.teacher.cert.file"));
		configureMobileTea.setCertPassword(configureMobileTea.getMchid());

		// 学生端支付配置
		Configure configureMobileStu = new Configure();
		configureMobileStu.setAppID(Env.getString("weixin.pay.mobile.student.appid"));
		configureMobileStu.setKey(Env.getString("weixin.pay.mobile.student.appkey"));
		configureMobileStu.setMchID(Env.getString("weixin.pay.mobile.student.mchID"));
		configureMobileStu.setNotifyUrl(Env.getString("weixin.pay.mobile.student.notifyUrl"));
		configureMobileStu.setCertLocalPath(Env.getString("weixin.pay.mobile.student.cert.file"));
		configureMobileStu.setCertPassword(configureMobileStu.getMchid());

		configures.put(configureH5.getAppid(), configureH5);
		configures.put(configureWeb.getAppid(), configureWeb);
		configures.put(configureMobileTea.getAppid(), configureMobileTea);
		configures.put(configureMobileStu.getAppid(), configureMobileStu);
	}
}
