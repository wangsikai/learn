package com.lanking.uxb.service.thirdparty.youngy.form;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
public class YoungyeduUser {
	private String userId;
	// 账户名称
	private String name;
	// 实际名称
	private String realName;
	// 昵称
	private String nickName;
	// 阶段
	private Integer phase;
	// 性别
	private Integer sex;
	// 用户类型
	private Integer userType;
	// 版本
	private Integer categoryCode;
	// 教材
	private Integer textbookCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

}
