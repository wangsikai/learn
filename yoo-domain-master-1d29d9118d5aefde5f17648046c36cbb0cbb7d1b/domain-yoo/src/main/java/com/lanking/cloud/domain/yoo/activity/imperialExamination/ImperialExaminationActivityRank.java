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
 * 科举活动排名表（每个阶段都有排名）
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity_rank")
public class ImperialExaminationActivityRank implements Serializable {

	private static final long serialVersionUID = -5406919684121849079L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "type", precision = 3)
	private ImperialExaminationType type;

	@Column(name = "activity_homework_id")
	private long activityHomeworkId;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "do_time")
	private Integer doTime;

	@Column(name = "score", precision = 3)
	private Integer score;

	/**
	 * 综合表现分(人工干预),生成数据的时候需要设置和score一样
	 */
	@Column(name = "manual_score", precision = 3)
	private Integer manualScore;

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
}
