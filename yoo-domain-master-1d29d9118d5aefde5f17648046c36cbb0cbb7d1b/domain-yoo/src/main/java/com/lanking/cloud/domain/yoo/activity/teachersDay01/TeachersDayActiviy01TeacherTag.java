package com.lanking.cloud.domain.yoo.activity.teachersDay01;

import java.io.Serializable;

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
@Table(name = "teachersday_activity_01_tea_tag")
public class TeachersDayActiviy01TeacherTag implements Serializable {

	private static final long serialVersionUID = -1324116638053168354L;

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
	 * 教师ID
	 */
	@Column(name = "teacher_id")
	private long teacherId;

	/**
	 * 次数
	 */
	@Column(name = "num")
	private int num;

}
