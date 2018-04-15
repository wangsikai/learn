package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * 解题方法模块
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_problemsolving")
public class TeachAssistElementProblemSolving extends AbstractTeachAssistElement {

	private static final long serialVersionUID = 828999029140194463L;

	/**
	 * 解题方法及要点小结内容
	 */
	@Lob
	@Type(type = "text")
	@Column(name = "content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
