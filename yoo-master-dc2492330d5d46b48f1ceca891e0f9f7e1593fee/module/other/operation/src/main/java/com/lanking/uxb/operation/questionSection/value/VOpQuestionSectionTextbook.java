package com.lanking.uxb.operation.questionSection.value;

public class VOpQuestionSectionTextbook {
	private int code;
	private String name;
	// 此教材是否存在进行数据转换
	private boolean converting = false;

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

	public boolean isConverting() {
		return converting;
	}

	public void setConverting(boolean converting) {
		this.converting = converting;
	}

}
