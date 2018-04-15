package com.lanking.cloud.domain.base.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 消息时间轴
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@IdClass(MsgTimelineKey.class)
@Table(name = "msg_timeline")
public class MsgTimeline extends MsgTimelineKey {
	private static final long serialVersionUID = 6345541363087921306L;

	/**
	 * 未读消息数量
	 */
	@Column(name = "count0", precision = 5, nullable = false)
	private int count;

	/**
	 * 已读消息对应收件箱记录的ID
	 */
	@Column(name = "inbox_msg_id", nullable = false)
	private long inboxMsgId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", nullable = false)
	private long updateAt;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	public MsgTimeline(long receiverId, long senderId) {
		super(receiverId, senderId);
	}

	public MsgTimeline() {
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getInboxMsgId() {
		return inboxMsgId;
	}

	public void setInboxMsgId(long inboxMsgId) {
		this.inboxMsgId = inboxMsgId;
	}

	public long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
