package com.lanking.uxb.service.doQuestion.value;

import java.io.Serializable;

public class VDoQuestionGoal implements Serializable {

	private static final long serialVersionUID = -7997918783844984054L;

	private long userId;
	private VDoQuestionGoalLevel level;
	private long goal;
	// 已经完成的题目数量
	private long completedGoal = 0;
	// 距离目标的数量
	private long remainingGoal = 0;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public VDoQuestionGoalLevel getLevel() {
		return level;
	}

	public void setLevel(VDoQuestionGoalLevel level) {
		this.level = level;
	}

	public long getGoal() {
		return goal;
	}

	public void setGoal(long goal) {
		this.goal = goal;
	}

	public long getCompletedGoal() {
		return completedGoal;
	}

	public void setCompletedGoal(long completedGoal) {
		this.completedGoal = completedGoal;
		this.remainingGoal = this.goal - this.completedGoal;
	}

	public long getRemainingGoal() {
		if (remainingGoal < 0) {
			return 0;
		}
		return remainingGoal;
	}

	public void setRemainingGoal(long remainingGoal) {
		this.remainingGoal = remainingGoal;
	}

}
