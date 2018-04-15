package com.lanking.uxb.service.user.value;

import java.io.Serializable;

/**
 * 用户中心数据,子类有(VTeacherCenter,VStudentCenter,VParentCenter)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月23日
 *
 */
public class VUserCenter implements Serializable {

	private static final long serialVersionUID = 6742778644989647958L;

	private VUser user;
	private boolean isMe;
	private boolean isFollow;
	/**
	 * 当前用户id
	 * 
	 * @since 2.1
	 */
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public boolean isMe() {
		return isMe;
	}

	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}

	public boolean isFollow() {
		if (isMe()) {
			return false;
		}
		return isFollow;
	}

	public void setFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

}
