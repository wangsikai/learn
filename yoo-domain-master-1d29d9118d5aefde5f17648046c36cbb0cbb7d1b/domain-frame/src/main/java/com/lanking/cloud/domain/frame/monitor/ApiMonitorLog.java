package com.lanking.cloud.domain.frame.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * api访问明细
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "api_monitor_log")
public class ApiMonitorLog implements Serializable {

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
	 * 参数
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "params")
	private String params;

	/**
	 * 花费时间
	 */
	@Column(name = "cost_time", precision = 10)
	private int costTime;

	/**
	 * 异常
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "ex")
	private String ex;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 访问token
	 */
	@Column(length = 32, name = "token")
	private String token;

	/**
	 * 访问域名
	 */
	@Column(length = 64, name = "host_name")
	private String hostName;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

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

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public int getCostTime() {
		return costTime;
	}

	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}

	public String getEx() {
		return ex;
	}

	public void setEx(String ex) {
		this.ex = ex;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
