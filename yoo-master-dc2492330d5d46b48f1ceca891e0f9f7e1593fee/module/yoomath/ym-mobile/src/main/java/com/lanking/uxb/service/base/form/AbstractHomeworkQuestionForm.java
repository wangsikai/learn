package com.lanking.uxb.service.base.form;

import java.util.List;

/**
 * 客户端提交作业参数基类
 * 
 * @since yoomath mobile V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月22日
 */
public abstract class AbstractHomeworkQuestionForm {

	// 1:作业2:寒假作业3:错题练习
	private int type;
	// 题目ID
	private long questionId;
	// 答案列表
	private String asciimathAnswers;
	private String mathmlAnswers;
	private List<String> asciimathAnswerList;
	private List<String> latexAnswerList;
	// 作业用时
	private int time;
	// 解题过程(非解答题的时候此字段为解题过程,解答题的时候此字段为答案图片) since 2.0.0
	private Long image;
	// 完成率(寒假作业和普通作业用到)
	private Double completionRate;
	// 上传多张图片
	private List<Long> images;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public List<String> getAsciimathAnswerList() {
		return asciimathAnswerList;
	}

	public void setAsciimathAnswerList(List<String> asciimathAnswerList) {
		this.asciimathAnswerList = asciimathAnswerList;
	}

	public String getAsciimathAnswers() {
		return asciimathAnswers;
	}

	public void setAsciimathAnswers(String asciimathAnswers) {
		this.asciimathAnswers = asciimathAnswers;
	}

	public List<String> getLatexAnswerList() {
		return latexAnswerList;
	}

	public void setLatexAnswerList(List<String> latexAnswerList) {
		this.latexAnswerList = latexAnswerList;
	}

	public String getMathmlAnswers() {
		return mathmlAnswers;
	}

	public void setMathmlAnswers(String mathmlAnswers) {
		this.mathmlAnswers = mathmlAnswers;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Long getImage() {
		return image;
	}

	public void setImage(Long image) {
		this.image = image;
	}

	public Double getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(Double completionRate) {
		this.completionRate = completionRate;
	}

	public List<Long> getImages() {
		return images;
	}

	public void setImages(List<Long> images) {
		this.images = images;
	}
}
