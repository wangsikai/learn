package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 知识点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "knowpoint")
public class Knowpoint implements Serializable {

	private static final long serialVersionUID = 1581667763156804875L;

	/**
	 * 知识点代码
	 */
	@Id
	private Integer code;

	/**
	 * 父级代码
	 */
	@Column(name = "pcode")
	private Integer pcode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 60)
	private String name;

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
	 * 层级
	 */
	@Column(name = "level", precision = 3)
	private Integer level;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getPcode() {
		return pcode;
	}

	public void setPcode(Integer pcode) {
		this.pcode = pcode;
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

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
