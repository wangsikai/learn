package com.lanking.uxb.service.ranking.value;

import com.lanking.uxb.service.user.value.VUser;

/**
 * 班级内学生做题排名
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月24日
 */
public class VClassDoQuestionRanking extends VDoQuestionRanking {

	private static final long serialVersionUID = 7510414889871295787L;

	private VUser user;
	private Integer rank;

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
}
