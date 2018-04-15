package com.lanking.uxb.service.payment.weixin.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支付结果通知.<br>
 * 详见：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
@XmlRootElement(name = "xml")
public class PayCallbackResult {

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
	 * 设备号.
	 */
	private String deviceInfo;

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
	 * 错误代码.
	 */
	private String errCode;

	/**
	 * 错误描述.
	 */
	private String errMsg;

	/**
	 * 用户OPENID.
	 */
	private String openid;

	/**
	 * 是否关注公众账号，用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	 */
	private String isSubscribe;

	/**
	 * 交易类型 JSAPI、NATIVE、APP.
	 */
	private String tradeType;

	/**
	 * 付款银行. 银行类型，采用字符串类型的银行标识。
	 */
	private String bankType;

	/**
	 * 微信支付订单号.
	 */
	private String transactionId;

	/**
	 * 商户订单号.
	 */
	private String outTradeNo;

	/**
	 * 商家附加数据.
	 */
	private String attach;

	/**
	 * 支付完成时间.<br>
	 * 格式为yyyyMMddHHmmss
	 */
	private String timeEnd;

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

	@XmlElement(name = "device_info")
	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
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

	@XmlElement(name = "err_code")
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	@XmlElement(name = "err_code_des")
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@XmlElement(name = "openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@XmlElement(name = "is_subscribe")
	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	@XmlElement(name = "trade_type")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	@XmlElement(name = "bank_type")
	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	@XmlElement(name = "transaction_id")
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@XmlElement(name = "out_trade_no")
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@XmlElement(name = "attach")
	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	@XmlElement(name = "time_end")
	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
}
