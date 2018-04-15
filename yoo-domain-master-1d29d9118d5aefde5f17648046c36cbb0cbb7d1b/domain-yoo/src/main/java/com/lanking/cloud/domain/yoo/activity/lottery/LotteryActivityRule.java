package com.lanking.cloud.domain.yoo.activity.lottery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 抽奖活动规则
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "lottery_activity_rule")
public class LotteryActivityRule implements Serializable {

	private static final long serialVersionUID = 7530683850259824803L;

	/**
	 * 规则代码
	 */
	@Id
	@Column(name = "code")
	private int code;

	/**
	 * 管理活动代码
	 */
	@Column(name = "activity_code")
	private Long activityCode;

	/**
	 * 用户操作类型
	 */
	@Column(name = "action", precision = 3, nullable = false)
	private UserAction action;

	/**
	 * 规则描述
	 */
	@Column(name = "description", length = 500)
	private String description;

	/**
	 * 规则限定用户类型，默认全部
	 */
	@Column(name = "user_type", precision = 3, nullable = false)
	private UserType userType = UserType.NULL;

	/**
	 * 规则限定会员类型，默认全部
	 */
	@Column(name = "member_type", precision = 3, nullable = false)
	private MemberType memberType = MemberType.NONE;

	/**
	 * 存储的规则JSON字串
	 * 
	 * @see LotteryActivityRuleJSON
	 */
	@Column(name = "json", length = 500)
	private String json;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public UserAction getAction() {
		return action;
	}

	public void setAction(UserAction action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
