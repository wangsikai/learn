package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;

/**
 * 视频素材.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public class WXTemplateVideo implements Serializable {
	private static final long serialVersionUID = -5761736184277490365L;

	private String title;

	private String description;

	private String down_url;

	/**
	 * 错误码.
	 */
	private String errcode;

	/**
	 * 错误信息.
	 */
	private String errmsg;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDown_url() {
		return down_url;
	}

	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
