package com.lanking.uxb.channelSales.memberPackage.form;

import com.lanking.cloud.domain.frame.system.Product;

/**
 * @author zemin.song
 */
public class ParameterForm {

	private Product product;
	private Long id;
	private String key;
	private String value;
	private String note;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
