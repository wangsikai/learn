package com.lanking.cloud.domain.yoo.activity.holiday002;

import java.io.Serializable;
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

@Getter
@Setter
@Entity
@Table(name = "holiday_activity_02_week_power_rank")
public class HolidayActivity02WeekPowerRank implements Serializable {

	private static final long serialVersionUID = 2475848949982776170L;

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
	 * 周开始时间(包含)
	 */
	@Column(name = "start_time", columnDefinition = "date")
	private Date startTime;

	/**
	 * 周结束时间(包含)
	 */
	@Column(name = "end_time", columnDefinition = "date")
	private Date endTime;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 本周得到的战力值
	 */
	@Column(name = "week_power")
	private Integer weekPower;

	/**
	 * 真实的战力值
	 */
	@Column(name = "real_week_power")
	private Integer realWeekPower;

	/**
	 * 排名
	 */
	@Column(name = "rank", precision = 5)
	private Integer rank;

	/**
	 * 排名(中间值，计算过程中使用，排名成功后更新到rank字段中)
	 */
	@Column(name = "rank0", precision = 5)
	private Integer rank0;

}
