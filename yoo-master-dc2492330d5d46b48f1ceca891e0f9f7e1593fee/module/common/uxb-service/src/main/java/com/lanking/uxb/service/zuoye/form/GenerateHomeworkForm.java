package com.lanking.uxb.service.zuoye.form;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 生成作业的参数
 * 
 * @since 2.1
 * @since 2.1.2 添加新知识点
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public class GenerateHomeworkForm {
	private Long textbookExerciseId;
	private Long sectionCode;
	private Integer count;
	private BigDecimal difficulty;
	private List<Integer> metaKnowpoints = Lists.newArrayList();

	// 新知识点 yoomath v2.1.2 wanlong.che
	private List<Long> knowledgePoints = Lists.newArrayList();

	// 7:2:1 5:3:2 4:4:2目前为三种
	private String difficultyMap;

	// 普通题数量：[0.8,1]
	private Integer simple;
	// 难题数量[0.4,0.8)
	private Integer difficult;
	// 提高题数量[0,0.4)
	private Integer improve;
	/**
	 * 版本从1.2开始,默认为1.2<br>
	 * 1.2:填空题 <br>
	 * 1.3:单选、多选、填空题
	 */
	private String version = "1.2";

	// 最小难度
	private BigDecimal minDifficulty;
	// 最大难度
	private BigDecimal maxDifficulty;

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
		if (count == null || count < 0) {
			setCount(0);
		}
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public List<Integer> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Integer> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public String getDifficultyMap() {
		return difficultyMap;
	}

	public void setDifficultyMap(String difficultyMap) {
		this.difficultyMap = difficultyMap;
	}

	public Integer getSimple() {
		if (simple == null || simple < 0) {
			setSimple(0);
		}
		return simple;
	}

	public void setSimple(Integer simple) {
		this.simple = simple;
	}

	public Integer getImprove() {
		if (improve == null || improve < 0) {
			setImprove(0);
		}
		return improve;
	}

	public void setImprove(Integer improve) {
		this.improve = improve;
	}

	public Integer getDifficult() {
		if (difficult == null || difficult < 0) {
			setDifficult(0);
		}
		return difficult;
	}

	public void setDifficult(Integer difficult) {
		this.difficult = difficult;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

}
