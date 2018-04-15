package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 自由编辑
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_freeedit")
public class TeachAssistElementFreeEdit extends AbstractTeachAssistElement {

	private static final long serialVersionUID = -5298402453828119816L;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 128)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
