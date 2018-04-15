package com.lanking.uxb.service.activity.form;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;

public class HolidayActivity01OrderForm {

	private Long id;

	private String name;

	private CoinsLotteryGoodsLevel level;

	private Long userId;

	private String userName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CoinsLotteryGoodsLevel getLevel() {
		return level;
	}

	public void setLevel(CoinsLotteryGoodsLevel level) {
		this.level = level;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
