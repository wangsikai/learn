package com.lanking.cloud.domain.yoomath.stat;

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
 * 刷题数量班级范围内的统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "do_question_class_stat")
public class DoQuestionClassStat implements Serializable {

	private static final long serialVersionUID = -596543889222289415L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 班级老师的所属学校
	 */
	@Column(name = "school_id")
	private long schoolId;

	/**
	 * 班级老师的所属阶段
	 */
	@Column(name = "phase_code")
	private int phaseCode;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private long classId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

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
	 * 班级排名
	 */
	@Column(name = "rank")
	private Integer rank;

	/**
	 * n天的统计,如最近7天30天365天
	 */
	@Column(name = "day0")
	private int day;

	/**
	 * 状态,统计最新数据的时候保持上一份数据不变,当前入库数据为DISABLED,统计完成后删除上一份统计数据,将当前数据修改为ENABLED
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.DISABLED;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public int getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(int phaseCode) {
		this.phaseCode = phaseCode;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
