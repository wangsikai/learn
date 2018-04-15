package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 作业项VO
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月22日 下午4:04:57
 */
public class VHolidayHomeworkItem implements Serializable {

	private static final long serialVersionUID = 1117256973675265520L;

	private long id;
	private long holidayHomeworkId;
	private String name;
	/**
	 * 难度
	 * 
	 * @since 1.9
	 */
	private BigDecimal difficulty;
	/**
	 * 专项作业总的分发数
	 */
	private Long distributeCount = 0L;
	/**
	 * 专项作业学生提交数
	 */
	private Long commitCount = 0L;
	/**
	 * 作业平均用时
	 */
	private Integer homeworkTime = 0;
	/**
	 * 平均正确率
	 */
	private BigDecimal rightRate;
	private String rightRateTitle;
	private BigDecimal finishRate;
	private String finishRateTitle;
	private BigDecimal completionRate;
	private String completionRateTitle;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date deadline;
	/**
	 * 布置时间
	 */
	private Date createAt;
	private long questionCount = 0;
	/**
	 * 元知识点
	 */
	private List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();
	/**
	 * 状态
	 */
	private HomeworkStatus status;

	/**
	 * 班级信息
	 * 
	 * @since 2.1
	 */
	private VHomeworkClazz clazz;

	/**
	 * 新知识点列表
	 * @since 3.0.1
	 */
	private List<VKnowledgePoint> knowledgePoints;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

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

	public Long getDistributeCount() {
		return distributeCount;
	}

	public void setDistributeCount(Long distributeCount) {
		this.distributeCount = distributeCount;
	}

	public Long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(Long commitCount) {
		this.commitCount = commitCount;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getFinishRateTitle() {
		return finishRateTitle;
	}

	public void setFinishRateTitle(String finishRateTitle) {
		this.finishRateTitle = finishRateTitle;
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

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public List<VMetaKnowpoint> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<VMetaKnowpoint> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public VHomeworkClazz getClazz() {
		return clazz;
	}

	public void setClazz(VHomeworkClazz clazz) {
		this.clazz = clazz;
	}

	public BigDecimal getFinishRate() {
		return finishRate;
	}

	public void setFinishRate(BigDecimal finishRate) {
		this.finishRate = finishRate;
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

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
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

	public List<VKnowledgePoint> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<VKnowledgePoint> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}
}
