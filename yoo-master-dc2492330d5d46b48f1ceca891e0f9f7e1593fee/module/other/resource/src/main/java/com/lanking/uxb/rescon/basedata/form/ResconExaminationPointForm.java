package com.lanking.uxb.rescon.basedata.form;

import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.ExaminationPointFrequency;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 考点表单.
 * 
 * @author wlche
 * @since v2.0.1
 */
public class ResconExaminationPointForm {

	private Long id;
	private String name;
	private ExaminationPointFrequency frequency = ExaminationPointFrequency.LOW;
	private Integer phaseCode;
	private Integer subjectCode;
	private Long pcode;
	private List<Long> knowpoints = Lists.newArrayList();
	private List<Long> questions = Lists.newArrayList();
	private Status status = Status.DELETED;
	private int editFlag = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExaminationPointFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(ExaminationPointFrequency frequency) {
		this.frequency = frequency;
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

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
	}

	public List<Long> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<Long> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public List<Long> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Long> questions) {
		this.questions = questions;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(int editFlag) {
		this.editFlag = editFlag;
	}
}
