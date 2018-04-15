package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 新知识体系知识点与章节对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class KnowledgeSectionKey implements Serializable {
	private static final long serialVersionUID = -724611744722319471L;
	/**
	 * 章节代码
	 */
	@Id
	@Column(name = "section_code", nullable = false)
	private long sectionCode;

	/**
	 * 知识点代码
	 */
	@Id
	@Column(name = "knowledge_code", nullable = false)
	private long knowledgeCode;

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public long getKnowledgeCode() {
		return knowledgeCode;
	}

	public void setKnowledgeCode(long knowledgeCode) {
		this.knowledgeCode = knowledgeCode;
	}
}
