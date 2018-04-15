package com.lanking.cloud.domain.base.file.api;

import java.io.File;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 抽象的文件工具类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public abstract class AbstractFileUtil {

	/**
	 * 从文件名中解析文件扩展名
	 * 
	 * @param fullName
	 *            文件名
	 * @return 扩展名
	 */
	public static String getSimpleExt(String fullName) {
		return StringUtils.substringAfterLast(fullName, ".").toLowerCase();
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param file
	 *            文件对象 {@link File}
	 * @return 扩展名
	 */
	public static String getExt(java.io.File file) {
		return getSimpleExt(file.getName());
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 扩展名
	 */
	public static String getExt(String filePath) {
		return getExt(new File(filePath));
	}

}