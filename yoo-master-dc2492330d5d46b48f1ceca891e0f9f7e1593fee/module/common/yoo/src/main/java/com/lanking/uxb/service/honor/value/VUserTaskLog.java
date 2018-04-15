package com.lanking.uxb.service.honor.value;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
public class VUserTaskLog implements Serializable {
	private static final long serialVersionUID = -4116536645257073382L;

	private long id;
	private long userId;
	private UserTaskType type;
	private Date createAt;
	private int coins;
	private int growth;
	private int star;
	// 完成情况显示值
	private String completeTitle;
	private UserTaskLogStatus status;
	private Map<String, Object> detail;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public UserTaskType getType() {
		return type;
	}

	public void setType(UserTaskType type) {
		this.type = type;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public UserTaskLogStatus getStatus() {
		return status;
	}

	public void setStatus(UserTaskLogStatus status) {
		this.status = status;
	}

	public Map<String, Object> getDetail() {
		return detail;
	}

	public void setDetail(Map<String, Object> detail) {
		this.detail = detail;
	}

	public String getCompleteTitle() {
		return completeTitle;
	}

	public void setCompleteTitle(String completeTitle) {
		this.completeTitle = completeTitle;
	}
}
