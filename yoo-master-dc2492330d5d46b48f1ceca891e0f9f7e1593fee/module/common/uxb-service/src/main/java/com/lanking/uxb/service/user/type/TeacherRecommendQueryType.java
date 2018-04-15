package com.lanking.uxb.service.user.type;

/**
 * 老师推荐查询类型
 *
 * @author xinyu.zhou
 * @since v2.1
 */
public enum TeacherRecommendQueryType {

	NULL(0),

	ALL(1),

	NEAR(2),

	SUBJECT(3),

	GRADE(4);

	private int value;

	TeacherRecommendQueryType(int value) {
		this.value = value;
	}
}
