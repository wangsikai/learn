package com.lanking.uxb.service.thirdparty.jsedu.response;

import httl.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import com.lanking.uxb.service.thirdparty.TokenInfo;

public class JSEduUser implements Serializable {
	private static final long serialVersionUID = 8760858156768931191L;

	/**
	 * 结果返回码.
	 */
	private String resultCode;

	/**
	 * 用户id，同uid.
	 */
	private String identityId;

	/**
	 * 用户类型，1-老师 5-家长.
	 */
	private String identityType;

	/**
	 * 用户名.
	 */
	private String userName;

	/**
	 * 机构代码.
	 */
	private String orgCode;

	/**
	 * 用户昵称.
	 */
	private String nickName;

	/**
	 * 附加属性.
	 */
	private JSEduUserAttrMap JSEduUserAttrMap;

	/**
	 * TOKEN.
	 */
	private TokenInfo tokenInfo;

	/**
	 * 教师班级.
	 */
	private List<JSEduTeacherClass> teacherClasses;

	/**
	 * 学生详细信息.
	 */
	private JSEduStudent studentinfo;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		String name = StringUtils.isBlank(userName) ? "jsedu" : "jsedu" + userName;
		String newName = "";
		char[] cs = name.toCharArray();
		int len = 0;
		for (char c : cs) {
			String n = String.valueOf(c);
			if (Pattern.compile("([a-z]|[A-Z]|[0-9]|[_])+").matcher(n).matches()) {
				len += 1;
			} else if (Pattern.compile("([\\u4e00-\\u9fa5])+").matcher(n).matches()) {
				len += 2;
			}
			newName += n;
			if (len >= 16) {
				break;
			}
		}
		this.userName = newName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public JSEduUserAttrMap getJSEduUserAttrMap() {
		return JSEduUserAttrMap;
	}

	public void setJSEduUserAttrMap(JSEduUserAttrMap jSEduUserAttrMap) {
		JSEduUserAttrMap = jSEduUserAttrMap;
	}

	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}

	public void setTokenInfo(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}

	public List<JSEduTeacherClass> getTeacherClasses() {
		return teacherClasses;
	}

	public void setTeacherClasses(List<JSEduTeacherClass> teacherClasses) {
		this.teacherClasses = teacherClasses;
	}

	public JSEduStudent getStudentinfo() {
		return studentinfo;
	}

	public void setStudentinfo(JSEduStudent studentinfo) {
		this.studentinfo = studentinfo;
	}
}
