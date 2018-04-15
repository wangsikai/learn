package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

import lombok.Getter;
import lombok.Setter;

/**
 * 习题批改日志（仅记录填空及简答题）.
 * 
 * @since 小优快批，2018-2-24
 * @author wanlong.che
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "question_correct_log")
public class QuestionCorrectLog implements Serializable {
	private static final long serialVersionUID = -4897295507750712816L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生作业题目ID
	 */
	@Column(name = "student_homework_question_id")
	private long studentHomeworkQuestionId;

	/**
	 * 创建时间即批改时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 批改结果
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;

	/**
	 * 批改员（为空时表示自动批改）
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 批改方式.
	 */
	@Column(name = "correct_type", precision = 3)
	private QuestionCorrectType correctType = QuestionCorrectType.DEFAULT;

	/**
	 * 自动批改的方式方法（批改方式为自动批改时可用，顺序为对应答案顺序）.
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "auto_methods", length = 500)
	private List<QuestionAutoCorrectMethod> autoCorrectMethods;

	/**
	 * 习题作业的来源.
	 */
	@Column(name = "question_source", columnDefinition = "tinyint default 0")
	private HomeworkType homeworkType = HomeworkType.HOMEWORK;
}
