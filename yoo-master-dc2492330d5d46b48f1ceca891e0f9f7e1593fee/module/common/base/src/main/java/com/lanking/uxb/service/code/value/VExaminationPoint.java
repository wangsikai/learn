package com.lanking.uxb.service.code.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.baseData.ExaminationPointFrequency;

/**
 * 考点Value
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class VExaminationPoint implements Serializable {
	private static final long serialVersionUID = 7029781197344975761L;

	private long id;
	private String name;
	private ExaminationPointFrequency frequency;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

}
