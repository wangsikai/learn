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
@Table(name = "holiday_activity_02_power_record")
public class HolidayActivity02PowerRecord implements Serializable {

	private static final long serialVersionUID = -8847873885388725034L;

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
	 * 排名记录ID
	 */
	@Column(name = "rank_id")
	private long rankId;

	/**
	 * 0:周1：全部
	 */
	@Column(name = "type", precision = 3)
	private int type;

	/**
	 * 后台操作的用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 调整的战力
	 */
	@Column(name = "power")
	private Integer power;

}
