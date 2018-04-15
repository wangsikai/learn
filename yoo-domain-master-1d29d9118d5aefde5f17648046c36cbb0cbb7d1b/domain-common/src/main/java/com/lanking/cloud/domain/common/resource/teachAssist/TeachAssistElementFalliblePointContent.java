package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;

/**
 * 易错点模块内容
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_fallpoint_content")
public class TeachAssistElementFalliblePointContent implements Serializable {
	private static final long serialVersionUID = 5588873610678638229L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属易错点模块ID
	 */
	@Column(name = "fallpoint_id")
	private Long fallpointId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 500)
	private String name;

	/**
	 * 易错点辨析
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "analysis")
	private String analysis;

	/**
	 * 易错点例题
	 */
	@Column(name = "fall_example_question")
	private Long fallExampleQuestion;

	/**
	 * 错解
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "wrong_answer")
	private String wrongAnswer;

	/**
	 * 错因分析
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "wrong_analysis")
	private String wrongAnalysis;

	/**
	 * 解题策略
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "strategy")
	private String strategy;

	/**
	 * 针对训练题 最多三题
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "practice_questions", length = 1000)
	private List<Long> practiceQuestions;

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

	public Long getFallpointId() {
		return fallpointId;
	}

	public void setFallpointId(Long fallpointId) {
		this.fallpointId = fallpointId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public Long getFallExampleQuestion() {
		return fallExampleQuestion;
	}

	public void setFallExampleQuestion(Long fallExampleQuestion) {
		this.fallExampleQuestion = fallExampleQuestion;
	}

	public String getWrongAnswer() {
		return wrongAnswer;
	}

	public void setWrongAnswer(String wrongAnswer) {
		this.wrongAnswer = wrongAnswer;
	}

	public String getWrongAnalysis() {
		return wrongAnalysis;
	}

	public void setWrongAnalysis(String wrongAnalysis) {
		this.wrongAnalysis = wrongAnalysis;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public List<Long> getPracticeQuestions() {
		return practiceQuestions;
	}

	public void setPracticeQuestions(List<Long> practiceQuestions) {
		this.practiceQuestions = practiceQuestions;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
