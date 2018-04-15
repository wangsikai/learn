package com.lanking.uxb.zycon.mall.value;

import java.util.Map;

import com.lanking.cloud.sdk.value.VPage;

public class VZycPageOrdersTotal extends VPage<Map> {
	private static final long serialVersionUID = -7565169337933454539L;

	private VZycTotalOrdersData totalData;

	public VZycTotalOrdersData getTotalData() {
		return totalData;
	}

	public void setTotalData(VZycTotalOrdersData totalData) {
		this.totalData = totalData;
	}

}
