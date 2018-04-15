package com.lanking.cloud.domain.yoo.activity.holiday002;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "holiday_activity_02_answer")
public class HolidayActivity02Answer implements Serializable {

	private static final long serialVersionUID = -429942629010519938L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * {@link HolidayActivity02Question.id}
	 */
	@Column(name = "questions_id")
	private Long questionsId;

	/**
	 * 答案列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "answer_list")
	private List<String> answerList = Lists.newArrayList();

	/**
	 * 对错列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "result_list")
	private List<HomeworkAnswerResult> resultList = Lists.newArrayList();

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 练习时间
	 */
	@Column(name = "homework_time")
	private Integer homeworkTime;
}
