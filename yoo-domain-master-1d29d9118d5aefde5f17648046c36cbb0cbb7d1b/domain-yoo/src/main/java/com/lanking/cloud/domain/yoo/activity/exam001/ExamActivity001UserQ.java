package com.lanking.cloud.domain.yoo.activity.exam001;

import java.io.Serializable;
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
 * 用户Q点礼包
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年12月26日
 */
@Setter
@Getter
@Entity
@Table(name = "exam_activity_001_user_q")
public class ExamActivity001UserQ implements Serializable {

	private static final long serialVersionUID = -8225904545877471127L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "user_id")
	private long userId;

	/**
	 * Q点价值(谢谢惠顾设置为0)
	 */
	@Column(name = "value0", precision = 5)
	private Integer value0;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "date")
	private Date createAt;

	/**
	 * 是否查看过礼包 0:否,1:是
	 */
	@Column(name = "viewed", precision = 3)
	private Integer viewed;

	/**
	 * 查看礼包时间
	 */
	@Column(name = "view_at", columnDefinition = "datetime(3)")
	private Date viewAt;

}
