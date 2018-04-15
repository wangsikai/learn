package com.lanking.cloud.domain.yoomath.stat;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 学生作业统计(不区分班级)，student_homework_stat这个区分班级
 * 
 * @since 4.6.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月18日
 */
@Getter
@Setter
@Entity
@Table(name = "student_homework_statistic")
public class StudentHomeworkStatistic implements Serializable {

	private static final long serialVersionUID = -4388732910377863L;

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "homework_count")
	private long homeWorkCount;

	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	@Column(name = "completion_rate", scale = 2)
	private BigDecimal completionRate;

}
