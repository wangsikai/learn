package com.lanking.uxb.service.payment.weixin.request;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * 统一支付参数数据.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
@XmlRootElement(name = "xml")
public class UnifiedPayData {

	private String appid = ""; // 公众账号ID
	private String mch_id = ""; // 商户号
	private String device_info = "WEB"; // 设备号
	private String nonce_str = ""; // 随机字串
	private String sign = ""; // 签名
	private String body = ""; // 商品描述
	private String detail = ""; // 商品详情（非必需），使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来
	private String attach = ""; // 附加数据（非必需），在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	private String out_trade_no = ""; // 商户订单号（size <= 32）
	private int total_fee = 0; // 订单总金额
	private String spbill_create_ip = ""; // APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	private String notify_url = ""; // 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数
	private String trade_type = "NATIVE"; // JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
	private String openid = ""; // trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public void setTrade_type(String trade_type) {
		if (StringUtils.isNotBlank(trade_type)) {
			this.trade_type = trade_type;
		}
	}

	public String getTrade_type() {
		return trade_type;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
