package com.lanking.cloud.domain.yoomath.fallible;

import com.lanking.cloud.sdk.bean.Valueable;

public enum ClassFallibleExportRecordStatus implements Valueable {

	/**
	 * 默认初始化状态.
	 */
	INIT(0),

	/**
	 * 文件处理中.
	 */
	PROCESSING(1),

	/**
	 * 已完成.
	 */
	COMPLETE(2),

	/**
	 * 已删除.
	 */
	DELETED(3),
	
	/**
	 * 所选的学生没有错题记录
	 */
	NONE(4),
	/**
	 * 导出过程遇到错误
	 */
	ERROR(5);
	
	private final int value;

	private ClassFallibleExportRecordStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ClassFallibleExportRecordStatus findByValue(int value) {
		switch (value) {
		case 0:
			return INIT;
		case 1:
			return PROCESSING;
		case 2:
			return COMPLETE;
		case 3:
			return DELETED;
		case 4:
			return NONE;
		case 5:
			return ERROR;
		default:
			return null;
		}
	}
}
