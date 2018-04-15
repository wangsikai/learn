package com.lanking.uxb.service.resources.convert;

/**
 * 作业题目转换选项
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月18日
 */
public class HomeworkQuestionConvertOption {

	private boolean rightRate = false;
	private boolean initSub = true;

	public HomeworkQuestionConvertOption() {
		super();
	}

	public HomeworkQuestionConvertOption(boolean rightRate, boolean initSub) {
		super();
		this.rightRate = rightRate;
		this.initSub = initSub;
	}

	public boolean isRightRate() {
		return rightRate;
	}

	public void setRightRate(boolean rightRate) {
		this.rightRate = rightRate;
	}

	public boolean isInitSub() {
		return initSub;
	}

	public void setInitSub(boolean initSub) {
		this.initSub = initSub;
	}

}
