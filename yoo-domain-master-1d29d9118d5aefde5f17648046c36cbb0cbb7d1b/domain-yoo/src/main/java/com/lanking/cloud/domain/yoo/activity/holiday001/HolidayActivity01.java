package com.lanking.cloud.domain.yoo.activity.holiday001;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 假期活动01(后面要是有活动和此活动相似度>90可以考虑复用，其他一律不建议复用)
 * 
 * <pre>
 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日)
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
@Entity
@Table(name = "holiday_activity_01")
public class HolidayActivity01 implements Serializable {

	private static final long serialVersionUID = -3729798361688553567L;

	/**
	 * 活动代码
	 */
	@Id
	@Column(name = "code")
	private Long code;

	/**
	 * 活动名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 活动介绍
	 */
	@Column(name = "introduction", length = 1024)
	private String introduction;

	/**
	 * 活动创建时间
	 */
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
	 * 活动配置 {@link HolidayActivity01Cfg}
	 */
	@Column(name = "cfg", length = 20000)
	private String config;

	// @Type(type = JSONType.TYPE)
	// @Column(name = "cfg", length = 60000)
	@Transient
	private HolidayActivity01Cfg cfg;

	/**
	 * 活动状态（注意查询时该活动状态与活动起止时间要联合判断）
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getConfig() {
		return config;
	}

	private void setConfig(String config) {
		this.config = config;
	}

	public HolidayActivity01Cfg getCfg() {
		if (cfg == null) {
			cfg = JSON.parseObject(config, HolidayActivity01Cfg.class);
		}
		return cfg;
	}

	public void setCfg(HolidayActivity01Cfg cfg) {
		setConfig(JSON.toJSON(cfg).toString());
		this.cfg = cfg;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
