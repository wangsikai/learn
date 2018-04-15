package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;

/**
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public class VZycYoomathCustomerServiceSession implements Serializable {

	private static final long serialVersionUID = 6780258919443472245L;

	private long id;
	private long userId;
	private String userName;
	private Long schoolId;
	private Integer phaseCode;
	private String schoolName;
	private long unreadCount;
	private VZycAccount account;
	private String sex;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(long unreadCount) {
		this.unreadCount = unreadCount;
	}

	public VZycAccount getAccount() {
		return account;
	}

	public void setAccount(VZycAccount account) {
		this.account = account;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
