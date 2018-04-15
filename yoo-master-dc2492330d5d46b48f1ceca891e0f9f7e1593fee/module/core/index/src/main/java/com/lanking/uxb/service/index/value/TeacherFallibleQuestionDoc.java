package com.lanking.uxb.service.index.value;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 错题索引对象
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月10日 下午3:25:08
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.TEACHER_FALLIBLE_QUESTION)
public class TeacherFallibleQuestionDoc extends AbstraceIndexDoc {

	private static final long serialVersionUID = 5718125902987009590L;

	@IndexMapping(type = MappingType.LONG)
	private Long id;

	@IndexMapping(type = MappingType.LONG)
	private Long questionId;

	@IndexMapping(type = MappingType.LONG)
	private Long teacherId;

	@IndexMapping(type = MappingType.DOUBLE)
	private double rightRate;

	@IndexMapping(type = MappingType.LONG)
	private Long updateAt;

	// 教材章节(包含子节点的父节点).
	@IndexMapping(type = MappingType.LONG)
	private List<Long> sectionCodes;

	// 来源.
	@IndexMapping(type = MappingType.TEXT, ignore = true)
	private String source;

	// 科目题型代码.'
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

	@IndexMapping(type = MappingType.INTEGER)
	private Integer status;

	// 教材修改,题目一对多教材
	@IndexMapping(type = MappingType.LONG)
	private List<Long> textbookCodes;

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

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public double getRightRate() {
		return rightRate;
	}

	public void setRightRate(double rightRate) {
		this.rightRate = rightRate;
	}

	public Long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Long updateAt) {
		this.updateAt = updateAt;
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

}
