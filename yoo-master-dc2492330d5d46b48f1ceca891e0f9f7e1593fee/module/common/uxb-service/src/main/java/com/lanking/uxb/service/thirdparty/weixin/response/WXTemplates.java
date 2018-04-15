package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;
import java.util.List;

/**
 * 素材集合.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public class WXTemplates implements Serializable {
	private static final long serialVersionUID = 6464671667852202810L;

	List<WXTemplate> item;

	/**
	 * 该类型的素材的总数.
	 */
	private Integer total_count;

	/**
	 * 本次调用获取的素材的数量.
	 */
	private Integer item_count;

	/**
	 * 错误码.
	 */
	private String errcode;

	/**
	 * 错误信息.
	 */
	private String errmsg;

	public Integer getTotal_count() {
		return total_count;
	}

	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}

	public Integer getItem_count() {
		return item_count;
	}

	public void setItem_count(Integer item_count) {
		this.item_count = item_count;
	}

	public List<WXTemplate> getItem() {
		return item;
	}

	public void setItem(List<WXTemplate> item) {
		this.item = item;
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
