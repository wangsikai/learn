package com.lanking.uxb.zycon.parameter.value;

import java.io.Serializable;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 系统参数VO
 */
public class VParameter implements Serializable {

	private static final long serialVersionUID = -3993378583408575130L;

	private Long id;
	private String key;
	private String value;
	private Product product;
	private String note;
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
