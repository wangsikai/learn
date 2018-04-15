package com.lanking.uxb.channelSales.finance.value;

import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlementStatus;

/**
 * 会员套餐订单渠道结算 vo
 * 
 * @author wangsenhao
 *
 */
public class VMemberPackageOrderChannelSettlement {

	private Long id;

	// 渠道代码
	private int channelCode;

	// 交易总金额
	private BigDecimal transactionAmount;

	// 线上交易总金额
	private BigDecimal onlineTransactionAmount;

	// 管理员代开交易额
	private BigDecimal adminTransactionAmount;

	// 渠道开通交易额
	private BigDecimal channelTransactionAmount;

	// 渠道利润
	private BigDecimal channelProfits;

	// 公司利润
	private BigDecimal profits;

	// 结算利润
	private BigDecimal settlementProfits;

	// 结算期-年
	private int settlementYear;

	// 结算期-月
	private int settlementMonth;

	// 结算状态
	private MemberPackageOrderChannelSettlementStatus status;
	/**
	 * 结算返还差额
	 */
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

	public BigDecimal getProfitsGap() {
		return profitsGap;
	}

	public void setProfitsGap(BigDecimal profitsGap) {
		this.profitsGap = profitsGap;
	}

}
