package com.lanking.cloud.domain.base.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 推送消息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "push")
public class Push extends Message {

	private static final long serialVersionUID = 2368148665004877905L;

	/**
	 * 标题
	 */
	@Column(name = "title", length = 256)
	private String title;

	/**
	 * 第三方平台返回的结果
	 */
	@Column(name = "ret_msg", length = 128)
	private String retMsg;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

}
