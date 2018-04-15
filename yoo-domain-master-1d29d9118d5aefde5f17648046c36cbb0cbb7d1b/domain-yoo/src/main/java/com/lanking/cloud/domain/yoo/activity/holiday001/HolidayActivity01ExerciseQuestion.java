package com.lanking.cloud.domain.yoo.activity.holiday001;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 假期活动01-习题对应的题目
 * 
 * <pre>
 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日)
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
@Entity
@Table(name = "holiday_activity_01_exercise_question")
public class HolidayActivity01ExerciseQuestion implements Serializable {

	private static final long serialVersionUID = 8190457242011205481L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 活动代码
	 */
	@Column(name = "activity_code")
	private long activityCode;

	/**
	 * 对应的习题ID
	 */
	@Column(name = "holiday_activity_01_exercise_id")
	private long holidayActivity01ExerciseId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private long questionId;

	/**
	 * 序号（从1开始）
	 */
	@Column(name = "sequence", precision = 3)
	private int sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(long activityCode) {
		this.activityCode = activityCode;
	}

	public long getHolidayActivity01ExerciseId() {
		return holidayActivity01ExerciseId;
	}

	public void setHolidayActivity01ExerciseId(long holidayActivity01ExerciseId) {
		this.holidayActivity01ExerciseId = holidayActivity01ExerciseId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
