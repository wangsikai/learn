package com.lanking.cloud.domain.frame.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * api监控统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "api_monitor")
public class ApiMonitor implements Serializable {

	private static final long serialVersionUID = 4149619942494226309L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * api
	 */
	@Column(name = "api", length = 512)
	private String api;

	/**
	 * 调用次数
	 */
	@Column(name = "request_time")
	private long requestTime;

	/**
	 * 失败次数
	 */
	@Column(name = "fail_time")
	private long failTime;

	/**
	 * 调用花费总时间
	 */
	@Column(name = "cost_time")
	private long costTime;

	/**
	 * 平均耗时
	 */
	@Column(name = "avg_cost_time")
	private Integer avgCostTime;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public long getFailTime() {
		return failTime;
	}

	public void setFailTime(long failTime) {
		this.failTime = failTime;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public Integer getAvgCostTime() {
		return avgCostTime;
	}

	public void setAvgCostTime(Integer avgCostTime) {
		this.avgCostTime = avgCostTime;
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
