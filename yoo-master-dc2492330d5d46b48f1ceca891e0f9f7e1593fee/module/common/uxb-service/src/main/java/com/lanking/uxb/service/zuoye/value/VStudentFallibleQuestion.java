package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 学生错题VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月17日
 */
public class VStudentFallibleQuestion implements Serializable {

	private static final long serialVersionUID = 4663075299702443933L;

	private long id;
	private long questionId;
	private long doNum;
	private long mistakeNum;
	private long exerciseNum;
	private Date createAt;
	private Date updateAt;

	private long mistakePeople;

	// 来源
	private StudentQuestionAnswerSource source;
	private String sourceTitle;

	// 拍照相关
	private String ocrImg;
	private List<VMetaKnowpoint> knowpoints;
	private List<String> knowpointNames;

	private VQuestion question;

	// 最近一次答案的相关信息
	private List<String> latestAnswer;
	private HomeworkAnswerResult latestResult;
	private List<HomeworkAnswerResult> latestItemResults;
	private String latestAnswerImg = StringUtils.EMPTY;
	private List<String> latestAnswerImgs = Lists.newArrayList();
	private Integer latestRightRate;

	private List<VOcrHisAnswer> ocrHisAnswerImgs = Lists.newArrayList();
	// 正确率
	private BigDecimal rightRate;
	private String rightRateTitle;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getDoNum() {
		return doNum;
	}

	public void setDoNum(long doNum) {
		this.doNum = doNum;
	}

	public long getMistakeNum() {
		return mistakeNum;
	}

	public void setMistakeNum(long mistakeNum) {
		this.mistakeNum = mistakeNum;
	}

	public long getExerciseNum() {
		return exerciseNum;
	}

	public void setExerciseNum(long exerciseNum) {
		this.exerciseNum = exerciseNum;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}

	public List<String> getLatestAnswer() {
		return latestAnswer;
	}

	public void setLatestAnswer(List<String> latestAnswer) {
		this.latestAnswer = latestAnswer;
	}

	public HomeworkAnswerResult getLatestResult() {
		return latestResult;
	}

	public void setLatestResult(HomeworkAnswerResult latestResult) {
		this.latestResult = latestResult;
	}

	public List<HomeworkAnswerResult> getLatestItemResults() {
		return latestItemResults;
	}

	public void setLatestItemResults(List<HomeworkAnswerResult> latestItemResults) {
		this.latestItemResults = latestItemResults;
	}

	public String getLatestAnswerImg() {
		return latestAnswerImg;
	}

	public void setLatestAnswerImg(String latestAnswerImg) {
		this.latestAnswerImg = latestAnswerImg;
	}

	public List<String> getLatestAnswerImgs() {
		return latestAnswerImgs;
	}

	public void setLatestAnswerImgs(List<String> latestAnswerImgs) {
		this.latestAnswerImgs = latestAnswerImgs;
	}

	public Integer getLatestRightRate() {
		return latestRightRate;
	}

	public void setLatestRightRate(Integer latestRightRate) {
		this.latestRightRate = latestRightRate;
	}

	public StudentQuestionAnswerSource getSource() {
		return source;
	}

	public void setSource(StudentQuestionAnswerSource source) {
		this.source = source;
	}

	public String getSourceTitle() {
		return sourceTitle;
	}

	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public String getOcrImg() {
		return ocrImg;
	}

	public void setOcrImg(String ocrImg) {
		this.ocrImg = ocrImg;
	}

	public List<VMetaKnowpoint> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<VMetaKnowpoint> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public long getMistakePeople() {
		return mistakePeople;
	}

	public void setMistakePeople(long mistakePeople) {
		this.mistakePeople = mistakePeople;
	}

	public List<VOcrHisAnswer> getOcrHisAnswerImgs() {
		return ocrHisAnswerImgs;
	}

	public void setOcrHisAnswerImgs(List<VOcrHisAnswer> ocrHisAnswerImgs) {
		this.ocrHisAnswerImgs = ocrHisAnswerImgs;
	}

	public List<String> getKnowpointNames() {
		return knowpointNames;
	}

	public void setKnowpointNames(List<String> knowpointNames) {
		this.knowpointNames = knowpointNames;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		if (rightRate == null) {
			return null;
		}
		return rightRate + "%";
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}
}
