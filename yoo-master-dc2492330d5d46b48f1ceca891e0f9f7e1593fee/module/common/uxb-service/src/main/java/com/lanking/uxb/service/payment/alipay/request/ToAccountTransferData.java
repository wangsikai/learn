package com.lanking.uxb.service.payment.alipay.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 转账交易.
 * 
 * @author wanlong.che
 *
 */
@Getter
@Setter
public class ToAccountTransferData {

	/**
	 * 商户转账唯一订单号.
	 */
	private String out_biz_no;

	/**
	 * 使用支付宝登录号作为转账依据.
	 */
	private String payee_type = "ALIPAY_LOGONID";

	/**
	 * 收款方账号
	 */
	private String payee_account;

	/**
	 * 转账金额.
	 * <p>
	 * 只支持2位小数，小数点前最大支持13位，金额必须大于等于0.1元。 最大转账金额以实际签约的限额为准
	 * </p>
	 */
	private String amount;

	/**
	 * 付款方姓名.
	 */
	private String payer_show_name;

	/**
	 * 收款方真实姓名.
	 */
	private String payee_real_name;

	/**
	 * 转账备注.
	 */
	private String remark;
}
