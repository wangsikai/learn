package com.lanking.uxb.channelSales.channel.value;

import java.util.Map;

import com.lanking.cloud.sdk.value.VPage;

public class VPageChannel extends VPage<Map> {
	private static final long serialVersionUID = 8298988798846906372L;

	private VUserChannel vUserChannel;

	public VUserChannel getvUserChannel() {
		return vUserChannel;
	}

	public void setvUserChannel(VUserChannel vUserChannel) {
		this.vUserChannel = vUserChannel;
	}

}
