package com.lanking.cloud.domain.common.resource.question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 题目&章节关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(QuestionSectionKey.class)
@Table(name = "question_section")
public class QuestionSection extends QuestionSectionKey {

	private static final long serialVersionUID = 5757560843262245362L;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textBookCode;

	/**
	 * 版本1（第一版本知识点转换而来）
	 */
	@Column(name = "v1", columnDefinition = "bit default 1")
	private Boolean v1 = false;

	/**
	 * 版本2（第二版本知识点转换而来）
	 */
	@Column(name = "v2", columnDefinition = "bit default 0")
	private Boolean v2 = false;

	/**
	 * 版本3（第三版本知识点转换而来）
	 */
	@Column(name = "v3", columnDefinition = "bit default 0")
	private Boolean v3 = false;

	public Integer getTextBookCode() {
		return textBookCode;
	}

	public void setTextBookCode(Integer textBookCode) {
		this.textBookCode = textBookCode;
	}

	public Boolean getV1() {
		return v1;
	}

	public void setV1(Boolean v1) {
		this.v1 = v1;
	}

	public Boolean getV2() {
		return v2;
	}

	public void setV2(Boolean v2) {
		this.v2 = v2;
	}

	public Boolean getV3() {
		return v3;
	}

	public void setV3(Boolean v3) {
		this.v3 = v3;
	}

}
