package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 知识点回顾
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_knowledgespec")
public class TeachAssistElementKnowledgeSpec extends AbstractTeachAssistElement {

	private static final long serialVersionUID = 8220047884877279700L;

	/**
	 * 类型 知识说明 -> false 知识回顾 -> true
	 */
	@Column(name = "review")
	private boolean review = false;

	public boolean isReview() {
		return review;
	}

	public void setReview(boolean review) {
		this.review = review;
	}
}
