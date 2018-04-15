package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.base.mongodb.AbstractMongoObject;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 知识点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "knowledge_point")
public class KnowledgePoint extends AbstractMongoObject implements Serializable {

	private static final long serialVersionUID = -4803623565128620802L;

	/**
	 * 知识点代码
	 */
	@Id
	@org.springframework.data.annotation.Id
	private Long code;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 60)
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
	 * 考纲要求
	 */
	@Column(name = "difficulty", precision = 3)
	private KnowledgePointDifficulty difficulty;

	/**
	 * 学习要求
	 */
	@Column(name = "study_difficulty", precision = 3)
	private StudyDifficulty studyDifficulty;

	/**
	 * 重点&难点
	 */
	@Column(name = "focal_difficult", precision = 3)
	private FocalDifficult focalDifficult;

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

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
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

	public KnowledgePointDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(KnowledgePointDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public StudyDifficulty getStudyDifficulty() {
		return studyDifficulty;
	}

	public void setStudyDifficulty(StudyDifficulty studyDifficulty) {
		this.studyDifficulty = studyDifficulty;
	}

	public FocalDifficult getFocalDifficult() {
		return focalDifficult;
	}

	public void setFocalDifficult(FocalDifficult focalDifficult) {
		this.focalDifficult = focalDifficult;
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
