package com.lanking.uxb.rescon.book.form;

import java.io.Serializable;
import java.util.List;

/**
 * 书本表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月22日
 */
public class BookForm implements Serializable {
	private static final long serialVersionUID = 1468920392268111862L;

	/**
	 * 书本ID.
	 */
	private Long bookId;

	/**
	 * 书本版本ID.
	 */
	private Long bookVersionId;

	/**
	 * 封面.
	 */
	private Long coverId;

	/**
	 * 书本名称.
	 */
	private String name;

	/**
	 * 书本简称.
	 */
	private String sname;

	/**
	 * 阶段.
	 */
	private Integer phaseCode;

	/**
	 * 科目.
	 */
	private Integer subjectCode;

	/**
	 * 教材版本.
	 */
	private Integer textbookCategoryCode;

	/**
	 * 教材.
	 */
	private Integer textbookCode;

	/**
	 * 顺序章节.
	 */
	private List<Long> sectionCodes;

	/**
	 * 书本类型.
	 */
	private Integer resourceCategoryCode;

	/**
	 * ISBN号.
	 */
	private String isbn;

	/**
	 * 备注.
	 */
	private String description;

	/**
	 * 出版社.
	 */
	private String press;

	/**
	 * 来源于商品.
	 */
	private Long productId;

	/**
	 * 学校ID.
	 */
	private Long schoolId;

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(Long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public Integer getResourceCategoryCode() {
		return resourceCategoryCode;
	}

	public void setResourceCategoryCode(Integer resourceCategoryCode) {
		this.resourceCategoryCode = resourceCategoryCode;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
}
