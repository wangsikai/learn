package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;

/**
 * 习题内容模块内容
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_practicecontent")
public class TeachAssistElementPracticeContent implements Serializable {

	private static final long serialVersionUID = 1718214122780811197L;
	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属习题内容模块ID
	 */
	@Column(name = "practice_id")
	private Long practiceId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 500)
	private String name;

	/**
	 * 例题列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "questions", length = 4000)
	private List<Long> questions;

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

	public Long getPracticeId() {
		return practiceId;
	}

	public void setPracticeId(Long practiceId) {
		this.practiceId = practiceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Long> questions) {
		this.questions = questions;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
