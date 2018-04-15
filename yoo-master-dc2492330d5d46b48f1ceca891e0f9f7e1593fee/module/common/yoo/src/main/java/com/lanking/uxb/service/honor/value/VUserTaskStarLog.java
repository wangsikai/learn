package com.lanking.uxb.service.honor.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
public class VUserTaskStarLog implements Serializable {

	private static final long serialVersionUID = 9139346242637717112L;

	private long id;
	private long userId;
	private Date createAt;
	private int star;

	private List<Map<String, Object>> starLevels;

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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public List<Map<String, Object>> getStarLevels() {
		return starLevels;
	}

	public void setStarLevels(List<Map<String, Object>> starLevels) {
		this.starLevels = starLevels;
	}
}
