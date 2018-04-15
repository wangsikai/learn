package com.lanking.uxb.service.imperial02.value;

import java.io.Serializable;

/**
 * 活动规则相关信息VO
 * 
 * @author peng.zhao
 * @version 2017年11月10日
 */
public class VImperialExaminationActivityLottery implements Serializable {

	private static final long serialVersionUID = -5209466008594538503L;

	private long id;
	private long code;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
