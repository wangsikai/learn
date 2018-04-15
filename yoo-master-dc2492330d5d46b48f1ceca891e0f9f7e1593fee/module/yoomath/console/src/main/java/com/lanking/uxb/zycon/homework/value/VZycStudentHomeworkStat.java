package com.lanking.uxb.zycon.homework.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 学生作业统计VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public class VZycStudentHomeworkStat implements Serializable {

	private static final long serialVersionUID = -8272990341492892355L;

	private long id;
	private long courseId;
	private long homeworkClassId;
	private long userId;
	private long homeWorkNum = 0;
	private long todoNum = 0;
	private BigDecimal rightRate = new BigDecimal(0);
	private String rightRateTitle;
	private int homeworkTime = 0;
	private int rank = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getHomeWorkNum() {
		return homeWorkNum;
	}

	public void setHomeWorkNum(long homeWorkNum) {
		this.homeWorkNum = homeWorkNum;
	}

	public long getTodoNum() {
		return todoNum;
	}

	public void setTodoNum(long todoNum) {
		this.todoNum = todoNum;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(String.valueOf(getRightRate().setScale(2, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public int getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(int homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
