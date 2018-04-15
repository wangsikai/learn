package com.lanking.uxb.service.zuoye.api;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 试卷参数
 * 
 * @since 2.1
 * @since 2.1.2 新知识点
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月16日
 */
public class PaperParam {
	// 题目数量
	private int count;
	// 题目平均难度
	private BigDecimal difficulty;
	// 包含的知识点
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
	// 只获取ids?
	private boolean ids = true;
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

	private boolean isMobile = false;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
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
		return simple;
	}

	public void setSimple(Integer simple) {
		this.simple = simple;
	}

	public Integer getImprove() {
		return improve;
	}

	public void setImprove(Integer improve) {
		this.improve = improve;
	}

	public Integer getDifficult() {
		return difficult;
	}

	public void setDifficult(Integer difficult) {
		this.difficult = difficult;
	}

	public boolean isIds() {
		return ids;
	}

	public void setIds(boolean ids) {
		this.ids = ids;
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

	public boolean isMobile() {
		return isMobile;
	}

	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}
}
