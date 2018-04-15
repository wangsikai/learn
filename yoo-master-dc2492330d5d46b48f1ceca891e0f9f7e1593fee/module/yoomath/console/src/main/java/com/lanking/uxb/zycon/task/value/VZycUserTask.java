/**
 * 
 */
package com.lanking.uxb.zycon.task.value;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
public class VZycUserTask {

	private int code;

	private String name;

	private UserTaskType type;

	private UserType userType;

	private UserTaskUserScope userScope;

	private UserTaskStatus status;

	private String note;

	private String growthNote;

	private String coinsNote;

	private Integer sequence;

	private Long icon;

	private String iconUrl;

	private UserTaskRuleCfg userTaskRuleCfg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

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

	public UserTaskStatus getStatus() {
		return status;
	}

	public void setStatus(UserTaskStatus status) {
		this.status = status;
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

	public Long getIcon() {
		return icon;
	}

	public void setIcon(Long icon) {
		this.icon = icon;
	}

	public UserTaskRuleCfg getUserTaskRuleCfg() {
		return userTaskRuleCfg;
	}

	public void setUserTaskRuleCfg(UserTaskRuleCfg userTaskRuleCfg) {
		this.userTaskRuleCfg = userTaskRuleCfg;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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

}
