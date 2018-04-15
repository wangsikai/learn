package com.lanking.uxb.zycon.wx.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateContent;

public class VWXButton implements Serializable {
	private static final long serialVersionUID = 6014275594210869718L;

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
	private String mediaId;

	/**
	 * 子菜单集合.
	 */
	private List<VWXButton> subButtons;

	// 以下为不同按钮关联类型的内容，图片、音频直接通过mediaId下载

	/**
	 * 按钮关联内容类型.
	 */
	private RelationType relationType = RelationType.DEFUALT;

	/**
	 * 图文素材内容.
	 */
	private WXTemplateContent wxTemplateContent;

	/**
	 * 客服消息内容.
	 */
	private String message;

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

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public List<VWXButton> getSubButtons() {
		return subButtons;
	}

	public void setSubButtons(List<VWXButton> subButtons) {
		this.subButtons = subButtons;
	}

	public RelationType getRelationType() {
		return relationType;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	public WXTemplateContent getWxTemplateContent() {
		return wxTemplateContent;
	}

	public void setWxTemplateContent(WXTemplateContent wxTemplateContent) {
		this.wxTemplateContent = wxTemplateContent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
