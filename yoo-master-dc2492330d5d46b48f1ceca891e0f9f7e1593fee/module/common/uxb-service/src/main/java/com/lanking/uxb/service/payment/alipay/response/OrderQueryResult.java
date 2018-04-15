package com.lanking.uxb.service.payment.alipay.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支付宝单笔交易订单查询结果集 single_trade_query
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月31日
 */
@XmlRootElement(name = "alipay")
public class OrderQueryResult {
	// 统一参数

	/**
	 * 返回码（T 代表成功，F 代表失败）.
	 */
	private String isSuccess;

	/**
	 * 签名.
	 */
	private String sign;

	/**
	 * 签名方式.
	 */
	private String sign_type;

	/**
	 * 错误信息.
	 */
	private String error;

	private OrderQueryResponse response;

	@XmlElement(name = "is_success")
	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	@XmlElement(name = "sign")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@XmlElement(name = "sign_type")
	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "response")
	public OrderQueryResponse getResponse() {
		return response;
	}

	public void setResponse(OrderQueryResponse response) {
		this.response = response;
	}

}
