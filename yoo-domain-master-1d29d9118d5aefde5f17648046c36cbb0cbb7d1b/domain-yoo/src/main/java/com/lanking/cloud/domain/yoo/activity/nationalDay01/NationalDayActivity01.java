package com.lanking.cloud.domain.yoo.activity.nationalDay01;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 国庆活动(不考虑后面国庆活动的复用，所以相关表没有保存对应活动的代码)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@Getter
@Setter
@Entity
@Table(name = "national_day_activity_01")
public class NationalDayActivity01 implements Serializable {

	private static final long serialVersionUID = 1625527470695057077L;

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

}
