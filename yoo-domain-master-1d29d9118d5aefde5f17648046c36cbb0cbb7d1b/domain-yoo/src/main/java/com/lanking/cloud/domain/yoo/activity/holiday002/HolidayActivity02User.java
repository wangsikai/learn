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
@Table(name = "holiday_activity_02_user")
public class HolidayActivity02User implements Serializable {

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
	 * 参赛编号
	 */
	@Column(name = "activity_user_code")
	private Long activityUserCode;

	/**
	 * 战力值
	 */
	@Column(name = "power")
	private Integer power;

	/**
	 * PK平局次数
	 */
	@Column(name = "draw", precision = 5)
	private Integer draw;

	/**
	 * PK赢局次数
	 */
	@Column(name = "win", precision = 5)
	private Integer win;

	/**
	 * PK败局次数
	 */
	@Column(name = "lose", precision = 5)
	private Integer lose;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

}
