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
 * 专题模块内容
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_topiccontent")
public class TeachAssistElementTopicContent implements Serializable {
	private static final long serialVersionUID = 2317401349716464465L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属专题模块ID
	 */
	@Column(name = "topic_id")
	private Long topicId;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 专题解读
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "content")
	private String content;

	/**
	 * 例题1
	 */
	@Column(name = "question1")
	private Long question1;

	/**
	 * 例题1解题策略
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "question1_strategy")
	private String question1Strategy;

	/**
	 * 例题2
	 */
	@Column(name = "question2")
	private Long question2;

	/**
	 * 例题2解题策略
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "question2_strategy")
	private String question2Strategy;

	/**
	 * 例题3
	 */
	@Column(name = "question3")
	private Long question3;

	/**
	 * 例题3解题策略
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "question3_strategy")
	private String question3Strategy;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getQuestion1() {
		return question1;
	}

	public void setQuestion1(Long question1) {
		this.question1 = question1;
	}

	public String getQuestion1Strategy() {
		return question1Strategy;
	}

	public void setQuestion1Strategy(String question1Strategy) {
		this.question1Strategy = question1Strategy;
	}

	public Long getQuestion2() {
		return question2;
	}

	public void setQuestion2(Long question2) {
		this.question2 = question2;
	}

	public String getQuestion2Strategy() {
		return question2Strategy;
	}

	public void setQuestion2Strategy(String question2Strategy) {
		this.question2Strategy = question2Strategy;
	}

	public Long getQuestion3() {
		return question3;
	}

	public void setQuestion3(Long question3) {
		this.question3 = question3;
	}

	public String getQuestion3Strategy() {
		return question3Strategy;
	}

	public void setQuestion3Strategy(String question3Strategy) {
		this.question3Strategy = question3Strategy;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
