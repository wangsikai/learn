package com.lanking.uxb.zycon.HolidayActivity02.value;

import java.io.Serializable;

/**
 * 周阶段VO
 * 
 * @author peng.zhao
 * @version 2018-1-18
 */
public class VWeekPhases implements Serializable {

	private static final long serialVersionUID = -1828434267196738345L;

	private Integer code;
	private String name;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
