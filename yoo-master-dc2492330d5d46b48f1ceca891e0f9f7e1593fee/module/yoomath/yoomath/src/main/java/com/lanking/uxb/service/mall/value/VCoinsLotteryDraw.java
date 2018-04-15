package com.lanking.uxb.service.mall.value;

import java.io.Serializable;

/**
 * 抽奖信息
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
public class VCoinsLotteryDraw implements Serializable {
	private static final long serialVersionUID = 6910126397743893922L;

	private String userInfo;
	private String goodsInfo;

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(String goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
}
