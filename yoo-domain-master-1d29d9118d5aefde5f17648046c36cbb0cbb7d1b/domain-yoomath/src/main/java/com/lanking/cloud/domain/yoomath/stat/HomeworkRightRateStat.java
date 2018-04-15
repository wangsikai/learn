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
 * 作业正确率统计
 * 
 * <pre>
 * 定点统计班级之前左右作业的平均正确率，目前是每周一统计
 * </pre>
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年7月18日
 */
@Entity
@Table(name = "homework_rightrate_stat")
public class HomeworkRightRateStat implements Serializable {

	private static final long serialVersionUID = -5810199175351346860L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 班级ID
	 */
	@Column(name = "homework_class_id")
	private Long homeworkClassId;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 统计时间,存储精度到天,目前是存储触发统时间的年月日
	 */
	@Column(name = "statistics_time", columnDefinition = "date")
	private Date statisticsTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public Date getStatisticsTime() {
		return statisticsTime;
	}

	public void setStatisticsTime(Date statisticsTime) {
		this.statisticsTime = statisticsTime;
	}

}
