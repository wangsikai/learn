package com.lanking.uxb.service.index.value;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 学校题库索引
 * 
 * @since yoomath V1.4.2
 * @author wangsenhao
 *
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.SCHOOL_QUESTION)
public class SchoolQuestionDoc extends AbstraceIndexDoc {

	private static final long serialVersionUID = -5438217690964586962L;

	@IndexMapping(type = MappingType.LONG)
	private Long id;

	@IndexMapping(type = MappingType.LONG)
	private Long questionId;

	@IndexMapping(type = MappingType.LONG)
	private Long schoolId;

	@IndexMapping(type = MappingType.LONG)
	private Long createAt;

	// 教材章节(包含子节点的父节点).
	@IndexMapping(type = MappingType.LONG)
	private List<Long> sectionCodes;

	// 来源
	@IndexMapping(ignore = true, type = MappingType.TEXT)
	private String source;

	// 科目题型代码.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer typeCode;

	// ~应用场景.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer sceneCode;

	// 传统题型代码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer type;

	// 难度.
	@IndexMapping(type = MappingType.DOUBLE)
	private BigDecimal difficulty;

	// 习题：（题干 + 选项 + 子题题干 + 子题选项）、流媒体：名称.
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String contents;

	// 知识点CODE集合(旧)
	@IndexMapping(type = MappingType.INTEGER)
	private List<Integer> metaKnowpointCodes = Lists.newArrayList();

	// 知识点字串拼接.(旧)
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String metaKnowpoints;

	// 知识点CODE集合(新)
	@IndexMapping(type = MappingType.LONG)
	private List<Long> knowpointCodes = Lists.newArrayList();

	// 知识点字串拼接(新).
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String knowpointnames;

	// 教材修改,题目一对多教材
	@IndexMapping(type = MappingType.LONG)
	private List<Long> textbookCodes;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCategoryCode;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public List<Integer> getMetaKnowpointCodes() {
		return metaKnowpointCodes;
	}

	public void setMetaKnowpointCodes(List<Integer> metaKnowpointCodes) {
		this.metaKnowpointCodes = metaKnowpointCodes;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(String metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public List<Long> getTextbookCodes() {
		return textbookCodes;
	}

	public void setTextbookCodes(List<Long> textbookCodes) {
		this.textbookCodes = textbookCodes;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public List<Long> getKnowpointCodes() {
		return knowpointCodes;
	}

	public void setKnowpointCodes(List<Long> knowpointCodes) {
		this.knowpointCodes = knowpointCodes;
	}

	public String getKnowpointnames() {
		return knowpointnames;
	}

	public void setKnowpointnames(String knowpointnames) {
		this.knowpointnames = knowpointnames;
	}

	public Integer getSceneCode() {
		return sceneCode;
	}

	public void setSceneCode(Integer sceneCode) {
		this.sceneCode = sceneCode;
	}

}
