package com.lanking.cloud.domain.yoomath.diagnostic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 诊断-班级-班级内学生最近n天的相关统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "diagno_class_student")
public class DiagnosticClassStudent implements Serializable {

	private static final long serialVersionUID = 8456872015644879007L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

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
	 * 排名
	 */
	@Column(name = "rank", precision = 5)
	private Integer rank;

	/**
	 * 排名浮动
	 */
	@Column(name = "float_rank", precision = 5)
	private int floatRank;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 作业次数
	 */
	@Column(name = "homework_count", precision = 5)
	private int homeworkCount;

	/**
	 * 表示全部时为0
	 */
	@Column(name = "day0", precision = 5)
	private int day0;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	/**
	 * 统计时间
	 */
	@Column(name = "statistic_at")
	private long statisticAt;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 统计范围内的正确率变化
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "right_rates", length = 4000)
	private List<BigDecimal> rightRates;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public int getFloatRank() {
		return floatRank;
	}

	public void setFloatRank(int floatRank) {
		this.floatRank = floatRank;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public int getHomeworkCount() {
		return homeworkCount;
	}

	public void setHomeworkCount(int homeworkCount) {
		this.homeworkCount = homeworkCount;
	}

	public int getDay0() {
		return day0;
	}

	public void setDay0(int day0) {
		this.day0 = day0;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getStatisticAt() {
		return statisticAt;
	}

	public void setStatisticAt(long statisticAt) {
		this.statisticAt = statisticAt;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<BigDecimal> getRightRates() {
		return rightRates;
	}

	public void setRightRates(List<BigDecimal> rightRates) {
		this.rightRates = rightRates;
	}
}
