package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 习题应用场景
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "question_scene")
public class QuestionScene implements Serializable {
	private static final long serialVersionUID = -3956782585196408876L;

	/**
	 * 代码
	 */
	@Id
	private int code;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(precision = 3, columnDefinition = "tinyint default 0")
	private Status status = Status.ENABLED;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
