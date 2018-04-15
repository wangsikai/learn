package com.lanking.uxb.service.code.value;

import java.io.Serializable;

public class VSubject implements Serializable {

	private static final long serialVersionUID = -6519766279547556973L;

	private int code;
	private String name;
	private int sequence;
	private String acronym;
	private int phaseCode;

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

	public int getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(int phaseCode) {
		this.phaseCode = phaseCode;
	}

}
