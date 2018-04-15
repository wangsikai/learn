package com.lanking.uxb.rescon.basedata.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.uxb.rescon.question.value.VQuestion;

/**
 * @author xinyu.zhou
 * @since 2.0.1
 */
public class VResconExaminationPointCard implements Serializable {
	private long id;
	private List<VQuestion> questions;
	private String description;
	private CardStatus checkStatus;
	private Date createAt;
	private Date updateAt;
	private String checkStatusTitle;
	private Long examinationId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<VQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<VQuestion> questions) {
		this.questions = questions;
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getCheckStatusTitle() {
		return checkStatusTitle;
	}

	public Long getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Long examinationId) {
		this.examinationId = examinationId;
	}

	public void setCheckStatusTitle(String checkStatusTitle) {
		this.checkStatusTitle = checkStatusTitle;
	}
}
