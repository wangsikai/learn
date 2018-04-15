package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.base.mongodb.AbstractMongoObject;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 复习知识点
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年10月30日
 */
@Setter
@Getter
@Entity
@Table(name = "knowledge_review")
public class KnowledgeReview extends AbstractMongoObject implements Serializable {

	private static final long serialVersionUID = 238294045250048188L;

	@Id
	@org.springframework.data.annotation.Id
	private Long code;

	@Column(name = "name", length = 60)
	private String name;

	@Column(name = "pcode")
	private Long pcode;

	@Column(name = "phase_code")
	private Integer phaseCode;

	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 课标要求，考纲要求
	 */
	@Column(name = "study_difficulty", precision = 3)
	private StudyDifficulty studyDifficulty;

	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	@Column(name = "status", precision = 3)
	private Status status;

	/**
	 * 层级
	 */
	@Column(name = "level", precision = 3)
	private Integer level;
}
