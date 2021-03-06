package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 课时子标题模块
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_periodchildtitle")
public class TeachAssistElementPeriodChildTitle extends AbstractTeachAssistElement {

	private static final long serialVersionUID = 5048901524182357784L;

	/**
	 * 标题
	 */
	@Column(name = "title", length = 64)
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
