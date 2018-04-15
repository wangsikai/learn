package com.lanking.cloud.domain.yoo.activity.exam001;

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
@Table(name = "exam_activity_001_answer")
public class ExamActivity001Answer implements Serializable {

	private static final long serialVersionUID = 6842624235716125181L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "user_id")
	private long userId;
	/**
	 * 对应试卷代码
	 */
	@Column(name = "exam_question_code")
	private Long examQuestioncode;
	
	@Column(name = "activity_code")
	private long activityCode;

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
