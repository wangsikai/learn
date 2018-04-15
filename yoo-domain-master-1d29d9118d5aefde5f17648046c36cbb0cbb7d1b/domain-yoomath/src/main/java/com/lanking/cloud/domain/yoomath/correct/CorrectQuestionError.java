package com.lanking.cloud.domain.yoomath.correct;

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
 * 批改员错题反馈.
 * 
 * @author wanlong.che
 *
 */
@Getter
@Setter
@Entity
@Table(name = "correct_question_error")
public class CorrectQuestionError implements Serializable {
	private static final long serialVersionUID = -7683047342340675389L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生习题题目ID
	 */
	@Column(name = "student_question_id")
	private long studentQuestionId;

	/**
	 * 习题ID.
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 关联的QuestionError表的ID.
	 */
	@Column(name = "question_error_id")
	private Long questionErrorId;

	/**
	 * 上报错误用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 提交时间.
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;
}
