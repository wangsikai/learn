package com.lanking.cloud.domain.base.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 消息模板基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@MappedSuperclass
public abstract class MessageTemplate implements Serializable {

	private static final long serialVersionUID = 9132336551061534202L;

	/**
	 * 消息模板编码
	 * 
	 * <pre>
	 *      sms:从10000000开始编码
	 * 	  email:从11000000开始编码
	 * 	   push:从12000000开始编码
	 * 	 notice:从13000000开始编码
	 * </pre>
	 */
	@Id
	private int code;

	/**
	 * 内容
	 */
	@Column(name = "body", length = 4000)
	private String body;

	/**
	 * 备注
	 */
	@Column(name = "note", length = 1024)
	private String note;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
