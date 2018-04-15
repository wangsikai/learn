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
 * 会员套餐订单渠道结算表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_order_channel_settlement")
public class MemberPackageOrderChannelSettlement implements Serializable {

	private static final long serialVersionUID = -2781270469161858158L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 渠道代码
	 */
	@Column(name = "channel_code", precision = 5)
	private int channelCode;

	/**
	 * 交易总金额
	 */
	@Column(name = "transaction_amount", scale = 2, nullable = false)
	private BigDecimal transactionAmount;

	/**
	 * 线上交易总金额
	 */
	@Column(name = "online_transaction_amount", scale = 2, nullable = false)
	private BigDecimal onlineTransactionAmount;

	/**
	 * 管理员代开交易额（不算透支额度）
	 */
	@Column(name = "admin_transaction_amount", scale = 2, nullable = false)
	private BigDecimal adminTransactionAmount;

	/**
	 * 渠道开通交易额（透支额度）
	 */
	@Column(name = "channel_transaction_amount", scale = 2, nullable = false)
	private BigDecimal channelTransactionAmount;

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
	 * 结算利润
	 */
	@Column(name = "settlement_profits", scale = 2, nullable = false)
	private BigDecimal settlementProfits;

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
	 * 结算状态
	 */
	@Column(name = "status", precision = 3)
	private MemberPackageOrderChannelSettlementStatus status;

	/**
	 * 创建时间(对应status里面的INIT状态)(线上结算时间,即结算账单生成的时间)
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 结算时间(对应status里面的SETTLED状态)(线下结算时间)
	 */
	@Column(name = "settlement_at", columnDefinition = "datetime(3)")
	private Date settlementAt;

	/**
	 * 结算返还差额
	 */
	@Column(name = "profits_gap", scale = 2, nullable = false)
	private BigDecimal profitsGap;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(int channelCode) {
		this.channelCode = channelCode;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getOnlineTransactionAmount() {
		return onlineTransactionAmount;
	}

	public void setOnlineTransactionAmount(BigDecimal onlineTransactionAmount) {
		this.onlineTransactionAmount = onlineTransactionAmount;
	}

	public BigDecimal getAdminTransactionAmount() {
		return adminTransactionAmount;
	}

	public void setAdminTransactionAmount(BigDecimal adminTransactionAmount) {
		this.adminTransactionAmount = adminTransactionAmount;
	}

	public BigDecimal getChannelTransactionAmount() {
		return channelTransactionAmount;
	}

	public void setChannelTransactionAmount(BigDecimal channelTransactionAmount) {
		this.channelTransactionAmount = channelTransactionAmount;
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

	public BigDecimal getSettlementProfits() {
		return settlementProfits;
	}

	public void setSettlementProfits(BigDecimal settlementProfits) {
		this.settlementProfits = settlementProfits;
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

	public MemberPackageOrderChannelSettlementStatus getStatus() {
		return status;
	}

	public void setStatus(MemberPackageOrderChannelSettlementStatus status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getSettlementAt() {
		return settlementAt;
	}

	public void setSettlementAt(Date settlementAt) {
		this.settlementAt = settlementAt;
	}

	public BigDecimal getProfitsGap() {
		return profitsGap;
	}

	public void setProfitsGap(BigDecimal profitsGap) {
		this.profitsGap = profitsGap;
	}
}
