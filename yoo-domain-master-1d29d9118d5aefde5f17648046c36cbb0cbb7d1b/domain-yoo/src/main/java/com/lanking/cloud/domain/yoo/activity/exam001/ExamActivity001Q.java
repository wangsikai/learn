package com.lanking.cloud.domain.yoo.activity.exam001;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 * q点
 * 
 * <pre>
 * 提前初始化好
 * </pre>
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年12月26日
 */
@Setter
@Getter
@Entity
@Table(name = "exam_activity_001_q")
public class ExamActivity001Q implements Serializable {

	private static final long serialVersionUID = -8142588183379071665L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "name", length = 32)
	private String name;

	/**
	 * Q点价值
	 */
	@Column(name = "value0", precision = 5)
	private Integer value0;

	@Column(name = "num", precision = 5)
	private Integer num;

	/**
	 * 已经发放的
	 */
	@Column(name = "cost", precision = 5)
	private Integer cost;

	@Column(name = "day0", columnDefinition = "date")
	private Date day0;
	
	/**
	 * 中奖概率
	 */
	@Column(name = "awards_rate", scale = 2)
	private BigDecimal awardsRate;

}
