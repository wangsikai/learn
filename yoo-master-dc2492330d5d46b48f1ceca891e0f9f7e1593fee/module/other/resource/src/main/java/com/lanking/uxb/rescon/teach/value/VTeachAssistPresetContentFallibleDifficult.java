package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.uxb.rescon.question.value.VQuestion;

public class VTeachAssistPresetContentFallibleDifficult implements Serializable {

	private static final long serialVersionUID = 1264183260244360762L;

	// 预置内容ID
	private Long teachassistPresetcontentId;

	// 名称
	private String name;

	// 易错点辨析
	private String analysis;

	// 针对性训练题目列表
	private List<Long> targetedTrainingQuestions;

	private List<VQuestion> vTargetedTrainingQuestions;

	// 校验状态
	private CardStatus checkStatus = CardStatus.EDITING;

	private VTeachAssistPresetContentFallibleDifficultExample example;

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

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public List<Long> getTargetedTrainingQuestions() {
		return targetedTrainingQuestions;
	}

	public void setTargetedTrainingQuestions(List<Long> targetedTrainingQuestions) {
		this.targetedTrainingQuestions = targetedTrainingQuestions;
	}

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public VTeachAssistPresetContentFallibleDifficultExample getExample() {
		return example;
	}

	public void setExample(VTeachAssistPresetContentFallibleDifficultExample example) {
		this.example = example;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<VQuestion> getvTargetedTrainingQuestions() {
		return vTargetedTrainingQuestions;
	}

	public void setvTargetedTrainingQuestions(List<VQuestion> vTargetedTrainingQuestions) {
		this.vTargetedTrainingQuestions = vTargetedTrainingQuestions;
	}

}
