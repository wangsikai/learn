package com.lanking.uxb.service.index.value;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 学生错题索引
 * 
 * @since yoomath V2.0
 * @author wangsenhao
 *
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.STUDENT_FALLIBLE_QUESTION)
public class StudentFallibleQuestionDoc extends AbstraceIndexDoc {

	private static final long serialVersionUID = -9071404539799188801L;

	@IndexMapping(type = MappingType.LONG)
	private Long id;

	@IndexMapping(type = MappingType.LONG)
	private Long questionId;

	@IndexMapping(type = MappingType.LONG)
	private Long studentId;

	@IndexMapping(type = MappingType.LONG)
	private Long createAt;

	@IndexMapping(type = MappingType.LONG)
	private Long updateAt;

	// 错题次数
	@IndexMapping(type = MappingType.LONG)
	private Long mistakeNum;

	// 知识点CODE集合(新)
	@IndexMapping(type = MappingType.LONG)
	private List<Long> knowpointCodes = Lists.newArrayList();

	// 知识点字串拼接(新).
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String knowpointnames;

	// 知识点CODE集合(旧)
	@IndexMapping(type = MappingType.INTEGER)
	private List<Integer> metaKnowpointCodes = Lists.newArrayList();

	// 知识点字串拼接(旧).
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String metaKnowpoints;

	// 教材章节(包含子节点的父节点).
	@IndexMapping(type = MappingType.LONG)
	private List<Long> sectionCodes;

	// 来源.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer latestSource;

	// 科目题型代码.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer typeCode;

	// 传统题型代码
	@IndexMapping(type = MappingType.INTEGER)
	private Integer type;

	// 难度.
	@IndexMapping(type = MappingType.DOUBLE)
	private BigDecimal difficulty;

	// 习题：（题干 + 选项 + 子题题干 + 子题选项）、流媒体：名称.
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String contents;

	// 状态
	@IndexMapping(type = MappingType.INTEGER)
	private Integer status;

	// 教材修改,题目一对多教材
	@IndexMapping(type = MappingType.LONG)
	private List<Long> textbookCodes;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCategoryCode;

	// 查询question_section,一个题目可能对应多个教材
	@IndexMapping(type = MappingType.LONG)
	private List<Long> textbookCategoryCodes;

	// ocr识别图片
	@IndexMapping(type = MappingType.LONG, ignore = true)
	private Long ocrImageId;

	// ocr识别后语义识别后的知识点
	@IndexMapping(type = MappingType.LONG, ignore = true)
	private List<Long> ocrKnowpointCodes = Lists.newArrayList();

	// ocr语义标签后归档的教材代码
	@IndexMapping(type = MappingType.INTEGER, ignore = true)
	private Integer ocrTextbookCode;

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

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Long updateAt) {
		this.updateAt = updateAt;
	}

	public Long getMistakeNum() {
		return mistakeNum;
	}

	public void setMistakeNum(Long mistakeNum) {
		this.mistakeNum = mistakeNum;
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

	public List<Integer> getMetaKnowpointCodes() {
		return metaKnowpointCodes;
	}

	public void setMetaKnowpointCodes(List<Integer> metaKnowpointCodes) {
		this.metaKnowpointCodes = metaKnowpointCodes;
	}

	public String getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(String metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Integer getLatestSource() {
		return latestSource;
	}

	public void setLatestSource(Integer latestSource) {
		this.latestSource = latestSource;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public List<Long> getTextbookCategoryCodes() {
		return textbookCategoryCodes;
	}

	public void setTextbookCategoryCodes(List<Long> textbookCategoryCodes) {
		this.textbookCategoryCodes = textbookCategoryCodes;
	}

	public Long getOcrImageId() {
		return ocrImageId;
	}

	public void setOcrImageId(Long ocrImageId) {
		this.ocrImageId = ocrImageId;
	}

	public List<Long> getOcrKnowpointCodes() {
		return ocrKnowpointCodes;
	}

	public void setOcrKnowpointCodes(List<Long> ocrKnowpointCodes) {
		this.ocrKnowpointCodes = ocrKnowpointCodes;
	}

	public Integer getOcrTextbookCode() {
		return ocrTextbookCode;
	}

	public void setOcrTextbookCode(Integer ocrTextbookCode) {
		this.ocrTextbookCode = ocrTextbookCode;
	}

}
