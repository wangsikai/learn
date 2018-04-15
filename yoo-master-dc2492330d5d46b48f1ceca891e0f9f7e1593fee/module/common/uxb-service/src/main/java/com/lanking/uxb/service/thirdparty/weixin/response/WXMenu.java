package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;
import java.util.List;

public class WXMenu implements Serializable {
	private static final long serialVersionUID = 9213621303790009992L;

	/**
	 * 按钮集合.
	 */
	private List<WXButton> button;

	public List<WXButton> getButton() {
		return button;
	}

	public void setButton(List<WXButton> button) {
		this.button = button;
	}
}
