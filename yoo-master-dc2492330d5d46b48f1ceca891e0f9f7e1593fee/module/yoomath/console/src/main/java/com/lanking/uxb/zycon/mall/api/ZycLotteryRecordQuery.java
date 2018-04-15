package com.lanking.uxb.zycon.mall.api;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 抽奖记录查询条件
 * 
 * @author wangsenhao
 *
 */
public class ZycLotteryRecordQuery {

	private Long seasonId;

	private String startAt;

	private String endAt;

	private UserType userType;

	private String accountName;
	/**
	 * 是否只看中奖
	 */
	private Boolean justLookWin = false;
	/**
	 * 当前活动对应的code
	 */
	private Integer code;

	private CoinsLotteryType type;

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Boolean getJustLookWin() {
		return justLookWin;
	}

	public void setJustLookWin(Boolean justLookWin) {
		this.justLookWin = justLookWin;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public CoinsLotteryType getType() {
		return type;
	}

	public void setType(CoinsLotteryType type) {
		this.type = type;
	}

}
