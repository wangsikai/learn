package com.lanking.uxb.zycon.mall.form;

import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;

public class CoinsGoodsForm {

	private Long goodsId = 0L;

	private String name;

	private BigDecimal price;

	private Integer daySellCount = 0;

	private String userTypeStr;

	private CoinsGoodsType coinsGoodsType;

	// 上架时间
	private Date salesTime;

	// 下架时间
	private Date soldOutTime;

	// 商品可购买开始时间-时
	private Integer dayStartHour = 0;

	// 商品可购买开始时间-分
	private Integer dayStartMin = 0;

	// 商品可购买结束时间-时
	private Integer dayEndHour = 24;

	// 商品可购买结束时间-分
	private Integer dayEndMin = 0;

	private CoinsGoodsStatus status;// 商品状态

	// 简介描述
	private String introduction;

	// 详情描述
	private String content;

	private Long imageId;
	/**
	 * 指定日期 购买开始时间
	 * 
	 * @since 2.4.0
	 */
	private String dateStart;
	/**
	 * 时间段类型<br>
	 * 0.每天 1.指定日期 2.选周几
	 */
	private Integer buyTimeFlag;
	// 周内时间的限制(从右到左分别表示周一二三四五六日)
	private Integer weekdayLimit;

	// 单个用户每天可兑换数量
	private Integer dayBuyCount;

	// 商品组Id 20170321 senhao.wang
	private long groupId;

	// 前台显示数量 20170321 senhao.wang
	private Integer daySellShowCount;

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getDaySellCount() {
		return daySellCount;
	}

	public void setDaySellCount(Integer daySellCount) {
		this.daySellCount = daySellCount;
	}

	public String getUserTypeStr() {
		return userTypeStr;
	}

	public void setUserTypeStr(String userTypeStr) {
		this.userTypeStr = userTypeStr;
	}

	public CoinsGoodsType getVirtualGoodsType() {
		return coinsGoodsType;
	}

	public void setVirtualGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}

	public Date getSalesTime() {
		return salesTime;
	}

	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}

	public Date getSoldOutTime() {
		return soldOutTime;
	}

	public void setSoldOutTime(Date soldOutTime) {
		this.soldOutTime = soldOutTime;
	}

	public Integer getDayStartHour() {
		return dayStartHour;
	}

	public void setDayStartHour(Integer dayStartHour) {
		this.dayStartHour = dayStartHour;
	}

	public Integer getDayStartMin() {
		return dayStartMin;
	}

	public void setDayStartMin(Integer dayStartMin) {
		this.dayStartMin = dayStartMin;
	}

	public Integer getDayEndHour() {
		return dayEndHour;
	}

	public void setDayEndHour(Integer dayEndHour) {
		this.dayEndHour = dayEndHour;
	}

	public Integer getDayEndMin() {
		return dayEndMin;
	}

	public void setDayEndMin(Integer dayEndMin) {
		this.dayEndMin = dayEndMin;
	}

	public CoinsGoodsStatus getStatus() {
		return status;
	}

	public void setStatus(CoinsGoodsStatus status) {
		this.status = status;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public Integer getBuyTimeFlag() {
		return buyTimeFlag;
	}

	public void setBuyTimeFlag(Integer buyTimeFlag) {
		this.buyTimeFlag = buyTimeFlag;
	}

	public Integer getWeekdayLimit() {
		return weekdayLimit;
	}

	public void setWeekdayLimit(Integer weekdayLimit) {
		this.weekdayLimit = weekdayLimit;
	}

	public Integer getDayBuyCount() {
		return dayBuyCount;
	}

	public void setDayBuyCount(Integer dayBuyCount) {
		this.dayBuyCount = dayBuyCount;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Integer getDaySellShowCount() {
		return daySellShowCount;
	}

	public void setDaySellShowCount(Integer daySellShowCount) {
		this.daySellShowCount = daySellShowCount;
	}

}
