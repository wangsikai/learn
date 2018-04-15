package com.lanking.cloud.domain.base.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 短信消息模板
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "sms_template")
public class SmsTemplate extends MessageTemplate {

	private static final long serialVersionUID = -9110007259771509365L;

	/**
	 * 是否mock发送
	 */
	@Column(name = "mock", columnDefinition = "bit default 0")
	private boolean mock;

	public boolean isMock() {
		return mock;
	}

	public void setMock(boolean mock) {
		this.mock = mock;
	}

}
