package com.lanking.uxb.service.payment.alipay.request;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 即时到帐交易提交数据.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
public class DirectPayData {

	/**
	 * 商户订单号.
	 */
	private String out_trade_no;

	/**
	 * 订单名称.
	 */
	private String subject;

	/**
	 * 订单金额.
	 */
	private String total_fee;

	/**
	 * 商品描述.
	 */
	private String body;

	/**
	 * 商品单价.
	 */
	private String price;

	/**
	 * 商品数量.
	 */
	private int quantity;

	/**
	 * 附加数据（长度不超过100位）.<br>
	 * 如果用户请求时传递了该参数，则返回给商户时会回传该参数。
	 */
	private String extra_common_param;

	/**
	 * 商品类型.<br>
	 * 商品类型：1表示实物类商品 、 0表示虚拟类商品
	 */
	private String goods_type;

	/**
	 * 超时时间，默认2h.
	 */
	private String it_b_pay = "2h";

	/**
	 * 是否使用支付宝客户端支付, Y 是
	 */
	private String app_pay;

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj.toString());
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		String productTitle = StringUtils.isBlank(subject) ? "订单商品" : subject;
		productTitle = productTitle.length() > 30 ? (productTitle.substring(0, 30) + "...") : productTitle;
		this.subject = productTitle;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getExtra_common_param() {
		return extra_common_param;
	}

	public void setExtra_common_param(String extra_common_param) {
		this.extra_common_param = extra_common_param;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getIt_b_pay() {
		return it_b_pay;
	}

	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}

	public String getApp_pay() {
		return app_pay;
	}

	public void setApp_pay(String app_pay) {
		this.app_pay = app_pay;
	}
}
