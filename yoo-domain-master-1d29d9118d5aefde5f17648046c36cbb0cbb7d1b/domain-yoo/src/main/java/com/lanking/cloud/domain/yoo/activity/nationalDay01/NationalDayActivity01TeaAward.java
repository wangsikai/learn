package com.lanking.cloud.domain.yoo.activity.nationalDay01;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 国庆活动老师奖项
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@Getter
@Setter
@Entity
@Table(name = "national_day_activity_01_tea_award")
public class NationalDayActivity01TeaAward implements Serializable {

	private static final long serialVersionUID = 5265253304747118357L;

	@Id
	@Column(name = "user_id")
	private long userId;

	/**
	 * 奖项（几等奖）
	 */
	@Column(name = "award", precision = 3)
	private Integer award;

	@Column(name = "rank", precision = 3)
	private Integer rank;
}
