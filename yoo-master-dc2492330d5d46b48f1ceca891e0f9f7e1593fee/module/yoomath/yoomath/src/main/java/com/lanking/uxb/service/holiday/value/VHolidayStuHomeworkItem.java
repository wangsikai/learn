package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 学生查看寒假作业的VO
 * 
 * @author wangsenhao
 *
 */
public class VHolidayStuHomeworkItem implements Serializable {

	private static final long serialVersionUID = 5481046861342559857L;

	/**
	 * holidayStudentHomeworkItem Id
	 */
	private Long holidayStuHomeworkItemId;

	private Long holidayHomeworkId;
	/**
	 * 专项作业名称
	 */
	private String name;
	/**
	 * 难度
	 */
	private BigDecimal difficulty;
	/**
	 * 题目数量
	 */
	private Integer questionCount;
	private BigDecimal rightRate;
	private String rightRateTitle;
	private BigDecimal completeRate;
	private BigDecimal completionRate;
	private String completionRateTitle;
	// 已经完成的数量
	private int completionCount = 0;
	/**
	 * 排名
	 */
	private Integer rank;
	/**
	 * 状态
	 */
	private StudentHomeworkStatus status;
	/**
	 * 学生ID
	 */
	private Long studentId;
	/**
	 * 作业所花时间
	 */
	private Integer homeworkTime;
	/**
	 * 正确个数
	 */
	private Integer rightCount;
	/**
	 * 错误个数
	 */
	private Integer wrongCount;

	// 提交作业时间
	private Date submitAt;
	/**
	 * 假期作业专项
	 */
	private VHolidayHomeworkItem holidayHomeworkItem;
	/**
	 * 假期作业更新时间(用于与本地进行对比)
	 *
	 * @since 3.0
	 */
	private Date updateAt;

	private VUser user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public BigDecimal getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(BigDecimal completeRate) {
		this.completeRate = completeRate;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Long getHolidayStuHomeworkItemId() {
		return holidayStuHomeworkItemId;
	}

	public void setHolidayStuHomeworkItemId(Long holidayStuHomeworkItemId) {
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
	}

	public StudentHomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(StudentHomeworkStatus status) {
		this.status = status;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public VHolidayHomeworkItem getHolidayHomeworkItem() {
		return holidayHomeworkItem;
	}

	public void setHolidayHomeworkItem(VHolidayHomeworkItem holidayHomeworkItem) {
		this.holidayHomeworkItem = holidayHomeworkItem;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public Long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(Long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

	public Integer getRightCount() {
		return rightCount;
	}

	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

	public Date getSubmitAt() {
		return submitAt;
	}

	public void setSubmitAt(Date submitAt) {
		this.submitAt = submitAt;
	}

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(String.valueOf(getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public String getCompletionRateTitle() {
		if (StringUtils.isBlank(completionRateTitle)) {
			if (getCompletionRate() == null) {
				setCompletionRateTitle(StringUtils.EMPTY);
			} else {
				setCompletionRateTitle(String.valueOf(getCompletionRate().setScale(0, BigDecimal.ROUND_HALF_UP)
						.intValue()) + "%");
			}
		}
		return completionRateTitle;
	}

	public void setCompletionRateTitle(String completionRateTitle) {
		this.completionRateTitle = completionRateTitle;
	}

	public int getCompletionCount() {
		return completionCount;
	}

	public void setCompletionCount(int completionCount) {
		this.completionCount = completionCount;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}
