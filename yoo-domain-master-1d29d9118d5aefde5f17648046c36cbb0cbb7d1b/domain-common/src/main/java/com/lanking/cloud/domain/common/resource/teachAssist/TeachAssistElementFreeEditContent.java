package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 自由编辑模块内容
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_freeedit_content")
public class TeachAssistElementFreeEditContent implements Serializable {

	private static final long serialVersionUID = 2994748195572555503L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属自由编辑模块ID
	 */
	@Column(name = "free_edit_id")
	private Long freeEditId;

	/**
	 * 内容
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "content")
	private String content;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private int sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFreeEditId() {
		return freeEditId;
	}

	public void setFreeEditId(Long freeEditId) {
		this.freeEditId = freeEditId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
