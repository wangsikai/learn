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
 * 学生班级周报告
 * 
 * @since 4.6.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月7日
 */
@Getter
@Setter
@Entity
@Table(name = "student_class_week_report")
public class StudentClassWeekReport implements Serializable {

	private static final long serialVersionUID = 662516118933977947L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 班级ID
	 */
	@Column(name = "clazz_id")
	private long clazzId;

	/**
	 * 开始时间
	 */
	@Column(name = "start_date", columnDefinition = "date")
	private Date startDate;

	/**
	 * 结束时间
	 */
	@Column(name = "end_date", columnDefinition = "date")
	private Date endDate;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 正确率班级排名
	 */
	@Column(name = "right_rate_rank", precision = 5)
	private Integer rightRateRank;

	/**
	 * 正确率班级排名浮动（与正确率浮动区分）
	 */
	@Column(name = "right_rate_rank_float", precision = 5)
	private Integer rightRateRankFloat;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

}
