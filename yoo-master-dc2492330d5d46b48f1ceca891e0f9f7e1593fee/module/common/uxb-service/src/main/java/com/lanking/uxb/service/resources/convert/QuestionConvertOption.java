package com.lanking.uxb.service.resources.convert;

/**
 * 题目VO转换选项.
 * 
 * @since yoomath V 1.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月9日
 */
public class QuestionConvertOption {

	private boolean initSub = false;
	private boolean isAnalysis = false; // 分析
	private boolean isAnswer = false;
	private boolean isCollect = false; // 题目收藏
	// since 2.3.0 是否初始化转换题目对应的考点
	private boolean initExamination = false;
	private Long studentHomeworkId;

	// since 学生端v1.4.3 添加配置项 2017-5-24

	private boolean initTextbookCategory = true; // 初始化教材版本（兼容设置，默认true）
	private boolean initMetaKnowpoint = true; // 初始化旧知识点（兼容设置，默认true）
	private boolean initPhase = true; // 初始化阶段（兼容设置，默认true）
	private boolean initSubject = true; // 初始化学科（兼容设置，默认true）
	private boolean initQuestionType = true; // 初始化题目类型（兼容设置，默认true）
	private boolean initKnowledgePoint = true; // 初始化新知识点（兼容设置，默认true）
	private boolean initStudentQuestionCount = true; // 当前学生作对题数量及所有人做题情况（兼容设置，默认true）

	// since 教师端v1.3.0 添加配置项 2017-7-3
	private boolean initPublishCount = false; // 布置过作业次数
	private boolean initQuestionSimilarCount = false; // 相似题数量
	// since 教师端v1.3.0 添加配置项 2017-7-24
	private boolean initQuestionTag = false; // 标签

	public QuestionConvertOption() {
		super();
	}

	public QuestionConvertOption(boolean initSub, boolean isAnalysis, boolean isAnswer, Long studentHomeworkId) {
		super();
		this.initSub = initSub;
		this.isAnalysis = isAnalysis;
		this.isAnswer = isAnswer;
		this.studentHomeworkId = studentHomeworkId;
	}

	public QuestionConvertOption(boolean initSub, boolean isAnalysis, boolean isAnswer, boolean isCollect,
			Long studentHomeworkId) {
		super();
		this.initSub = initSub;
		this.isAnalysis = isAnalysis;
		this.isAnswer = isAnswer;
		this.isCollect = isCollect;
		this.studentHomeworkId = studentHomeworkId;
	}

	public QuestionConvertOption(boolean initSub, boolean isAnalysis, boolean isAnswer, boolean isCollect,
			boolean initExamination, Long studentHomeworkId) {
		this.initSub = initSub;
		this.isAnalysis = isAnalysis;
		this.isAnswer = isAnswer;
		this.isCollect = isCollect;
		this.initExamination = initExamination;
		this.studentHomeworkId = studentHomeworkId;
	}

	public boolean isInitSub() {
		return initSub;
	}

	public void setInitSub(boolean initSub) {
		this.initSub = initSub;
	}

	public boolean isAnalysis() {
		return isAnalysis;
	}

	public void setAnalysis(boolean isAnalysis) {
		this.isAnalysis = isAnalysis;
	}

	public boolean isAnswer() {
		return isAnswer;
	}

	public void setAnswer(boolean isAnswer) {
		this.isAnswer = isAnswer;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}

	public Long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(Long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public boolean isInitExamination() {
		return initExamination;
	}

	public void setInitExamination(boolean initExamination) {
		this.initExamination = initExamination;
	}

	public boolean isInitTextbookCategory() {
		return initTextbookCategory;
	}

	public void setInitTextbookCategory(boolean initTextbookCategory) {
		this.initTextbookCategory = initTextbookCategory;
	}

	public boolean isInitMetaKnowpoint() {
		return initMetaKnowpoint;
	}

	public void setInitMetaKnowpoint(boolean initMetaKnowpoint) {
		this.initMetaKnowpoint = initMetaKnowpoint;
	}

	public boolean isInitPhase() {
		return initPhase;
	}

	public void setInitPhase(boolean initPhase) {
		this.initPhase = initPhase;
	}

	public boolean isInitSubject() {
		return initSubject;
	}

	public void setInitSubject(boolean initSubject) {
		this.initSubject = initSubject;
	}

	public boolean isInitQuestionType() {
		return initQuestionType;
	}

	public void setInitQuestionType(boolean initQuestionType) {
		this.initQuestionType = initQuestionType;
	}

	public boolean isInitKnowledgePoint() {
		return initKnowledgePoint;
	}

	public void setInitKnowledgePoint(boolean initKnowledgePoint) {
		this.initKnowledgePoint = initKnowledgePoint;
	}

	public boolean isInitStudentQuestionCount() {
		return initStudentQuestionCount;
	}

	public void setInitStudentQuestionCount(boolean initStudentQuestionCount) {
		this.initStudentQuestionCount = initStudentQuestionCount;
	}

	public boolean isInitPublishCount() {
		return initPublishCount;
	}

	public void setInitPublishCount(boolean initPublishCount) {
		this.initPublishCount = initPublishCount;
	}

	public boolean isInitQuestionSimilarCount() {
		return initQuestionSimilarCount;
	}

	public void setInitQuestionSimilarCount(boolean initQuestionSimilarCount) {
		this.initQuestionSimilarCount = initQuestionSimilarCount;
	}

	public boolean isInitQuestionTag() {
		return initQuestionTag;
	}

	public void setInitQuestionTag(boolean initQuestionTag) {
		this.initQuestionTag = initQuestionTag;
	}

}
