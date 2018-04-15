package com.lanking.uxb.service.mall.value;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;

/**
 * 金币商品VO
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
public class VCoinsGoods extends VGoods {

	private static final long serialVersionUID = 7674925805661542550L;

	private long coinsGoodsSnapshotId;
	// 金币价格,convert里面将商品的price转换过来,一遍前端直接使用
	private int coinsPrice;
	// MATH_STUDENT:V1.3.4开始使用coinsGoodsType
	private CoinsGoodsType coinsGoodsType;
	// TODO:此字段后期删掉
	@Deprecated
	private CoinsGoodsType virtualGoodsType;
	// 每日限制消失的数量
	private int daySellCount;
	private int dayStartHour;
	private int dayStartMin;
	private int dayEndHour;
	private int dayEndMin;
	// 已经销售的数量
	private int daySelledCount;
	// 剩余数量
	private int dayRemainingCount;

	// 每人每天可以购买的数量
	private int dayBuyCount;
	// 周内限制的描述
	private String weekdayLimit;

	// 服务器到销售开始的时间毫秒数
	private long toStartTime;
	// 服务器到销售截止的时间毫秒数
	private long toEndTime;

	public long getCoinsGoodsSnapshotId() {
		return coinsGoodsSnapshotId;
	}

	public void setCoinsGoodsSnapshotId(long coinsGoodsSnapshotId) {
		this.coinsGoodsSnapshotId = coinsGoodsSnapshotId;
	}

	public int getCoinsPrice() {
		return coinsPrice;
	}

	public void setCoinsPrice(int coinsPrice) {
		this.coinsPrice = coinsPrice;
	}

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}

	public CoinsGoodsType getVirtualGoodsType() {
		return virtualGoodsType;
	}

	public void setVirtualGoodsType(CoinsGoodsType virtualGoodsType) {
		this.virtualGoodsType = virtualGoodsType;
	}

	public int getDaySellCount() {
		return daySellCount;
	}

	public void setDaySellCount(int daySellCount) {
		this.daySellCount = daySellCount;
	}

	public int getDayStartHour() {
		return dayStartHour;
	}

	public void setDayStartHour(int dayStartHour) {
		this.dayStartHour = dayStartHour;
	}

	public int getDayStartMin() {
		return dayStartMin;
	}

	public void setDayStartMin(int dayStartMin) {
		this.dayStartMin = dayStartMin;
	}

	public int getDayEndHour() {
		return dayEndHour;
	}

	public void setDayEndHour(int dayEndHour) {
		this.dayEndHour = dayEndHour;
	}

	public int getDayEndMin() {
		return dayEndMin;
	}

	public void setDayEndMin(int dayEndMin) {
		this.dayEndMin = dayEndMin;
	}

	public int getDaySelledCount() {
		return daySelledCount;
	}

	public void setDaySelledCount(int daySelledCount) {
		this.daySelledCount = daySelledCount;
	}

	public int getDayRemainingCount() {
		return dayRemainingCount;
	}

	public void setDayRemainingCount(int dayRemainingCount) {
		this.dayRemainingCount = dayRemainingCount;
	}

	public int getDayBuyCount() {
		return dayBuyCount;
	}

	public void setDayBuyCount(int dayBuyCount) {
		this.dayBuyCount = dayBuyCount;
	}

	public String getWeekdayLimit() {
		return weekdayLimit;
	}

	public void setWeekdayLimit(String weekdayLimit) {
		this.weekdayLimit = weekdayLimit;
	}

	public long getToStartTime() {
		return toStartTime;
	}

	public void setToStartTime(long toStartTime) {
		this.toStartTime = toStartTime;
	}

	public long getToEndTime() {
		return toEndTime;
	}

	public void setToEndTime(long toEndTime) {
		this.toEndTime = toEndTime;
	}
}
