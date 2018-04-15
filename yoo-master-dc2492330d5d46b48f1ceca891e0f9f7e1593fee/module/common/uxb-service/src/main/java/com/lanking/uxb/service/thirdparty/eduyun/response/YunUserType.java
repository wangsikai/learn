package com.lanking.uxb.service.thirdparty.eduyun.response;

/**
 * 用户类型.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年7月3日
 */
public enum YunUserType {
	/**
	 * 学生.
	 */
	STUDENT(0),
	/**
	 * 教师.
	 */
	TEACHER(1),
	/**
	 * 家长.
	 */
	PARENT(2),
	/**
	 * 机构.
	 */
	ORG(3),
	/**
	 * 学校.
	 */
	SCHOOL(4),
	/**
	 * 学校工作人员.
	 */
	SCHOOLSTAFF(5),
	/**
	 * 机构工作人员.
	 */
	ORGSTAFF(6),
	/**
	 * 社会学习者.
	 */
	LEARNER(7),
	/**
	 * 专家.
	 */
	EXPERTS(8);

	private int value;

	YunUserType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static YunUserType findByValue(int value) {
		switch (value) {
		case 0:
			return STUDENT;
		case 1:
			return TEACHER;
		case 2:
			return PARENT;
		case 3:
			return ORG;
		case 4:
			return SCHOOL;
		case 5:
			return SCHOOLSTAFF;
		case 6:
			return ORGSTAFF;
		case 7:
			return LEARNER;
		case 8:
			return EXPERTS;
		default:
			return null;
		}
	}
}
