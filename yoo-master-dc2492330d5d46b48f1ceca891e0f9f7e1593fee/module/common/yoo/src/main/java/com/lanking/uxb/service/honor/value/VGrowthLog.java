package com.lanking.uxb.service.honor.value;

import java.io.Serializable;
import java.util.Date;

public class VGrowthLog implements Serializable {

	private static final long serialVersionUID = 8936867311360054225L;

	private Date createAt;
	private String description;
	private int growth;

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

}
