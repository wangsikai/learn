package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 题目具体类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "question_type")
public class QuestionType implements Serializable {

	private static final long serialVersionUID = -7131265944160336165L;

	/**
	 * 代码
	 */
	@Id
	private Integer code;

	/**
	 * 学科名称
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 序号
	 */
	@Column(name = "sequence")
	private Long sequence;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
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

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
