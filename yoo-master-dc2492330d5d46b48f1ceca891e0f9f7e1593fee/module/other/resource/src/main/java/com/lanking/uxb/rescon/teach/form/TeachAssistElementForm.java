package com.lanking.uxb.rescon.teach.form;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;

/**
 * 教辅模块form
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class TeachAssistElementForm {
	// id
	private Long id;
	// 模块类型
	private TeachAssistElementType type;
	// 传递form参数
	private String paramForm;
	// 对应目录id
	private Long catalogId;
	// 操作人id
	private Long userId;
	// 排序值
	private Integer sequence;

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TeachAssistElementType getType() {
		return type;
	}

	public void setType(TeachAssistElementType type) {
		this.type = type;
	}

	public String getParamForm() {
		return paramForm;
	}

	public void setParamForm(String paramForm) {
		this.paramForm = paramForm;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
