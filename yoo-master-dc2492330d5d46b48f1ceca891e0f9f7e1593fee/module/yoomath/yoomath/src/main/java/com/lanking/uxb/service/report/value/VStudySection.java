package com.lanking.uxb.service.report.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;

/**
 * 
 * @author wangsenhao
 *
 */
public class VStudySection implements Serializable {

	private static final long serialVersionUID = -4393784116794218915L;

	private long code;
	private long pcode;
	private int textbookCode;
	private String name;
	private int level;

	private List<VStudySection> children = Lists.newArrayList();
	private String allChildren;
	// 章节掌握情况
	private MasterStatus masterStatus;

	private Long doCount = 0L;
	private Long rightCount = 0L;

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

	public int getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(int textbookCode) {
		this.textbookCode = textbookCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<VStudySection> getChildren() {
		return children;
	}

	public void setChildren(List<VStudySection> children) {
		this.children = children;
	}

	public String getAllChildren() {
		return allChildren;
	}

	public void setAllChildren(String allChildren) {
		this.allChildren = allChildren;
	}

	public MasterStatus getMasterStatus() {
		return masterStatus;
	}

	public void setMasterStatus(MasterStatus masterStatus) {
		this.masterStatus = masterStatus;
	}

	public Long getDoCount() {
		return doCount;
	}

	public void setDoCount(Long doCount) {
		this.doCount = doCount;
	}

	public Long getRightCount() {
		return rightCount;
	}

	public void setRightCount(Long rightCount) {
		this.rightCount = rightCount;
	}

}
