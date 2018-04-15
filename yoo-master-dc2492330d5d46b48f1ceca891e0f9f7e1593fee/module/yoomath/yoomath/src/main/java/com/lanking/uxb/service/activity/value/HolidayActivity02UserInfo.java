package com.lanking.uxb.service.activity.value;

import java.io.Serializable;


/**
 * 假期活动02 学生参加活动所需信息
 * 
 * <pre>
 * 20180116寒假活动
 * </pre>
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 * @version 2018年1月16日
 */
public class HolidayActivity02UserInfo implements Serializable {

	private static final long serialVersionUID = 2575966222650233741L;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 真实姓名
	 */
	private String realName;
	
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 教材版本
	 */
	private String category;
	
	/**
	 * 年级
	 */
	private String grade;
	
	/**
	 * 学校
	 */
	private String school;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
}
