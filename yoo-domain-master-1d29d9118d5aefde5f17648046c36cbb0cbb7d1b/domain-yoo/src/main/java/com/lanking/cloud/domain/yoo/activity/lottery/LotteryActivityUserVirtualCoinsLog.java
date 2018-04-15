package com.lanking.cloud.domain.yoo.activity.lottery;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.user.UserAction;

/**
 * 用户活动虚拟货币历史记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "lottery_activity_user_virtual_coins")
public class LotteryActivityUserVirtualCoinsLog implements Serializable {
	private static final long serialVersionUID = -2814645190063191636L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 对应的抽奖活动code
	 */
	@Column(name = "activity_code")
	private Long activityCode;

	/**
	 * 使用的抽奖规则
	 */
	@Column(name = "rule_code")
	private Integer ruleCode;

	/**
	 * 获得虚拟币的动作行为
	 */
	@Column(name = "action", precision = 3)
	private UserAction action = UserAction.DEFUALT;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 用户增加的活动虚拟币数量
	 */
	@Column(name = "increase_coins")
	private int incrCoins;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public Integer getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(Integer ruleCode) {
		this.ruleCode = ruleCode;
	}

	public UserAction getAction() {
		return action;
	}

	public void setAction(UserAction action) {
		this.action = action;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getIncrCoins() {
		return incrCoins;
	}

	public void setIncrCoins(int incrCoins) {
		this.incrCoins = incrCoins;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
