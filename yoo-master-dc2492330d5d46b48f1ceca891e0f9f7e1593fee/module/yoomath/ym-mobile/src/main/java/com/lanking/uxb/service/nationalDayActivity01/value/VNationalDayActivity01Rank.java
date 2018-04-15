package com.lanking.uxb.service.nationalDayActivity01.value;

import java.io.Serializable;

import com.lanking.uxb.service.user.value.VUser;

/**
 * 国庆活动排行榜
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月25日
 */
public class VNationalDayActivity01Rank implements Serializable {

	private static final long serialVersionUID = -4589361631512684289L;

	private VUser user;
	private int rank;
	private int homeworkCount;
	private int score;
	private long rightCount;

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getHomeworkCount() {
		return homeworkCount;
	}

	public void setHomeworkCount(int homeworkCount) {
		this.homeworkCount = homeworkCount;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getRightCount() {
		return rightCount;
	}

	public void setRightCount(long rightCount) {
		this.rightCount = rightCount;
	}
}
