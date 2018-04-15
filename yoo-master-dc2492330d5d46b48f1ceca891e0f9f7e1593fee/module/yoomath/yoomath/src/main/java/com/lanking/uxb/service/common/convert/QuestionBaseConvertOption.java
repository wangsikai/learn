package com.lanking.uxb.service.common.convert;

/**
 * 基础题库转换VO
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月25日
 */
public class QuestionBaseConvertOption {

	private boolean initSub = true;
	private boolean isAnalysis = false;
	private boolean isAnswer = false;
	private boolean isCollect = false;
	private boolean initExamination = false;

	private boolean initTextbookCategory = true; // 初始化教材版本（兼容设置，默认true）
	private boolean initMetaKnowpoint = true; // 初始化旧知识点（兼容设置，默认true）
	private boolean initPhase = true; // 初始化阶段（兼容设置，默认true）
	private boolean initSubject = true; // 初始化学科（兼容设置，默认true）
	private boolean initQuestionType = true; // 初始化题目类型（兼容设置，默认true）
	private boolean initKnowledgePoint = true; // 初始化新知识点（兼容设置，默认true）
	private boolean initStudentQuestionCount = true; // 当前学生作对题数量及所有人做题情况（兼容设置，默认true）

	public QuestionBaseConvertOption() {
		super();
	}

	public QuestionBaseConvertOption(boolean initSub, boolean isAnalysis, boolean isAnswer, boolean isCollect) {
		super();
		this.initSub = initSub;
		this.isAnalysis = isAnalysis;
		this.isAnswer = isAnswer;
		this.isCollect = isCollect;
	}

	public QuestionBaseConvertOption(boolean initSub, boolean isAnalysis, boolean isAnswer, boolean isCollect,
			boolean initExamination) {
		super();
		this.initSub = initSub;
		this.isAnalysis = isAnalysis;
		this.isAnswer = isAnswer;
		this.isCollect = isCollect;
		this.initExamination = initExamination;
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

}
