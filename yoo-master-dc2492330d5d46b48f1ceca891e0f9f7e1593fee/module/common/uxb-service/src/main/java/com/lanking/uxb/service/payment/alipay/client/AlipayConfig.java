package com.lanking.uxb.service.payment.alipay.client;

import com.lanking.cloud.springboot.environment.Env;

/**
 * 支付宝支付配置.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
public class AlipayConfig {

	// 合作身份者ID
	public static String partner = Env.getString("alipay.pid");

	// 沙箱pid
	public static String sandboxPid = Env.getString("alipay.sanbox.pid");

	// 收款支付宝账号，一般情况下收款账号就是签约账号
	public static String seller_id = Env.getString("alipay.seller_id");

	// 商户的私钥，需要PKCS8格式
	public static String private_key = Env.getString("alipay.private_key");

	// 支付宝的公钥，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key = Env.getString("alipay.public_key");

	// 服务器异步通知页面路径
	public static String notify_url = Env.getString("alipay.notify_url");

	// 页面跳转同步通知页面路径
	public static String return_url = Env.getString("alipay.return_url");

	// 页面跳转地址
	public static String return_server = Env.getString("alipay.return_server");

	// 签名方式
	public static String sign_type = "RSA";

	// 支付类型 ，无需修改
	public static String payment_type = "1";

	// 调用的接口名，无需修改
	public static String service = "create_direct_pay_by_user";

	// 支付网关
	public static String gateway = Env.getString("alipay.gateway");

	// 公共API网关
	public static String gatewayDev = Env.getString("alipay.gateway.dev");

	// 悠数学应用APPID
	public static String app_yoomath_appid = Env.getString("alipay.app.yoomath.appid");

	// 悠数学应用支付宝的公钥
	public static String app_yoomath_alipay_public_key = Env.getString("alipay.app.yoomath.public_key");

}
