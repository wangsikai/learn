package com.lanking.uxb.service.user.form;

import java.io.Serializable;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;

public class RegisterForm implements Serializable {

	private static final long serialVersionUID = 3107891101978244700L;

	// 用户名
	private String name;
	private String email;
	private String mobile;
	private String password;
	private String pwd;
	private String classCode;
	private Long classId;
	private long schoolId;
	private UserType type;

	/**
	 * 验证码
	 * 
	 * @since 2.1
	 */
	private String verifyCode;
	/**
	 * 阶段代码
	 * 
	 * @since 2.1
	 */
	private Integer phaseCode;
	/**
	 * 学科代码
	 * 
	 * @since 2.1
	 */
	private Integer subjectCode;
	/**
	 * 正式姓名
	 * 
	 * @since 2.1
	 */
	private String realName;
	/**
	 * 昵称
	 * 
	 * @since 2.3
	 */
	private String nickname;
	/**
	 * 密码强度.
	 * 
	 * @since 2.1
	 */
	private Integer strength;

	// *** 第三方注册属性

	/**
	 * 绑定的账户ID.
	 */
	private Long accountId;

	/**
	 * 第三方类型(thirdparty).
	 * 
	 * @since 2.1
	 */
	private CredentialType credentialType;

	/**
	 * TOKEN(thirdparty).
	 * 
	 * @since 2.1
	 */
	private String token;

	/**
	 * 第三方用户ID(thirdparty).
	 * 
	 * @since 2.1
	 */
	private String uid;

	/**
	 * 第三方用户名.
	 * 
	 * @since yoomath 1.7
	 */
	private String thirdName;

	/**
	 * TOKEN有效期
	 * 
	 * @since 2.1
	 */
	private Long endTime;

	/**
	 * 注册源
	 */
	private Product source;

	/**
	 * 转换好的本地头像ID.
	 */
	private Long avatarId;

	/**
	 * qq号
	 * 
	 * @since yoomath(mobile) V1.1.0
	 */
	private String qq;

	/**
	 * 性别
	 * 
	 * @since yoomath(mobile) V1.1.0
	 */
	private Sex sex;
	/**
	 * 与融捷数据进行同步设置用户渠道商code
	 *
	 * 其他地方都不应该设置此值！！！！
	 *
	 * @since 3.0.3
	 */
	private Integer channelCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public CredentialType getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(CredentialType credentialType) {
		this.credentialType = credentialType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Product getSource() {
		return source;
	}

	public void setSource(Product source) {
		this.source = source;
	}

	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

}
