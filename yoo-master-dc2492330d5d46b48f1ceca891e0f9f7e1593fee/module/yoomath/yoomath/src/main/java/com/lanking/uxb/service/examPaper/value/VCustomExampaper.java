package com.lanking.uxb.service.examPaper.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperCfg;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperType;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 组卷信息
 * 
 * @author zemin.song
 * @version 2016年8月15日
 */
public class VCustomExampaper implements Serializable {

	private static final long serialVersionUID = 9185419682327824791L;
	private Long id;
	// 标题
	private String title;
	// 是否下载
	private Boolean download = false;
	// 时长
	private Integer time;
	// 题型
	private List<CustomExampaperTopic> topic;
	// 总分
	private Integer score;
	// 题目总数
	private Integer questionCount;
	// 平均难度
	private BigDecimal difficulty;
	// 更新时间
	private Date updateAt;
	// 开卷时间
	private Date openAt;
	// 生成时间
	private Date enableAt;
	// 选择题分值
	private CustomExampaperCfg cfg;
	// 类型
	private CustomExampaperType type;
	// 试卷状态
	private CustomExampaperStatus status;
	// 问题
	private List<VCustomExamPaperQuestion> questions;
	// 开卷的班级列表
	private List<VHomeworkClazz> openClasses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getDownload() {
		return download;
	}

	public void setDownload(Boolean download) {
		this.download = download;
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

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public CustomExampaperCfg getCfg() {
		return cfg;
	}

	public void setCfg(CustomExampaperCfg cfg) {
		this.cfg = cfg;
	}

	public CustomExampaperType getType() {
		return type;
	}

	public void setType(CustomExampaperType type) {
		this.type = type;
	}

	public CustomExampaperStatus getStatus() {
		return status;
	}

	public void setStatus(CustomExampaperStatus status) {
		this.status = status;
	}

	public List<VCustomExamPaperQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<VCustomExamPaperQuestion> questions) {
		this.questions = questions;
	}

	public List<CustomExampaperTopic> getTopic() {
		return topic;
	}

	public void setTopic(List<CustomExampaperTopic> topic) {
		this.topic = topic;
	}

	public List<VHomeworkClazz> getOpenClasses() {
		return openClasses;
	}

	public void setOpenClasses(List<VHomeworkClazz> openClasses) {
		this.openClasses = openClasses;
	}

	public Date getOpenAt() {
		return openAt;
	}

	public void setOpenAt(Date openAt) {
		this.openAt = openAt;
	}

	public Date getEnableAt() {
		return enableAt;
	}

	public void setEnableAt(Date enableAt) {
		this.enableAt = enableAt;
	}

}
