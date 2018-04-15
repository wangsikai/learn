package com.lanking.cloud.domain.yoomath.doQuestion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 刷题目标
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "do_question_goal")
public class DoQuestionGoal implements Serializable {

	private static final long serialVersionUID = -4478550418904248387L;

	/**
	 * 用户ID
	 */
	@Id
	private long userId;

	/**
	 * 做题目标级别
	 */
	@Column(name = "level", precision = 3)
	private DoQuestionGoalLevel level;

	/**
	 * 自定义刷题数量
	 */
	@Column(name = "goal")
	private long goal;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public DoQuestionGoalLevel getLevel() {
		return level;
	}

	public void setLevel(DoQuestionGoalLevel level) {
		this.level = level;
	}

	public long getGoal() {
		return goal;
	}

	public void setGoal(long goal) {
		this.goal = goal;
	}

}
