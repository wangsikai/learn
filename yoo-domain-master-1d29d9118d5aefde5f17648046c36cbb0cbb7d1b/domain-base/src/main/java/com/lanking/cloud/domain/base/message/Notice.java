package com.lanking.cloud.domain.base.message;

import java.io.Serializable;
import java.util.Date;

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
 * 提醒消息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "notice")
public class Notice implements Serializable {

	private static final long serialVersionUID = 3343795373163716568L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 接受者
	 */
	@Column(name = "uid")
	private long uid;

	/**
	 * 触发者
	 */
	@Column(name = "notice_id")
	private long noticeId;

	/**
	 * 类别
	 */
	@Column(name = "catgory", precision = 3)
	private int catgory;

	/**
	 * 类型
	 */
	@Column(name = "type", precision = 3)
	private int type;

	/**
	 * 消息体
	 */
	@Column(name = "body", length = 4000)
	private String body;

	/**
	 * 关联业务对象类型
	 * 
	 * @see Biz
	 */
	@Column(name = "biz", precision = 3)
	private int biz;

	/**
	 * 关联业务对象ID
	 */
	@Column(name = "biz_id")
	private long bizId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 查看时间
	 */
	@Column(name = "read_at", columnDefinition = "datetime(3)")
	private Date readAt;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(name = "status")
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(long noticeId) {
		this.noticeId = noticeId;
	}

	public int getCatgory() {
		return catgory;
	}

	public void setCatgory(int catgory) {
		this.catgory = catgory;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getReadAt() {
		return readAt;
	}

	public void setReadAt(Date readAt) {
		this.readAt = readAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
