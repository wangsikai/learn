package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;
import java.util.List;

/**
 * 微信菜单按钮集合.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public class WXButtons implements Serializable {
	private static final long serialVersionUID = 2906372948215752312L;

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
