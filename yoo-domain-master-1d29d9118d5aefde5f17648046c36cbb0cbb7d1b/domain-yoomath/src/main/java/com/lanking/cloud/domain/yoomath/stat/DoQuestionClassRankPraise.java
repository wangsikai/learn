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
@Table(name = "do_question_class_rank_praise")
public class DoQuestionClassRankPraise implements Serializable {

	private static final long serialVersionUID = -3157892113433360535L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	@Column(name = "rank_id")
	private long rankId;

	@Column(name = "user_id")
	private long userId;

}
