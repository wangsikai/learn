package com.lanking.uxb.channelSales.finance.value;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.value.VPage;

public class VPageStat extends VPage<Map> {

	private static final long serialVersionUID = 4117640326460394178L;
	private List<Map> totalSum;

	public List<Map> getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(List<Map> totalSum) {
		this.totalSum = totalSum;
	}

}
