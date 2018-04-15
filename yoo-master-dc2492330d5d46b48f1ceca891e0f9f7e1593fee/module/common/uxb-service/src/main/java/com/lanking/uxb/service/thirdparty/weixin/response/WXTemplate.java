package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;

/**
 * 微信素材.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public class WXTemplate implements Serializable {
	private static final long serialVersionUID = -535464802403442811L;

	/**
	 * 素材ID.
	 */
	private String media_id;

	/**
	 * 名称.
	 */
	private String name;

	/**
	 * 更新时间.
	 */
	private Long update_time;

	/**
	 * 图片、语音、视频地址
	 */
	private String url;

	/**
	 * 图文素材分块内容.
	 */
	private WXTemplateContent content;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Long update_time) {
		this.update_time = update_time * 1000;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public WXTemplateContent getContent() {
		return content;
	}

	public void setContent(WXTemplateContent content) {
		this.content = content;
	}
}
