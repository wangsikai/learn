package com.lanking.cloud.domain.yoo.activity.nationalDay01;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 国庆活动教师参与情况
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@Getter
@Setter
@Entity
@Table(name = "national_day_activity_01_tea")
public class NationalDayActivity01Tea implements Serializable {

	private static final long serialVersionUID = 450027625483358849L;

	@Id
	@Column(name = "user_id")
	private long userId;

	/**
	 * 作业数量
	 */
	@Column(name = "homework_count", precision = 5)
	private Integer homeworkCount;

	/**
	 * 提交率
	 */
	@Column(name = "commit_rate", precision = 3)
	private Integer commitRate;

	/**
	 * 综合得分
	 */
	@Column(name = "score", precision = 5)
	private Integer score;
}
