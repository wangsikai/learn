package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;

import com.lanking.uxb.service.code.value.VTextbook;

public class VTeaCollectionTextbook implements Serializable {

	private static final long serialVersionUID = 1481275291320414666L;

	private VTextbook textbook;
	// 教材下的收藏数量
	private Integer collectionCount;

	public VTeaCollectionTextbook() {
		super();
	}

	public VTeaCollectionTextbook(VTextbook textbook) {
		super();
		this.textbook = textbook;
	}

	public VTeaCollectionTextbook(VTextbook textbook, Integer collectionCount) {
		super();
		this.textbook = textbook;
		this.collectionCount = collectionCount;
	}

	public VTextbook getTextbook() {
		return textbook;
	}

	public void setTextbook(VTextbook textbook) {
		this.textbook = textbook;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}

}
