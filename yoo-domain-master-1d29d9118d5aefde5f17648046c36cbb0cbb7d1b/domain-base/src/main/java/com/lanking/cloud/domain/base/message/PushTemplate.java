package com.lanking.cloud.domain.base.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 推送消息模板
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "push_template")
public class PushTemplate extends MessageTemplate {

	private static final long serialVersionUID = -2253576263023123290L;

	/**
	 * 标题
	 */
	@Column(name = "title", length = 256)
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
