package com.lanking.uxb.service.resources.convert;

public class StudentHomeworkConvertOption {
	/**
	 * 是否统计有没有批改完
	 */
	private boolean statisticCorrected = false;
	/**
	 * 是否设置作业对象
	 */
	private boolean initHomework = false;
	/**
	 * 是否设置用户信息
	 */
	private boolean initUser = false;
	/**
	 * 是否只获取简单的作业对象
	 */
	private boolean simpleHomework = false;
	
	/**
	 * 是否统计学生的错题和已订正的题
	 */
	private boolean initStuHomeworkWrongAndCorrect = false;
	
	/**
	 * 是否统计已批改的题目数量和正在人工批改中的的题目数
	 */
	private boolean initStuHomeworkCorrectedAndCorrecting = false;
	
	/**
	 * 是否统计待批改题目数
	 */
	private boolean statisticTobeCorrected = false;
	
	/**
	 * 是否查询留言信息
	 */
	private boolean initMessages = false;
	
	public boolean isInitMessages() {
		return initMessages;
	}
	public void setInitMessages(boolean initMessages) {
		this.initMessages = initMessages;
	}
	public boolean isStatisticCorrected() {
		return statisticCorrected;
	}
	public void setStatisticCorrected(boolean statisticCorrected) {
		this.statisticCorrected = statisticCorrected;
	}
	public boolean isInitHomework() {
		return initHomework;
	}
	public void setInitHomework(boolean initHomework) {
		this.initHomework = initHomework;
	}
	public boolean isInitUser() {
		return initUser;
	}
	public void setInitUser(boolean initUser) {
		this.initUser = initUser;
	}
	public boolean isSimpleHomework() {
		return simpleHomework;
	}
	public void setSimpleHomework(boolean simpleHomework) {
		this.simpleHomework = simpleHomework;
	}
	public boolean isInitStuHomeworkWrongAndCorrect() {
		return initStuHomeworkWrongAndCorrect;
	}
	public void setInitStuHomeworkWrongAndCorrect(boolean initStuHomeworkWrongAndCorrect) {
		this.initStuHomeworkWrongAndCorrect = initStuHomeworkWrongAndCorrect;
	}
	public boolean isInitStuHomeworkCorrectedAndCorrecting() {
		return initStuHomeworkCorrectedAndCorrecting;
	}
	public void setInitStuHomeworkCorrectedAndCorrecting(boolean initStuHomeworkCorrectedAndCorrecting) {
		this.initStuHomeworkCorrectedAndCorrecting = initStuHomeworkCorrectedAndCorrecting;
	}
	public boolean isStatisticTobeCorrected() {
		return statisticTobeCorrected;
	}
	public void setStatisticTobeCorrected(boolean statisticTobeCorrected) {
		this.statisticTobeCorrected = statisticTobeCorrected;
	}
	
}
