package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.util.Date;

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
 * 科举抽奖
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity_lottery")
public class ImperialExaminationActivityLottery implements Serializable {

	private static final long serialVersionUID = -2274920240929232160L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "process", precision = 3)
	private ImperialExaminationProcess process;

	/**
	 * 关联奖项ID（null表示为抽奖，0表示未中奖）
	 */
	@Column(name = "prizes_id")
	private Long prizesId;

	/**
	 * 冗余奖项名称
	 */
	@Column(name = "name", length = 32)
	private String name;

	/**
	 * 状态 页面判断是否展示奖券用此字段
	 * ENABLED:已抽奖未刮奖
	 * DISABLED:已刮奖
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 抽奖时间
	 */
	@Column(name = "lottery_at", columnDefinition = "datetime(3)")
	private Date lotteryAt;
	
	/**
	 * 领取状态 0:未领取,1:已领取
	 */
	@Column(name = "received", precision = 3)
	private Integer received;
}
