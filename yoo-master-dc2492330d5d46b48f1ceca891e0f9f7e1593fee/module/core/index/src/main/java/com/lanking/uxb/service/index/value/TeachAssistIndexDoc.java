package com.lanking.uxb.service.index.value;

import org.springframework.stereotype.Component;

import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 教辅索引对象
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.TEACH_ASSIST)
public class TeachAssistIndexDoc extends AbstraceIndexDoc {
	private static final long serialVersionUID = 2920800555986972703L;

	@IndexMapping(type = MappingType.LONG)
	private Long id;

	// 删除状态
	@IndexMapping(type = MappingType.INTEGER)
	private Integer delStatus;

	// 创建者id
	@IndexMapping(type = MappingType.LONG)
	private Long createId;

	// 供应商ID
	@IndexMapping(type = MappingType.LONG)
	private Long vendorId;

	// 创建时间
	@IndexMapping(type = MappingType.LONG)
	private Long createAt;

	// 主版本名称
	@IndexMapping(type = MappingType.TEXT)
	private String name1;

	// 在编辑名称
	@IndexMapping(type = MappingType.TEXT)
	private String name2;

	// 主版本阶段码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer phaseCode1;

	// 在编辑阶段码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer phaseCode2;

	// 主版本学科码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode1;

	// 在编辑学科码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode2;

	// 主版本教材码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCode1;

	// 在编辑版本教材码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCode2;

	// 主版本章节码
	@IndexMapping(type = MappingType.LONG)
	private Long sectionCode1;

	// 在编辑版本章节码
	@IndexMapping(type = MappingType.LONG)
	private Long sectionCode2;

	// 主教材版本
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCategoryCode1;

	// 辅教材版本
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCategoryCode2;

	// 审核状态1
	@IndexMapping(type = MappingType.INTEGER)
	private Integer teachAssistStatus1;

	// 审核状态2
	@IndexMapping(type = MappingType.INTEGER)
	private Integer teachAssistStatus2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
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

	public Integer getPhaseCode1() {
		return phaseCode1;
	}

	public void setPhaseCode1(Integer phaseCode1) {
		this.phaseCode1 = phaseCode1;
	}

	public Integer getPhaseCode2() {
		return phaseCode2;
	}

	public void setPhaseCode2(Integer phaseCode2) {
		this.phaseCode2 = phaseCode2;
	}

	public Integer getSubjectCode1() {
		return subjectCode1;
	}

	public void setSubjectCode1(Integer subjectCode1) {
		this.subjectCode1 = subjectCode1;
	}

	public Integer getSubjectCode2() {
		return subjectCode2;
	}

	public void setSubjectCode2(Integer subjectCode2) {
		this.subjectCode2 = subjectCode2;
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

	public Integer getTeachAssistStatus1() {
		return teachAssistStatus1;
	}

	public void setTeachAssistStatus1(Integer teachAssistStatus1) {
		this.teachAssistStatus1 = teachAssistStatus1;
	}

	public Integer getTeachAssistStatus2() {
		return teachAssistStatus2;
	}

	public void setTeachAssistStatus2(Integer teachAssistStatus2) {
		this.teachAssistStatus2 = teachAssistStatus2;
	}

}
