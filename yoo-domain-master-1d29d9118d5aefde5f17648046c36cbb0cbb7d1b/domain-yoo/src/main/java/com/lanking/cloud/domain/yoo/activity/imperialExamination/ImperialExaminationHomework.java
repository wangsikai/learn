package com.lanking.cloud.domain.yoo.activity.imperialExamination;

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
 * 科举活动考试作业
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_homework")
public class ImperialExaminationHomework implements Serializable {

	private static final long serialVersionUID = 7255884814772720588L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private Long activityCode;

	@Column(name = "homework_id")
	private long homeworkId;

	@Column(name = "type", precision = 3)
	private ImperialExaminationType type;

	@Column(name = "grade", precision = 3)
	private ImperialExaminationGrade grade;

	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 标签1,2,3(2,3用于存储冲刺题目)
	 */
	@Column(name = "tag", precision = 3)
	private Integer tag;

	/**
	 * 考场
	 */
	@Column(name = "room", precision = 3)
	private Integer room;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "clazz_id")
	private long clazzId;

}
