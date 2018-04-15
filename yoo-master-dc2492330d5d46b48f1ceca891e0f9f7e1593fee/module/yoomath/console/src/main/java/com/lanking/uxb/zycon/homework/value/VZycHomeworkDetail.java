package com.lanking.uxb.zycon.homework.value;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VClazz;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 作业VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月17日
 */
public class VZycHomeworkDetail implements Serializable {

	private static final long serialVersionUID = 3097126094771311730L;

	private long id;
	private long exerciseId;
	private long courseId;
	private long homeworkClazzId;
	private String name;
	private Date startTime;
	private Date deadline;
	private Date createAt;
	private Date issueAt;
	private HomeworkStatus status;
	private String statusName;
	private HomeworkCorrectingType correctingType;

	private int rightCount;
	private int wrongCount;

	private BigDecimal rightRate;
	private String rightRateTitle;
	private int homeworkTime;
	private long questionCount = 0;
	/**
	 * 分发数量
	 * 
	 * @since 2.1
	 */
	private long distributeCount = 0;
	/**
	 * 主动提交数量
	 * 
	 * @since 2.1
	 */
	private long commitCount = 0;
	/**
	 * 已经批改的数量
	 * 
	 * @since 2.1
	 */
	private long correctingCount = 0;

	/**
	 * 难度
	 * 
	 * @since 2.1
	 */
	private BigDecimal difficulty;

	/**
	 * 班级信息
	 * 
	 * @since 2.1
	 */
	private VClazz clazz;

	/**
	 * 教材.
	 * 
	 * @since 2.1
	 */
	private Integer textbookCode;
	// 元知识点
	private List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();
	// 作业班级
	private VZycHomeworkClazz homeworkClazz;

	// 每道题的正确率
	private List<VZycHomeworkQuestion> homeworkQuestions;
	// 是否已经全部提交后X分钟
	private boolean allCommitMement = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public long getHomeworkClazzId() {
		return homeworkClazzId;
	}

	public void setHomeworkClazzId(long homeworkClazzId) {
		this.homeworkClazzId = homeworkClazzId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getIssueAt() {
		return issueAt;
	}

	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public String getStatusName() {
		if (StringUtils.isBlank(statusName)) {
			String sn = "";
			if (getStatus() == HomeworkStatus.INIT) {
				sn = "已布置";
			} else if (getStatus() == HomeworkStatus.PUBLISH) {
				sn = "作业中";
			} else if (getStatus() == HomeworkStatus.NOT_ISSUE) {
				sn = "待批改";
			} else if (getStatus() == HomeworkStatus.ISSUED) {
				sn = "已下发";
			}
			setStatusName(sn);
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public HomeworkCorrectingType getCorrectingType() {
		return correctingType;
	}

	public void setCorrectingType(HomeworkCorrectingType correctingType) {
		this.correctingType = correctingType;
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
				setRightRateTitle(String.valueOf(getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
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

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public VClazz getClazz() {
		return clazz;
	}

	public void setClazz(VClazz clazz) {
		this.clazz = clazz;
	}

	public long getDistributeCount() {
		return distributeCount;
	}

	public void setDistributeCount(long distributeCount) {
		this.distributeCount = distributeCount;
	}

	public long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(long commitCount) {
		this.commitCount = commitCount;
	}

	public long getCorrectingCount() {
		return correctingCount;
	}

	public void setCorrectingCount(long correctingCount) {
		this.correctingCount = correctingCount;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public List<VMetaKnowpoint> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<VMetaKnowpoint> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public VZycHomeworkClazz getHomeworkClazz() {
		return homeworkClazz;
	}

	public void setHomeworkClazz(VZycHomeworkClazz homeworkClazz) {
		this.homeworkClazz = homeworkClazz;
	}

	public List<VZycHomeworkQuestion> getHomeworkQuestions() {
		return homeworkQuestions;
	}

	public void setHomeworkQuestions(List<VZycHomeworkQuestion> homeworkQuestions) {
		this.homeworkQuestions = homeworkQuestions;
	}

	public boolean isAllCommitMement() {
		return allCommitMement;
	}

	public void setAllCommitMement(boolean allCommitMement) {
		this.allCommitMement = allCommitMement;
	}

}
