package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 学习目标模块
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_learngoal")
public class TeachAssistElementLearnGoal extends AbstractTeachAssistElement {

	private static final long serialVersionUID = 8332948444601401192L;

	/**
	 * 内容
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
