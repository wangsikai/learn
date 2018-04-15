package com.lanking.uxb.service.honor.value;

import java.io.Serializable;
import java.util.Date;

public class VCoinsLog implements Serializable {

	private static final long serialVersionUID = -6781528505125402022L;

	private Date createAt;
	private String description;
	private int coins;

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

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

}
