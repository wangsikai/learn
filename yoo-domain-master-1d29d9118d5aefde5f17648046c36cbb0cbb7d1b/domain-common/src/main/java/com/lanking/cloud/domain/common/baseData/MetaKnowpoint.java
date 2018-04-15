package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 元知识点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "meta_knowpoint")
public class MetaKnowpoint implements Serializable {

	private static final long serialVersionUID = 3600601644542293947L;

	/**
	 * 元知识点代码
	 */
	@Id
	private Integer code;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
