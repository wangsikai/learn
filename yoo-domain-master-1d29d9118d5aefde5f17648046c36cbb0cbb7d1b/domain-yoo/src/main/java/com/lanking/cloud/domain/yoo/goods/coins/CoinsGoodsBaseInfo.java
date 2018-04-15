package com.lanking.cloud.domain.yoo.goods.coins;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 金币兑换商品信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class CoinsGoodsBaseInfo implements Serializable {

	private static final long serialVersionUID = 1838872908176714619L;

	/**
	 * 针对的用户类型
	 * 
	 * <pre>
	 * 二进制转十进制 111 {家长,学生,老师},即1表示老师2表示学生3表示老师学生依次类推
	 * </pre>
	 */
	@Column(name = "user_type")
	private int userType;

	/**
	 * 序号1
	 */
	@Column(name = "sequence0")
	private Integer sequence0;

	/**
	 * 序号2
	 */
	@Column(name = "sequence1")
	private Integer sequence1;

	/**
	 * 序号3
	 */
	@Column(name = "sequence2")
	private Integer sequence2;

	/**
	 * 金币商品类型
	 */
	@Column(name = "coins_goods_type", precision = 3)
	private CoinsGoodsType coinsGoodsType;

	/**
	 * 每天销售的数量,为-1的时候表示没有限制
	 */
	@Column(name = "day_sell_count")
	private int daySellCount;

	/**
	 * 每天销售的数量(显示用)
	 */
	@Column(name = "day_sell_show_count")
	private int daySellShowCount;

	/**
	 * 每人每天可以购买的数量
	 */
	@Column(name = "day_buy_count", columnDefinition = "bigint default -1")
	private int dayBuyCount;

	/**
	 * 周内时间的限制(从右到左分别表示周一二三四五六日)
	 */
	@Column(name = "weekday_limit", precision = 7)
	private Integer weekdayLimit;

	/**
	 * 开始销售时间
	 */
	@Column(name = "date_start", columnDefinition = "datetime(3)")
	private Date dateStart;

	/**
	 * 商品可购买开始时间-时,!= null
	 */
	@Column(name = "day_start_hour", precision = 3)
	private int dayStartHour;

	/**
	 * 商品可购买开始时间-分,!= null
	 */
	@Column(name = "day_start_min", precision = 3)
	private int dayStartMin;

	/**
	 * 商品可购买结束时间-时,!= null
	 */
	@Column(name = "day_end_hour", precision = 3)
	private int dayEndHour;

	/**
	 * 商品可购买结束时间-分,!= null
	 */
	@Column(name = "day_end_min", precision = 3)
	private int dayEndMin;

	/**
	 * 商品状态
	 */
	@Column(name = "status", precision = 3)
	private CoinsGoodsStatus status;

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

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}

	public int getDaySellCount() {
		return daySellCount;
	}

	public void setDaySellCount(int daySellCount) {
		this.daySellCount = daySellCount;
	}

	public int getDaySellShowCount() {
		return daySellShowCount;
	}

	public void setDaySellShowCount(int daySellShowCount) {
		this.daySellShowCount = daySellShowCount;
	}

	public int getDayBuyCount() {
		return dayBuyCount;
	}

	public void setDayBuyCount(int dayBuyCount) {
		this.dayBuyCount = dayBuyCount;
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

	public CoinsGoodsStatus getStatus() {
		return status;
	}

	public void setStatus(CoinsGoodsStatus status) {
		this.status = status;
	}

}
