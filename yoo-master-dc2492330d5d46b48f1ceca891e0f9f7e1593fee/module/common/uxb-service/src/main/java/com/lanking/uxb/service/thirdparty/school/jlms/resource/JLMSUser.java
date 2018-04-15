package com.lanking.uxb.service.thirdparty.school.jlms.resource;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 九龙中学用户对象.
 * 
 * @author wlche
 *
 */
public class JLMSUser implements Serializable {
	private static final long serialVersionUID = 3435842818053264293L;

	/**
	 * 用户ID.
	 */
	private String userid;

	/**
	 * 姓名.
	 */
	private String name;

	/**
	 * 真实姓名.
	 */
	private String realName;

	/**
	 * 用户类型.
	 */
	private UserType userType;

	/**
	 * 用户性别.
	 */
	private Sex sex;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

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

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
