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
 * 预习点模块内容
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_preparegoalcontent")
public class TeachAssistElementPrepareGoalContent implements Serializable {
	private static final long serialVersionUID = -3696844607458659026L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属预习点模块ID
	 */
	@Column(name = "goal_id")
	private Long goalId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 500)
	private String name;

	/**
	 * 关联的知识点列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "knowpoints")
	private List<Long> knowpoints;

	/**
	 * 预习题目列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "preview_questions")
	private List<Long> previewQuestions;

	/**
	 * 自测题目列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "selftest_questions")
	private List<Long> selfTestQuestions;

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

	public Long getGoalId() {
		return goalId;
	}

	public void setGoalId(Long goalId) {
		this.goalId = goalId;
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

	public List<Long> getPreviewQuestions() {
		return previewQuestions;
	}

	public void setPreviewQuestions(List<Long> previewQuestions) {
		this.previewQuestions = previewQuestions;
	}

	public List<Long> getSelfTestQuestions() {
		return selfTestQuestions;
	}

	public void setSelfTestQuestions(List<Long> selfTestQuestions) {
		this.selfTestQuestions = selfTestQuestions;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
