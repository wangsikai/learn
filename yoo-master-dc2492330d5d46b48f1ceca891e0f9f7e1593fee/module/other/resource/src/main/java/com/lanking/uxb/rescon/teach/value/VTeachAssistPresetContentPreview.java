package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.uxb.rescon.question.value.VQuestion;

public class VTeachAssistPresetContentPreview implements Serializable {

	private static final long serialVersionUID = -2834301770912863116L;

	// 预置内容ID
	private Long teachassistPresetcontentId;

	// 名称
	private String name;

	// 知识点
	private List<String> vknowpoints = Lists.newArrayList();

	// 情景预设题目列表
	private List<VQuestion> vpreviewQuestions;

	// 预习自测题目列表
	private List<VQuestion> vselfTestQuestions;

	private List<Long> knowpoints = Lists.newArrayList();

	private List<Long> previewQuestions;

	private List<Long> selfTestQuestions;

	// 校验状态
	private CardStatus checkStatus = CardStatus.EDITING;

	// 预习点
	private Long id;

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

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public List<String> getVknowpoints() {
		return vknowpoints;
	}

	public void setVknowpoints(List<String> vknowpoints) {
		this.vknowpoints = vknowpoints;
	}

	public List<VQuestion> getVpreviewQuestions() {
		return vpreviewQuestions;
	}

	public void setVpreviewQuestions(List<VQuestion> vpreviewQuestions) {
		this.vpreviewQuestions = vpreviewQuestions;
	}

	public List<VQuestion> getVselfTestQuestions() {
		return vselfTestQuestions;
	}

	public void setVselfTestQuestions(List<VQuestion> vselfTestQuestions) {
		this.vselfTestQuestions = vselfTestQuestions;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
