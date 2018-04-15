package com.lanking.uxb.service.thirdparty.scedu.resource;

import java.io.Serializable;

import com.lanking.uxb.service.thirdparty.TokenInfo;

/**
 * 四川教育平台用户.
 * 
 * @author wlche
 *
 */
public class SCEduUser implements Serializable {
	private static final long serialVersionUID = 9205250731748458917L;

	/**
	 * 用户ID.
	 */
	private String personid;

	/**
	 * 姓名.
	 */
	private String name;

	/**
	 * 帐号.
	 */
	private String account;

	/**
	 * 原始姓名（未加工）.
	 */
	private String oaccount;

	/**
	 * 用户类型（26:学生;27：教师）.
	 */
	private Integer type;

	/**
	 * TOKEN.
	 */
	private TokenInfo tokenInfo;

	private SCEduTeacher teacher;

	private SCEduStudent student;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonid() {
		return personid;
	}

	public void setPersonid(String personid) {
		this.personid = personid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOaccount() {
		return oaccount;
	}

	public void setOaccount(String oaccount) {
		this.oaccount = oaccount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}

	public void setTokenInfo(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}

	public SCEduTeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(SCEduTeacher teacher) {
		this.teacher = teacher;
	}

	public SCEduStudent getStudent() {
		return student;
	}

	public void setStudent(SCEduStudent student) {
		this.student = student;
	}
}
