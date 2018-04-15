package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 知识图谱
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "meta_knowpoint_map")
public class MetaKnowpointMap implements Serializable {

	private static final long serialVersionUID = -5674684244092005181L;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 元知识点代码
	 */
	@Column(name = "meta_code")
	private Integer metaCode;

	/**
	 * 父级代码
	 */
	@Column(name = "pcode")
	private Integer pcode;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 章节代码
	 */
	@Column(name = "section_code")
	private long sectionCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMetaCode() {
		return metaCode;
	}

	public void setMetaCode(Integer metaCode) {
		this.metaCode = metaCode;
	}

	public Integer getPcode() {
		return pcode;
	}

	public void setPcode(Integer pcode) {
		this.pcode = pcode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

}
