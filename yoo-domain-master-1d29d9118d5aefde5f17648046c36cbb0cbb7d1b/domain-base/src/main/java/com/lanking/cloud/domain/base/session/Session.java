package com.lanking.cloud.domain.base.session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.lanking.cloud.sdk.bean.Userable;

/**
 * 会话
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "session")
public class Session extends AbstractSession {

	private static final long serialVersionUID = -8425569052327797189L;

	/**
	 * 开始活跃的点
	 */
	@Column(name = "start_active_at", columnDefinition = "bigint default 0")
	private long startActiveAt;

	@Transient
	private Userable user;
	@Transient
	private Device device;

	public long getStartActiveAt() {
		return startActiveAt;
	}

	public void setStartActiveAt(long startActiveAt) {
		this.startActiveAt = startActiveAt;
	}

	public Userable getUser() {
		return user;
	}

	public void setUser(Userable user) {
		this.user = user;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
