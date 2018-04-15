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
 * 学生做题自然月统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "do_question_stu_natural_month_stat")
public class DoQuestionStuNaturalMonthStat implements Serializable {

	private static final long serialVersionUID = 7174120672372801741L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 答题数量
	 */
	@Column(name = "do_count")
	private long doCount;

	/**
	 * 答对数量
	 */
	@Column(name = "right_count")
	private long rightCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 最近6个月的统计(1,2,3,4,5,6)
	 */
	@Column(name = "month0")
	private int month;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getDoCount() {
		return doCount;
	}

	public void setDoCount(long doCount) {
		this.doCount = doCount;
	}

	public long getRightCount() {
		return rightCount;
	}

	public void setRightCount(long rightCount) {
		this.rightCount = rightCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
