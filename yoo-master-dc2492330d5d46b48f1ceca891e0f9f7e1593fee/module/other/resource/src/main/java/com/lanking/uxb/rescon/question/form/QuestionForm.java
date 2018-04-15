package com.lanking.uxb.rescon.question.form;

import java.util.List;

import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;

/**
 * 习题表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月5日
 */
public class QuestionForm {

	private Long id; // 问题ID
	private Integer typeCode; // 科目题型
	private Question.Type type; // 基本类型
	private Integer textbookCategoryCode; // 教材分类
	private Integer textbookCode; // 版本
	private Integer subjectCode; // 科目代码
	private Integer phaseCode; // 阶段代码
	private String content; // 题干
	private Double difficulty; // 难度系数
	private String analysis; // 题目解析
	private String hint; // 提示
	private String source; // 来源
	private Integer answerNumber; // 答案数量

	private List<String> choices; // 选择题选项
	private List<String> answers; // 对应顺序的答案
	private List<String> latexAnswers; // 对应顺序的latex答案

	private List<QuestionForm> children; // 子题

	private Long catalogId; // 商品目录
	private Long productId; // 商品
	private Long vendorId; // 供应商

	private List<Integer> metaCodes; // 知识点
	private String codePrefix; // 编号前缀
	private Long sectionCode; // 教材章节节点

	private CheckStatus status;

	private Integer checkFlag = 0; // 校验修改标记
	private Integer errorFlag = 0; // 纠错修改标记
	private ChoiceFormat choiceFormat;

	private Long exampaperId; // 试卷ID

	private Boolean openAnswerFlag = false;

	private List<Long> knowledgePointCodes; // 新知识点
	private List<Long> examinationPointCodes; // 考点
	// private Integer questionSceneCode; // 类别

	private List<Long> questionCategorys; // 分类
	private List<Long> questionTags; // 标签

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Question.Type getType() {
		return type;
	}

	public void setType(Question.Type type) {
		this.type = type;
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

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Integer getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(Integer answerNumber) {
		this.answerNumber = answerNumber;
	}

	public List<String> getChoices() {
		return choices;
	}

	public void setChoices(List<String> choices) {
		this.choices = choices;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public List<QuestionForm> getChildren() {
		return children;
	}

	public void setChildren(List<QuestionForm> children) {
		this.children = children;
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public List<Integer> getMetaCodes() {
		return metaCodes;
	}

	public void setMetaCodes(List<Integer> metaCodes) {
		this.metaCodes = metaCodes;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public CheckStatus getStatus() {
		return status;
	}

	public void setStatus(CheckStatus status) {
		this.status = status;
	}

	public Integer getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}

	public Integer getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(Integer errorFlag) {
		this.errorFlag = errorFlag;
	}

	public ChoiceFormat getChoiceFormat() {
		return choiceFormat;
	}

	public void setChoiceFormat(ChoiceFormat choiceFormat) {
		this.choiceFormat = choiceFormat;
	}

	public Long getExampaperId() {
		return exampaperId;
	}

	public void setExampaperId(Long exampaperId) {
		this.exampaperId = exampaperId;
	}

	public List<String> getLatexAnswers() {
		return latexAnswers;
	}

	public void setLatexAnswers(List<String> latexAnswers) {
		this.latexAnswers = latexAnswers;
	}

	public Boolean getOpenAnswerFlag() {
		return openAnswerFlag;
	}

	public void setOpenAnswerFlag(Boolean openAnswerFlag) {
		this.openAnswerFlag = openAnswerFlag;
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
}
