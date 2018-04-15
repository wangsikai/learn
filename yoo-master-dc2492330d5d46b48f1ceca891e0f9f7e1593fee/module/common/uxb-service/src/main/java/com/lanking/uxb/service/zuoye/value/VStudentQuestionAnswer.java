package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 答题历史详情VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月10日
 */
public class VStudentQuestionAnswer implements Serializable {

	private static final long serialVersionUID = 4846462563026788862L;

	private long id;
	private long questionId;
	private Map<Long, List<String>> answers;
	private Map<Long, List<String>> answersAscii;
	private Date createAt;
	private String answerImg = StringUtils.EMPTY;
	private List<String> answerImgs = Lists.newArrayList();
	private List<HomeworkAnswerResult> itemResults;
	private HomeworkAnswerResult result;

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

	public Map<Long, List<String>> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<Long, List<String>> answers) {
		this.answers = answers;
	}

	public Map<Long, List<String>> getAnswersAscii() {
		return answersAscii;
	}

	public void setAnswersAscii(Map<Long, List<String>> answersAscii) {
		this.answersAscii = answersAscii;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getAnswerImg() {
		return answerImg;
	}

	public void setAnswerImg(String answerImg) {
		this.answerImg = answerImg;
	}

	public List<HomeworkAnswerResult> getItemResults() {
		return itemResults;
	}

	public void setItemResults(List<HomeworkAnswerResult> itemResults) {
		this.itemResults = itemResults;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public List<String> getAnswerImgs() {
		return answerImgs;
	}

	public void setAnswerImgs(List<String> answerImgs) {
		this.answerImgs = answerImgs;
	}

}
