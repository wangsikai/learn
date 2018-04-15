package com.lanking.cloud.domain.yoomath.stat;

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

@Getter
@Setter
@Entity
@Table(name = "do_question_school_rank")
public class DoQuestionSchoolRank implements Serializable {

	private static final long serialVersionUID = -232605085314546302L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	@Column(name = "school_id")
	private long schoolId;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "phase_code")
	private int phaseCode;

	@Column(name = "start_date")
	private Integer startDate;

	@Column(name = "end_date")
	private Integer endDate;

	@Column(name = "right_count")
	private Long rightCount;

	@Column(name = "right_count_0")
	private Long rightCount0;

	@Column(name = "rank")
	private Integer rank;

	@Column(name = "rank_0")
	private Integer rank0;

	@Column(name = "praise_count")
	private Long praiseCount;

}
