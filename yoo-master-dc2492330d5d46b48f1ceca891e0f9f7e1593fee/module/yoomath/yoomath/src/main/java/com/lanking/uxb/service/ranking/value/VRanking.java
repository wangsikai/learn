package com.lanking.uxb.service.ranking.value;

import java.io.Serializable;

public abstract class VRanking implements Serializable {

	private static final long serialVersionUID = 165821543391502884L;

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
