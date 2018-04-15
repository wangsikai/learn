package com.lanking.uxb.service.index.value;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 书本索引对象.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月23日
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.BOOK)
public class BookIndexDoc extends AbstraceIndexDoc {
	private static final long serialVersionUID = -1068508297885328633L;

	// 书本ID.
	@IndexMapping(type = MappingType.LONG)
	private Long bookId;

	// 书本名称（版本1）.
	@IndexMapping(type = MappingType.TEXT)
	private String name1;

	// 书本名称（版本2）.
	@IndexMapping(type = MappingType.TEXT)
	private String name2;

	// 阶段.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer phaseCode;

	// 科目.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode;

	// 创建人.
	@IndexMapping(type = MappingType.LONG)
	private Long createId;

	// 状态（版本1）.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer status1;

	// 状态（版本2）.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer status2;

	// 书本创建时间.
	@IndexMapping(type = MappingType.LONG)
	private Long createAt;

	// 教材版本（版本1）.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCategoryCode1;

	// 教材版本（版本2）.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCategoryCode2;

	// 教材（版本1）.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCode1;

	// 教材（版本2）.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCode2;

	// 章节（版本1）.
	@IndexMapping(type = MappingType.LONG, ignore = true)
	private Long sectionCode1;

	// 章节（版本2）.
	@IndexMapping(type = MappingType.LONG, ignore = true)
	private Long sectionCode2;

	// 顺序章节（版本1）
	@IndexMapping(type = MappingType.LONG, ignore = true)
	private List<Long> sectionCodes1 = Lists.newArrayList();

	// 顺序章节（版本2）
	@IndexMapping(type = MappingType.LONG, ignore = true)
	private List<Long> sectionCodes2 = Lists.newArrayList();

	// 书本类型（版本1）
	@IndexMapping(type = MappingType.INTEGER)
	private Integer resourceCategoryCode1;

	// 书本类型（版本2）
	@IndexMapping(type = MappingType.INTEGER)
	private Integer resourceCategoryCode2;

	// ISBN号（版本1）.
	@IndexMapping(type = MappingType.TEXT)
	private String isbn1;

	// ISBN号（版本2）.
	@IndexMapping(type = MappingType.TEXT)
	private String isbn2;

	// 供应商.
	@IndexMapping(type = MappingType.LONG)
	private Long vendorId;

	// 状态（书本）.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer status;

	// 校本学校.
	@IndexMapping(type = MappingType.LONG)
	private Long schoolId;

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
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

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Integer getStatus1() {
		return status1;
	}

	public void setStatus1(Integer status1) {
		this.status1 = status1;
	}

	public Integer getStatus2() {
		return status2;
	}

	public void setStatus2(Integer status2) {
		this.status2 = status2;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Integer getTextbookCategoryCode1() {
		return textbookCategoryCode1;
	}

	public void setTextbookCategoryCode1(Integer textbookCategoryCode1) {
		this.textbookCategoryCode1 = textbookCategoryCode1;
	}

	public Integer getTextbookCategoryCode2() {
		return textbookCategoryCode2;
	}

	public void setTextbookCategoryCode2(Integer textbookCategoryCode2) {
		this.textbookCategoryCode2 = textbookCategoryCode2;
	}

	public Integer getTextbookCode1() {
		return textbookCode1;
	}

	public void setTextbookCode1(Integer textbookCode1) {
		this.textbookCode1 = textbookCode1;
	}

	public Integer getTextbookCode2() {
		return textbookCode2;
	}

	public void setTextbookCode2(Integer textbookCode2) {
		this.textbookCode2 = textbookCode2;
	}

	public Long getSectionCode1() {
		return sectionCode1;
	}

	public void setSectionCode1(Long sectionCode1) {
		this.sectionCode1 = sectionCode1;
	}

	public Long getSectionCode2() {
		return sectionCode2;
	}

	public void setSectionCode2(Long sectionCode2) {
		this.sectionCode2 = sectionCode2;
	}

	public List<Long> getSectionCodes1() {
		return sectionCodes1;
	}

	public void setSectionCodes1(List<Long> sectionCodes1) {
		this.sectionCodes1 = sectionCodes1;
	}

	public List<Long> getSectionCodes2() {
		return sectionCodes2;
	}

	public void setSectionCodes2(List<Long> sectionCodes2) {
		this.sectionCodes2 = sectionCodes2;
	}

	public Integer getResourceCategoryCode1() {
		return resourceCategoryCode1;
	}

	public void setResourceCategoryCode1(Integer resourceCategoryCode1) {
		this.resourceCategoryCode1 = resourceCategoryCode1;
	}

	public Integer getResourceCategoryCode2() {
		return resourceCategoryCode2;
	}

	public void setResourceCategoryCode2(Integer resourceCategoryCode2) {
		this.resourceCategoryCode2 = resourceCategoryCode2;
	}

	public String getIsbn1() {
		return isbn1;
	}

	public void setIsbn1(String isbn1) {
		this.isbn1 = isbn1;
	}

	public String getIsbn2() {
		return isbn2;
	}

	public void setIsbn2(String isbn2) {
		this.isbn2 = isbn2;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

}
