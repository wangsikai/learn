package com.lanking.uxb.zycon.homework.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.uxb.zycon.base.value.CAnswer;

/**
 * 习题VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public class VZycQuestion implements Serializable {

	private static final long serialVersionUID = 889812161270297578L;

	private long id;

	private Type type;

	private String content;

	private boolean subFlag;
	private int sequence;
	private long parentId;
	private String hint = "";
	private String analysis = "";
	private List<VZycQuestion> children = Lists.newArrayList();

	private List<String> choices;

	private int answerNumber;

	private List<CAnswer> answers = Lists.newArrayList();
	private List<VZycStudentHomeworkAnswer> studentHomeworkAnswers = Lists.newArrayList();
	private VZycStudentHomeworkQuestion studentHomeworkQuestion;
	
	//订正答案
	private List<VZycStudentHomeworkAnswer> correctStudentHomeworkAnswers = Lists.newArrayList();
	//订正问题
	private VZycStudentHomeworkQuestion correctStudentHomeworkQuestion;
	
	private List<List<String>> allAnswers;
	
	private CheckStatus checkStatus;

	private Date createAt;
	private Date updateAt;
	private String code;

	// 是否是订正题
	private boolean correctQuestion = false;
	private Long studentHomeworkId;
	private Long homeworkId;
	private Long studentQuestionId;

	// 如果已经最后一个学生提交的5分钟倒计时开始
	private Long countDownTime;

	private ChoiceFormat choiceFormat;
	
	// 是否可以提交申述结果
	private boolean canSubmitAppeal = false;
	// 申述人的id
	private Long creator;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public List<VZycQuestion> getChildren() {
		return children;
	}

	public void setChildren(List<VZycQuestion> children) {
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

	public List<CAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<CAnswer> answers) {
		this.answers = answers;
	}

	public List<VZycStudentHomeworkAnswer> getStudentHomeworkAnswers() {
		return studentHomeworkAnswers;
	}

	public void setStudentHomeworkAnswers(List<VZycStudentHomeworkAnswer> studentHomeworkAnswers) {
		this.studentHomeworkAnswers = studentHomeworkAnswers;
	}

	public VZycStudentHomeworkQuestion getStudentHomeworkQuestion() {
		return studentHomeworkQuestion;
	}

	public void setStudentHomeworkQuestion(VZycStudentHomeworkQuestion studentHomeworkQuestion) {
		this.studentHomeworkQuestion = studentHomeworkQuestion;
	}

	public List<List<String>> getAllAnswers() {
		if (allAnswers == null) {
			allAnswers = Lists.newArrayList();
			List<String> pas = Lists.newArrayList();
			List<CAnswer> as = getAnswers();
			if (as != null) {
				for (CAnswer v : as) {
					if (type == Type.TRUE_OR_FALSE) {
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
			List<VZycQuestion> vqs = getChildren();
			if (vqs != null) {
				for (VZycQuestion vq : vqs) {
					List<String> cas = Lists.newArrayList();
					List<CAnswer> cvs = vq.getAnswers();
					if (cvs != null) {
						for (CAnswer v : cvs) {
							if (type == Type.TRUE_OR_FALSE) {
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

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isCorrectQuestion() {
		return correctQuestion;
	}

	public void setCorrectQuestion(boolean correctQuestion) {
		this.correctQuestion = correctQuestion;
	}

	public Long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(Long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public Long getStudentQuestionId() {
		return studentQuestionId;
	}

	public void setStudentQuestionId(Long studentQuestionId) {
		this.studentQuestionId = studentQuestionId;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public Long getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(Long countDownTime) {
		this.countDownTime = countDownTime;
	}

	public ChoiceFormat getChoiceFormat() {
		return choiceFormat;
	}

	public void setChoiceFormat(ChoiceFormat choiceFormat) {
		this.choiceFormat = choiceFormat;
	}

	public List<VZycStudentHomeworkAnswer> getCorrectStudentHomeworkAnswers() {
		return correctStudentHomeworkAnswers;
	}

	public void setCorrectStudentHomeworkAnswers(List<VZycStudentHomeworkAnswer> correctStudentHomeworkAnswers) {
		this.correctStudentHomeworkAnswers = correctStudentHomeworkAnswers;
	}

	public VZycStudentHomeworkQuestion getCorrectStudentHomeworkQuestion() {
		return correctStudentHomeworkQuestion;
	}

	public void setCorrectStudentHomeworkQuestion(VZycStudentHomeworkQuestion correctStudentHomeworkQuestion) {
		this.correctStudentHomeworkQuestion = correctStudentHomeworkQuestion;
	}

	public boolean isCanSubmitAppeal() {
		return canSubmitAppeal;
	}

	public void setCanSubmitAppeal(boolean canSubmitAppeal) {
		this.canSubmitAppeal = canSubmitAppeal;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}
	
}
