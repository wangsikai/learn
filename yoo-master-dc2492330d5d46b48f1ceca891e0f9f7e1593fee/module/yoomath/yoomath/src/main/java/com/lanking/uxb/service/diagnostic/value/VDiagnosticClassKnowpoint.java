package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VDiagnosticClassKnowpoint implements Serializable {
	private static final long serialVersionUID = 2288841413670903502L;

	private long classId;
	private String knowpointName;
	private BigDecimal minDifficulty;
	private BigDecimal maxDifficulty;
	// 平均难度
	private BigDecimal avgDifficulty;
	private int doHard1Count;
	private int doHard2Count;
	private int doHard3Count;
	private int rightHard1Count;
	private int rightHard2Count;
	private int rightHard3Count;

	private int doCount;
	private int rightCount;
	private BigDecimal rightRate;
	private String rightRateTitle;

	// 子节点数据
	private List<VDiagnosticClassKnowpoint> children = Lists.newArrayList();

	// 班级前10名的平均正确率
	private BigDecimal topnRightRate;
	private String topnRightRateTitle;

	private long knowledgeCode;
	@JSONField(serialize = false)
	private long parentCode;

	// @since v2.3.1 平滑公式计算 (n+1)/(n+2)
	private BigDecimal masterRate;

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getKnowpointName() {
		return knowpointName;
	}

	public void setKnowpointName(String knowpointName) {
		this.knowpointName = knowpointName;
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

	public BigDecimal getAvgDifficulty() {
		return avgDifficulty;
	}

	public void setAvgDifficulty(BigDecimal avgDifficulty) {
		this.avgDifficulty = avgDifficulty;
	}

	public int getDoHard1Count() {
		return doHard1Count;
	}

	public void setDoHard1Count(int doHard1Count) {
		this.doHard1Count = doHard1Count;
	}

	public int getDoHard2Count() {
		return doHard2Count;
	}

	public void setDoHard2Count(int doHard2Count) {
		this.doHard2Count = doHard2Count;
	}

	public int getDoHard3Count() {
		return doHard3Count;
	}

	public void setDoHard3Count(int doHard3Count) {
		this.doHard3Count = doHard3Count;
	}

	public int getRightHard1Count() {
		return rightHard1Count;
	}

	public void setRightHard1Count(int rightHard1Count) {
		this.rightHard1Count = rightHard1Count;
	}

	public int getRightHard2Count() {
		return rightHard2Count;
	}

	public void setRightHard2Count(int rightHard2Count) {
		this.rightHard2Count = rightHard2Count;
	}

	public int getRightHard3Count() {
		return rightHard3Count;
	}

	public void setRightHard3Count(int rightHard3Count) {
		this.rightHard3Count = rightHard3Count;
	}

	public int getDoCount() {
		return doCount;
	}

	public void setDoCount(int doCount) {
		this.doCount = doCount;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public List<VDiagnosticClassKnowpoint> getChildren() {
		return children;
	}

	public void setChildren(List<VDiagnosticClassKnowpoint> children) {
		this.children = children;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public long getKnowledgeCode() {
		return knowledgeCode;
	}

	public void setKnowledgeCode(long knowledgeCode) {
		this.knowledgeCode = knowledgeCode;
	}

	public long getParentCode() {
		return parentCode;
	}

	public void setParentCode(long parentCode) {
		this.parentCode = parentCode;
	}

	public BigDecimal getTopnRightRate() {
		return topnRightRate;
	}

	public void setTopnRightRate(BigDecimal topnRightRate) {
		this.topnRightRate = topnRightRate;
	}

	public String getTopnRightRateTitle() {
		return topnRightRateTitle;
	}

	public void setTopnRightRateTitle(String topnRightRateTitle) {
		this.topnRightRateTitle = topnRightRateTitle;
	}

	public BigDecimal getMasterRate() {
		return masterRate;
	}

	public void setMasterRate(BigDecimal masterRate) {
		this.masterRate = masterRate;
	}
}
