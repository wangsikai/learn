package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 科举活动
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity")
public class ImperialExaminationActivity implements Serializable {

	private static final long serialVersionUID = -1492078943314176034L;

	@Id
	@Column(name = "code")
	private Long code;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "introduction", length = 1024)
	private String introduction;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 活动开始时间（包含）
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 活动结束时间（包含）
	 */
	@Column(name = "end_time", columnDefinition = "datetime(3)")
	private Date endTime;

	/**
	 * 活动配置(包括配置) {@link ImperialExaminationActivityCfg}
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "cfg", length = 60000)
	private ImperialExaminationActivityCfg cfg;

	/**
	 * 期
	 * 
	 * <pre>
	 * 1.第一期
	 * 2.第二期
	 * </pre>
	 */
	@Column(name = "period", precision = 3)
	private Integer period;

	/**
	 * 活动状态（注意查询时该活动状态与活动起止时间要联合判断）
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

}
