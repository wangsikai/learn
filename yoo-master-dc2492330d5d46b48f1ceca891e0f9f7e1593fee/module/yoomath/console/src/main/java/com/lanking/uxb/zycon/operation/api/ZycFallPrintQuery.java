package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;

/**
 * 错题打印查询条件
 * 
 * @author wangsenhao
 * @since 2.5.0
 *
 */
public class ZycFallPrintQuery {
	private String startAt;
	private String endAt;
	private FallibleQuestionPrintOrderStatus status;
	private String accountName;
	private int page = 0;
	private int pageSize = 20;

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

	public FallibleQuestionPrintOrderStatus getStatus() {
		return status;
	}

	public void setStatus(FallibleQuestionPrintOrderStatus status) {
		this.status = status;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
