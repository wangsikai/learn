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

/**
 * PK记录
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 * @version 2018年1月16日
 */
@Getter
@Setter
@Entity
@Table(name = "holiday_activity_02_pkrecord")
public class HolidayActivity02PKRecord implements Serializable {

	private static final long serialVersionUID = -594405178880661749L;

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
	 * {@link HolidayActivity02PKRecord.id}
	 */
	@Column(name = "pk_record_id")
	private Long pkRecordId;

	/**
	 * 当前用户
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * PK用户
	 */
	@Column(name = "pk_user_id")
	private long pkUserId;

	/**
	 * PK时间
	 */
	@Column(name = "pk_at", columnDefinition = "datetime(3)")
	private Date pkAt;

	/**
	 * 战力值
	 */
	@Column(name = "power", precision = 3)
	private Integer power;
	
	/**
	 * 是否是真人对战 0:非真人 1:真人
	 */
	@Column(name = "real_man", precision = 3)
	private Integer realMan;

}
