package com.lanking.uxb.service.zuoye.form;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 布置作业
 * 
 * @since 2.1
 * @since 2.1.2 新旧知识点转换 wanlong.che 2016-11-22
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public class PullQuestionForm {
	// 拉取题目类型
	private PullQuestionType type;
	// 预置练习ID(必须设置)
	private Long textbookExerciseId;
	// 章节代码(必须设置)
	private Long sectionCode;
	// 获取题目的数量(必须设置)
	private Integer count;
	// 包含知识点(必须设置)
	private List<Long> metaKnowpoints = Lists.newArrayList();

	// 包含新知识点(必须设置)
	private List<Long> knowledgePoints = Lists.newArrayList();

	// 已经有的题目ID列表(当页面上有数据的时候需要设置此参数)
	private List<Long> qIds;
	// 题目所含知识点(调整难度、换题)
	private List<Long> questionMetaKnowpoints = Lists.newArrayList();

	// 题目所含新知识点(调整难度、换题)
	private List<Long> questionKnowledgePoints = Lists.newArrayList();

	// 题目难度(换题时使用)
	private BigDecimal difficulty;
	// 最小难度,增加难度使用(调整难度时使用)
	private BigDecimal minDifficulty;
	// 最大难度,降低难度使用(调整难度时使用)
	private BigDecimal maxDifficulty;
	/**
	 * 版本从1.2开始,默认为1.2<br>
	 * 1.2:填空题 <br>
	 * 1.3:单选、多选、填空题<br>
	 * 1.4：单选、多选
	 */
	private String version = "1.2";

	public PullQuestionType getType() {
		return type;
	}

	public void setType(PullQuestionType type) {
		this.type = type;
	}

	public Long getTextbookExerciseId() {
		return textbookExerciseId;
	}

	public void setTextbookExerciseId(Long textbookExerciseId) {
		this.textbookExerciseId = textbookExerciseId;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<Long> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Long> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public List<Long> getQuestionMetaKnowpoints() {
		return questionMetaKnowpoints;
	}

	public void setQuestionMetaKnowpoints(List<Long> questionMetaKnowpoints) {
		this.questionMetaKnowpoints = questionMetaKnowpoints;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public BigDecimal getMinDifficulty() {
		return minDifficulty;
	}

	public void setMinDifficulty(BigDecimal minDifficulty) {
		this.minDifficulty = minDifficulty;
	}

	public BigDecimal getMaxDifficulty() {
		return maxDifficulty;
	}

	public void setMaxDifficulty(BigDecimal maxDifficulty) {
		this.maxDifficulty = maxDifficulty;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public List<Long> getQuestionKnowledgePoints() {
		return questionKnowledgePoints;
	}

	public void setQuestionKnowledgePoints(List<Long> questionKnowledgePoints) {
		this.questionKnowledgePoints = questionKnowledgePoints;
	}
}
