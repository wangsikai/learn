package com.lanking.uxb.service.mall.api;

import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 金币商品查询条件
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
public class CoinsGoodsQuery {

	// 用户角色=null的时候表示所有
	private UserType userType;
	private int limit;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
