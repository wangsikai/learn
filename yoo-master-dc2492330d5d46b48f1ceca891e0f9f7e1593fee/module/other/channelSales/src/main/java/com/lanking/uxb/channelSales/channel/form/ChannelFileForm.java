package com.lanking.uxb.channelSales.channel.form;

/**
 * 渠道资料表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月3日
 */
public class ChannelFileForm {
	private Long id;
	private String name;
	private long coverId;
	private String uri;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCoverId() {
		return coverId;
	}

	public void setCoverId(long coverId) {
		this.coverId = coverId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
