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
@Table(name = "holiday_activity_02_medal")
public class HolidayActivity02Medal implements Serializable {

	private static final long serialVersionUID = 781925822718433103L;

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
	 * 勋章级别（一共五级）
	 */
	@Column(name = "level", precision = 3)
	private int level;

	/**
	 * 奖励战力值
	 */
	@Column(name = "power")
	private Integer power;
	
	/**
	 * 是否达到勋章领取条件 0:未达到1:已达到
	 */
	@Column(name = "achieved", precision = 3)
	private int achieved;

	/**
	 * 是否领取过0:未领取1:已领取
	 */
	@Column(name = "received", precision = 3)
	private int received;

}
