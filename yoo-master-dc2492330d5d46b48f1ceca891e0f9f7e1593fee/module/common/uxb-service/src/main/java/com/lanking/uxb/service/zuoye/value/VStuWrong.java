package com.lanking.uxb.service.zuoye.value;

import java.util.List;

import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;

/**
 * 学生错题 VO
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年9月29日 下午5:14:09
 */
public class VStuWrong {
	private String name;
	private List<VStudentHomeworkAnswer> vsTemp;
	private String solvingImage;
	private String notationAnswerImage;
	private Integer rightRate;
	private List<String> answerImgs;
	private List<String> notationAnswerImgs;
	private Long stuHkQuestionId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VStudentHomeworkAnswer> getVsTemp() {
		return vsTemp;
	}

	public void setVsTemp(List<VStudentHomeworkAnswer> vsTemp) {
		this.vsTemp = vsTemp;
	}

	public String getSolvingImage() {
		return solvingImage;
	}

	public void setSolvingImage(String solvingImage) {
		this.solvingImage = solvingImage;
	}

	public String getNotationAnswerImage() {
		return notationAnswerImage;
	}

	public void setNotationAnswerImage(String notationAnswerImage) {
		this.notationAnswerImage = notationAnswerImage;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public List<String> getAnswerImgs() {
		return answerImgs;
	}

	public void setAnswerImgs(List<String> answerImgs) {
		this.answerImgs = answerImgs;
	}

	public List<String> getNotationAnswerImgs() {
		return notationAnswerImgs;
	}

	public void setNotationAnswerImgs(List<String> notationAnswerImgs) {
		this.notationAnswerImgs = notationAnswerImgs;
	}

	public Long getStuHkQuestionId() {
		return stuHkQuestionId;
	}

	public void setStuHkQuestionId(Long stuHkQuestionId) {
		this.stuHkQuestionId = stuHkQuestionId;
	}

}
