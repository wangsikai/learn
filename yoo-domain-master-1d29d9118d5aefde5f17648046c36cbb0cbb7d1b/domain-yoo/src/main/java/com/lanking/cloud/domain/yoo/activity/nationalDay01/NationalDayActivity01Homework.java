package com.lanking.cloud.domain.yoo.activity.nationalDay01;

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

/**
 * 国庆活动参与的作业
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@Getter
@Setter
@Entity
@Table(name = "national_day_activity_01_homework")
public class NationalDayActivity01Homework implements Serializable {

	private static final long serialVersionUID = 3165475145053367639L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "teacher_id")
	private long teacherId;

	@Column(name = "homework_id")
	private long homeworkId;
}
