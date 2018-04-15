package com.lanking.uxb.service.examPaper.convert;

/**
 * 学生组卷转换参数.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月15日
 */
public class CustomExampaperStudentConvertOption {
	private boolean hasCustomExampaper = false;
	private boolean hasClazz = false;
	private boolean hasQuestion = false;

	public boolean isHasCustomExampaper() {
		return hasCustomExampaper;
	}

	public void setHasCustomExampaper(boolean hasCustomExampaper) {
		this.hasCustomExampaper = hasCustomExampaper;
	}

	public boolean isHasClazz() {
		return hasClazz;
	}

	public void setHasClazz(boolean hasClazz) {
		this.hasClazz = hasClazz;
	}

	public boolean isHasQuestion() {
		return hasQuestion;
	}

	public void setHasQuestion(boolean hasQuestion) {
		this.hasQuestion = hasQuestion;
	}
}
