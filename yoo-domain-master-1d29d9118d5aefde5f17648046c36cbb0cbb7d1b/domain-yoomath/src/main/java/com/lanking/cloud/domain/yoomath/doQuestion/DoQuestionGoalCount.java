package com.lanking.cloud.domain.yoomath.doQuestion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 刷题目标统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "do_question_goal_count")
public class DoQuestionGoalCount implements Serializable {

	private static final long serialVersionUID = 2503862986300445734L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 目前完成的答题目标
	 */
	@Column(name = "goal")
	private long goal;

	/**
	 * 日期,格式为:20160315
	 */
	@Column(name = "date0")
	private long date0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getGoal() {
		return goal;
	}

	public void setGoal(long goal) {
		this.goal = goal;
	}

	public long getDate0() {
		return date0;
	}

	public void setDate0(long date0) {
		this.date0 = date0;
	}

}
