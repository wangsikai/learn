package com.lanking.uxb.rescon.basedata.form;

import java.util.List;

import com.lanking.cloud.domain.common.resource.card.CardStatus;

/**
 * (新)知识点保存操作form
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
public class ResconKnowledgePointCardForm {
	private Long id;
	private String name;
	private long knowpointCode;
	private String description;
	private String hints;
	private List<Long> questions;
	private CardStatus checkStatus;
	// 详细描述
	private String detailDescription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public List<Long> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Long> questions) {
		this.questions = questions;
	}

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}
}
