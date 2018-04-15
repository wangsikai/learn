package com.lanking.uxb.zycon.base.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomeworkAnswer;

/**
 * 习题VO
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:13:36
 */
public class CQuestion implements Serializable {

	private static final long serialVersionUID = 889812161270297578L;

	private long id;

	private VPhase phase; // 阶段
	private VSubject subject; // 科目
	private CQuestionType questionType; // 科目题型
	private VTextbookCategory textbookCategory; // 教材分类
	private Type type;

	private String content;
	private double difficulty;
	private String hint;

	private boolean subFlag;
	private int sequence;
	private long parentId;
	private List<CQuestion> children = Lists.newArrayList();
	private List<String> choices;
	private int answerNumber;
	private String analysis;
	private String source;
	private List<CAnswer> answers = Lists.newArrayList();
	private List<String> allHints;
	private List<String> allAnalysis;
	private List<List<String>> allAnswers;
	private List<VZycStudentHomeworkAnswer> studentHomeworkAnswers = Lists.newArrayList();

	private CheckStatus checkStatus;
	private List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();
	private String allMetaKnowpoint;

	private Date createAt;
	private Date updateAt;
	private String code;

	// 是否是订正题
	private boolean correctQuestion = false;

	// 选择题选项排版方式
	private ChoiceFormat choiceFormat;

	private QuestionSource questionSource;

	// @since 2.6.0 新知识点
	private List<VKnowledgePoint> newKnowledgePoints = Lists.newArrayList();
	private String allNewKnowpoint;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CQuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(CQuestionType questionType) {
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

	public void setDifficulty(double difficulty) {
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

	public List<CQuestion> getChildren() {
		return children;
	}

	public void setChildren(List<CQuestion> children) {
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

	public List<CAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<CAnswer> answers) {
		this.answers = answers;
	}

	public List<String> getAllAnalysis() {
		if (allAnalysis == null) {
			allAnalysis = Lists.newArrayList();
			allAnalysis.add(StringUtils.isBlank(analysis) ? StringUtils.EMPTY : getAnalysis());
			if (getChildren() != null) {
				for (CQuestion vq : getChildren()) {
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
				for (CQuestion vq : getChildren()) {
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
			List<CAnswer> as = getAnswers();
			if (as != null) {
				for (CAnswer v : as) {
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
			List<CQuestion> vqs = getChildren();
			if (vqs != null) {
				for (CQuestion vq : vqs) {
					List<String> cas = Lists.newArrayList();
					List<CAnswer> cvs = vq.getAnswers();
					if (cvs != null) {
						for (CAnswer v : cvs) {
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

	public boolean isCorrectQuestion() {
		return correctQuestion;
	}

	public void setCorrectQuestion(boolean correctQuestion) {
		this.correctQuestion = correctQuestion;
	}

	public List<VZycStudentHomeworkAnswer> getStudentHomeworkAnswers() {
		return studentHomeworkAnswers;
	}

	public void setStudentHomeworkAnswers(List<VZycStudentHomeworkAnswer> studentHomeworkAnswers) {
		this.studentHomeworkAnswers = studentHomeworkAnswers;
	}

	public List<VKnowledgePoint> getNewKnowledgePoints() {
		return newKnowledgePoints;
	}

	public void setNewKnowledgePoints(List<VKnowledgePoint> newKnowledgePoints) {
		this.newKnowledgePoints = newKnowledgePoints;
	}

	public String getAllNewKnowpoint() {
		if (allNewKnowpoint == null) {
			String s = "";
			for (VKnowledgePoint v : newKnowledgePoints) {
				s += v.getName() + ",";
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
}
