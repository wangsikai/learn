package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 作业题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "homework_question")
public class HomeworkQuestion implements Serializable {

	private static final long serialVersionUID = -7341680209493778839L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 作业ID
	 */
	@Column(name = "homework_id")
	private Long homeworkId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 序号
	 */
	@Column(name = "sequence")
	private Integer sequence;

	/**
	 * 状态
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	/**
	 * 答对数量
	 */
	@Column(name = "right_count")
	private Integer rightCount;

	/**
	 * 答错数量
	 */
	@Column(name = "wrong_count")
	private Integer wrongCount;

	/**
	 * 半错数量
	 */
	@Column(name = "half_wrong_count")
	private Integer halfWrongCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal rightRate;

	/**
	 * 平均用时
	 */
	@Column(name = "do_time", columnDefinition = "bigint default 0")
	private Integer doTime;

	@Transient
	private boolean initRightRate = false;
	@Transient
	private boolean initSub = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getRightCount() {
		return rightCount;
	}

	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

	public Integer getHalfWrongCount() {
		return halfWrongCount;
	}

	public void setHalfWrongCount(Integer halfWrongCount) {
		this.halfWrongCount = halfWrongCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public boolean isInitRightRate() {
		return initRightRate;
	}

	public void setInitRightRate(boolean initRightRate) {
		this.initRightRate = initRightRate;
	}

	public boolean isInitSub() {
		return initSub;
	}

	public void setInitSub(boolean initSub) {
		this.initSub = initSub;
	}

	public Integer getDoTime() {
		return doTime;
	}

	public void setDoTime(Integer doTime) {
		this.doTime = doTime;
	}
}
