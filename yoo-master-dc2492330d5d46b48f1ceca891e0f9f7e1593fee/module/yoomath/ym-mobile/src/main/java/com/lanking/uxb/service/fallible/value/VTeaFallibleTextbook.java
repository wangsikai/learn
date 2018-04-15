package com.lanking.uxb.service.fallible.value;

import java.io.Serializable;

import com.lanking.uxb.service.code.value.VTextbook;

public class VTeaFallibleTextbook implements Serializable {

	private static final long serialVersionUID = 1014274187595215171L;

	private VTextbook textbook;
	// 教材下的错题数量
	private Integer fallibleCount;

	public VTeaFallibleTextbook() {
		super();
	}

	public VTeaFallibleTextbook(VTextbook textbook) {
		super();
		this.textbook = textbook;
	}

	public VTeaFallibleTextbook(VTextbook textbook, Integer fallibleCount) {
		super();
		this.textbook = textbook;
		this.fallibleCount = fallibleCount;
	}

	public VTextbook getTextbook() {
		return textbook;
	}

	public void setTextbook(VTextbook textbook) {
		this.textbook = textbook;
	}

	public Integer getFallibleCount() {
		return fallibleCount;
	}

	public void setFallibleCount(Integer fallibleCount) {
		this.fallibleCount = fallibleCount;
	}

}
