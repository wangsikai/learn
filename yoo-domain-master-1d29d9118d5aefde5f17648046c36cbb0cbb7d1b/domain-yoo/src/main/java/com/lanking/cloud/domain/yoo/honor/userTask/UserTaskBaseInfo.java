package com.lanking.cloud.domain.yoo.honor.userTask;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 用户任务基本信息
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
@MappedSuperclass
public class UserTaskBaseInfo implements Serializable {

	private static final long serialVersionUID = 2211880056440763184L;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 64)
	private String name;

	/**
	 * 任务类型
	 */
	@Column(name = "type", precision = 3)
	private UserTaskType type;

	/**
	 * 成就任务类型.
	 */
	@Column(name = "achievement_type", precision = 3)
	private UserTaskAchievementType achievementType;

	/**
	 * 用户类型
	 */
	@Column(name = "user_type", precision = 3)
	private UserType userType;

	/**
	 * 用户范围
	 */
	@Column(name = "user_scope", precision = 3)
	private UserTaskUserScope userScope;

	/**
	 * 任务状态
	 */
	@Column(name = "status", precision = 3)
	private UserTaskStatus status;

	/**
	 * 备注
	 */
	@Column(name = "note", length = 256)
	private String note;

	/**
	 * 备注(成长值)
	 */
	@Column(name = "growth_note", length = 256)
	private String growthNote;

	/**
	 * 备注(金币)
	 */
	@Column(name = "coins_note", length = 256)
	private String coinsNote;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 5)
	private Integer sequence;

	/**
	 * 规则配置
	 */
	@Column(name = "rule_cfg", length = 4000)
	private String ruleCfg;

	/**
	 * 任务图标
	 */
	@Column(name = "icon")
	private Long icon;

	@Transient
	private UserTaskRuleCfg userTaskRuleCfg;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserTaskType getType() {
		return type;
	}

	public void setType(UserTaskType type) {
		this.type = type;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public UserTaskUserScope getUserScope() {
		return userScope;
	}

	public void setUserScope(UserTaskUserScope userScope) {
		this.userScope = userScope;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public UserTaskStatus getStatus() {
		return status;
	}

	public void setStatus(UserTaskStatus status) {
		this.status = status;
	}

	private String getRuleCfg() {
		return ruleCfg;
	}

	private void setRuleCfg(String ruleCfg) {
		this.ruleCfg = ruleCfg;
	}

	public UserTaskRuleCfg getUserTaskRuleCfg() {
		if (getRuleCfg() == null) {
			return null;
		}
		return JSON.parseObject(getRuleCfg(), UserTaskRuleCfg.class);
	}

	public void setUserTaskRuleCfg(UserTaskRuleCfg userTaskRuleCfg) {
		if (userTaskRuleCfg == null) {
			setRuleCfg(null);
		} else {
			setRuleCfg(userTaskRuleCfg.toString());
		}
		this.userTaskRuleCfg = userTaskRuleCfg;
	}

	public Long getIcon() {
		return icon;
	}

	public void setIcon(Long icon) {
		this.icon = icon;
	}

	public String getGrowthNote() {
		return growthNote;
	}

	public void setGrowthNote(String growthNote) {
		this.growthNote = growthNote;
	}

	public String getCoinsNote() {
		return coinsNote;
	}

	public void setCoinsNote(String coinsNote) {
		this.coinsNote = coinsNote;
	}

	public UserTaskAchievementType getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(UserTaskAchievementType achievementType) {
		this.achievementType = achievementType;
	}

}