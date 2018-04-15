package com.lanking.uxb.service.index.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 习题索引对象.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月27日
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.QUESTION)
public class QuestionIndexDoc extends ResourceDoc implements Serializable {
	private static final long serialVersionUID = -1773978169093933028L;

	// ~习题类型.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer type;

	// ~习题编码.
	@IndexMapping(type = MappingType.TEXT, index = "not_analyzed")
	private String code;

	// ~来源.
	@IndexMapping(type = MappingType.TEXT)
	private String source;

	// ~科目题型代码.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer typeCode;

	// ~难度.
	@IndexMapping(type = MappingType.DOUBLE)
	private BigDecimal difficulty;

	// ~知识点CODE集合
	@IndexMapping(type = MappingType.INTEGER)
	private List<Integer> metaKnowpointCodes = Lists.newArrayList();

	// ~新知识点CODE集合
	@IndexMapping(type = MappingType.LONG)
	private List<Long> knowledgePointCodes = Lists.newArrayList();

	// ~同步知识点CODE集合
	@IndexMapping(type = MappingType.LONG)
	private List<Long> knowledgeSyncCodes = Lists.newArrayList();

	// ~复习知识点CODE集合
	@IndexMapping(type = MappingType.LONG)
	private List<Long> knowledgeReviewCodes = Lists.newArrayList();

	// ~新考点CODE集合
	@IndexMapping(type = MappingType.LONG)
	private List<Long> examinationPointCodes = Lists.newArrayList();

	// ~教材章节(包含子节点的父节点).
	@IndexMapping(type = MappingType.LONG)
	private List<Long> sectionCodes;

	// ~教材修改,题目一对多教材
	@IndexMapping(type = MappingType.LONG)
	private List<Long> textbookCodes;

	// 2016-10-14 删除未用字段
	// 题目来源.
	// @IndexMapping(type = MappingType.INTEGER)
	// private Integer questionSource;

	// ~学校ID
	@IndexMapping(type = MappingType.LONG)
	private long schoolId;

	// 2016-10-14 删除未用字段
	// 知识点转换功能中添加知识点的人员（统计中不包含正常新增修改提交）
	// @IndexMapping(type = MappingType.LONG)
	// private Long knowledgeCreateId;

	// ~添加新知识点的时间戳.
	@IndexMapping(type = MappingType.LONG)
	private Long knowledgeCreateAt;

	// ~应用场景.
	// @IndexMapping(type = MappingType.INTEGER)
	// private Integer sceneCode;

	// ~题目的标签
	// @IndexMapping(type = MappingType.INTEGER)
	// private List<Integer> categoryTypes;

	// 2017-7-31 废除原应用场景、标签，添加新的题目分类及题目标签索引属性
	@IndexMapping(type = MappingType.LONG)
	private List<Long> questionCategorys;

	// 2017-7-31 废除原应用场景、标签，添加新的题目分类及题目标签索引属性
	@IndexMapping(type = MappingType.LONG)
	private List<Long> questionTags;

	// ~重复题展示状态.
	@IndexMapping(type = MappingType.BOOLEAN)
	private Boolean sameShow;

	// ~重复题关联展示题目的ID.
	@IndexMapping(type = MappingType.LONG)
	private Long sameShowId;

	// ~教材新章节(包含子节点的父节点).(新)
	@IndexMapping(type = MappingType.LONG)
	private List<Long> sectionCodes2;

	// ~教材修改,题目一对多教材(新)
	@IndexMapping(type = MappingType.LONG)
	private List<Long> textbookCodes2;

	/**
	 * ~是否习题的内容（题干、选项、分析、解析、答案）字符适应Katex的解析
	 * 
	 * @since 2017-10-19
	 */
	@IndexMapping(type = MappingType.BOOLEAN)
	private Boolean isKatexSpecs = true;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public List<Integer> getMetaKnowpointCodes() {
		return metaKnowpointCodes;
	}

	public void setMetaKnowpointCodes(List<Integer> metaKnowpointCodes) {
		this.metaKnowpointCodes = metaKnowpointCodes;
	}

	public List<Long> getKnowledgePointCodes() {
		return knowledgePointCodes;
	}

	public void setKnowledgePointCodes(List<Long> knowledgePointCodes) {
		this.knowledgePointCodes = knowledgePointCodes;
	}

	public List<Long> getExaminationPointCodes() {
		return examinationPointCodes;
	}

	public void setExaminationPointCodes(List<Long> examinationPointCodes) {
		this.examinationPointCodes = examinationPointCodes;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public List<Long> getTextbookCodes() {
		return textbookCodes;
	}

	public void setTextbookCodes(List<Long> textbookCodes) {
		this.textbookCodes = textbookCodes;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getKnowledgeCreateAt() {
		return knowledgeCreateAt;
	}

	public void setKnowledgeCreateAt(Long knowledgeCreateAt) {
		this.knowledgeCreateAt = knowledgeCreateAt;
	}

	public List<Long> getQuestionCategorys() {
		return questionCategorys;
	}

	public void setQuestionCategorys(List<Long> questionCategorys) {
		this.questionCategorys = questionCategorys;
	}

	public List<Long> getQuestionTags() {
		return questionTags;
	}

	public void setQuestionTags(List<Long> questionTags) {
		this.questionTags = questionTags;
	}

	public Boolean getSameShow() {
		return sameShow;
	}

	public void setSameShow(Boolean sameShow) {
		this.sameShow = sameShow;
	}

	public Long getSameShowId() {
		return sameShowId;
	}

	public void setSameShowId(Long sameShowId) {
		this.sameShowId = sameShowId;
	}

	public List<Long> getSectionCodes2() {
		return sectionCodes2;
	}

	public void setSectionCodes2(List<Long> sectionCodes2) {
		this.sectionCodes2 = sectionCodes2;
	}

	public List<Long> getTextbookCodes2() {
		return textbookCodes2;
	}

	public void setTextbookCodes2(List<Long> textbookCodes2) {
		this.textbookCodes2 = textbookCodes2;
	}

	public Boolean getIsKatexSpecs() {
		return isKatexSpecs;
	}

	public void setIsKatexSpecs(Boolean isKatexSpecs) {
		this.isKatexSpecs = isKatexSpecs;
	}

	public List<Long> getKnowledgeSyncCodes() {
		return knowledgeSyncCodes;
	}

	public void setKnowledgeSyncCodes(List<Long> knowledgeSyncCodes) {
		this.knowledgeSyncCodes = knowledgeSyncCodes;
	}

	public List<Long> getKnowledgeReviewCodes() {
		return knowledgeReviewCodes;
	}

	public void setKnowledgeReviewCodes(List<Long> knowledgeReviewCodes) {
		this.knowledgeReviewCodes = knowledgeReviewCodes;
	}

}
