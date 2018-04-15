package com.lanking.cloud.domain.yoo.activity.exam001;

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
@Table(name = "exam_activity_001_user")
public class ExamActivity001User implements Serializable {

	private static final long serialVersionUID = 3899988194471426630L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "user_id")
	private long userId;

	/**
	 * 超越自我中选择的版本
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 超越自我中选择的年级
	 */
	@Column(name = "grade")
	private Integer grade;

	@Column(name = "qq", length = 20)
	private String qq;

	/**
	 * Q点价值(已经查看的Q包的总价值)
	 */
	@Column(name = "value0", precision = 5)
	private Integer value0;

	/**
	 * q包的数量
	 */
	@Column(name = "q_num", precision = 5)
	private Integer q_num;

	/**
	 * 已经查看的q包数量
	 */
	@Column(name = "view_q_num", precision = 5)
	private Integer viewQNum;

	/**
	 * 领取状态 0:未领取,1:已领取
	 */
	@Column(name = "received", precision = 3)
	private Integer received;

}
