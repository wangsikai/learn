package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 考点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "examination_point")
public class ExaminationPoint implements Serializable {

	private static final long serialVersionUID = 8647417808342230874L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 512)
	private String name;

	/**
	 * 父级代码
	 */
	@Column(name = "pcode")
	private Long pcode;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 考点频率
	 * 
	 * @see ExaminationPointFrequency
	 */
	@Column(name = "frequency", precision = 3)
	private ExaminationPointFrequency frequency;

	/**
	 * 关联知识点
	 */
	@Deprecated
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<Long> knowpoints = Lists.newArrayList();

	/**
	 * 关联的典型题目
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<Long> questions = Lists.newArrayList();

	/**
	 * 考点生效状态（默认未生效，生效后只能变为禁用DISABLED）.
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.DELETED;

	/**
	 * 关联的知识点个数
	 */
	@Column(name = "knowpoint_count", columnDefinition = "bigint default 0")
	private Integer knowpointCount;

	/**
	 * 关联题目的个数
	 */
	@Column(name = "question_count", columnDefinition = "bigint default 0")
	private Integer questionCount;

	@Transient
	private boolean hasPhase = false;
	@Transient
	private boolean hasSubject = false;

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

	public Integer getKnowpointCount() {
		return knowpointCount;
	}

	public void setKnowpointCount(Integer knowpointCount) {
		this.knowpointCount = knowpointCount;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public boolean isHasPhase() {
		return hasPhase;
	}

	public void setHasPhase(boolean hasPhase) {
		this.hasPhase = hasPhase;
	}

	public boolean isHasSubject() {
		return hasSubject;
	}

	public void setHasSubject(boolean hasSubject) {
		this.hasSubject = hasSubject;
	}

}
