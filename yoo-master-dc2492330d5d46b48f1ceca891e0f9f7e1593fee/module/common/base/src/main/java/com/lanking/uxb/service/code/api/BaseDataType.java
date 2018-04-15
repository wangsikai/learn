package com.lanking.uxb.service.code.api;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
public enum BaseDataType {
	/**
	 * 职称
	 */
	TITLE,
	/**
	 * 职称
	 */
	DUTY,
	/**
	 * 教材版本
	 */
	TEXTBOOKCATEGORY,
	/**
	 * 教材
	 */
	TEXTBOOK,
	/**
	 * 阶段
	 */
	PHASE,
	/**
	 * 学科
	 */
	SUBJECT,
	/**
	 * 学期
	 */
	TERM,
	/**
	 * 学校
	 */
	SCHOOL,
	/**
	 * 知识点
	 */
	KNOWPOINT,
	/**
	 * 元知识点
	 */
	META_KNOWPOINT,
	/**
	 * 章节
	 */
	SECTION,
	/**
	 * 章节元知识点
	 */
	SECTION_META_KNOWPOINT,
	/**
	 * 资源类别
	 */
	RESOURCE_CATEGORY,
	/**
	 * 密保问题
	 */
	PASSWORDQUESTION,
	/**
	 * 知识体系(新)
	 */
	KNOWLEDGESYSTEM,
	/**
	 * 知识点(新)
	 */
	KNOWLEDGEPOINT,
	/**
	 * 系统配置
	 */
	PARAMETER,
	/**
	 * 同步知识点(新)
	 */
	KNOWLEDGESYNC,
	/**
	 * 复习知识点(新)
	 */
	KNOWLEDGEREVIEW;

	public static BaseDataType findByName(String type) {
		if (StringUtils.isBlank(type)) {
			return null;
		}
		try {
			return Enum.valueOf(BaseDataType.class, type);
		} catch (Exception e) {
			return null;
		}
	}
}
