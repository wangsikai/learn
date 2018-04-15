package com.lanking.uxb.service.resources.value;

import java.util.List;

/**
 * 学生作业错误答案VO
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年8月23日
 */
public class VWrongAnswer extends VStudentHomeworkAnswer {

	private static final long serialVersionUID = -7412082977237757592L;

	// 正确率
	private Integer rightRate;

	private String rightRateTitle;

	// 答案图片
	private List<String> answerImages;

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public List<String> getAnswerImages() {
		return answerImages;
	}

	public void setAnswerImages(List<String> answerImages) {
		this.answerImages = answerImages;
	}

}
