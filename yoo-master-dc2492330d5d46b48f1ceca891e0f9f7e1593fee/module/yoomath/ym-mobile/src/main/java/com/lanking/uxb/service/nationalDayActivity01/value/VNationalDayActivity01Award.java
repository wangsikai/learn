package com.lanking.uxb.service.nationalDayActivity01.value;

import java.io.Serializable;

import com.lanking.uxb.service.user.value.VUser;

/**
 * 国庆活动获奖榜
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月26日
 */
public class VNationalDayActivity01Award implements Serializable {

	private static final long serialVersionUID = 6910483793271901053L;

	private int award;
	private VUser user;

	public int getAward() {
		return award;
	}

	public void setAward(int award) {
		this.award = award;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}
}
