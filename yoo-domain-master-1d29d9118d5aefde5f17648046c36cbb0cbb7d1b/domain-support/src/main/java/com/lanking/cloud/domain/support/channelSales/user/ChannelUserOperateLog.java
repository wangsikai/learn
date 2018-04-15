package com.lanking.cloud.domain.support.channelSales.user;

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
 * 渠道商-用户操作log
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "channel_user_operate_log")
public class ChannelUserOperateLog implements Serializable {

	private static final long serialVersionUID = 3764721825116943765L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 被操作的用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 操作类型
	 * 
	 * @see ChannelUserOperateLogType
	 */
	@Column(name = "type", precision = 3, nullable = true)
	private ChannelUserOperateLogType operateType;

	/**
	 * 操作人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 操作人对应的渠道
	 */
	@Column(name = "channel_code", precision = 5)
	private Integer channelCode;

	/**
	 * 操作时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ChannelUserOperateLogType getOperateType() {
		return operateType;
	}

	public void setOperateType(ChannelUserOperateLogType operateType) {
		this.operateType = operateType;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
