package com.lanking.cloud.domain.base.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 私信消息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "msg")
public class Msg implements Serializable {

	private static final long serialVersionUID = -8316390135106472714L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 发送者
	 */
	@Column(name = "send_id", nullable = false)
	private long sendId;

	/**
	 * 消息体
	 */
	@Column(name = "body", length = 2048)
	private String body;

	/**
	 * 关联文件ID
	 */
	@Column(name = "file_id")
	private long fileId;

	/**
	 * 关联业务对象类型
	 * 
	 * @see Biz
	 */
	@Column(precision = 3, nullable = false)
	private int biz;

	/**
	 * 关联业务对象ID
	 */
	@Column(name = "biz_id", nullable = false)
	private long bizId;

	/**
	 * 关联资源URL
	 */
	@Column(length = 512)
	private String url;

	/**
	 * 发送时间
	 */
	@Column(name = "send_at", nullable = false)
	private long sendAt;

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

	public long getSendId() {
		return sendId;
	}

	public void setSendId(long sendId) {
		this.sendId = sendId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public int getBiz() {
		return biz;
	}

	public void setBiz(int biz) {
		this.biz = biz;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getSendAt() {
		return sendAt;
	}

	public void setSendAt(long sendAt) {
		this.sendAt = sendAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
