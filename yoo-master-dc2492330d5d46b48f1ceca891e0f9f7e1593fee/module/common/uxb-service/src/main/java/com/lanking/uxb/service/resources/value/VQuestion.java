package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VExaminationPoint;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * 习题VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public class VQuestion implements Serializable {

	private static final long serialVersionUID = 889812161270297578L;

	private long id;

	private VPhase phase; // 阶段
	private VSubject subject; // 科目
	private VQuestionType questionType; // 科目题型
	private VTextbookCategory textbookCategory; // 教材分类
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
	private List<VAnswer> answers = Lists.newArrayList();
	private List<VStudentHomeworkAnswer> studentHomeworkAnswers = Lists.newArrayList();
	//订正答案
	private List<VStudentHomeworkAnswer> correctStudentHomeworkAnswers = Lists.newArrayList();
	private VStudentHomeworkQuestion studentHomeworkQuestion;
	//订正问题
	private VStudentHomeworkQuestion correctStudentHomeworkQuestion;
	private List<String> allHints;
	private List<String> allAnalysis;
	private List<List<String>> allAnswers;

	private CheckStatus checkStatus;
	private List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();
	private String allMetaKnowpoint;
	/**
	 * 新知识点 yoomath 2.0.7
	 */
	private List<VKnowledgePoint> newKnowpoints = Lists.newArrayList();
	private String allNewKnowpoint;

	private Date createAt;
	private Date updateAt;
	private String code;
	private Integer subjectCode;

	// 是否收藏,yoomath V1.3
	private Boolean isCollect = false;
	// 是否答了这道题,yoomath mobile V1.1.0
	private Boolean done;

	// 选择题选项排版方式
	private ChoiceFormat choiceFormat;
	// 是否是订正题
	private boolean correctQuestion = false;
	// 题目录入源
	private QuestionSource questionSource;

	// @since 2.3.0 考点列表
	private List<VExaminationPoint> examinationPoints;
	private String allExamination;

	// @since 2.3.0 题目标签
	private List<QuestionCategoryType> categoryTypes;

	// 判断当前错题在错题本里 since 2.4.0
	private boolean inStuFallQuestion = false;

	/**
	 * 是否在作业篮子中
	 *
	 * @since 3.0.1
	 */
	private boolean inQuestionCar = false;

	// @since 3.9.0 学生此题做过的次数
	private Long doCount;
	// @since 3.9.0 此题学生历史正确率
	private Double questionRightRate;
	// @since 3.9.0 做错错误人数
	private Long wrongPeopleCount;

	// 作业布置过次数 @since sprint72
	private Long publishCount;
	// 相似题数量 @since sprint72
	private Long questionSimilarCount;
	// 题目标签 @since sprint72
	private List<VQuestionTag> questionTags = Lists.newArrayList();

	// 习题难度类型
	private DifficultType difficultType;
	
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
		if (difficulty != null) {
			this.difficultType = DifficultType.findByDifficulty(difficulty);
		}
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

	public List<VStudentHomeworkAnswer> getStudentHomeworkAnswers() {
		return studentHomeworkAnswers;
	}

	public void setStudentHomeworkAnswers(List<VStudentHomeworkAnswer> studentHomeworkAnswers) {
		this.studentHomeworkAnswers = studentHomeworkAnswers;
	}

	public VStudentHomeworkQuestion getStudentHomeworkQuestion() {
		return studentHomeworkQuestion;
	}

	public void setStudentHomeworkQuestion(VStudentHomeworkQuestion studentHomeworkQuestion) {
		this.studentHomeworkQuestion = studentHomeworkQuestion;
	}

	public List<String> getAllAnalysis() {
		if (allAnalysis == null) {
			allAnalysis = Lists.newArrayList();
			allAnalysis.add(StringUtils.isBlank(analysis) ? StringUtils.EMPTY : getAnalysis());
			if (getChildren() != null) {
				for (VQuestion vq : getChildren()) {
					allAnalysis.add(StringUtils.isBlank(vq.getAnalysis()) ? StringUtils.EMPTY : vq.getAnalysis());
				}
			}
		}
		return allAnalysis;
	}

	public void setAllAnalysis(List<String> allAnalysis) {
		this.allAnalysis = allAnalysis;
	}

	public List<String> getAllHints() {
		if (allHints == null) {
			allHints = Lists.newArrayList();
			allHints.add(StringUtils.isBlank(hint) ? StringUtils.EMPTY : getHint());
			if (getChildren() != null) {
				for (VQuestion vq : getChildren()) {
					allHints.add(StringUtils.isBlank(vq.getHint()) ? StringUtils.EMPTY : vq.getHint());
				}
			}
		}
		return allHints;
	}

	public void setAllHints(List<String> allHints) {
		this.allHints = allHints;
	}

	public List<List<String>> getAllAnswers() {
		if (allAnswers == null) {
			allAnswers = Lists.newArrayList();
			List<String> pas = Lists.newArrayList();
			List<VAnswer> as = getAnswers();
			if (as != null) {
				for (VAnswer v : as) {
					if (type == Question.Type.TRUE_OR_FALSE) {
						if (v.getContent().trim().equals("1")) {
							pas.add("正确");
						} else if (v.getContent().trim().equals("0")) {
							pas.add("错误");
						} else {
							pas.add(v.getContent());
						}
					} else {
						pas.add(v.getContent());
					}
				}
			}
			allAnswers.add(pas);
			List<VQuestion> vqs = getChildren();
			if (vqs != null) {
				for (VQuestion vq : vqs) {
					List<String> cas = Lists.newArrayList();
					List<VAnswer> cvs = vq.getAnswers();
					if (cvs != null) {
						for (VAnswer v : cvs) {
							if (type == Question.Type.TRUE_OR_FALSE) {
								if (v.getContent().trim().equals("1")) {
									cas.add("正确");
								} else if (v.getContent().trim().equals("0")) {
									cas.add("错误");
								} else {
									cas.add(v.getContent());
								}
							} else {
								cas.add(v.getContent());
							}
						}
					}
					allAnswers.add(cas);
				}
			}
		}
		return allAnswers;
	}

	public void setAllAnswers(List<List<String>> allAnswers) {
		this.allAnswers = allAnswers;
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

	public String getAllMetaKnowpoint() {
		if (allMetaKnowpoint == null) {
			String s = "";
			for (VMetaKnowpoint v : metaKnowpoints) {
				s += v.getName() + ",";
			}
			if (StringUtils.isNotBlank(s)) {
				s = s.substring(0, s.length() - 1);
			}
			setAllMetaKnowpoint(s);
		}
		return allMetaKnowpoint;
	}

	public void setAllMetaKnowpoint(String allMetaKnowpoint) {
		this.allMetaKnowpoint = allMetaKnowpoint;
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

	public Boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Boolean isCollect) {
		this.isCollect = isCollect;
	}

	public Boolean isDone() {
		if (done == null) {
			if (studentHomeworkAnswers.size() > 0) {
				boolean $done = false;
				for (VStudentHomeworkAnswer sa : studentHomeworkAnswers) {
					if (StringUtils.isNotBlank(sa.getContent())) {
						$done = true;
						break;
					}
				}
				setDone($done);
			} else {
				setDone(false);
			}
		}
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public ChoiceFormat getChoiceFormat() {
		return choiceFormat;
	}

	public void setChoiceFormat(ChoiceFormat choiceFormat) {
		this.choiceFormat = choiceFormat;
	}

	public boolean isCorrectQuestion() {
		return correctQuestion;
	}

	public void setCorrectQuestion(boolean correctQuestion) {
		this.correctQuestion = correctQuestion;
	}

	public QuestionSource getQuestionSource() {
		return questionSource;
	}

	public void setQuestionSource(QuestionSource questionSource) {
		this.questionSource = questionSource;
	}

	public List<VExaminationPoint> getExaminationPoints() {
		return examinationPoints;
	}

	public void setExaminationPoints(List<VExaminationPoint> examinationPoints) {
		this.examinationPoints = examinationPoints;
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

	public String getAllNewKnowpoint() {
		if (allNewKnowpoint == null) {
			String s = "";
			for (VKnowledgePoint v : newKnowpoints) {
				s += v.getName() + "、";
			}
			if (StringUtils.isNotBlank(s)) {
				s = s.substring(0, s.length() - 1);
			}
			setAllNewKnowpoint(s);
		}
		return allNewKnowpoint;
	}

	public void setAllNewKnowpoint(String allNewKnowpoint) {
		this.allNewKnowpoint = allNewKnowpoint;
	}

	public boolean isInStuFallQuestion() {
		return inStuFallQuestion;
	}

	public void setInStuFallQuestion(boolean inStuFallQuestion) {
		this.inStuFallQuestion = inStuFallQuestion;
	}

	public boolean isInQuestionCar() {
		return inQuestionCar;
	}

	public void setInQuestionCar(boolean inQuestionCar) {
		this.inQuestionCar = inQuestionCar;
	}

	public String getAllExamination() {
		if (allExamination == null && examinationPoints != null) {
			String s = "";
			for (VExaminationPoint v : examinationPoints) {
				s += v.getName() + "、";
			}
			if (StringUtils.isNotBlank(s)) {
				s = s.substring(0, s.length() - 1);
			}
			setAllExamination(s);
		}
		return allExamination;
	}

	public void setAllExamination(String allExamination) {
		this.allExamination = allExamination;
	}

	public Long getDoCount() {
		return doCount;
	}

	public void setDoCount(Long doCount) {
		this.doCount = doCount;
	}

	public Double getQuestionRightRate() {
		return questionRightRate;
	}

	public void setQuestionRightRate(Double questionRightRate) {
		this.questionRightRate = questionRightRate;
	}

	public Long getWrongPeopleCount() {
		return wrongPeopleCount;
	}

	public void setWrongPeopleCount(Long wrongPeopleCount) {
		this.wrongPeopleCount = wrongPeopleCount;
	}

	public Long getPublishCount() {
		return publishCount;
	}

	public void setPublishCount(Long publishCount) {
		this.publishCount = publishCount;
	}

	public Long getQuestionSimilarCount() {
		return questionSimilarCount;
	}

	public void setQuestionSimilarCount(Long questionSimilarCount) {
		this.questionSimilarCount = questionSimilarCount;
	}

	public List<VQuestionTag> getQuestionTags() {
		return questionTags;
	}

	public void setQuestionTags(List<VQuestionTag> questionTags) {
		this.questionTags = questionTags;
	}

	public DifficultType getDifficultType() {
		return difficultType;
	}

	public void setDifficultType(DifficultType difficultType) {
		this.difficultType = difficultType;
	}

	public List<VStudentHomeworkAnswer> getCorrectStudentHomeworkAnswers() {
		return correctStudentHomeworkAnswers;
	}

	public void setCorrectStudentHomeworkAnswers(List<VStudentHomeworkAnswer> correctStudentHomeworkAnswers) {
		this.correctStudentHomeworkAnswers = correctStudentHomeworkAnswers;
	}

	public VStudentHomeworkQuestion getCorrectStudentHomeworkQuestion() {
		return correctStudentHomeworkQuestion;
	}

	public void setCorrectStudentHomeworkQuestion(VStudentHomeworkQuestion correctStudentHomeworkQuestion) {
		this.correctStudentHomeworkQuestion = correctStudentHomeworkQuestion;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}
	
}
