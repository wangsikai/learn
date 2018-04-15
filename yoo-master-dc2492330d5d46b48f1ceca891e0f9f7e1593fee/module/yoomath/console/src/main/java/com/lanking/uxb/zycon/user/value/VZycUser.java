package com.lanking.uxb.zycon.user.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 用户管理VO
 * 
 * @author wangsenhao
 *
 */
public class VZycUser implements Serializable {

	private static final long serialVersionUID = -1045917008935475374L;

	private UserType userType;
	private Status status;
	private Sex sex;
	private Integer phaseCode;
	private String phaseName;
	private Integer subjectCode;
	private String accountName;
	private String name;
	private String mobile;
	private String email;
	private String schoolName;
	private Date workAt;
	private Integer titleCode;
	private Integer dutyCode;
	private String titleName;
	private String dutyName;
	private Long userId;
	private Long accountId;
	private Date createAt;
	private boolean homeworkSms = true;

	private Integer channelCode;
	private String channelName;
	private Long schoolId;
	/**
	 * 用户会员描述
	 * 
	 * @since 2.5.0
	 */
	private String memberTypeDes;

	private Date StartAt;

	private Date endAt;

	// 学生加入学校时间
	private Integer enterYear;

	// 账户激活状态
	private Status activationStatus;

	public boolean isHomeworkSms() {
		return homeworkSms;
	}

	public void setHomeworkSms(boolean homeworkSms) {
		this.homeworkSms = homeworkSms;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getDutyName() {
		return dutyName;
	}

	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Integer getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(Integer titleCode) {
		this.titleCode = titleCode;
	}

	public Integer getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(Integer dutyCode) {
		this.dutyCode = dutyCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public Date getWorkAt() {
		return workAt;
	}

	public void setWorkAt(Date workAt) {
		this.workAt = workAt;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getMemberTypeDes() {
		return memberTypeDes;
	}

	public void setMemberTypeDes(String memberTypeDes) {
		this.memberTypeDes = memberTypeDes;
	}

	public Date getStartAt() {
		return StartAt;
	}

	public void setStartAt(Date startAt) {
		StartAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Integer getEnterYear() {
		return enterYear;
	}

	public void setEnterYear(Integer enterYear) {
		this.enterYear = enterYear;
	}

	public Status getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(Status activationStatus) {
		this.activationStatus = activationStatus;
	}

}
