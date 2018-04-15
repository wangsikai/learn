package com.lanking.uxb.service.thirdparty.weixin.response;

import javax.xml.bind.annotation.XmlElement;

/**
 * 图文消息项.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月2日
 */
public class WXArticle {

	/**
	 * 图文消息标题.
	 */
	private String title;

	/**
	 * 图文消息描述.
	 */
	private String description;

	/**
	 * 图文消息图片URL.
	 */
	private String picUrl;

	/**
	 * 图文消息链接.
	 */
	private String url;

	@XmlElement(name = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "PicUrl")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@XmlElement(name = "Url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
