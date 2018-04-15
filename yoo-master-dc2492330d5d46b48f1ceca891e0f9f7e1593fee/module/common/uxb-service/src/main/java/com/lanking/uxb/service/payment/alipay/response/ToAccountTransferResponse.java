package com.lanking.uxb.service.payment.alipay.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 转账响应.
 * 
 * @author wanlong.che
 *
 */
@Getter
@Setter
public class ToAccountTransferResponse {

	private String code;

	private String msg;

	private String sub_code;

	private String sub_msg;

	private String sign;
}
