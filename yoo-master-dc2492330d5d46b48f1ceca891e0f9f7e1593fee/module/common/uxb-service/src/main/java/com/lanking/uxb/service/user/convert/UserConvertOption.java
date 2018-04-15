package com.lanking.uxb.service.user.convert;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
public class UserConvertOption {
	private boolean initMemberType = false;
	// 教师的学科信息
	private boolean initTeaSubject = true;
	// 用户的统计信息
	private boolean initUserState = true;
	// 阶段
	private boolean initPhase = true;
	// 教师职称信息
	private boolean initTeaTitle = true;
	// 教师职务
	private boolean initTeaDuty = true;
	// 学生本信息
	private boolean initTextbookCategory = true;
	// 学生教材信息
	private boolean initTextbook = true;

	public UserConvertOption() {
	}

	public UserConvertOption(boolean initMemberType) {
		this.initMemberType = initMemberType;
	}

	public boolean isInitMemberType() {
		return initMemberType;
	}

	public void setInitMemberType(boolean initMemberType) {
		this.initMemberType = initMemberType;
	}

	public boolean isInitTeaSubject() {
		return initTeaSubject;
	}

	public void setInitTeaSubject(boolean initTeaSubject) {
		this.initTeaSubject = initTeaSubject;
	}

	public boolean isInitUserState() {
		return initUserState;
	}

	public void setInitUserState(boolean initUserState) {
		this.initUserState = initUserState;
	}

	public boolean isInitPhase() {
		return initPhase;
	}

	public void setInitPhase(boolean initPhase) {
		this.initPhase = initPhase;
	}

	public boolean isInitTeaTitle() {
		return initTeaTitle;
	}

	public void setInitTeaTitle(boolean initTeaTitle) {
		this.initTeaTitle = initTeaTitle;
	}

	public boolean isInitTeaDuty() {
		return initTeaDuty;
	}

	public void setInitTeaDuty(boolean initTeaDuty) {
		this.initTeaDuty = initTeaDuty;
	}

	public boolean isInitTextbookCategory() {
		return initTextbookCategory;
	}

	public void setInitTextbookCategory(boolean initTextbookCategory) {
		this.initTextbookCategory = initTextbookCategory;
	}

	public boolean isInitTextbook() {
		return initTextbook;
	}

	public void setInitTextbook(boolean initTextbook) {
		this.initTextbook = initTextbook;
	}

}
