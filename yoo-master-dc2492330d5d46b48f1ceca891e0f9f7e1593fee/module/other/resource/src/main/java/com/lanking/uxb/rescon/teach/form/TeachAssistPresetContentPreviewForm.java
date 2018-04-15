package com.lanking.uxb.rescon.teach.form;

import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.card.CardStatus;

public class TeachAssistPresetContentPreviewForm {

	private Long id;

	// 预置内容ID
	private Long teachassistPresetcontentId;

	// 名称
	private String name;

	// 知识点
	private List<Long> knowpoints = Lists.newArrayList();

	// 情景预设题目列表
	private List<Long> previewQuestions;

	// 预习自测题目列表
	private List<Long> selfTestQuestions;

	private Long userId;

	private Long knowledgeSystemCode;

	private CardStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeachassistPresetcontentId() {
		return teachassistPresetcontentId;
	}

	public void setTeachassistPresetcontentId(Long teachassistPresetcontentId) {
		this.teachassistPresetcontentId = teachassistPresetcontentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<Long> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public List<Long> getPreviewQuestions() {
		return previewQuestions;
	}

	public void setPreviewQuestions(List<Long> previewQuestions) {
		this.previewQuestions = previewQuestions;
	}

	public List<Long> getSelfTestQuestions() {
		return selfTestQuestions;
	}

	public void setSelfTestQuestions(List<Long> selfTestQuestions) {
		this.selfTestQuestions = selfTestQuestions;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getKnowledgeSystemCode() {
		return knowledgeSystemCode;
	}

	public void setKnowledgeSystemCode(Long knowledgeSystemCode) {
		this.knowledgeSystemCode = knowledgeSystemCode;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

}
