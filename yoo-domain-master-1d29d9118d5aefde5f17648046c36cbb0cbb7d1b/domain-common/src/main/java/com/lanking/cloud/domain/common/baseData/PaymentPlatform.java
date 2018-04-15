package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 支付平台
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "payment_platform")
public class PaymentPlatform implements Serializable {
	private static final long serialVersionUID = -679350310721582593L;

	/**
	 * 代码
	 */
	@Id
	private Integer code;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 20)
	private String name;

	/**
	 * 序号
	 */
	@Column(name = "sequence")
	private Integer sequence;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
