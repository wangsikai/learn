package com.lanking.cloud.domain.yoomath.stat;

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
 * 作业统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "homework_stat")
public class HomeworkStat implements Serializable {

	private static final long serialVersionUID = -1521713192996770095L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 课程ID
	 * 
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 */
	@Deprecated
	@Column(name = "course_id")
	private Long courseId;

	/**
	 * 班级ID
	 */
	@Column(name = "homework_class_id")
	private Long homeworkClassId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 作业数量
	 */
	@Column(name = "homework_num")
	private long homeWorkNum = 0;

	/**
	 * 待处理数量
	 */
	@Column(name = "doing_num")
	private long doingNum = 0;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal rightRate;

	/**
	 * 已下发作业的完成率
	 */
	@Column(name = "completion_rate", scale = 2)
	private BigDecimal completionRate;

	/**
	 * 作业平均用时
	 */
	@Column(name = "homework_time")
	private int homeworkTime = 0;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

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

	/**
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 * @return
	 */
	@Deprecated
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 * @param courseId
	 */
	@Deprecated
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public long getHomeWorkNum() {
		return homeWorkNum;
	}

	public void setHomeWorkNum(long homeWorkNum) {
		this.homeWorkNum = homeWorkNum;
	}

	public long getDoingNum() {
		return doingNum;
	}

	public void setDoingNum(long doingNum) {
		this.doingNum = doingNum;
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

	public int getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(int homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
