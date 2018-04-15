package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 教辅预置内容-易错疑难-例题
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_pc_fd_example")
public class TeachAssistPresetContentFallibleDifficultExample implements Serializable {

	private static final long serialVersionUID = -167242277943354433L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 教辅预置内容-易错疑难ID
	 */
	@Column(name = "teachassist_pc_fallibledifficult_id")
	private Long teachassistPcFallibleDifficultId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 错解
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "wrong_answer")
	private String wrongAnswer;

	/**
	 * 错因分析
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "wrong_analysis")
	private String wrongAnalysis;

	/**
	 * 解题策略
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "solving_strategy")
	private String solvingStrategy;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeachassistPcFallibleDifficultId() {
		return teachassistPcFallibleDifficultId;
	}

	public void setTeachassistPcFallibleDifficultId(Long teachassistPcFallibleDifficultId) {
		this.teachassistPcFallibleDifficultId = teachassistPcFallibleDifficultId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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

	public String getSolvingStrategy() {
		return solvingStrategy;
	}

	public void setSolvingStrategy(String solvingStrategy) {
		this.solvingStrategy = solvingStrategy;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
