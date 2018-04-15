package com.lanking.uxb.zycon.homework.value;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.zycon.user.value.VZycUser;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生作业VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月19日
 */
public class VZycStudentHomework implements Serializable {

	private static final long serialVersionUID = 2894559048867065793L;

	private long id;
	private long studentId;
	private VZycHomeworkStudentClazz studentClazz;

	private long homeworkId;
	private Date createAt;
	private Date submitAt;
	private Date issueAt;
	private StudentHomeworkStatus status;
	private String statusName;
	/**
	 * 此属性为有没有批改完成
	 */
	private boolean corrected;
	/**
	 * 此属性为学生有没有批改完成
	 */
	private boolean studentCorrected = true;

	private BigDecimal rightRate;
	private String rightRateTitle;
	private int homeworkTime;
	private int rank;
	private int rightCount;
	private int wrongCount;

	private VZycHomework homework;
	private VZycUser user;
	private Date stuSubmitAt;
	
	private StudentHomeworkCorrectStatus correctStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getSubmitAt() {
		return submitAt;
	}

	public void setSubmitAt(Date submitAt) {
		this.submitAt = submitAt;
	}

	public Date getIssueAt() {
		return issueAt;
	}

	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}

	public StudentHomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(StudentHomeworkStatus status) {
		this.status = status;
	}

	public String getStatusName() {
		if (StringUtils.isBlank(statusName)) {
			String sn = "";
			if (getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
				sn = "未提交";
			} else if (getStatus() == StudentHomeworkStatus.SUBMITED) {
				sn = "已提交";
			} else if (getStatus() == StudentHomeworkStatus.ISSUED) {
				sn = "下发";
			}
			setStatusName(sn);
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public boolean isCorrected() {
		return corrected;
	}

	public void setCorrected(boolean corrected) {
		this.corrected = corrected;
	}

	public boolean isStudentCorrected() {
		return studentCorrected;
	}

	public void setStudentCorrected(boolean studentCorrected) {
		this.studentCorrected = studentCorrected;
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

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}

	public VZycHomeworkStudentClazz getStudentClazz() {
		return studentClazz;
	}

	public void setStudentClazz(VZycHomeworkStudentClazz studentClazz) {
		this.studentClazz = studentClazz;
	}

	public VZycHomework getHomework() {
		return homework;
	}

	public void setHomework(VZycHomework homework) {
		this.homework = homework;
	}

	public VZycUser getUser() {
		return user;
	}

	public void setUser(VZycUser user) {
		this.user = user;
	}

	public Date getStuSubmitAt() {
		return stuSubmitAt;
	}

	public void setStuSubmitAt(Date stuSubmitAt) {
		this.stuSubmitAt = stuSubmitAt;
	}

	public StudentHomeworkCorrectStatus getCorrectStatus() {
		return correctStatus;
	}

	public void setCorrectStatus(StudentHomeworkCorrectStatus correctStatus) {
		this.correctStatus = correctStatus;
	}
	
}
