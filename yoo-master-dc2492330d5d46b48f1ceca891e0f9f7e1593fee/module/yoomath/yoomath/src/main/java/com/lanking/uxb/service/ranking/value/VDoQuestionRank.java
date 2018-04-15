package com.lanking.uxb.service.ranking.value;

import java.io.Serializable;

import com.lanking.uxb.service.user.value.VUser;

/**
 * 新版班级内学生做题排名
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月11日
 */
public class VDoQuestionRank implements Serializable {

	private static final long serialVersionUID = -5498652197305765080L;

	private long id;
	private VUser user;
	private Integer rank;
	private long praiseCount;
	private long rightCount;
	// 是否被当前用户点赞过
	private boolean hasPraised = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public long getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(long praiseCount) {
		this.praiseCount = praiseCount;
	}

	public long getRightCount() {
		return rightCount;
	}

	public void setRightCount(long rightCount) {
		this.rightCount = rightCount;
	}

	public boolean isHasPraised() {
		return hasPraised;
	}

	public void setHasPraised(boolean hasPraised) {
		this.hasPraised = hasPraised;
	}
}
