package com.lanking.uxb.rescon.basedata.form;

import com.lanking.cloud.domain.common.resource.card.CardStatus;

/**
 * 考点操作form
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
public class ResconExaminationPointCardForm {
	private Long id;
	private long examinationPointId;
	private String description;
	private CardStatus checkStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getExaminationPointId() {
		return examinationPointId;
	}

	public void setExaminationPointId(long examinationPointId) {
		this.examinationPointId = examinationPointId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

}
