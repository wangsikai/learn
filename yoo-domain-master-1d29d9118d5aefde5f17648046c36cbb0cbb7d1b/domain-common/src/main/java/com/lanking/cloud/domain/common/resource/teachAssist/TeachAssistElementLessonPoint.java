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
 * 课内教学模块-教学点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_lessonpoint")
public class TeachAssistElementLessonPoint implements Serializable {

	private static final long serialVersionUID = -6878270948537004636L;
	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属课内教学模块ID
	 */
	@Column(name = "lesson_id")
	private Long lessonId;

	/**
	 * 教学点名称
	 */
	@Column(name = "name", length = 500)
	private String name;

	/**
	 * 关联知识点
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "knowpoints")
	private List<Long> knowpoints;

	/**
	 * 例题
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "example_questions")
	private List<Long> exampleQuestions;

	/**
	 * 例题拓展
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "expand_questions")
	private List<Long> expandQuestions;

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

	public Long getLessonId() {
		return lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<Long> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public List<Long> getExampleQuestions() {
		return exampleQuestions;
	}

	public void setExampleQuestions(List<Long> exampleQuestions) {
		this.exampleQuestions = exampleQuestions;
	}

	public List<Long> getExpandQuestions() {
		return expandQuestions;
	}

	public void setExpandQuestions(List<Long> expandQuestions) {
		this.expandQuestions = expandQuestions;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
