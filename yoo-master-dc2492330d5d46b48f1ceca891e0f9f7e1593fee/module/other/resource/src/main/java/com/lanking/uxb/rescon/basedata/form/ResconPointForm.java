package com.lanking.uxb.rescon.basedata.form;

import com.lanking.uxb.rescon.basedata.api.ResconPointType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class ResconPointForm {
	private Integer code;
	private String name;
	private ResconPointType type;
	private Integer pcode;
	private Boolean switchType;
	private Integer subjectCode;
	private Integer phaseCode;
	private Boolean isAdd;
	private Integer level;
	private Integer sequence;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPcode() {
		return pcode;
	}

	public void setPcode(Integer pcode) {
		this.pcode = pcode;
	}

	public Boolean getSwitchType() {
		return switchType;
	}

	public void setSwitchType(Boolean switchType) {
		this.switchType = switchType;
	}

	public ResconPointType getType() {
		return type;
	}

	public void setType(ResconPointType type) {
		this.type = type;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Boolean getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Boolean isAdd) {
		this.isAdd = isAdd;
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
}
