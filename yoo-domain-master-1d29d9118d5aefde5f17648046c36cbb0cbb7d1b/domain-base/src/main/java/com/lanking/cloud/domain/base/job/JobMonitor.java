package com.lanking.cloud.domain.base.job;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 分布式任务监控记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "job_monitor")
public class JobMonitor implements Serializable {

	private static final long serialVersionUID = 5976801445261272714L;

	@Id
	private Long id;

	/**
	 * 任务组
	 */
	@Column(name = "job_group", precision = 128)
	private String jobGroup;

	/**
	 * 任务名称
	 */
	@Column(name = "job_name", precision = 128)
	private String jobName;

	/**
	 * 任务描述
	 */
	@Column(name = "job_description", precision = 128)
	private String jobDescription;

	/**
	 * 触发器组
	 */
	@Column(name = "trigger_group", precision = 128)
	private String triggerGroup;

	/**
	 * 触发器名称
	 */
	@Column(name = "trigger_name", precision = 128)
	private String triggerName;

	/**
	 * 触发器描述
	 */
	@Column(name = "trigger_description", precision = 128)
	private String triggerDescription;

	/**
	 * 触发器corn表达式
	 */
	@Column(name = "trigger_cronexpression", precision = 64)
	private String triggerCronExpression;

	/**
	 * 任务执行开始时间
	 */
	@Column(name = "start_at", columnDefinition = "datetime(3)")
	private Date startAt;

	/**
	 * 任务执行结束时间
	 */
	@Column(name = "end_at", columnDefinition = "datetime(3)")
	private Date endAt;

	/**
	 * 任务执行结果状态
	 * 
	 * @see JobExeState
	 */
	@Column(name = "state", precision = 3)
	private JobExeState state;

	/**
	 * 记录创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 记录更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public enum JobExeState {
		/**
		 * 开始执行
		 */
		START,
		/**
		 * 执行成功
		 */
		END_SUCCESS,
		/**
		 * 执行失败
		 */
		END_FAIL;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerDescription() {
		return triggerDescription;
	}

	public void setTriggerDescription(String triggerDescription) {
		this.triggerDescription = triggerDescription;
	}

	public String getTriggerCronExpression() {
		return triggerCronExpression;
	}

	public void setTriggerCronExpression(String triggerCronExpression) {
		this.triggerCronExpression = triggerCronExpression;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public JobExeState getState() {
		return state;
	}

	public void setState(JobExeState state) {
		this.state = state;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
