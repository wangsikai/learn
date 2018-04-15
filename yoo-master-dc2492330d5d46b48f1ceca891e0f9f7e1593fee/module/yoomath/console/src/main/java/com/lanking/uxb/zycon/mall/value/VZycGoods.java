package com.lanking.uxb.zycon.mall.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;

/**
 * 商品VO
 * 
 * @author zdy
 *
 */
public class VZycGoods implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long goodsId;

	// 基本信息
	// 名称
	private String name;

	// 简介描述
	private String introduction;

	// 详情描述
	private String content;

	// 图片
	private Long imageId;

	// 价格
	private BigDecimal price;

	private Date salesTime;

	// 下架时间
	private Date soldOutTime;

	// 金币商品信息

	private int userType;

	private String userTypeStr;

	private Integer sequence0;

	private Integer sequence1;

	private Integer sequence2;

	// 虚拟商品类型
	private CoinsGoodsType coinsGoodsType;

	// 每天销售的数量,为-1的时候表示没有限制
	private int daySellCount;

	// 商品可购买开始时间-时
	private int dayStartHour;

	// 商品可购买开始时间-分
	private int dayStartMin;

	// 商品可购买结束时间-时
	private int dayEndHour;

	// 商品可购买结束时间-分
	private int dayEndMin;

	// 1：已经上架，0：待上架 ,2:未上架
	private int canOffGoods = 2;

	// 商品状态
	private CoinsGoodsStatus goodsStatus;

	private String dayStartHourStr;

	private String dayStartMinStr;

	// 周内时间的限制(从右到左分别表示周一二三四五六日),例如：周三是100
	private Integer weekdayLimit;

	// 开始销售时间
	private Date dateStart;

	// 可兑换时间的描述
	private String des;

	private List<Integer> weekIndexList;

	// 0.每天 1.指定时间 2.选周几
	private Integer buyTimeType;

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public Integer getSequence0() {
		return sequence0;
	}

	public void setSequence0(Integer sequence0) {
		this.sequence0 = sequence0;
	}

	public Integer getSequence1() {
		return sequence1;
	}

	public void setSequence1(Integer sequence1) {
		this.sequence1 = sequence1;
	}

	public Integer getSequence2() {
		return sequence2;
	}

	public void setSequence2(Integer sequence2) {
		this.sequence2 = sequence2;
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

	public CoinsGoodsStatus getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(CoinsGoodsStatus goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public int getCanOffGoods() {
		return canOffGoods;
	}

	public void setCanOffGoods(int canOffGoods) {
		this.canOffGoods = canOffGoods;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getDayStartHourStr() {
		return dayStartHourStr;
	}

	public void setDayStartHourStr(String dayStartHourStr) {
		this.dayStartHourStr = dayStartHourStr;
	}

	public String getDayStartMinStr() {
		return dayStartMinStr;
	}

	public void setDayStartMinStr(String dayStartMinStr) {
		this.dayStartMinStr = dayStartMinStr;
	}

	public Integer getWeekdayLimit() {
		return weekdayLimit;
	}

	public void setWeekdayLimit(Integer weekdayLimit) {
		this.weekdayLimit = weekdayLimit;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Integer getBuyTimeType() {
		return buyTimeType;
	}

	public void setBuyTimeType(Integer buyTimeType) {
		this.buyTimeType = buyTimeType;
	}

	public List<Integer> getWeekIndexList() {
		return weekIndexList;
	}

	public void setWeekIndexList(List<Integer> weekIndexList) {
		this.weekIndexList = weekIndexList;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
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
