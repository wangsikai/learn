package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 学生作业习题VO
 * 
 * @since yoomath v2.3.0 添加做题毫秒数 2016-12-16
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月20日
 */
public class VStudentHomeworkQuestion implements Serializable {

	private static final long serialVersionUID = 5599084460077712421L;

	private long id;
	private long studentHomeworkId;
	private long questionId;
	private String solvingImg = StringUtils.EMPTY;
	private long solvingImgId = 0;
	private String answerImg = StringUtils.EMPTY;
	private long answerImgId = 0;
	private String notationAnswerImg = StringUtils.EMPTY;
	private long notationAnswerImgId = 0;
	private String answerNotationPoints;

	private HomeworkAnswerResult result;
	private Integer rightRate;
	private String rightRateTitle;
	private String comment;
	private Date correctAt;
	//找不到用的地方 2018/2/27
//	private VQuestion question;

	private List<String> answerImgs;
	private List<Long> answerImgIds;
	private List<Long> notationAnswerImgIds;
	private List<String> notationAnswerImgs;
	
	private List<String> answerNotations;
	private List<String> answerNotationPointList;

	private List<String> handWriting;
	private int dotime; // 做题毫秒数
	private Integer voiceTime; // 语音时间
	private String voiceUrl; // 语音文件url地址
	//新的留言处理，支持多条留言
	private List<VHomeworkMessage> messages;
	
	/* 这道题目是否可申述
	 * 1.填空题任意一空被批改错误的，有申诉入口
	 * 2.解答题悠数学后台进行人工批改的，且被批改为完全错误的，有申诉入口
	 * 3.其他情况下，无申诉入口
	*/
	private boolean canAppeal = false;
	
	// 是否已订正
	private boolean revised = false;
	/**
	 * 是否是新订正题(默认值为false，小悠快批上线后新流程下的订正题才有此标记)
	 * 
	 * @since 小优快批
	 */
	private boolean newCorrect = false;
	/**
	 * 习题的最终批改方式.
	 * 
	 * @since 小优快批
	 */
	private QuestionCorrectType correctType = QuestionCorrectType.DEFAULT;
	private List<VStudentHomeworkAnswer> studentHomeworkAnswers = Lists.newArrayList();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public String getSolvingImg() {
		return solvingImg;
	}

	public void setSolvingImg(String solvingImg) {
		this.solvingImg = solvingImg;
	}

	public long getSolvingImgId() {
		return solvingImgId;
	}

	public void setSolvingImgId(long solvingImgId) {
		this.solvingImgId = solvingImgId;
	}

	public String getAnswerImg() {
		return answerImg;
	}

	public void setAnswerImg(String answerImg) {
		this.answerImg = answerImg;
	}

	public long getAnswerImgId() {
		return answerImgId;
	}

	public void setAnswerImgId(long answerImgId) {
		this.answerImgId = answerImgId;
	}

	public String getNotationAnswerImg() {
		return notationAnswerImg;
	}

	public void setNotationAnswerImg(String notationAnswerImg) {
		this.notationAnswerImg = notationAnswerImg;
	}

	public long getNotationAnswerImgId() {
		return notationAnswerImgId;
	}

	public void setNotationAnswerImgId(long notationAnswerImgId) {
		this.notationAnswerImgId = notationAnswerImgId;
	}

	public String getAnswerNotationPoints() {
		return answerNotationPoints;
	}

	public void setAnswerNotationPoints(String answerNotationPoints) {
		this.answerNotationPoints = answerNotationPoints;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(rightRate.intValue() + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCorrectAt() {
		return correctAt;
	}

	public void setCorrectAt(Date correctAt) {
		this.correctAt = correctAt;
	}
	
//	public VQuestion getQuestion() {
//		return question;
//	}
//
//	public void setQuestion(VQuestion question) {
//		this.question = question;
//	}

	public List<String> getAnswerImgs() {
		return answerImgs;
	}

	public void setAnswerImgs(List<String> answerImgs) {
		this.answerImgs = answerImgs;
	}

	public List<Long> getAnswerImgIds() {
		return answerImgIds;
	}

	public void setAnswerImgIds(List<Long> answerImgIds) {
		this.answerImgIds = answerImgIds;
	}

	public List<Long> getNotationAnswerImgIds() {
		return notationAnswerImgIds;
	}

	public void setNotationAnswerImgIds(List<Long> notationAnswerImgIds) {
		this.notationAnswerImgIds = notationAnswerImgIds;
	}

	public List<String> getNotationAnswerImgs() {
		return notationAnswerImgs;
	}

	public void setNotationAnswerImgs(List<String> notationAnswerImgs) {
		this.notationAnswerImgs = notationAnswerImgs;
	}

	public List<String> getAnswerNotationPointList() {
		return answerNotationPointList;
	}

	public void setAnswerNotationPointList(List<String> answerNotationPointList) {
		this.answerNotationPointList = answerNotationPointList;
	}

	public List<String> getHandWriting() {
		return handWriting;
	}

	public void setHandWriting(List<String> handWriting) {
		this.handWriting = handWriting;
	}

	public List<String> getAnswerNotations() {
		return answerNotations;
	}

	public void setAnswerNotations(List<String> answerNotations) {
		this.answerNotations = answerNotations;
	}

	public int getDotime() {
		return dotime;
	}

	public void setDotime(int dotime) {
		this.dotime = dotime;
	}

	public Integer getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(Integer voiceTime) {
		this.voiceTime = voiceTime;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public List<VHomeworkMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<VHomeworkMessage> messages) {
		this.messages = messages;
	}

	public boolean isCanAppeal() {
		return canAppeal;
	}

	public void setCanAppeal(boolean canAppeal) {
		this.canAppeal = canAppeal;
	}

	public boolean isNewCorrect() {
		return newCorrect;
	}

	public void setNewCorrect(boolean newCorrect) {
		this.newCorrect = newCorrect;
	}

	public boolean isRevised() {
		return revised;
	}

	public void setRevised(boolean revised) {
		this.revised = revised;
	}


	public QuestionCorrectType getCorrectType() {
		return correctType;
	}

	public void setCorrectType(QuestionCorrectType correctType) {
		this.correctType = correctType;
	}

	public List<VStudentHomeworkAnswer> getStudentHomeworkAnswers() {
		return studentHomeworkAnswers;
	}

	public void setStudentHomeworkAnswers(List<VStudentHomeworkAnswer> studentHomeworkAnswers) {
		this.studentHomeworkAnswers = studentHomeworkAnswers;
	}
}
