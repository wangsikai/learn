package com.lanking.uxb.service.homework.value;

import com.lanking.uxb.service.code.value.VKnowledgePoint;

public class VWeakKonwledgePoint extends VKnowledgePoint {

	private static final long serialVersionUID = 3223770606686675825L;

	// 掌握情况
	private String masterStatus;

	public String getMasterStatus() {
		return masterStatus;
	}

	public void setMasterStatus(String masterStatus) {
		this.masterStatus = masterStatus;
	}
}
