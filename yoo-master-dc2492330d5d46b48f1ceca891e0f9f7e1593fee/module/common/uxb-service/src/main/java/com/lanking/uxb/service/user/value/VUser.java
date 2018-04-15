package com.lanking.uxb.service.user.value;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VSchool;

/**
 * 用户VO(只包括基本信息)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public class VUser implements Serializable {

	private static final long serialVersionUID = -7093412121493049523L;

	private long id;
	private String name;
	private String nickname;
	private String introduce;
	private Sex sex;
	private String sexName;
	private Date birthday;
	private String qq;

	private Long avatarId;
	private String avatarUrl = "";
	private String minAvatarUrl = "";

	private VUserState userState;

	private long schoolId;
	private VSchool school;

	private UserType type;
	private String userTypeName;

	private Object info;
	@JsonIgnore
	private VStudent s;
	@JsonIgnore
	private VTeacher t;
	@JsonIgnore
	private VParent p;

	private boolean isMe;
	private boolean isFriend;
	private boolean isFollow;
	private boolean isFan;
	/**
	 * @since 3.0.2 用户会员类型(目前只有手机端排名要显示学生会员状态)
	 */
	private MemberType memberType;

	/**
	 * 是不是渠道商用户
	 */
	private boolean isChannelUser = false;

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getSexName() {
		if (StringUtils.isBlank(sexName)) {
			setSexName(Sex.getCnName(sex));
		}
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getMinAvatarUrl() {
		return minAvatarUrl;
	}

	public void setMinAvatarUrl(String minAvatarUrl) {
		this.minAvatarUrl = minAvatarUrl;
	}

	public VUserState getUserState() {
		return userState;
	}

	public void setUserState(VUserState userState) {
		this.userState = userState;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public VSchool getSchool() {
		return school;
	}

	public void setSchool(VSchool school) {
		this.school = school;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getUserTypeName() {
		if (StringUtils.isBlank(userTypeName)) {
			setUserTypeName(type.getCnName());
		}
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public Object getInfo() {
		if (info == null) {
			if (getType() == UserType.TEACHER) {
				setInfo(getT());
			} else if (getType() == UserType.STUDENT) {
				setInfo(getS());
			} else if (getType() == UserType.PARENT) {
				setInfo(getP());
			}
		}
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	public VStudent getS() {
		return s;
	}

	public void setS(VStudent s) {
		this.s = s;
	}

	public VTeacher getT() {
		return t;
	}

	public void setT(VTeacher t) {
		this.t = t;
	}

	public VParent getP() {
		return p;
	}

	public void setP(VParent p) {
		this.p = p;
	}

	public boolean isMe() {
		return isMe;
	}

	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}

	public boolean isFriend() {
		if (isMe()) {
			return false;
		}
		return isFriend;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public boolean isFollow() {
		if (isMe()) {
			return false;
		}
		return isFollow;
	}

	public void setFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

	public boolean isFan() {
		if (isMe()) {
			return false;
		}
		return isFan;
	}

	public void setFan(boolean isFan) {
		this.isFan = isFan;
	}

	public boolean isChannelUser() {
		return isChannelUser;
	}

	public void setChannelUser(boolean isChannelUser) {
		this.isChannelUser = isChannelUser;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}
}
