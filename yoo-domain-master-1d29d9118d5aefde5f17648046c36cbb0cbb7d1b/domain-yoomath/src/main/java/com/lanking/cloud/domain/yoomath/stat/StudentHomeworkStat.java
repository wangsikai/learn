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
 * 学生作业统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_homework_stat")
public class StudentHomeworkStat implements Serializable {

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
	 * 待处理作业数量
	 */
	@Column(name = "todo_num")
	private long todoNum = 0;

	/**
	 * 过期未提交数量
	 */
	@Column(name = "overdue_num", columnDefinition = "bigint default 0")
	private long overdueNum = 0;

	/**
	 * 平均正确率
	 */
	@Column(name = "right_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal rightRate;

	/**
	 * 平均用时
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

	/**
	 * 班级排名
	 */
	@Column(name = "rank")
	private Integer rank;

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

	public long getTodoNum() {
		return todoNum;
	}

	public void setTodoNum(long todoNum) {
		this.todoNum = todoNum;
	}

	public long getOverdueNum() {
		return overdueNum;
	}

	public void setOverdueNum(long overdueNum) {
		this.overdueNum = overdueNum;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
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

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

}
