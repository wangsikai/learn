package com.lanking.uxb.service.activity.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class VHolidayActivity01Class implements Serializable {

	private static final long serialVersionUID = 4561958260453578296L;

	private Long id;

	/**
	 * 班级id
	 */
	private Long classId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 作业提交率
	 */
	private BigDecimal submitRate;

	List<VHolidayActivity01ClassUser> userList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSubmitRate() {
		return submitRate;
	}

	public void setSubmitRate(BigDecimal submitRate) {
		this.submitRate = submitRate;
	}

	public List<VHolidayActivity01ClassUser> getUserList() {
		return userList;
	}

	public void setUserList(List<VHolidayActivity01ClassUser> userList) {
		this.userList = userList;
	}

}
