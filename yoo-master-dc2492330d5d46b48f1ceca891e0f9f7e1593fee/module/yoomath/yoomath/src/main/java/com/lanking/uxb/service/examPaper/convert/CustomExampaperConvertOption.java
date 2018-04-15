package com.lanking.uxb.service.examPaper.convert;

/**
 * 题目VO转换选项.
 * 
 * @since zemin.song
 * @version 2016年8月19日
 */
public class CustomExampaperConvertOption {

	private boolean isShowQuestions = false;
	private boolean isShowTopic = false;
	private boolean isShowClazz = false;

	public CustomExampaperConvertOption() {
		super();
	}

	public CustomExampaperConvertOption(boolean isShowQuestions, boolean isShowTopic, boolean isShowClazz) {
		super();
		this.isShowQuestions = isShowQuestions;
		this.isShowTopic = isShowTopic;
		this.isShowClazz = isShowClazz;
	}

	public boolean isShowQuestions() {
		return isShowQuestions;
	}

	public void setShowQuestions(boolean isShowQuestions) {
		this.isShowQuestions = isShowQuestions;
	}

	public boolean isShowTopic() {
		return isShowTopic;
	}

	public void setShowTopic(boolean isShowTopic) {
		this.isShowTopic = isShowTopic;
	}

	public boolean isShowClazz() {
		return isShowClazz;
	}

	public void setShowClazz(boolean isShowClazz) {
		this.isShowClazz = isShowClazz;
	}

}
