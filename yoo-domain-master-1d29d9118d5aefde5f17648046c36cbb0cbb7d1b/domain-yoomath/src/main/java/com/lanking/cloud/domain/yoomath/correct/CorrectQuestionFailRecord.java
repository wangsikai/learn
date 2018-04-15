package com.lanking.cloud.domain.yoomath.correct;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 小悠快批传题记录.
 * 
 * @author wanlong.che
 *
 */
@Getter
@Setter
@Entity
@Table(name = "correct_question_fail_record")
public class CorrectQuestionFailRecord {

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 习题ID.
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 学生ID.
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 习题类型.
	 */
	@Column(name = "question_type", precision = 3)
	private Question.Type type;

	/**
	 * 习题来源.
	 */
	@Column(name = "question_source", precision = 3)
	private int source;

	/**
	 * 习题分类.
	 */
	@Column(name = "question_category", precision = 3)
	private int category;

	/**
	 * 业务ID，由CorrectQuestionSource定义，服务通信定位获取业务数据时使用.
	 * <p>
	 * 作业题：此处为student_homework_question的ID<br>
	 * </p>
	 */
	@Column(name = "biz_id")
	private Long bizId;

	/**
	 * 创建时间.
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 失败次数.
	 */
	@Column(name = "fail_count")
	private Integer failCount = 1;

	/**
	 * 最终传输成功时间.
	 */
	@Column(name = "success_at", columnDefinition = "datetime(3)")
	private Date successAt;

	/**
	 * 数据状态（传输成功后设置为delete）.
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;
}
