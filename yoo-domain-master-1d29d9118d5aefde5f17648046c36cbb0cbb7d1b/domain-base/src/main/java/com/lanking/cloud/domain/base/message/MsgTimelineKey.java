package com.lanking.cloud.domain.base.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 消息时间轴主键
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@MappedSuperclass
public class MsgTimelineKey implements Serializable {
	private static final long serialVersionUID = 1477052148187927469L;

	/**
	 * 消息主体方
	 */
	@Id
	@Column(name = "main_id", nullable = false)
	private long mainId;

	/**
	 * 消息关联方
	 */
	@Id
	@Column(name = "other_id", nullable = false)
	private long otherId;

	public MsgTimelineKey(long mainId, long otherId) {
		this.mainId = mainId;
		this.otherId = otherId;
	}

	public MsgTimelineKey() {
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

	@Override
	public int hashCode() {
		return (int) (mainId * 37 + otherId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MsgTimelineKey) {
			MsgTimelineKey other = (MsgTimelineKey) obj;
			return mainId == other.mainId && otherId == other.otherId;
		}
		return false;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[] { mainId, otherId }, '-');
	}
}
