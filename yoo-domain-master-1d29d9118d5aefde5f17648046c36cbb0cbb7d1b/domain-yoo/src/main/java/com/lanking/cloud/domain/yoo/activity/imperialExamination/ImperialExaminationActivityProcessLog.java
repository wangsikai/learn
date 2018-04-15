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

import lombok.Getter;
import lombok.Setter;

/**
 * 阶段处理记录
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity_process_log")
public class ImperialExaminationActivityProcessLog implements Serializable {

	private static final long serialVersionUID = 4833649196848290412L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 活动代码
	 */
	@Id
	@Column(name = "activity_code", length = 32)
	private Long activityCode;

	/**
	 * 活动阶段
	 */
	@Column(name = "process", precision = 3)
	private ImperialExaminationProcess process;

	/**
	 * 开始处理时间
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 处理结束时间
	 */
	@Column(name = "end_time", columnDefinition = "datetime(3)")
	private Date endTime;

	/**
	 * 记录处理过程性数据
	 */
	@Column(name = "process_data", length = 4000)
	private String processData;

	/**
	 * 处理是否成功
	 * 
	 * <pre>
	 * true|1:成功
	 * false|0:失败
	 * null:未知
	 * </pre>
	 */
	@Column(name = "success")
	private Boolean success;

}
