package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 密保问题表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "password_question")
public class PasswordQuestion implements Serializable {

	private static final long serialVersionUID = 6270218929347249456L;

	/**
	 * 代码
	 */
	@Id
	private Integer code;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
