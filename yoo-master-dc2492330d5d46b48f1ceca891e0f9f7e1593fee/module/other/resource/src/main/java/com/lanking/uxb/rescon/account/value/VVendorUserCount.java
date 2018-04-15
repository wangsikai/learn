package com.lanking.uxb.rescon.account.value;

import java.io.Serializable;

/**
 * 供应商用户统计VO
 * 
 * @author wangsenhao
 * @version 2015年8月13日
 */
public class VVendorUserCount implements Serializable {

	private static final long serialVersionUID = -1308263427591924506L;

	private Long total;
	private Long enableCount;
	private Long buildCount;
	private Long checkCount;
	private Long headCount;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getEnableCount() {
		return enableCount;
	}

	public void setEnableCount(Long enableCount) {
		this.enableCount = enableCount;
	}

	public Long getBuildCount() {
		return buildCount;
	}

	public void setBuildCount(Long buildCount) {
		this.buildCount = buildCount;
	}

	public Long getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Long checkCount) {
		this.checkCount = checkCount;
	}

	public Long getHeadCount() {
		return headCount;
	}

	public void setHeadCount(Long headCount) {
		this.headCount = headCount;
	}
}
