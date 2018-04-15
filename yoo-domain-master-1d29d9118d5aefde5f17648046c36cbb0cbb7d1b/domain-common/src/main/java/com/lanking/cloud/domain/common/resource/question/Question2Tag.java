package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.common.baseData.QuestionTag;

/**
 * 题目对应的{@link QuestionTag}
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年7月18日
 */
@Entity
@Table(name = "question_2_tag")
public class Question2Tag implements Serializable {

	private static final long serialVersionUID = 1473368807446368971L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "question_id")
	private long questionId;

	@Column(name = "tag_code")
	private long tagCode;

	/**
	 * 是否为系统自动标注
	 */
	@Column(name = "system")
	private Boolean system = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getTagCode() {
		return tagCode;
	}

	public void setTagCode(long tagCode) {
		this.tagCode = tagCode;
	}

	public Boolean getSystem() {
		return system;
	}

	public void setSystem(Boolean system) {
		this.system = system;
	}

}
