package com.lanking.uxb.service.payment.weixin.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 统一支付返回数据.<br>
 * 详见：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
@XmlRootElement(name = "xml")
public class UnifiedPayResult {

	/**
	 * 返回状态码.
	 */
	private String returnCode;

	/**
	 * 返回信息.
	 */
	private String returnMsg;

	/**
	 * 公众号APPID.
	 */
	private String appid;

	/**
	 * 商户ID.
	 */
	private String mchId;

	/**
	 * 随机字串.
	 */
	private String nonceStr;

	/**
	 * 签名.
	 */
	private String sign;

	/**
	 * 业务结果.
	 */
	private String resultCode;

	/**
	 * 微信端业务支付ID.
	 */
	private String prepayId;

	/**
	 * 微信支付模式.
	 */
	private String tradeType;

	/**
	 * 二维码链接.
	 */
	private String code_url;

	/**
	 * 随机字串2（调用时产生的，非返回产生的随机字串）.
	 */
	private String nonceStrRequest;

	@XmlElement(name = "return_code")
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	@XmlElement(name = "return_msg")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	@XmlElement(name = "appid")
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	@XmlElement(name = "mch_id")
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	@XmlElement(name = "nonce_str")
	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	@XmlElement(name = "sign")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@XmlElement(name = "result_code")
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@XmlElement(name = "prepay_id")
	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	@XmlElement(name = "trade_type")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	@XmlElement(name = "code_url")
	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}

	public String getNonceStrRequest() {
		return nonceStrRequest;
	}

	public void setNonceStrRequest(String nonceStrRequest) {
		this.nonceStrRequest = nonceStrRequest;
	}

}
