package com.lanking.cloud.domain.yoo.activity.holiday002;

import java.io.Serializable;

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
@Table(name = "holiday_activity_02_power_rank")
public class HolidayActivity02PowerRank implements Serializable {

	private static final long serialVersionUID = -4409010846888691938L;

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
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 战力值（调整后的）
	 */
	@Column(name = "power")
	private Integer power;

	/**
	 * 真实的战力值
	 */
	@Column(name = "real_power")
	private Integer realPower;

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
