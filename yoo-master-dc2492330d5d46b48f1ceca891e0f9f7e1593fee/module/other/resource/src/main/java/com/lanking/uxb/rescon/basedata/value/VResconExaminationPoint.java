package com.lanking.uxb.rescon.basedata.value;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.ExaminationPointFrequency;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;

/**
 * 考点.
 * 
 * @author wlche
 *
 */
public class VResconExaminationPoint {
	private Long id;
	private String name;
	private Long pcode;
	private Integer subjectCode;
	private Integer phaseCode;
	private ExaminationPointFrequency frequency;
	private List<Long> knowpointCodes;
	private List<Long> questionIds;
	private Status status;

	private VPhase phase;
	private VSubject subject;

	// 关联知识点
	private List<VKnowledgePoint> knowpoints;

	// 关联习题.
	private List<VQuestion> questions;

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

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
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

	public ExaminationPointFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(ExaminationPointFrequency frequency) {
		this.frequency = frequency;
	}

	public List<Long> getKnowpointCodes() {
		return knowpointCodes;
	}

	public void setKnowpointCodes(List<Long> knowpointCodes) {
		this.knowpointCodes = knowpointCodes;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public List<VKnowledgePoint> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<VKnowledgePoint> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public List<VQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<VQuestion> questions) {
		this.questions = questions;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public VSubject getSubject() {
		return subject;
	}

	public void setSubject(VSubject subject) {
		this.subject = subject;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
