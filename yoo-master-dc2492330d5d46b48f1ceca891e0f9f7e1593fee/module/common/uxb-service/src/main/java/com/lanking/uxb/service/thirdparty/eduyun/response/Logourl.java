package com.lanking.uxb.service.thirdparty.eduyun.response;

import java.io.Serializable;

/**
 * 头像列表.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月2日
 */
public class Logourl implements Serializable {
	private static final long serialVersionUID = 3274162738998311626L;

	private String logotype;

	private String logourl;

	public String getLogotype() {
		return logotype;
	}

	public void setLogotype(String logotype) {
		this.logotype = logotype;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}
}
