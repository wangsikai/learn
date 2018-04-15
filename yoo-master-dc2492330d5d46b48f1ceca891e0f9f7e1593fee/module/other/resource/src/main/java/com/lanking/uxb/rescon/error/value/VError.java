package com.lanking.uxb.rescon.error.value;

import java.io.Serializable;
import java.util.List;

/**
 * 纠错VO
 * 
 * @author wangsenhao
 *
 */
public class VError implements Serializable {

	private static final long serialVersionUID = -8344881112392289735L;
	/**
	 * 题目编号
	 */
	private String code;
	private String phaseName;
	/**
	 * 纠错次数
	 */
	private Long errorCount;
	/**
	 * 题目类型
	 */
	private String typeName;
	private Long questionId;
	private List<VErrorUser> errorUserList;

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Long errorCount) {
		this.errorCount = errorCount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<VErrorUser> getErrorUserList() {
		return errorUserList;
	}

	public void setErrorUserList(List<VErrorUser> errorUserList) {
		this.errorUserList = errorUserList;
	}
}
