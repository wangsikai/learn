package com.lanking.uxb.service.user.form;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 编辑个人资料form
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年6月17日 上午10:38:07
 */
public class EditProfileForm implements Serializable {

	private static final long serialVersionUID = 6018718550840121237L;
	private Long id;
	private String name;
	private String nickname;
	private String accountName;
	private Integer enterYear;
	private Sex sex;
	private String schoolName;
	private Integer titleCode;
	private Integer dutyCode;
	private String workAt;
	private UserType type;
	private Long schoolCode;
	private String birthDay;
	private Integer phaseCode;
	private Integer textBookCode;
	private Integer textBookCategoryCode;

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
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

	public String getWorkAt() {
		return workAt;
	}

	public void setWorkAt(String workAt) {
		this.workAt = workAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public Long getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(Long schoolCode) {
		this.schoolCode = schoolCode;
	}

	public Integer getTextBookCode() {
		return textBookCode;
	}

	public void setTextBookCode(Integer textBookCode) {
		this.textBookCode = textBookCode;
	}

	public Integer getTextBookCategoryCode() {
		return textBookCategoryCode;
	}

	public void setTextBookCategoryCode(Integer textBookCategoryCode) {
		this.textBookCategoryCode = textBookCategoryCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Integer getEnterYear() {
		return enterYear;
	}

	public void setEnterYear(Integer enterYear) {
		this.enterYear = enterYear;
	}

}
