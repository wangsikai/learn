package com.lanking.uxb.service.zuoye.form;

import java.util.List;

import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeSettings;

/**
 * 每日练习保存表单
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public class DailyPractiseSaveForm {
	// 用户id
	private long userId;
	// 对应章节码
	private long sectionCode;
	// 章节练习的题目id列表
	private List<Long> qIds;
	// 难度
	private double difficulty;
	// 练习的名称
	private String name;
	// 每日一练设置
	private DailyPracticeSettings settings;
	// 版本code
	private int textbookCode;

	public DailyPractiseSaveForm() {
	}

	public DailyPractiseSaveForm(long userId, long sectionCode, List<Long> qIds, double difficulty, String name,
			DailyPracticeSettings settings, int textbookCode) {
		this.userId = userId;
		this.sectionCode = sectionCode;
		this.qIds = qIds;
		this.difficulty = difficulty;
		this.name = name;
		this.settings = settings;
		this.textbookCode = textbookCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DailyPracticeSettings getSettings() {
		return settings;
	}

	public void setSettings(DailyPracticeSettings settings) {
		this.settings = settings;
	}

	public int getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(int textbookCode) {
		this.textbookCode = textbookCode;
	}
}
