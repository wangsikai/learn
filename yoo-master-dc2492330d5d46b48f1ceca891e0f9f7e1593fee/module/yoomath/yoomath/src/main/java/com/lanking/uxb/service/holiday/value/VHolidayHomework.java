package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 寒假作业VO
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月25日
 */
public class VHolidayHomework implements Serializable {

	private static final long serialVersionUID = 7695384249553541816L;
	private Long id;
	// 假日作业类型
	private HolidayHomeworkType type;
	// 班级ID
	private Long homeworkClassId;
	// 作业名称
	private String name;
	// 难度
	private BigDecimal difficulty;
	private Integer questionCount;
	// 包含知识点
	private List<Long> metaKnowpoints;
	/**
	 * 新知识点
	 * @since 3.0.1
	 */
	private List<Long> knowledgePoints;
	private Date startTime;
	private Date deadline;
	private long createId;
	private Date createAt;
	private HomeworkStatus status = HomeworkStatus.INIT;
	private Status delStatus = Status.ENABLED;
	private Integer homeworkTime = 0;
	private Integer rightCount;
	private Integer wrongCount;
	private BigDecimal rightRate;
	private String rightRateTitle;
	private BigDecimal completionRate;
	private String completionRateTitle;
	// 对应班级信息
	private VHomeworkClazz clazz;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
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

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public List<Long> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Long> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
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

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public Integer getRightCount() {
		return rightCount;
	}

	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
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

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

	public VHomeworkClazz getClazz() {
		return clazz;
	}

	public void setClazz(VHomeworkClazz clazz) {
		this.clazz = clazz;
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

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}
}
