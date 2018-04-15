package com.lanking.uxb.service.common.value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.uxb.service.code.value.VExaminationPoint;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.resources.value.VQuestionType;

/**
 * 作业题目公共VO
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月25日
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VQuestionBase implements Serializable {

	private static final long serialVersionUID = 2902569632608775322L;

	// ID
	private long id;
	// 编号
	private String code;
	// 基础题型
	private Type type;
	// 科目题型
	private VQuestionType questionType;
	// 版本
	private VTextbookCategory textbookCategory;
	// 科目
	private VSubject subject;
	// 阶段
	private VPhase phase;
	// 题干
	private String content;
	// 难度
	private Double difficulty;
	// 解析
	private String analysis;
	// 提示
	private String hint;
	// 来源
	private String source;
	// 是否是子题
	private boolean subFlag;
	// 序号
	private int sequence;
	// 主题ID
	private long parentId;
	// 子题列表
	private List<VQuestionBase> children = Lists.newArrayList();
	// 选项列表
	private List<String> choices;
	// 选择题选项排版方式
	private ChoiceFormat choiceFormat;
	// 答案数量
	private int answerNumber;
	// 标准答案列表
	private List<VAnswer> answers = Lists.newArrayList();

	// 所有解析(包括子题的)
	private List<String> allAnalysis;
	// 所有提示(包括子题的)
	private List<String> allHints;

	// 题目状态
	private CheckStatus checkStatus;
	// 知识点列表
	private List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();
	// 知识点拼接的字符串
	private String allMetaKnowpoint;

	// @since 3.9.0 新知识点
	private List<VKnowledgePoint> newKnowpoints = Lists.newArrayList();
	// @since 3.9.0 考点
	private List<VExaminationPoint> examinationPoints;

	// 当前用户是否收藏
	private Boolean isCollect = false;

	/**
	 * 题目标签
	 *
	 * @since 3.0.1
	 */
	private List<QuestionCategoryType> categoryTypes;

	// 存放临时变量
	@JSONField(serialize = false)
	private Map<String, Long> transientIds;
	@JSONField(serialize = false)
	private Map<String, Boolean> transientBooleans;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public VQuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(VQuestionType questionType) {
		this.questionType = questionType;
	}

	public VTextbookCategory getTextbookCategory() {
		return textbookCategory;
	}

	public void setTextbookCategory(VTextbookCategory textbookCategory) {
		this.textbookCategory = textbookCategory;
	}

	public VSubject getSubject() {
		return subject;
	}

	public void setSubject(VSubject subject) {
		this.subject = subject;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isSubFlag() {
		return subFlag;
	}

	public void setSubFlag(boolean subFlag) {
		this.subFlag = subFlag;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<VQuestionBase> getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public List<String> getChoices() {
		return choices;
	}

	public void setChoices(List<String> choices) {
		this.choices = choices;
	}

	public ChoiceFormat getChoiceFormat() {
		return choiceFormat;
	}

	public void setChoiceFormat(ChoiceFormat choiceFormat) {
		this.choiceFormat = choiceFormat;
	}

	public int getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(int answerNumber) {
		this.answerNumber = answerNumber;
	}

	public List<VAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<VAnswer> answers) {
		this.answers = answers;
	}

	public List<String> getAllAnalysis() {
		return allAnalysis;
	}

	public void setAllAnalysis(List<String> allAnalysis) {
		this.allAnalysis = allAnalysis;
	}

	public List<String> getAllHints() {
		return allHints;
	}

	public void setAllHints(List<String> allHints) {
		this.allHints = allHints;
	}

	public CheckStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CheckStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public List<VMetaKnowpoint> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<VMetaKnowpoint> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public String getAllMetaKnowpoint() {
		return allMetaKnowpoint;
	}

	public void setAllMetaKnowpoint(String allMetaKnowpoint) {
		this.allMetaKnowpoint = allMetaKnowpoint;
	}

	public Boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Boolean isCollect) {
		this.isCollect = isCollect;
	}

	public Map<String, Long> getTransientIds() {
		return transientIds;
	}

	public void setTransientIds(Map<String, Long> transientIds) {
		this.transientIds = transientIds;
	}

	public void putTransientId(String key, Long value) {
		if (this.transientIds == null) {
			this.transientIds = new HashMap<String, Long>();
		}
		this.transientIds.put(key, value);
	}

	public Long getTransientId(String key) {
		if (this.transientIds == null) {
			return null;
		}
		return this.transientIds.get(key);
	}

	public Map<String, Boolean> getTransientBooleans() {
		return transientBooleans;
	}

	public void setTransientBooleans(Map<String, Boolean> transientBooleans) {
		this.transientBooleans = transientBooleans;
	}

	public void putTransientBoolean(String key, Boolean value) {
		if (this.transientBooleans == null) {
			this.transientBooleans = new HashMap<String, Boolean>();
		}
		this.transientBooleans.put(key, value);
	}

	public Boolean getTransientBoolean(String key) {
		if (this.transientBooleans == null) {
			return null;
		}
		return this.transientBooleans.get(key);
	}

	public List<QuestionCategoryType> getCategoryTypes() {
		return categoryTypes;
	}

	public void setCategoryTypes(List<QuestionCategoryType> categoryTypes) {
		this.categoryTypes = categoryTypes;
	}

	public List<VKnowledgePoint> getNewKnowpoints() {
		return newKnowpoints;
	}

	public void setNewKnowpoints(List<VKnowledgePoint> newKnowpoints) {
		this.newKnowpoints = newKnowpoints;
	}

	public List<VExaminationPoint> getExaminationPoints() {
		return examinationPoints;
	}

	public void setExaminationPoints(List<VExaminationPoint> examinationPoints) {
		this.examinationPoints = examinationPoints;
	}
}
