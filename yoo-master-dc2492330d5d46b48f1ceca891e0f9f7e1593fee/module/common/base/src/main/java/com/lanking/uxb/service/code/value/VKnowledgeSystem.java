package com.lanking.uxb.service.code.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * KnowledgeSystem Value
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class VKnowledgeSystem implements Serializable {
	private static final long serialVersionUID = -1254535472578615014L;

	private long code;
	private long pcode;
	private Integer phaseCode;
	private Integer subjectCode;
	private Integer level;
	private Integer sequence;
	private String name;
	private List<VKnowledgeSystem> children = Lists.newArrayList();
	// 是否练习过(有数据)
	private boolean hasData = false;
	// 掌握百分比
	private BigDecimal masterRate;

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getPcode() {
		return pcode;
	}

	public void setPcode(long pcode) {
		this.pcode = pcode;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<VKnowledgeSystem> getChildren() {
		return children;
	}

	public void setChildren(List<VKnowledgeSystem> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHasData() {
		return hasData;
	}

	public void setHasData(boolean hasData) {
		this.hasData = hasData;
	}

	public BigDecimal getMasterRate() {
		return masterRate;
	}

	public void setMasterRate(BigDecimal masterRate) {
		this.masterRate = masterRate;
	}

}
