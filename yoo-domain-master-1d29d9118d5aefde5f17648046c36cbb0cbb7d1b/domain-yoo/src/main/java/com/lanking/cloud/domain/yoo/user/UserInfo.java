package com.lanking.cloud.domain.yoo.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 不同用户类型公共信息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -8728082796576507702L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户名(和user中的name对应)
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 昵称(和user中的nickname对应)
	 */
	@Column(name = "nickname", length = 40)
	private String nickname;

	/**
	 * 介绍
	 */
	@Column(name = "introduce", length = 500)
	private String introduce;

	/**
	 * 头像ID
	 */
	@Column(name = "avatar_id")
	private Long avatarId;

	/**
	 * 性别
	 */
	@Column(name = "sex", precision = 3)
	private Sex sex = Sex.UNKNOWN;

	/**
	 * 出生年月
	 */
	@Column(name = "birthday", columnDefinition = "date")
	private Date birthday;

	/**
	 * QQ
	 */
	@Column(name = "qq", length = 20)
	private String qq;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	@Transient
	private UserType userType;
	@Transient
	private Integer userChannelCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getUserChannelCode() {
		return userChannelCode;
	}

	public void setUserChannelCode(Integer userChannelCode) {
		this.userChannelCode = userChannelCode;
	}

}
