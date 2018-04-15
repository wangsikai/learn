package com.lanking.uxb.rescon.account.api;

import com.lanking.cloud.domain.support.resources.vendor.Vendor;

public interface ResconVendorManage {

	/**
	 * 获取供应商信息
	 * 
	 * @param id
	 *            供应商ID
	 */
	Vendor get(long id);
}
