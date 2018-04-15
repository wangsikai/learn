package com.lanking.cloud.domain.yoo.activity.nationalDay01;

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
 * 国庆活动页面用户访问详细记录
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@Getter
@Setter
@Entity
@Table(name = "national_day_activity_01_h5uv")
public class NationalDayActivity01H5UV implements Serializable {

	private static final long serialVersionUID = -2175024153518147743L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "h5", precision = 3)
	private NationalDayActivity01H5 h5;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "view_count")
	private long viewCount;

	@Column(name = "first_view_at", columnDefinition = "datetime(3)")
	private Date firstViewAt;

	@Column(name = "latest_view_at", columnDefinition = "datetime(3)")
	private Date latestViewAt;

	/**
	 * 年月日拼接的整数，记录全部时此字段为0，如：20170922
	 */
	@Column(name = "view_at")
	private long viewAt;
}
