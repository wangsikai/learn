package com.lanking.uxb.rescon.basedata.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.rescon.question.value.VQuestion;

/**
 * VO -> KnowledgePointCard
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
public class VResconKnowledgePointCard implements Serializable {
	private long id;
	private List<VQuestion> questions;
	private String description;
	private String detailDescription;
	private String hints;
	private CardStatus checkStatus;
	private Date createAt;
	private Date updateAt;
	private String name;
	private String checkStatusTitle;
	private Status status;

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

	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCheckStatusTitle() {
		return checkStatusTitle;
	}

	public void setCheckStatusTitle(String checkStatusTitle) {
		this.checkStatusTitle = checkStatusTitle;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}
}
