package com.lanking.cloud.domain.yoo.order.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 会员套餐订单结算表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_order_settlement")
public class MemberPackageOrderSettlement implements Serializable {

	private static final long serialVersionUID = -8541872201285251095L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 订单编号
	 */
	@Column(name = "order_id")
	private long orderId;

	/**
	 * 订单编号
	 */
	@Column(name = "order_code", length = 128)
	private String orderCode;

	/**
	 * 交易金额
	 */
	@Column(name = "transaction_amount", scale = 2, nullable = false)
	private BigDecimal transactionAmount;

	/**
	 * 订单来源
	 */
	@Column(name = "type", precision = 3)
	private MemberPackageOrderType type;

	/**
	 * 渠道利润
	 */
	@Column(name = "channel_profits", scale = 2, nullable = false)
	private BigDecimal channelProfits;

	/**
	 * 公司利润
	 */
	@Column(name = "profits", scale = 2, nullable = false)
	private BigDecimal profits;

	/**
	 * 返还差额利润
	 */
	@Column(name = "profits_gap", scale = 2, nullable = false)
	private BigDecimal profitsGap;

	/**
	 * 结算期-年
	 */
	@Column(name = "settlement_year", precision = 5)
	private int settlementYear;

	/**
	 * 结算期-月
	 */
	@Column(name = "settlement_month", precision = 3)
	private int settlementMonth;

	/**
	 * 结算时间
	 */
	@Column(name = "settlement_at", columnDefinition = "datetime(3)")
	private Date settlementAt;

	/**
	 * 渠道
	 */
	@Column(name = "user_channel_code", precision = 5)
	private Integer userChannelCode;

	/**
	 * 订单会员人数
	 */
	@Column(name = "member_count", precision = 5)
	private int memberCount = 1;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 对应套餐的时长年份数
	 */
	@Column(name = "year_count", precision = 2)
	private int yearCount;

	/**
	 * 确定返还差额利润的时间
	 */
	@Column(name = "profits_gap_at", columnDefinition = "datetime(3)")
	private Date profitsGapAt;

	/**
	 * 所属套餐组ID
	 */
	@Column(name = "member_package_group_id")
	private Long memberPackageGroupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getChannelProfits() {
		return channelProfits;
	}

	public void setChannelProfits(BigDecimal channelProfits) {
		this.channelProfits = channelProfits;
	}

	public BigDecimal getProfits() {
		return profits;
	}

	public void setProfits(BigDecimal profits) {
		this.profits = profits;
	}

	public BigDecimal getProfitsGap() {
		return profitsGap;
	}

	public void setProfitsGap(BigDecimal profitsGap) {
		this.profitsGap = profitsGap;
	}

	public int getSettlementYear() {
		return settlementYear;
	}

	public void setSettlementYear(int settlementYear) {
		this.settlementYear = settlementYear;
	}

	public int getSettlementMonth() {
		return settlementMonth;
	}

	public void setSettlementMonth(int settlementMonth) {
		this.settlementMonth = settlementMonth;
	}

	public Date getSettlementAt() {
		return settlementAt;
	}

	public void setSettlementAt(Date settlementAt) {
		this.settlementAt = settlementAt;
	}

	public MemberPackageOrderType getType() {
		return type;
	}

	public void setType(MemberPackageOrderType type) {
		this.type = type;
	}

	public Integer getUserChannelCode() {
		return userChannelCode;
	}

	public void setUserChannelCode(Integer userChannelCode) {
		this.userChannelCode = userChannelCode;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getProfitsGapAt() {
		return profitsGapAt;
	}

	public void setProfitsGapAt(Date profitsGapAt) {
		this.profitsGapAt = profitsGapAt;
	}

	public int getYearCount() {
		return yearCount;
	}

	public void setYearCount(int yearCount) {
		this.yearCount = yearCount;
	}

	public Long getMemberPackageGroupId() {
		return memberPackageGroupId;
	}

	public void setMemberPackageGroupId(Long memberPackageGroupId) {
		this.memberPackageGroupId = memberPackageGroupId;
	}
}
