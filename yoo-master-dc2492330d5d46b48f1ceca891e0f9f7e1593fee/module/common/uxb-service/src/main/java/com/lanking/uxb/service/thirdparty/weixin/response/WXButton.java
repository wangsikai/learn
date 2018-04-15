package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;
import java.util.List;

/**
 * 按钮
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public class WXButton implements Serializable {
	private static final long serialVersionUID = 7225904899675203356L;

	/**
	 * 响应动作类型.
	 */
	private String type;

	/**
	 * 菜单名称.
	 */
	private String name;

	/**
	 * 菜单KEY，用于关联消息推送.
	 */
	private String key;

	/**
	 * URL链接.
	 */
	private String url;

	/**
	 * 素材ID.
	 */
	private String media_id;

	/**
	 * 子菜单集合.
	 */
	private List<WXButton> sub_button;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public List<WXButton> getSub_button() {
		return sub_button;
	}

	public void setSub_button(List<WXButton> sub_button) {
		this.sub_button = sub_button;
	}
}
