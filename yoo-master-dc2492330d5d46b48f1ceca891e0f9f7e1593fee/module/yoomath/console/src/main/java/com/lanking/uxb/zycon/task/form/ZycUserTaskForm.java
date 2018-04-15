/**
 * 
 */
package com.lanking.uxb.zycon.task.form;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
public class ZycUserTaskForm {

	private Integer code;

	private Long icon;

	private String name;

	private String note;

	private String growthNote;

	private String coinsNote;

	private int type;

	private UserTaskUserScope userScope;

	private UserTaskUserScope userTaskUserScope;

	private String ruleCfg;

	private UserTaskRuleCfg userTaskRuleCfg;

	private Integer sequence;
	private UserTaskStatus userTaskStatus;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Long getIcon() {
		return icon;
	}

	public void setIcon(Long icon) {
		this.icon = icon;
	}

	public UserTaskUserScope getUserScope() {
		return userScope;
	}

	public void setUserScope(UserTaskUserScope userScope) {
		this.userScope = userScope;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserTaskUserScope getUserTaskUserScope() {
		return userTaskUserScope;
	}

	public void setUserTaskUserScope(UserTaskUserScope userTaskUserScope) {
		this.userTaskUserScope = userTaskUserScope;
	}

	public String getRuleCfg() {
		return ruleCfg;
	}

	public void setRuleCfg(String ruleCfg) {
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public UserTaskStatus getUserTaskStatus() {
		return userTaskStatus;
	}

	public void setUserTaskStatus(UserTaskStatus userTaskStatus) {
		this.userTaskStatus = userTaskStatus;
	}
}
