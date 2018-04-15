package com.lanking.uxb.service.examPaper.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudentStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperType;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 学生试卷VO.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月12日
 */
public class VCustomExampaperStudent implements Serializable {
	private static final long serialVersionUID = -6826079989375043888L;

	// 学生组卷信息
	private Long id;
	private Long studentId;
	private long customExampaperId;
	private CustomExampaperStudentStatus status;
	private Date createAt;
	private Date statisticsAt;
	private long classId;

	// 组卷信息
	private String name;
	private CustomExampaperType type;
	private Integer time;
	private Integer score;
	private Integer questionCount;
	private BigDecimal difficulty;
	private CustomExampaperStatus customExampaperstatus;
	private Date customExampaperCreateAt;
	private Date openAt;

	// 班级信息
	private VHomeworkClazz clazz;

	// 习题信息
	private List<VCustomExampaperStudentTopic> topics;

	private boolean read = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public long getCustomExampaperId() {
		return customExampaperId;
	}

	public void setCustomExampaperId(long customExampaperId) {
		this.customExampaperId = customExampaperId;
	}

	public CustomExampaperStudentStatus getStatus() {
		return status;
	}

	public void setStatus(CustomExampaperStudentStatus status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getStatisticsAt() {
		return statisticsAt;
	}

	public void setStatisticsAt(Date statisticsAt) {
		this.statisticsAt = statisticsAt;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public VHomeworkClazz getClazz() {
		return clazz;
	}

	public void setClazz(VHomeworkClazz clazz) {
		this.clazz = clazz;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomExampaperType getType() {
		return type;
	}

	public void setType(CustomExampaperType type) {
		this.type = type;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public CustomExampaperStatus getCustomExampaperstatus() {
		return customExampaperstatus;
	}

	public void setCustomExampaperstatus(CustomExampaperStatus customExampaperstatus) {
		this.customExampaperstatus = customExampaperstatus;
	}

	public Date getCustomExampaperCreateAt() {
		return customExampaperCreateAt;
	}

	public void setCustomExampaperCreateAt(Date customExampaperCreateAt) {
		this.customExampaperCreateAt = customExampaperCreateAt;
	}

	public Date getOpenAt() {
		return openAt;
	}

	public void setOpenAt(Date openAt) {
		this.openAt = openAt;
	}

	public List<VCustomExampaperStudentTopic> getTopics() {
		return topics;
	}

	public void setTopics(List<VCustomExampaperStudentTopic> topics) {
		this.topics = topics;
	}
}
