package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.common.baseData.QuestionCategory;

/**
 * 题目对应的{@link QuestionCategory}
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年7月18日
 */
@Entity
@Table(name = "question_2_category")
public class Question2Category implements Serializable {

	private static final long serialVersionUID = 6526574277819755305L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "question_id")
	private long questionId;

	@Column(name = "category_code")
	private long categoryCode;

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

	public long getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(long categoryCode) {
		this.categoryCode = categoryCode;
	}

}
