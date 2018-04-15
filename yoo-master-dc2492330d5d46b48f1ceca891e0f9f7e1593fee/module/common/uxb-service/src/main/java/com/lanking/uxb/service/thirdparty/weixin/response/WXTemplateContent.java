package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;
import java.util.List;

/**
 * 图文素材分块内容.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public class WXTemplateContent implements Serializable {
	private static final long serialVersionUID = 3679204282708139856L;

	/**
	 * 图文模块集合.
	 */
	private List<WXTemplateItem> news_item;

	/**
	 * 错误码.
	 */
	private String errcode;

	/**
	 * 错误信息.
	 */
	private String errmsg;

	public List<WXTemplateItem> getNews_item() {
		return news_item;
	}

	public void setNews_item(List<WXTemplateItem> news_item) {
		this.news_item = news_item;
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
