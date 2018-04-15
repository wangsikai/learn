package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VClazz;

/**
 * 作业统计VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public class VHomeworkStat implements Serializable {

	private static final long serialVersionUID = 2126765673156525043L;

	private long id;
	/**
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 */
	@Deprecated
	private long courseId;
	private long homeworkClassId;
	private long userId;
	private long homeWorkNum = 0;
	private long doingNum = 0;
	private BigDecimal rightRate = new BigDecimal(0);
	private String rightRateTitle;
	private BigDecimal completionRate = new BigDecimal(0);
	private String completionRateTitle;
	private int homeworkTime = 0;

	private VClazz clazz;
	private long classId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 * @return
	 */
	@Deprecated
	public long getCourseId() {
		return courseId;
	}

	/**
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 * @param courseId
	 */
	@Deprecated
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

	public long getDoingNum() {
		return doingNum;
	}

	public void setDoingNum(long doingNum) {
		this.doingNum = doingNum;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		if (rightRateTitle == null) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(
						String.valueOf(getRightRate().setScale(2, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public String getCompletionRateTitle() {
		if (completionRateTitle == null) {
			if (getCompletionRate() == null) {
				setCompletionRateTitle(StringUtils.EMPTY);
			} else {
				setCompletionRateTitle(
						String.valueOf(getCompletionRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return completionRateTitle;
	}

	public void setCompletionRateTitle(String completionRateTitle) {
		this.completionRateTitle = completionRateTitle;
	}

	public int getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(int homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public VClazz getClazz() {
		return clazz;
	}

	public void setClazz(VClazz clazz) {
		this.clazz = clazz;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

}
