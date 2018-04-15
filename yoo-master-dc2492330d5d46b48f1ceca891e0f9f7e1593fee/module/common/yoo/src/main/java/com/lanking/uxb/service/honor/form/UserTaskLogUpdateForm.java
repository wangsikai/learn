package com.lanking.uxb.service.honor.form;

import java.util.Date;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
public class UserTaskLogUpdateForm {
	private Long id;
	private Integer coins;
	private String content;
	private Integer growth;
	private Integer star;
	private UserTaskLogStatus status;
	private int taskCode;
	private UserTaskType type;
	private long userId;
	private Date date;
	private Date completeAt;
	private Date receiveAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCoins() {
		return coins;
	}

	public void setCoins(Integer coins) {
		this.coins = coins;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getGrowth() {
		return growth;
	}

	public void setGrowth(Integer growth) {
		this.growth = growth;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public UserTaskLogStatus getStatus() {
		return status;
	}

	public void setStatus(UserTaskLogStatus status) {
		this.status = status;
	}

	public int getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(int taskCode) {
		this.taskCode = taskCode;
	}

	public UserTaskType getType() {
		return type;
	}

	public void setType(UserTaskType type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getCompleteAt() {
		return completeAt;
	}

	public void setCompleteAt(Date completeAt) {
		this.completeAt = completeAt;
	}

	public Date getReceiveAt() {
		return receiveAt;
	}

	public void setReceiveAt(Date receiveAt) {
		this.receiveAt = receiveAt;
	}
}
