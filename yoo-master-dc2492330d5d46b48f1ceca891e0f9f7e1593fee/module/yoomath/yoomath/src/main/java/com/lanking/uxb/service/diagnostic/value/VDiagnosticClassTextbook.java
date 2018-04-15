package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VDiagnosticClassTextbook implements Serializable {
	private static final long serialVersionUID = -2242098467468179507L;

	private long id;
	private long textbookCode;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(long textbookCode) {
		this.textbookCode = textbookCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
