package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;

public class WXMenus implements Serializable {
	private static final long serialVersionUID = -3240084975356424861L;

	private WXMenu menu;

	/**
	 * 错误码.
	 */
	private String errcode;

	/**
	 * 错误信息.
	 */
	private String errmsg;

	public WXMenu getMenu() {
		return menu;
	}

	public void setMenu(WXMenu menu) {
		this.menu = menu;
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
