package com.lanking.cloud.domain.yoo.activity.nationalDay01;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 国庆活动学生参与情况
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@Getter
@Setter
@Entity
@Table(name = "national_day_activity_01_stu")
public class NationalDayActivity01Stu implements Serializable {

	private static final long serialVersionUID = 66061279774573039L;

	@Id
	@Column(name = "user_id")
	private long userId;

	/**
	 * 答对题的数量
	 */
	@Column(name = "right_count")
	private Long rightCount;
}
