package com.lanking.uxb.rescon.basedata.form;

import com.lanking.uxb.rescon.basedata.api.ResconTTSType;

/**
 * 章节目录提交表单
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public class ResconTTSForm {
	private Long id;
	private Long pcode;
	private String name;
	private Integer level;
	private ResconTTSType type;
	private Integer phaseCode;
	private Integer subjectCode;
	private Integer sequence;
	private Long pcode2;
	private Long icon;

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ResconTTSType getType() {
		return type;
	}

	public void setType(ResconTTSType type) {
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Long getPcode2() {
		return pcode2;
	}

	public void setPcode2(Long pcode2) {
		this.pcode2 = pcode2;
	}

	public Long getIcon() {
		return icon;
	}

	public void setIcon(Long icon) {
		this.icon = icon;
	}
}
