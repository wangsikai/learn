package com.lanking.uxb.rescon.basedata.value;

import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.rescon.basedata.api.ResconPointType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class VResconPoint {
	private Integer id;
	private String name;
	private ResconPointType type;
	private Integer parent;
	private Integer phaseCode;
	private Integer subjectCode;
	private Integer level;
	private Status status;
	private Integer sequence;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public ResconPointType getType() {
		return type;
	}

	public void setType(ResconPointType type) {
		this.type = type;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
