package com.lanking.cloud.domain.base.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 短信消息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "sms")
public class Sms extends Message {

	private static final long serialVersionUID = 7463664277399153312L;

	/**
	 * 发送结果
	 */
	@Column(name = "call_ret", length = 512)
	private String callRet;

	public String getCallRet() {
		return callRet;
	}

	public void setCallRet(String callRet) {
		this.callRet = callRet;
	}

}
