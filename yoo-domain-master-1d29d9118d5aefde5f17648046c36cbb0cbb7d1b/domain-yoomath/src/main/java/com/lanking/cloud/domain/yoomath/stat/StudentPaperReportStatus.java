package com.lanking.cloud.domain.yoomath.stat;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月25日
 */
public enum StudentPaperReportStatus {
	/**
	 * 数据生成中
	 */
	DATA_PRODUCING,
	/**
	 * 文件生成中
	 */
	FILE_PRODUCING,
	/**
	 * 生成成功
	 */
	SUCCESS,
	/**
	 * 生成失败
	 */
	FAIL;

}
