package com.lanking.uxb.service.base.type;

public interface CommonSettings {
	// 做试卷的最小题量
	int MIN_PAPER_QUESTION_NUM = 100;
	// 拉取题目数量(试卷、每日练、章节练习)
	int QUESTION_PULL_COUNT = 20;
	// 一个用户没有对每日练习进行设置的时候，第４天不再进行显示
	int NOT_SETTING_SHOW_DAY = 4;
	// 薄弱知识点个数
	int WEAK_KNOWPOINT_COUNT = 10;
}
