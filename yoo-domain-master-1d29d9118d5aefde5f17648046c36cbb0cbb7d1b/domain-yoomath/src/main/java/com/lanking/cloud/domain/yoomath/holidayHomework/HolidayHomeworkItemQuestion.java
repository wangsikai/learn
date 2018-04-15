package com.lanking.cloud.domain.yoomath.holidayHomework;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 假日作业项题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "holiday_homework_item_question")
public class HolidayHomeworkItemQuestion implements Serializable {

	private static final long serialVersionUID = 2609630824531856449L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 假日作业项ID {@link HolidayHomeworkItem}.id
	 */
	@Column(name = "holiday_homework_item_id")
	private long holidayHomeworkItemId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 顺序
	 */
	@Column(name = "sequence")
	private Integer sequence;

	/**
	 * 状态
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	/**
	 * 对题数量
	 */
	@Column(name = "right_count")
	private Integer rightCount;

	/**
	 * 错题数量
	 */
	@Column(name = "wrong_count")
	private Integer wrongCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 完成率
	 */
	@Column(name = "completion_rate", scale = 2)
	private BigDecimal completionRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getHolidayHomeworkItemId() {
		return holidayHomeworkItemId;
	}

	public void setHolidayHomeworkItemId(long holidayHomeworkItemId) {
		this.holidayHomeworkItemId = holidayHomeworkItemId;
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

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

}
