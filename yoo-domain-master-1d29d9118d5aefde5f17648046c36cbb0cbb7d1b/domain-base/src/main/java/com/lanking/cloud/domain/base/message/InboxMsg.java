package com.lanking.cloud.domain.base.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 私信收件箱
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "msg_inbox")
public class InboxMsg implements Serializable {

	private static final long serialVersionUID = 7907514696179706597L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 收件箱主体方
	 */
	@Column(name = "main_id", nullable = false)
	private long mainId;

	/**
	 * 收件箱关联方
	 */
	@Column(name = "other_id", nullable = false)
	private long otherId;

	/**
	 * 关联消息ID
	 */
	@Column(name = "msg_id", nullable = false)
	private long msgId;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getMainId() {
		return mainId;
	}

	public void setMainId(long mainId) {
		this.mainId = mainId;
	}

	public long getOtherId() {
		return otherId;
	}

	public void setOtherId(long otherId) {
		this.otherId = otherId;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
