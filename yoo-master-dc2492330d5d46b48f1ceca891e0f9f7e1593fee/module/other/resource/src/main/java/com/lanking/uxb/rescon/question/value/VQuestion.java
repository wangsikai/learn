package com.lanking.uxb.rescon.question.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.type.AsciiStatus;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.basedata.value.VMetaKnowpoint;
import com.lanking.uxb.rescon.basedata.value.VResconExaminationPoint;
import com.lanking.uxb.rescon.basedata.value.VResconKnowledgeReview;
import com.lanking.uxb.rescon.basedata.value.VResconKnowledgeSync;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * 习题VO
 * 
 */
public class VQuestion implements Serializable {
	private static final long serialVersionUID = -8387039374614571826L;

	private long id;

	private VPhase phase; // 阶段
	private VSubject subject; // 科目
	private VQuestionType questionType; // 科目题型
	private VTextbookCategory textbookCategory; // 教材分类
	private VTextbook textbook; // 版本
	private VSection section; // 章节
	private Type type;

	private String content;
	private Double difficulty;
	private String hint;

	private boolean subFlag;
	private int sequence;
	private long parentId;
	private List<VQuestion> children = Lists.newArrayList();

	private List<String> choices;

	private int answerNumber;

	private String analysis;
	private String source;
	private String sourceTextbook; // 校验时查看的书本章节来源
	private List<VAnswer> answers = Lists.newArrayList();

	private CheckStatus checkStatus;
	private List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();
	private List<VKnowledgePoint> knowledgePoints = Lists.newArrayList(); // 新知识点
	private List<VResconExaminationPoint> examinationPoints = Lists.newArrayList(); // 考点
	private List<VResconKnowledgeReview> knowledgeReviews = Lists.newArrayList(); // 知识点V3-复习知识点
	private List<VResconKnowledgeSync> knowledgeSyncs = Lists.newArrayList(); // 知识点V3-同步知识点

	private Long createId;
	private Long verifyId;
	private Long verify2Id;

	private Date createAt;
	private Date updateAt;
	private Date verifyAt;
	private Date verify2At;
	private String code;

	// 选择题选项排版方式
	private ChoiceFormat choiceFormat;

	private QuestionSource questionSource;

	private Integer score;

	/**
	 * 未通过的理由.
	 */
	private String nopassContent;

	/**
	 * 未通过的图片;
	 */
	private List<String> nopassImages;

	/**
	 * 校本题目.
	 */
	private VSchool school;

	/**
	 * 是否为开放性答案习题.
	 */
	private boolean openAnswerFlag;

	/**
	 * 转换状态
	 */
	private AsciiStatus asciiStatus = AsciiStatus.NOCHANGE;

	/**
	 * 应用场景.
	 */
	// private QuestionScene scene;

	/**
	 * 习题分类.
	 */
	private List<VQuestionCategory> questionCategorys;

	/**
	 * 习题标签.
	 */
	private List<VQuestionTag> questionTags;

	/**
	 * @since 2.1.0 标签数据
	 */
	private List<String> ctypes;

	/**
	 * 是否包含相似题.
	 * 
	 * @since 2.5.0
	 */
	private boolean hasSimilar;

	/**
	 * 重复题展示状态.
	 * 
	 * @since 2.5.0
	 */
	private Boolean sameShow;

	/**
	 * 重复题关联展示题目的ID.
	 * 
	 * @since 2.5.0
	 */
	private Long sameShowId;

	/**
	 * 是否一校重申.
	 */
	private boolean checkRefund;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
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

	public List<VQuestion> getChildren() {
		return children;
	}

	public void setChildren(List<VQuestion> children) {
		this.children = children;
	}

	public List<String> getChoices() {
		return choices;
	}

	public void setChoices(List<String> choices) {
		this.choices = choices;
	}

	public int getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(int answerNumber) {
		this.answerNumber = answerNumber;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public List<VAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<VAnswer> answers) {
		this.answers = answers;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public VSubject getSubject() {
		return subject;
	}

	public void setSubject(VSubject subject) {
		this.subject = subject;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public VTextbook getTextbook() {
		return textbook;
	}

	public void setTextbook(VTextbook textbook) {
		this.textbook = textbook;
	}

	public VSection getSection() {
		return section;
	}

	public void setSection(VSection section) {
		this.section = section;
	}

	public Date getVerifyAt() {
		return verifyAt;
	}

	public void setVerifyAt(Date verifyAt) {
		this.verifyAt = verifyAt;
	}

	public Date getVerify2At() {
		return verify2At;
	}

	public void setVerify2At(Date verify2At) {
		this.verify2At = verify2At;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getVerifyId() {
		return verifyId;
	}

	public void setVerifyId(Long verifyId) {
		this.verifyId = verifyId;
	}

	public Long getVerify2Id() {
		return verify2Id;
	}

	public void setVerify2Id(Long verify2Id) {
		this.verify2Id = verify2Id;
	}

	public ChoiceFormat getChoiceFormat() {
		return choiceFormat;
	}

	public void setChoiceFormat(ChoiceFormat choiceFormat) {
		this.choiceFormat = choiceFormat;
	}

	public QuestionSource getQuestionSource() {
		return questionSource;
	}

	public void setQuestionSource(QuestionSource questionSource) {
		this.questionSource = questionSource;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getNopassContent() {
		return nopassContent;
	}

	public void setNopassContent(String nopassContent) {
		this.nopassContent = nopassContent;
	}

	public List<String> getNopassImages() {
		return nopassImages;
	}

	public void setNopassImages(List<String> nopassImages) {
		this.nopassImages = nopassImages;
	}

	public VSchool getSchool() {
		return school;
	}

	public void setSchool(VSchool school) {
		this.school = school;
	}

	public boolean isOpenAnswerFlag() {
		return openAnswerFlag;
	}

	public void setOpenAnswerFlag(boolean openAnswerFlag) {
		this.openAnswerFlag = openAnswerFlag;
	}

	public AsciiStatus getAsciiStatus() {
		return asciiStatus;
	}

	public void setAsciiStatus(AsciiStatus asciiStatus) {
		this.asciiStatus = asciiStatus;
	}

	public String getSourceTextbook() {
		return sourceTextbook;
	}

	public void setSourceTextbook(String sourceTextbook) {
		this.sourceTextbook = sourceTextbook;
	}

	public List<VKnowledgePoint> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<VKnowledgePoint> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public List<VResconExaminationPoint> getExaminationPoints() {
		return examinationPoints;
	}

	public void setExaminationPoints(List<VResconExaminationPoint> examinationPoints) {
		this.examinationPoints = examinationPoints;
	}

	// public QuestionScene getScene() {
	// return scene;
	// }
	//
	// public void setScene(QuestionScene scene) {
	// this.scene = scene;
	// }

	public List<String> getCtypes() {
		return ctypes;
	}

	public List<VQuestionCategory> getQuestionCategorys() {
		return questionCategorys;
	}

	public void setQuestionCategorys(List<VQuestionCategory> questionCategorys) {
		this.questionCategorys = questionCategorys;
	}

	public List<VQuestionTag> getQuestionTags() {
		return questionTags;
	}

	public void setQuestionTags(List<VQuestionTag> questionTags) {
		this.questionTags = questionTags;
	}

	public void setCtypes(List<String> ctypes) {
		this.ctypes = ctypes;
	}

	public boolean isHasSimilar() {
		return hasSimilar;
	}

	public void setHasSimilar(boolean hasSimilar) {
		this.hasSimilar = hasSimilar;
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

	public boolean isCheckRefund() {
		return checkRefund;
	}

	public void setCheckRefund(boolean checkRefund) {
		this.checkRefund = checkRefund;
	}

	public List<VResconKnowledgeReview> getKnowledgeReviews() {
		return knowledgeReviews;
	}

	public void setKnowledgeReviews(List<VResconKnowledgeReview> knowledgeReviews) {
		this.knowledgeReviews = knowledgeReviews;
	}

	public List<VResconKnowledgeSync> getKnowledgeSyncs() {
		return knowledgeSyncs;
	}

	public void setKnowledgeSyncs(List<VResconKnowledgeSync> knowledgeSyncs) {
		this.knowledgeSyncs = knowledgeSyncs;
	}

}
