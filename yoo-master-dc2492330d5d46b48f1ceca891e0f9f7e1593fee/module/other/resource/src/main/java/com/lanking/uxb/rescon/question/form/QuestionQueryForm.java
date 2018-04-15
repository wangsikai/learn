package com.lanking.uxb.rescon.question.form;

import java.util.List;

import com.lanking.cloud.domain.type.CheckStatus;

/**
 * 题目查询表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年8月15日
 */
public class QuestionQueryForm {

	/**
	 * 关键字查询类型 1：全部、2：知识点、3：内容、4：题号
	 */
	private Integer selectType = 0;

	/**
	 * 查询关键字.
	 */
	private String key;

	/**
	 * 阶段.
	 */
	private Integer phaseCode;

	/**
	 * 学科.
	 */
	private Integer subjectCode;

	/**
	 * 题型.
	 */
	private String type;

	/**
	 * 教材版本.
	 */
	private Integer categoryCode;

	/**
	 * 检测状态.
	 */
	private CheckStatus checkStatus;

	/**
	 * 难度范围.
	 */
	private String difficulty;

	/**
	 * 录入人员.
	 */
	private Long createId;

	/**
	 * 供应商.
	 */
	private Long vendorId;

	/**
	 * 录入时间范围.
	 */
	private Long createBt;
	private Long createEt;

	/**
	 * 校验时间范围.
	 */
	private Long verifyBt;
	private Long verifyEt;

	/**
	 * 排序类型. 1：创建时间，2：难度系数，3：校验时间
	 */
	private Integer sortType = 1;

	/**
	 * 排序方式. 0：正序，1：倒序.
	 */
	private Integer sort = 0;

	/**
	 * 知识点code.
	 */
	private Integer metaknowCode;

	/**
	 * 新知识点code.
	 */
	private Long knowledgePointCode;

	/**
	 * 同步知识点code.
	 */
	private Long knowledgeSyncCode;

	/**
	 * 复习知识点code.
	 */
	private Long knowledgeReviewCode;

	/**
	 * 考点code.
	 */
	private Long examinationPointCode;

	/**
	 * 题号.
	 */
	private String code;

	/**
	 * 来源.
	 */
	private String source;
	/**
	 * 校验员
	 */
	private Long verifyId;
	/**
	 * 章节码
	 */
	private Long sectionCode;
	/**
	 * 版本
	 */
	private Integer textbookCode;

	private Integer pageSize = 20;
	private Integer page = 1;

	/**
	 * 查询标记
	 * 0：题库、1：我的录题，2：我的校验，3：新旧知识点搜索(旧)，4：新旧知识点搜索(新)，5：新旧知识点搜索(同步)，6：新旧知识点搜索(复习)
	 */
	private Integer searchFlag = 0;

	private List<Integer> typeCodes; // 学科题型集合

	private List<Long> notHasQuestionIds; // 不包含的习题ID集合

	// 是否查询相似题
	private Boolean querySimilar = false;

	// @since 教师端v2.3.0 新的题目分类标签
	private List<Long> questionCategorys; // 分类
	private List<Long> questionTags; // 标签

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

	public Long getVerifyId() {
		return verifyId;
	}

	public void setVerifyId(Long verifyId) {
		this.verifyId = verifyId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public CheckStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CheckStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
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

	public Long getVerifyBt() {
		return verifyBt;
	}

	public void setVerifyBt(Long verifyBt) {
		this.verifyBt = verifyBt;
	}

	public Long getVerifyEt() {
		return verifyEt;
	}

	public void setVerifyEt(Long verifyEt) {
		this.verifyEt = verifyEt;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSelectType() {
		return selectType;
	}

	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}

	public Integer getMetaknowCode() {
		return metaknowCode;
	}

	public void setMetaknowCode(Integer metaknowCode) {
		this.metaknowCode = metaknowCode;
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

	public Long getCreateBt() {
		return createBt;
	}

	public void setCreateBt(Long createBt) {
		this.createBt = createBt;
	}

	public Long getCreateEt() {
		return createEt;
	}

	public void setCreateEt(Long createEt) {
		this.createEt = createEt;
	}

	public Integer getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(Integer searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Long getKnowledgePointCode() {
		return knowledgePointCode;
	}

	public void setKnowledgePointCode(Long knowledgePointCode) {
		this.knowledgePointCode = knowledgePointCode;
	}

	public Long getExaminationPointCode() {
		return examinationPointCode;
	}

	public void setExaminationPointCode(Long examinationPointCode) {
		this.examinationPointCode = examinationPointCode;
	}

	public List<Integer> getTypeCodes() {
		return typeCodes;
	}

	public void setTypeCodes(List<Integer> typeCodes) {
		this.typeCodes = typeCodes;
	}

	public List<Long> getNotHasQuestionIds() {
		return notHasQuestionIds;
	}

	public void setNotHasQuestionIds(List<Long> notHasQuestionIds) {
		this.notHasQuestionIds = notHasQuestionIds;
	}

	public Boolean getQuerySimilar() {
		return querySimilar;
	}

	public void setQuerySimilar(Boolean querySimilar) {
		this.querySimilar = querySimilar;
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

	public Long getKnowledgeSyncCode() {
		return knowledgeSyncCode;
	}

	public void setKnowledgeSyncCode(Long knowledgeSyncCode) {
		this.knowledgeSyncCode = knowledgeSyncCode;
	}

	public Long getKnowledgeReviewCode() {
		return knowledgeReviewCode;
	}

	public void setKnowledgeReviewCode(Long knowledgeReviewCode) {
		this.knowledgeReviewCode = knowledgeReviewCode;
	}

}
