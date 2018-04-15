package com.lanking.cloud.domain.yoo.activity.holiday001;

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

/**
 * 假期活动01-参与活动的用户布置的作业
 * 
 * <pre>
 * 1.”这个暑假，有我，由你”(2017年6月13日):参与用户布置作业的时候写入,布置作业的时候写入
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
@Entity
@Table(name = "holiday_activity_01_homework")
public class HolidayActivity01Homework implements Serializable {

	private static final long serialVersionUID = 6677207806687000984L;

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
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private long classId;

	/**
	 * 作业ID
	 */
	@Column(name = "homework_id")
	private Long homeworkId;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "deadline", columnDefinition = "datetime(3)")
	private Date deadline;

	/**
	 * 对应的预置习题ID
	 */
	@Column(name = "holiday_activity_01_exercise_id")
	private long holidayActivity01ExerciseId;

	/**
	 * 作业提交率
	 */
	@Column(name = "submit_rate", scale = 2)
	private BigDecimal submitRate;

	/**
	 * 因为此作业获取的抽奖机会次数(动态变化的)
	 */
	@Column(name = "lucky_draw")
	private int luckyDraw;

	/**
	 * 此作业是否有效(班级人数小于20个无效)
	 */
	@Column(name = "valid")
	private Boolean valid;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public long getHolidayActivity01ExerciseId() {
		return holidayActivity01ExerciseId;
	}

	public void setHolidayActivity01ExerciseId(long holidayActivity01ExerciseId) {
		this.holidayActivity01ExerciseId = holidayActivity01ExerciseId;
	}

	public BigDecimal getSubmitRate() {
		return submitRate;
	}

	public void setSubmitRate(BigDecimal submitRate) {
		this.submitRate = submitRate;
	}

	public int getLuckyDraw() {
		return luckyDraw;
	}

	public void setLuckyDraw(int luckyDraw) {
		this.luckyDraw = luckyDraw;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
