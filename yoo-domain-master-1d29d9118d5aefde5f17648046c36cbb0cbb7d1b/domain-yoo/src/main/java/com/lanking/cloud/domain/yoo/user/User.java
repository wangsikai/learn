package com.lanking.cloud.domain.yoo.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.bean.Userable;

/**
 * 用户
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user")
public class User implements Userable, Serializable {

	private static final long serialVersionUID = -5821996501274854322L;

	@Id
	private Long id;

	/**
	 * 对应账户ID
	 */
	@Column(name = "account_id")
	private Long accountId;

	/**
	 * 用户类型
	 */
	@Column(name = "user_type", precision = 3)
	private UserType userType;

	/**
	 * 用户名
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 昵称
	 */
	@Column(name = "nickname", length = 40)
	private String nickname;

	/**
	 * 用户状态,对应account中的status
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	/**
	 * 是否是导入用户
	 */
	@Column(name = "import0", columnDefinition = "bit default 0")
	private boolean import0 = false;

	/**
	 * 用户所属渠道
	 */
	@Column(name = "user_channel_code", precision = 5)
	private Integer userChannelCode = UserChannel.YOOMATH;

	@Transient
	private UserInfo userInfo;
	@Transient
	private Product loginSource;
	@Transient
	private boolean initMemberType = false;
	@Transient
	private boolean initTeaSubject = true;
	@Transient
	private boolean initUserState = true;
	@Transient
	private boolean initPhase = true;
	@Transient
	private boolean initTeaTitle = true;
	@Transient
	private boolean initTeaDuty = true;
	@Transient
	private boolean initTextbookCategory = true;
	@Transient
	private boolean initTextbook = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isImport0() {
		return import0;
	}

	public void setImport0(boolean import0) {
		this.import0 = import0;
	}

	public Integer getUserChannelCode() {
		return userChannelCode;
	}

	public void setUserChannelCode(Integer userChannelCode) {
		this.userChannelCode = userChannelCode;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Product getLoginSource() {
		return loginSource;
	}

	public void setLoginSource(Product loginSource) {
		this.loginSource = loginSource;
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
