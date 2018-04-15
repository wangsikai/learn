package com.lanking.uxb.service.honor.value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskAchievementType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * VO for UserTask
 *
 * @author xinyu.zhou
 * @since 4.0.0
 */
public class VUserTask implements Serializable {
	private static final long serialVersionUID = 3368772196930930767L;

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

	private Integer growthValue;
	private Integer lGrowthValue;
	private Integer rGrowthValue;
	private String growthValueTitle;

	private Integer coinsValue;
	private Integer lCoinsValue;
	private Integer rCoinsValue;
	private String coinsValueTitle;

	boolean showStar = false;
	private Integer hasStar;
	private Integer maxStar;

	private String url;

	/*
	 * 任务中每一项子任务数据 Map -> 中 数据结构为 content: 子任务内容 coins: 子任务完成的金币值 index: 子任务顺序
	 * growth: 子任务完成的成长值 star: 子任务完成的星值
	 */
	private List<Map<String, Object>> items;
	private String icon;

	// 用户任务完成情况
	private VUserTaskLog log;

	/**
	 * 成就任务类型.
	 */
	private UserTaskAchievementType achievementType;

	/**
	 * 成就任务，是否还有后续任务.
	 */
	private boolean hasNextTask = false;

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

	public Integer getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(Integer growthValue) {
		this.growthValue = growthValue;
	}

	public Integer getlGrowthValue() {
		return lGrowthValue;
	}

	public void setlGrowthValue(Integer lGrowthValue) {
		this.lGrowthValue = lGrowthValue;
	}

	public Integer getrGrowthValue() {
		return rGrowthValue;
	}

	public void setrGrowthValue(Integer rGrowthValue) {
		this.rGrowthValue = rGrowthValue;
	}

	public Integer getCoinsValue() {
		return coinsValue;
	}

	public void setCoinsValue(Integer coinsValue) {
		this.coinsValue = coinsValue;
	}

	public Integer getlCoinsValue() {
		return lCoinsValue;
	}

	public void setlCoinsValue(Integer lCoinsValue) {
		this.lCoinsValue = lCoinsValue;
	}

	public Integer getrCoinsValue() {
		return rCoinsValue;
	}

	public void setrCoinsValue(Integer rCoinsValue) {
		this.rCoinsValue = rCoinsValue;
	}

	/*
	 * public Integer getActiveStar() { return activeStar; }
	 * 
	 * public void setActiveStar(Integer activeStar) { this.activeStar =
	 * activeStar; }
	 * 
	 * public Integer getlActiveStar() { return lActiveStar; }
	 * 
	 * public void setlActiveStar(Integer lActiveStar) { this.lActiveStar =
	 * lActiveStar; }
	 * 
	 * public Integer getrActiveStar() { return rActiveStar; }
	 * 
	 * public void setrActiveStar(Integer rActiveStar) { this.rActiveStar =
	 * rActiveStar; }
	 */

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public VUserTaskLog getLog() {
		return log;
	}

	public void setLog(VUserTaskLog log) {
		this.log = log;
	}

	public String getGrowthValueTitle() {
		return growthValueTitle;
	}

	public void setGrowthValueTitle(String growthValueTitle) {
		this.growthValueTitle = growthValueTitle;
	}

	public String getCoinsValueTitle() {
		return coinsValueTitle;
	}

	public void setCoinsValueTitle(String coinsValueTitle) {
		this.coinsValueTitle = coinsValueTitle;
	}

	public List<Map<String, Object>> getItems() {
		return items;
	}

	public void setItems(List<Map<String, Object>> items) {
		this.items = items;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
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

	public Integer getHasStar() {
		return hasStar;
	}

	public void setHasStar(Integer hasStar) {
		this.hasStar = hasStar;
	}

	public Integer getMaxStar() {
		return maxStar;
	}

	public void setMaxStar(Integer maxStar) {
		this.maxStar = maxStar;
	}

	public boolean isShowStar() {
		return showStar;
	}

	public void setShowStar(boolean showStar) {
		this.showStar = showStar;
	}

	public boolean isHasNextTask() {
		return hasNextTask;
	}

	public void setHasNextTask(boolean hasNextTask) {
		this.hasNextTask = hasNextTask;
	}
}
