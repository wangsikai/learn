package com.lanking.cloud.domain.yoomath.examPaper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 组卷题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "custom_exampaper_question")
public class CustomExampaperQuestion implements Serializable {

	private static final long serialVersionUID = -5378511652778020140L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 组卷ID {@link CustomExampaper}.id
	 */
	@Column(name = "custom_exampaper_id")
	private Long customExampaperId;

	/**
	 * 对应的题目大的类型 {@link CustomExampaperTopic}.id
	 */
	@Column(name = "custom_exampaper_topic_id")
	private Long customExampaperTopicId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 难度
	 */
	@Column(name = "difficulty", scale = 2)
	private BigDecimal difficulty;

	/**
	 * 答案数量
	 * 
	 * <pre>
	 * 单选题1个,填空题为填空的空数,解答题1个
	 * </pre>
	 */
	@Column(name = "answer_count", precision = 3)
	private Integer answerCount;

	/**
	 * 题目基本类型
	 */
	@Column(name = "question_type", precision = 3)
	private Type questionType;

	/**
	 * 题目类型
	 */
	@Column(name = "type", precision = 3)
	private CustomExampaperTopicType type;

	/**
	 * 分值
	 */
	@Column(name = "score", precision = 3)
	private Integer score;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 序号
	 */
	@Column(name = "sequence")
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomExampaperId() {
		return customExampaperId;
	}

	public void setCustomExampaperId(Long customExampaperId) {
		this.customExampaperId = customExampaperId;
	}

	public Long getCustomExampaperTopicId() {
		return customExampaperTopicId;
	}

	public void setCustomExampaperTopicId(Long customExampaperTopicId) {
		this.customExampaperTopicId = customExampaperTopicId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	public Type getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Type questionType) {
		this.questionType = questionType;
	}

	public CustomExampaperTopicType getType() {
		return type;
	}

	public void setType(CustomExampaperTopicType type) {
		this.type = type;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
