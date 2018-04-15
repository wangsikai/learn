package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 学生作业VO
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月25日 上午10:20:57
 */
public class VHolidayStuHomework implements Serializable {

	private static final long serialVersionUID = -5618490170701956080L;
	private Long id;
	// 假日作业类型
	private HolidayHomeworkType type;
	// 学生ID
	private Long studentId;
	// 假日作业ID
	private Long holidayHomeworkId;
	// 创建时间
	private Date createAt;
	// 学生作业状态
	private StudentHomeworkStatus status;
	// 删除状态
	private Status delStatus = Status.ENABLED;
	// 对题数量
	private Integer rightCount;
	// 错题数量
	private Integer wrongCount;
	// 正确率
	private BigDecimal rightRate;
	private String rightRateTitle;
	private BigDecimal completionRate;
	private String completionRateTitle;
	// 已经完成的数量
	private int completionCount = 0;
	// 排名
	private Integer rank;
	private Integer homeworkTime = 0;
	// 已提交专项
	private int commitItemCount = 0;
	// 所有专项
	private int allItemCount = 0;
	private VUser user;
	private VHolidayHomework holidayHomework;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(Long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public StudentHomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(StudentHomeworkStatus status) {
		this.status = status;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
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

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
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

	public int getCommitItemCount() {
		return commitItemCount;
	}

	public void setCommitItemCount(int commitItemCount) {
		this.commitItemCount = commitItemCount;
	}

	public int getAllItemCount() {
		return allItemCount;
	}

	public void setAllItemCount(int allItemCount) {
		this.allItemCount = allItemCount;
	}

	public VHolidayHomework getHolidayHomework() {
		return holidayHomework;
	}

	public void setHolidayHomework(VHolidayHomework holidayHomework) {
		this.holidayHomework = holidayHomework;
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

}
