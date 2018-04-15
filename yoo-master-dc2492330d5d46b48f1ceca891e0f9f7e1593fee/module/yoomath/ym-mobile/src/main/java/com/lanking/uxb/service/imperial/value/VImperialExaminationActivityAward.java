package com.lanking.uxb.service.imperial.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 颁奖排行信息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月5日
 */
public class VImperialExaminationActivityAward implements Serializable {
	private static final long serialVersionUID = 6845206572616721397L;

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 用户ID
	 */
	private long userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 班级ID
	 */
	private long clazzId;

	/**
	 * 班级名称.
	 */
	private String clazzName;

	/**
	 * 综合表现分.
	 */
	private Integer score;

	/**
	 * 排名.
	 */
	private Long rank;

	/**
	 * 平均用时(三次作业的平均用时).
	 */
	private Integer doTime;

	/**
	 * 奖品级别.
	 */
	private Integer awardLevel;

	/**
	 * 奖品联系人.
	 */
	private String awardContact;

	/**
	 * 奖品联系电话.
	 */
	private String awardContactNumber;

	/**
	 * 奖品送货地址.
	 */
	private String awardDeliveryAddress;
	
	/**
	 * 状态
	 */
	private Status status;

	/**
	 * 学校名称
	 */
	private String schoolName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getClazzId() {
		return clazzId;
	}

	public void setClazzId(long clazzId) {
		this.clazzId = clazzId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getDoTime() {
		return doTime;
	}

	public void setDoTime(Integer doTime) {
		this.doTime = doTime;
	}

	public Integer getAwardLevel() {
		return awardLevel;
	}

	public void setAwardLevel(Integer awardLevel) {
		this.awardLevel = awardLevel;
	}

	public String getAwardContact() {
		return awardContact;
	}

	public void setAwardContact(String awardContact) {
		this.awardContact = awardContact;
	}

	public String getAwardContactNumber() {
		return awardContactNumber;
	}

	public void setAwardContactNumber(String awardContactNumber) {
		this.awardContactNumber = awardContactNumber;
	}

	public String getAwardDeliveryAddress() {
		return awardDeliveryAddress;
	}

	public void setAwardDeliveryAddress(String awardDeliveryAddress) {
		this.awardDeliveryAddress = awardDeliveryAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
}
