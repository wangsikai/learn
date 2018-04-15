package com.lanking.cloud.domain.common.resource.book;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 书本目录-教材章节对应关系.
 * 
 * @author wlche
 *
 */
@Entity
@Table(name = "book_catalog_section")
public class BookCatalogSection implements Serializable {
	private static final long serialVersionUID = 3956372184018748633L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联书本版本ID
	 */
	@Column(name = "book_version_id")
	private Long bookVersionId;

	/**
	 * 书本目录ID
	 */
	@Column(name = "book_catalog_id")
	private Long bookCatalogId;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 对应的章节代码
	 */
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 对应到教材章节里的序号.
	 */
	@Column(name = "sequence")
	private int sequence;

	/**
	 * 添加人.
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 添加时间.
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(Long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public Long getBookCatalogId() {
		return bookCatalogId;
	}

	public void setBookCatalogId(Long bookCatalogId) {
		this.bookCatalogId = bookCatalogId;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
