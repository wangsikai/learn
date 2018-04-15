package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 科举活动获奖记录表(学生)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity_award_student")
public class ImperialExaminationActivityAwardStudent implements Serializable {

	private static final long serialVersionUID = 9085040208240706631L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "clazz_id")
	private long clazzId;

	@Column(name = "score", precision = 3)
	private Integer score;

	/**
	 * 平均用时(三次作业的平均用时)
	 */
	@Column(name = "do_time")
	private Integer doTime;

	/**
	 * 最终排名
	 */
	@Column(name = "rank")
	private Long rank;

	/**
	 * 奖品级别(具体信息从 {@link ImperialExaminationActivityCfg} 中获取)
	 */
	@Column(name = "award_level", precision = 3)
	private Integer awardLevel;

	/**
	 * 奖品联系人
	 */
	@Column(name = "award_contact", precision = 64)
	private String awardContact;

	/**
	 * 奖品联系电话
	 */
	@Column(name = "award_contact_number", precision = 20)
	private String awardContactNumber;

	/**
	 * 奖品送货地址
	 */
	@Column(name = "award_delivery_address", precision = 256)
	private String awardDeliveryAddress;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

}
