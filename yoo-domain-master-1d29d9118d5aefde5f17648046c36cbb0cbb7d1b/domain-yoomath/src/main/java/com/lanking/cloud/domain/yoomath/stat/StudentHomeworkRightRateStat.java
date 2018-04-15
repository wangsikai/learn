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

import lombok.Getter;
import lombok.Setter;

/**
 * 学生作业正确率统计
 * 
 * <pre>
 * 每天00:00以后统计截止前一天（包括前一天）所有作业平均正确率，PS:每个学生只保留90day的记录
 * </pre>
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年7月18日
 */
@Setter
@Getter
@Entity
@Table(name = "student_homework_rightrate_stat")
public class StudentHomeworkRightRateStat implements Serializable {

	private static final long serialVersionUID = 1103367465943981247L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 设置为统计的截止时间,包括这一天
	 */
	@Column(name = "statistics_time", columnDefinition = "date")
	private Date statisticsTime;

}
