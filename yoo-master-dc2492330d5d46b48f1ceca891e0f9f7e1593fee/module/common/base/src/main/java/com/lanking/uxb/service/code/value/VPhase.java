package com.lanking.uxb.service.code.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class VPhase implements Serializable {

	private static final long serialVersionUID = 4509923242498494071L;

	private int code;
	private String name;
	private int sequence;
	private String acronym;
	private List<VSubject> subjects = Lists.newArrayList();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public List<VSubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<VSubject> subjects) {
		this.subjects = subjects;
	}

}
