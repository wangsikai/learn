package com.lanking.uxb.service.push.type;

public enum OpenPath {

	/**
	 * 作业首页
	 */
	HOMEWORK_HOME("homework-home"),
	/**
	 * 班级排行榜页面
	 */
	RANKING_CLASS("ranking-class"),
	/**
	 * 作业详情
	 */
	HOMEWORK_DETAIL("homework-detail"),
	/**
	 * 班级管理页
	 */
	CLASSMANAGER_HOME("classManager-home"),
	/**
	 * 班级排行榜页面
	 */
	RANKING_CLASS2("ranking-class2");

	private String name;

	OpenPath(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
