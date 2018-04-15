package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 学生题目答案来源
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum StudentQuestionAnswerSource implements Valueable {
	/**
	 * 作业
	 */
	HOMEWORK(0),
	/**
	 * 错题练习
	 */
	FALLIBLE(1),
	/**
	 * 按章节练习
	 */
	SECTION_EXERCISE(2),
	/**
	 * 按知识点练习
	 */
	KNOWPOINT_EXERCISE(3),
	/**
	 * 每日练
	 */
	DAILY_PRACTICE(4),
	/**
	 * 智能试卷
	 */
	SMART_PAPER(5),
	/**
	 * 假期作业
	 */
	HOLIDAY_HOMEWORK(6),
	/**
	 * 拍照
	 */
	OCR(7),
	/**
	 * 二维码扫题
	 */
	QR(8),
	/**
	 * 组卷
	 */
	CUSTOM_EXAMPAPER(9),
	/**
	 * 专题练习
	 */
	TOPIC_PRACTICE(10),
	/**
	 * 作业报告知识点 加强练习
	 */
	ENHANCE_PRACTICE(11),
	/**
	 * 作业错题针对性练习
	 */
	HOMEWORK_FALLIBLE_PRACTICE(12);

	private int value;

	StudentQuestionAnswerSource(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static StudentQuestionAnswerSource findByValue(int value) {
		switch (value) {
		case 0:
			return HOMEWORK;
		case 1:
			return FALLIBLE;
		case 2:
			return SECTION_EXERCISE;
		case 3:
			return KNOWPOINT_EXERCISE;
		case 4:
			return DAILY_PRACTICE;
		case 5:
			return SMART_PAPER;
		case 6:
			return HOLIDAY_HOMEWORK;
		case 7:
			return OCR;
		case 8:
			return QR;
		case 9:
			return CUSTOM_EXAMPAPER;
		case 10:
			return TOPIC_PRACTICE;
		case 11:
			return ENHANCE_PRACTICE;
		case 12:
			return HOMEWORK_FALLIBLE_PRACTICE;
		default:
			return null;
		}
	}

	public String getName() {
		switch (value) {
		case 0:
			return "日常作业";
		case 1:
			return "错题练习";
		case 2:
			return "章节练习";
		case 3:
			return "加强练习";
		case 4:
			return "每日练";
		case 5:
			return "智能出卷";
		case 6:
			return "假期作业";
		case 7:
			return "拍照";
		case 8:
			return "二维码扫题";
		case 9:
			return "组卷";
		case 10:
			return "专题练习";
		case 11:
			return "知识点加强练习";
		case 12:
			return "错题针对性练习";
		default:
			return "";
		}
	}
}