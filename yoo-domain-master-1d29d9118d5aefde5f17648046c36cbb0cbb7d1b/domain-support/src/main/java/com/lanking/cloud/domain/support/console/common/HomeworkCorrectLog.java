package com.lanking.cloud.domain.support.console.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 作业批改日志
 * 
 * @since 3.9.3
 * @since 小优快批，2018-2-24 不再使用该表
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Deprecated
@Entity
@Table(name = "homework_correct_log")
public class HomeworkCorrectLog implements Serializable {

	private static final long serialVersionUID = 8734225337532722854L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 批改员ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 学生作业题目ID
	 */
	@Column(name = "student_homeworkquestion_id")
	private long studentHomeworkQuestionId;

	/**
	 * 创建时间即批改时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 批改结果
	 */
	@Column(name = "result", columnDefinition = "tinyint default 3")
	private HomeworkAnswerResult result;

	/**
	 * 作业类型
	 */
	@Column(name = "type", columnDefinition = "tinyint default 0")
	private HomeworkCorrectLogType type = HomeworkCorrectLogType.HOMEWORK;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getStudentHomeworkQuestionId() {
		return studentHomeworkQuestionId;
	}

	public void setStudentHomeworkQuestionId(long studentHomeworkQuestionId) {
		this.studentHomeworkQuestionId = studentHomeworkQuestionId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public HomeworkCorrectLogType getType() {
		return type;
	}

	public void setType(HomeworkCorrectLogType type) {
		this.type = type;
	}
}
