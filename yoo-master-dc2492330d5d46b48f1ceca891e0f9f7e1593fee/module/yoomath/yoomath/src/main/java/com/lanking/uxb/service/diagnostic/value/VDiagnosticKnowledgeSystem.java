package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;

/**
 * 知识图谱专用VO
 *
 * 其他模块请勿使用此VO
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VDiagnosticKnowledgeSystem implements Serializable {
	private static final long serialVersionUID = 4565083826113141688L;
	// 专项code
	private long code;
	// 专项名
	private String name;
	// 小专题code 注意 再往上层级并不需要知道
	private long pcode;

	// 大专题名
	private String level1Name;
	// 小专题名
	private String level2Name;
	// 是否练习过(有数据)
	private boolean hasData = false;

	public boolean isHasData() {
		return hasData;
	}

	public void setHasData(boolean hasData) {
		this.hasData = hasData;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPcode() {
		return pcode;
	}

	public void setPcode(long pcode) {
		this.pcode = pcode;
	}

	public String getLevel1Name() {
		return level1Name;
	}

	public void setLevel1Name(String level1Name) {
		this.level1Name = level1Name;
	}

	public String getLevel2Name() {
		return level2Name;
	}

	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
	}
}
