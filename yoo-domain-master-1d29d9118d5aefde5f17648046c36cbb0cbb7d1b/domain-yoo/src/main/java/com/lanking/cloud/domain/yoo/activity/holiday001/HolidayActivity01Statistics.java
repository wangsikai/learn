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
 * 假期活动01-最终统计
 * 
 * <pre>
 * 1.”这个暑假，有我，由你”(2017年6月13日):按照时间段统计
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
@Entity
@Table(name = "holiday_activity_01_statistics")
public class HolidayActivity01Statistics implements Serializable {

	private static final long serialVersionUID = -621641552176231682L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 活动代码
	 */
	@Column(name = "activity_code")
	private Long activityCode;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private Long classId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 作业数量
	 */
	@Column(name = "homework_count")
	private int homeworkCount;

	/**
	 * 提交率
	 */
	@Column(name = "submit_rate", scale = 2)
	private BigDecimal submitRate;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 评分
	 */
	@Column(name = "score", precision = 5)
	private int score;

	/**
	 * 时间段评分
	 */
	@Column(name = "period_score", precision = 5)
	private int periodScore;

	/**
	 * 时间段 -开始时间
	 */
	@Column(name = "start_period_time", columnDefinition = "datetime(3)")
	private Date startPeriodTime;

	/**
	 * 时间段 -结束时间
	 */
	@Column(name = "end_period_time", columnDefinition = "datetime(3)")
	private Date endPeriodTime;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getHomeworkCount() {
		return homeworkCount;
	}

	public void setHomeworkCount(int homeworkCount) {
		this.homeworkCount = homeworkCount;
	}

	public BigDecimal getSubmitRate() {
		return submitRate;
	}

	public void setSubmitRate(BigDecimal submitRate) {
		this.submitRate = submitRate;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPeriodScore() {
		return periodScore;
	}

	public void setPeriodScore(int periodScore) {
		this.periodScore = periodScore;
	}

	public Date getStartPeriodTime() {
		return startPeriodTime;
	}

	public void setStartPeriodTime(Date startPeriodTime) {
		this.startPeriodTime = startPeriodTime;
	}

	public Date getEndPeriodTime() {
		return endPeriodTime;
	}

	public void setEndPeriodTime(Date endPeriodTime) {
		this.endPeriodTime = endPeriodTime;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
