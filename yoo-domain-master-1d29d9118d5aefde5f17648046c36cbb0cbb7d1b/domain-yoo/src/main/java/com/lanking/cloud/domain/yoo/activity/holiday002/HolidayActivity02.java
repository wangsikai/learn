package com.lanking.cloud.domain.yoo.activity.holiday002;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.type.JSONType;

import lombok.Getter;
import lombok.Setter;

/**
 * 假期活动02
 * 
 * <pre>
 * 20180116寒假活动
 * </pre>
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 * @version 2018年1月16日
 */
@Getter
@Setter
@Entity
@Table(name = "holiday_activity_02")
public class HolidayActivity02 implements Serializable {

	private static final long serialVersionUID = 2575966222650233741L;

	/**
	 * 活动代码
	 */
	@Id
	@Column(name = "code")
	private Long code;

	/**
	 * 活动名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 活动介绍
	 */
	@Column(name = "introduction", length = 1024)
	private String introduction;

	/**
	 * 规则
	 */
	@Column(name = "rule", length = 1024)
	private String rule;

	/**
	 * 奖励
	 */
	@Column(name = "reward", length = 1024)
	private String reward;

	/**
	 * 活动创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 活动开始时间（包含）
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 活动结束时间（包含）
	 */
	@Column(name = "end_time", columnDefinition = "datetime(3)")
	private Date endTime;
	
	@Type(type = JSONType.TYPE)
	@Column(name = "cfg", length = 4000)
	private HolidayActivity02Cfg cfg;
}
