package com.lanking.cloud.domain.yoo.activity.teachersDay01;

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

@Setter
@Getter
@Entity
@Table(name = "teachersday_activity_01_stu_tag_tea")
public class TeachersDayActiviy01StudentTagTeacher implements Serializable {

	private static final long serialVersionUID = 7241093358754723741L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 标签代码
	 */
	@Column(name = "code_tag")
	private long codeTag;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 教师ID
	 */
	@Column(name = "teacher_id")
	private long teacherId;

	@Column(name = "tag_at", columnDefinition = "datetime(3)")
	private Date tagAt;
}