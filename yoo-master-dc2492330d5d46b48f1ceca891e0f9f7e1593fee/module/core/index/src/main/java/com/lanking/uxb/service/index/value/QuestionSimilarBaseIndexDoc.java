package com.lanking.uxb.service.index.value;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 相似题搜索使用的基本数据索引.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月19日
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.QUESTION_SIMILAR_BASE)
public class QuestionSimilarBaseIndexDoc extends AbstraceIndexDoc {
	private static final long serialVersionUID = -2072739084284222923L;

	// 题目ID
	@IndexMapping(type = MappingType.LONG)
	private Long questionId;

	// 阶段.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer phaseCode;

	// 科目.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode;

	// 题目类型
	@IndexMapping(type = MappingType.INTEGER)
	private Integer type;

	// 修剪过的题干
	@IndexMapping(type = MappingType.TEXT)
	private String content;

	// 题目状态
	@IndexMapping(type = MappingType.INTEGER)
	private Integer status;

	// 学校ID
	@IndexMapping(type = MappingType.LONG)
	private long schoolId;

	// VENDOR_ID
	@IndexMapping(type = MappingType.LONG)
	private Long vendorId;

	// ~新知识点CODE集合
	@IndexMapping(type = MappingType.LONG)
	private List<Long> knowledgePointCodes = Lists.newArrayList();

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public List<Long> getKnowledgePointCodes() {
		return knowledgePointCodes;
	}

	public void setKnowledgePointCodes(List<Long> knowledgePointCodes) {
		this.knowledgePointCodes = knowledgePointCodes;
	}
}
