package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 学科
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "subject")
public class Subject implements Serializable {
	private static final long serialVersionUID = -8637698767077240639L;

	/**
	 * 学科代码
	 */
	@Id
	private int code;

	/**
	 * 学科名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 首字母
	 */
	@Column(name = "acronym", length = 10)
	private String acronym;

	/**
	 * 序号
	 */
	@Column(name = "sequence", columnDefinition = "bigint default 0")
	private Integer sequence;

	/**
	 * 状态
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

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

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
