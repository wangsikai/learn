package com.lanking.uxb.service.zuoye.form;

/**
 * 拉取题目类型
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月22日
 */
public enum PullQuestionType {
	/**
	 * 调整难度
	 */
	CHANGE_DIFFICULTY,
	/**
	 * 换题
	 */
	CHANGE_QUESTION,
	/**
	 * 增加两题
	 */
	ADD_TWO,
	/**
	 * 加强练习题目拉取
	 */
	PRACTISE,
	/**
	 * 智能出卷
	 */
	SMART_PAPER,
	/**
	 * 每日练
	 */
	DAILY_PRACTISE,
	/**
	 * 自动生成预置习题
	 */
	TEXTBOOK_EXERCISE,
	/**
	 * 知识点加强练习
	 * @since 3.9.0
	 */
	KNOWPOINT_ENHANCE_EXERCISE;
}
