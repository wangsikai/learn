package com.lanking.uxb.service.payment.alipay.response;

/**
 * 支付宝支付通知返回结果.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
public class PayCallbackResult {

	// 统一参数

	/**
	 * 返回码.
	 */
	private String code;

	/**
	 * 返回码描述.
	 */
	private String msg;

	/**
	 * 业务返回码.
	 */
	private String sub_code;

	/**
	 * 业务返回码描述.
	 */
	private String sub_msg;

	/**
	 * 签名.
	 */
	private String sign;

	// 业务参数

	/**
	 * 通知发送时间.<br>
	 * 
	 * 格式为yyyy-MM-dd HH:mm:ss。
	 */
	private String notify_time;

	/**
	 * 商户本地订单号.
	 */
	private String out_trade_no;

	/**
	 * 商品名称.
	 */
	private String subject;

	/**
	 * 支付宝交易流水号（最长64位）.
	 */
	private String trade_no;

	/**
	 * 交易状态.<br>
	 * WAIT_BUYER_PAY 交易创建，等待买家付款<br>
	 * TRADE_CLOSED 在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。<br>
	 * TRADE_SUCCESS 交易成功，且可对该交易做操作，如：多级分润、退款等。<br>
	 * TRADE_PENDING 等待卖家收款（买家付款后，如果卖家账号被冻结）。<br>
	 * TRADE_FINISHED 交易成功且结束，即不可再做任何操作
	 */
	private String trade_status;

	/**
	 * 交易创建时间，格式为yyyy-MM-dd HH:mm:ss.
	 */
	private String gmt_create;

	/**
	 * 交易付款时间.
	 */
	private String gmt_payment;

	/**
	 * 交易关闭时间.
	 */
	private String gmt_close;

	/**
	 * 买家支付宝帐号（买家支付宝账号，可以是Email或手机号码）.
	 */
	private String buyer_email;

	/**
	 * 买家支付宝ID（买家支付宝账号对应的支付宝唯一用户号，以2088开头的纯16位数字）.
	 */
	private String buyer_id;

	/**
	 * 交易金额.
	 */
	private String total_fee;

	/**
	 * 折扣（支付宝系统会把discount的值加到交易金额上，如果需要折扣，本参数为负数）.
	 */
	private String discount;

	/**
	 * 是否调整过价格（Y、N）.
	 */
	private String is_total_fee_adjust;

	/**
	 * 附加数据（用于商户回传参数，该值不能包含“=”、“&”等特殊字符）.
	 */
	private String extra_common_param;

	/**
	 * 是否是扫码支付.<br>
	 * 回传给商户此标识为qrpay时，表示对应交易为扫码支付。<br>
	 * 目前只有qrpay一种回传值。<br>
	 * 非扫码支付方式下，目前不会返回该参数。<br>
	 */
	private String business_scene;

	public String getNotify_time() {
		return notify_time;
	}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
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
		this.subject = subject;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	public String getGmt_payment() {
		return gmt_payment;
	}

	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	public String getGmt_close() {
		return gmt_close;
	}

	public void setGmt_close(String gmt_close) {
		this.gmt_close = gmt_close;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getIs_total_fee_adjust() {
		return is_total_fee_adjust;
	}

	public void setIs_total_fee_adjust(String is_total_fee_adjust) {
		this.is_total_fee_adjust = is_total_fee_adjust;
	}

	public String getExtra_common_param() {
		return extra_common_param;
	}

	public void setExtra_common_param(String extra_common_param) {
		this.extra_common_param = extra_common_param;
	}

	public String getBusiness_scene() {
		return business_scene;
	}

	public void setBusiness_scene(String business_scene) {
		this.business_scene = business_scene;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSub_code() {
		return sub_code;
	}

	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}

	public String getSub_msg() {
		return sub_msg;
	}

	public void setSub_msg(String sub_msg) {
		this.sub_msg = sub_msg;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
