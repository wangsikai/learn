package com.lanking.uxb.channelSales.memberPackage.value;

import java.io.Serializable;

/**
 * 创建用户信息
 * 
 * @author zemin.song
 * @version 2016年10月18日
 */
public class VMemberPackageCreateUser implements Serializable {

	String createName;
	Long createId;

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

}
