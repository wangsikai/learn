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
@Table(name = "do_question_school_rank_praise")
public class DoQuestionSchoolRankPraise implements Serializable {

	private static final long serialVersionUID = 6460909775905198407L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	@Column(name = "rank_id")
	private long rankId;

	@Column(name = "user_id")
	private long userId;

}
