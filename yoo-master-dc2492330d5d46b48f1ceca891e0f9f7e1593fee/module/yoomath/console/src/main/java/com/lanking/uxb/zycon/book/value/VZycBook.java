package com.lanking.uxb.zycon.book.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.uxb.service.code.value.VSchool;

/**
 * 书本VO
 * 
 * @author wangsenhao
 *
 */
public class VZycBook implements Serializable {

	private static final long serialVersionUID = 8846018354442837659L;

	private Long bookVersionId;
	private String coverUrl;
	private Long bookId;
	private String name;
	private String version;
	private String subjectName;
	private String phaseName;
	private String bookType;
	private String isbn;
	private String categoryName;
	private String textbookName;
	private String openstatus;
	private List<VSchool> schoolList;
	private Long schoolId;

	public Long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(Long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTextbookName() {
		return textbookName;
	}

	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}

	public String getOpenstatus() {
		return openstatus;
	}

	public void setOpenstatus(String openstatus) {
		this.openstatus = openstatus;
	}

	public List<VSchool> getSchoolList() {
		return schoolList;
	}

	public void setSchoolList(List<VSchool> schoolList) {
		this.schoolList = schoolList;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

}
