package com.lanking.uxb.service.file.ex;

import com.lanking.cloud.ex.base.AbstractBasicServiceException;

public class FileException extends AbstractBasicServiceException {

	private static final long serialVersionUID = -449480125936938870L;

	static final int FILE_ERROR = 150;
	/**
	 * 文件空间不存在
	 */
	public static final int SPACE_NOT_EXIST = FILE_ERROR + 1;

	/**
	 * 文件空间大小达到上限
	 */
	public static final int SPACE_SIZE_LIMIT = FILE_ERROR + 2;

	/**
	 * 文件大小达到上限
	 */
	public static final int FILE_SIZE_LIMIT = FILE_ERROR + 3;

	/**
	 * 文件格式不合法
	 */
	public static final int FILE_TYPE_NOT_VALID = FILE_ERROR + 4;

	/**
	 * 图片样式不存在
	 */
	public static final int IMAGE_STYLE_NOT_EXIST = FILE_ERROR + 5;

	/**
	 * 处理图片样式错误
	 */
	public static final int IMAGE_STYLE_DEAL_ERROR = FILE_ERROR + 6;

	/**
	 * 文件已经存在
	 */
	public static final int FILE_EXIST = FILE_ERROR + 7;

	public FileException(int code, Object... args) {
		super(code, args);
	}
}
