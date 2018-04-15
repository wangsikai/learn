package com.lanking.uxb.service.examPaper.form;

import java.math.BigDecimal;
import java.util.List;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperCfg;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperType;

/**
 * 组卷表单
 * 
 * @author zemin.song
 */
public class CustomExamPaperForm {
	// 组卷ID
	private Long id;
	// 教师ID
	private Long teachId;
	// 组卷标题
	private String title;
	// 时长
	private Integer time;
	// 班级
	private Long classId;
	// 总分
	private Integer score;
	// 选择题
	private List<CustomExampaperQuestion> singleQuestions;
	// 填空题
	private List<CustomExampaperQuestion> fillQuestions;
	// 问答题目
	private List<CustomExampaperQuestion> answerQuestions;
	// 组卷类型
	private CustomExampaperType type;
	// 难度
	private BigDecimal difficulty;
	// 状态
	private CustomExampaperStatus status;
	// 分值配置
	private CustomExampaperCfg cfg;
	// 是否下载
	private Boolean download = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeachId() {
		return teachId;
	}

	public void setTeachId(Long teachId) {
		this.teachId = teachId;
	}

	public List<CustomExampaperQuestion> getSingleQuestions() {
		return singleQuestions;
	}

	public void setSingleQuestions(List<CustomExampaperQuestion> singleQuestions) {
		this.singleQuestions = singleQuestions;
	}

	public List<CustomExampaperQuestion> getFillQuestions() {
		return fillQuestions;
	}

	public void setFillQuestions(List<CustomExampaperQuestion> fillQuestions) {
		this.fillQuestions = fillQuestions;
	}

	public List<CustomExampaperQuestion> getAnswerQuestions() {
		return answerQuestions;
	}

	public void setAnswerQuestions(List<CustomExampaperQuestion> answerQuestions) {
		this.answerQuestions = answerQuestions;
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

	public CustomExampaperCfg getCfg() {
		return cfg;
	}

	public void setCfg(CustomExampaperCfg cfg) {
		this.cfg = cfg;
	}

	public Boolean getDownload() {
		return download;
	}

	public void setDownload(Boolean download) {
		this.download = download;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}
}
