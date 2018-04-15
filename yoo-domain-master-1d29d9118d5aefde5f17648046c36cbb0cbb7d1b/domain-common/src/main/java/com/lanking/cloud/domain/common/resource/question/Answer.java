package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 答案
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "answer")
public class Answer implements Serializable {

	private static final long serialVersionUID = -7681024472877637474L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	/**
	 * 原有的答案保持不变
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content")
	private String content;

	/**
	 * 重新编辑的ascii math格式转出来的latex编码
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content_latex")
	private String contentLatex;

	/**
	 * 重新编辑的ascii math格式
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content_ascii")
	private String contentAscii;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentLatex() {
		return contentLatex;
	}

	public void setContentLatex(String contentLatex) {
		this.contentLatex = contentLatex;
	}

	public String getContentAscii() {
		return contentAscii;
	}

	public void setContentAscii(String contentAscii) {
		this.contentAscii = contentAscii;
	}

}
