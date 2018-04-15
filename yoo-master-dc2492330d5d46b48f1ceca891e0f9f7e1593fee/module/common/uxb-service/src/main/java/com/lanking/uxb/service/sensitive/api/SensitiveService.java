package com.lanking.uxb.service.sensitive.api;

import java.util.Collection;

import com.lanking.cloud.domain.common.baseData.SensitiveType;
import com.lanking.uxb.service.sensitive.ex.SensitiveException;

public interface SensitiveService {

	/**
	 * 判断是否含有禁止敏感词,有则抛出异常
	 * 
	 * @param text
	 *            文本
	 * @throws SensitiveException
	 */
	void containsForbidden(String text) throws SensitiveException;

	/**
	 * 判断是否含有禁止敏感词,有则抛出异常
	 * 
	 * @param texts
	 *            文本
	 * @throws SensitiveException
	 */
	void containsForbidden(Collection<String> texts) throws SensitiveException;

	/**
	 * 判断是否含有敏感词
	 * 
	 * @param type
	 *            敏感词类型
	 * @param text
	 *            文本
	 * @return true|false
	 */
	boolean contains(SensitiveType type, String text);

	/**
	 * 判断是否含有审核敏感词
	 * 
	 * @param text
	 *            文本
	 * @return true|false
	 */
	boolean containsAudit(String text);

	/**
	 * 替换敏感词
	 * 
	 * @param text
	 *            文本
	 * @return 处理替换词
	 */
	String replace(String text);

	/**
	 * 标黄
	 * 
	 * @param content
	 *            要处理的内容
	 * @return 处理后的内容
	 */
	String tagYellow(String text);

	void reload();
}
