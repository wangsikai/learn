package com.lanking.uxb.service.resources.convert;

/**
 * 作业转换选项
 * 
 * @since 2.1
 * @since 学生端v1.3.4 2017-5-27 添加初始化开关
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月24日
 * 
 */
public class HomeworkConvertOption {

	/**
	 * 是否初始化统计字段.
	 */
	private boolean initCount = false;

	// since 2017-5-27
	private boolean initExercise = true; // 初始化作业对应的练习（兼容设置，默认true）
	private boolean initSectionOrBookCatalog = true; // 初始化章节信息，获得该数据时initExercise必须为true（兼容设置，默认true）
	private boolean initMetaKnowpoint = true; // 初始化旧知识点（兼容设置，默认true）
	private boolean initKnowledgePoint = true; // 初始化新知识点（兼容设置，默认true）

	// since 2017-12-15 增加可选项
	private boolean initTeacherName = true; // 初始化教师姓名（兼容设置，默认true）
	/**
	 * 是否查询留言信息
	 */
	private boolean initMessages = false;

	public HomeworkConvertOption() {
		super();
	}

	public HomeworkConvertOption(boolean initCount) {
		super();
		this.initCount = initCount;
	}

	public boolean isInitCount() {
		return initCount;
	}

	public void setInitCount(boolean initCount) {
		this.initCount = initCount;
	}

	public boolean isInitExercise() {
		return initExercise;
	}

	public void setInitExercise(boolean initExercise) {
		this.initExercise = initExercise;
	}

	public boolean isInitSectionOrBookCatalog() {
		return initSectionOrBookCatalog;
	}

	public void setInitSectionOrBookCatalog(boolean initSectionOrBookCatalog) {
		this.initSectionOrBookCatalog = initSectionOrBookCatalog;
	}

	public boolean isInitMetaKnowpoint() {
		return initMetaKnowpoint;
	}

	public void setInitMetaKnowpoint(boolean initMetaKnowpoint) {
		this.initMetaKnowpoint = initMetaKnowpoint;
	}

	public boolean isInitKnowledgePoint() {
		return initKnowledgePoint;
	}

	public void setInitKnowledgePoint(boolean initKnowledgePoint) {
		this.initKnowledgePoint = initKnowledgePoint;
	}

	public boolean isInitTeacherName() {
		return initTeacherName;
	}

	public void setInitTeacherName(boolean initTeacherName) {
		this.initTeacherName = initTeacherName;
	}

	public boolean isInitMessages() {
		return initMessages;
	}

	public void setInitMessages(boolean initMessages) {
		this.initMessages = initMessages;
	}
}
