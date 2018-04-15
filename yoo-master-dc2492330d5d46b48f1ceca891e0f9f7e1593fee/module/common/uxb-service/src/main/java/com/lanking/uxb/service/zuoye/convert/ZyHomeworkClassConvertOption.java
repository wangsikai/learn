package com.lanking.uxb.service.zuoye.convert;

/**
 * 作业班级转换参数
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月23日
 */
public class ZyHomeworkClassConvertOption {

	private boolean initTeacher = false;
	private boolean initStat = false;
	private boolean initLatestHomework = false;
	private boolean initClassGroup = false;

	public ZyHomeworkClassConvertOption() {
		super();
	}

	public ZyHomeworkClassConvertOption(boolean initTeacher, boolean initStat, boolean initLatestHomework) {
		super();
		this.initStat = initStat;
		this.initTeacher = initTeacher;
		this.initLatestHomework = initLatestHomework;
	}

	public ZyHomeworkClassConvertOption(boolean initTeacher, boolean initStat, boolean initLatestHomework,
			boolean initClassGroup) {
		super();
		this.initStat = initStat;
		this.initTeacher = initTeacher;
		this.initLatestHomework = initLatestHomework;
		this.initClassGroup = initClassGroup;
	}

	public boolean isInitTeacher() {
		return initTeacher;
	}

	public void setInitTeacher(boolean initTeacher) {
		this.initTeacher = initTeacher;
	}

	public boolean isInitStat() {
		return initStat;
	}

	public void setInitStat(boolean initStat) {
		this.initStat = initStat;
	}

	public boolean isInitLatestHomework() {
		return initLatestHomework;
	}

	public void setInitLatestHomework(boolean initLatestHomework) {
		this.initLatestHomework = initLatestHomework;
	}

	public boolean isInitClassGroup() {
		return initClassGroup;
	}

	public void setInitClassGroup(boolean initClassGroup) {
		this.initClassGroup = initClassGroup;
	}

}
