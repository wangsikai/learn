package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.user.UserType;

import lombok.Getter;
import lombok.Setter;

/**
 * 科举抽奖奖品
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity_prizes")
public class ImperialExaminationActivityPrizes implements Serializable {

	private static final long serialVersionUID = -2274920240929232160L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "process", precision = 3)
	private ImperialExaminationProcess process;

	@Column(name = "name", length = 32)
	private String name;

	@Column(name = "num", precision = 5)
	private Integer num;

	/**
	 * 已经发放的
	 */
	@Column(name = "cost", precision = 5)
	private Integer cost;

	/**
	 * 考场，可以为null
	 */
	@Column(name = "room", precision = 3)
	private Integer room;

	@Column(name = "user_type", precision = 3)
	private UserType userType;
	
	/**
	 * 中奖概率
	 */
	@Column(name = "awards_rate", scale = 2)
	private BigDecimal awardsRate;
}
