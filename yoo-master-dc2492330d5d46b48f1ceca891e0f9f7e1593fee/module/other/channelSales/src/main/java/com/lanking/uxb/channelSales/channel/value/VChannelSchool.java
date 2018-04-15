package com.lanking.uxb.channelSales.channel.value;

import com.lanking.uxb.service.code.value.VSchool;

import java.io.Serializable;

/**
 * ChannelSchool VO
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class VChannelSchool implements Serializable {
	private long id;
	private VSchool school;
	private VUserChannel userChannel;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VSchool getSchool() {
		return school;
	}

	public void setSchool(VSchool school) {
		this.school = school;
	}

	public VUserChannel getUserChannel() {
		return userChannel;
	}

	public void setUserChannel(VUserChannel userChannel) {
		this.userChannel = userChannel;
	}
}
