package com.lanking.uxb.rescon.basedata.value;

import com.lanking.uxb.rescon.basedata.api.ResconTTSType;

/**
 * @author xinyu.zhou
 */
public class VResconTTS {
	private Long id;
	private Long parent;
	private String name;
	private ResconTTSType type;
	private Integer level;
	private Integer phaseCode;
	private Integer subjectCode;
	private Integer sequence;
	private Integer pcode2;
	private String iconUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResconTTSType getType() {
		return type;
	}

	public void setType(ResconTTSType type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getPcode2() {
		return pcode2;
	}

	public void setPcode2(Integer pcode2) {
		this.pcode2 = pcode2;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}
