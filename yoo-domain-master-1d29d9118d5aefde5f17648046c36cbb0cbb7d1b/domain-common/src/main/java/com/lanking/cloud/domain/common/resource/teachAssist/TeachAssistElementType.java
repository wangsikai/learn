package com.lanking.cloud.domain.common.resource.teachAssist;

import com.lanking.cloud.sdk.bean.Valueable;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 教辅元素类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum TeachAssistElementType implements Valueable {
	NULL(0),

	/**
	 * 课时标题模块
	 */
	PERIOD_TITLE(1),

	/**
	 * 课时子标题模块
	 */
	PERIOD_CHILD_TITLE(2),

	/**
	 * 知识说明/回顾
	 */
	KNOWLEDGE_SPEC(3),

	/**
	 * 学习目标模块
	 */
	LEARN_GOAL(4),

	/**
	 * 预习目标模块
	 */
	PREPARE_GOAL(5),

	/**
	 * 预习评价模块
	 */
	PREPARE_COMMENT(6),

	/**
	 * 课内教学模块
	 */
	LESSON_TEACH(7),

	/**
	 * 回顾反思模块
	 */
	REVIEW(8),

	/**
	 * 习题内容模块
	 */
	PRACTICE(9),

	/**
	 * 训练评价模块
	 */
	PRACTICE_COMMENT(10),

	/**
	 * 易错点模块
	 */
	FALLIBLE_POINT(11),

	/**
	 * 知识结构模块
	 */
	POINT_STRUCTURE(12),

	/**
	 * 解题方法及要点模块
	 */
	PROBLEM_SOLVING(13),

	/**
	 * 知识拓朴模块
	 */
	POINT_MAP(14),

	/**
	 * 专题模块
	 */
	TOPIC(15),

	/**
	 * 自由编辑模块
	 */
	FREE_EDIT(16);

	private int value;

	@Override
	public int getValue() {
		return value;
	}

	TeachAssistElementType(int value) {
		this.value = value;
	}

	public static TeachAssistElementType findByValue(int value) {
		switch (value) {
		case 0:
			return NULL;
		case 1:
			return PERIOD_TITLE;
		case 2:
			return PERIOD_CHILD_TITLE;
		case 3:
			return KNOWLEDGE_SPEC;
		case 4:
			return LEARN_GOAL;
		case 5:
			return PREPARE_GOAL;
		case 6:
			return PREPARE_COMMENT;
		case 7:
			return LESSON_TEACH;
		case 8:
			return REVIEW;
		case 9:
			return PRACTICE;
		case 10:
			return PRACTICE_COMMENT;
		case 11:
			return FALLIBLE_POINT;
		case 12:
			return POINT_STRUCTURE;
		case 13:
			return PROBLEM_SOLVING;
		case 14:
			return POINT_MAP;
		case 15:
			return TOPIC;
		case 16:
			return FREE_EDIT;
		default:
			return NULL;
		}
	}

	public String getName() {
		switch (this.value) {
		case 0:
			return "";
		case 1:
			return "课时标题模块";
		case 2:
			return "课时子标题模块";
		case 3:
			return "知识说明/回顾";
		case 4:
			return "学习目标模块";
		case 5:
			return "预习点模块";
		case 6:
			return "预习评价模块";
		case 7:
			return "课内教学模块";
		case 8:
			return "回顾反思模块";
		case 9:
			return "习题内容模块";
		case 10:
			return "训练评价模块";
		case 11:
			return "易错点模块";
		case 12:
			return "知识结构模块";
		case 13:
			return "解题方法及要点小结模块";
		case 14:
			return "知识拓扑图";
		case 15:
			return "专题说明模块";
		case 16:
			return "自由编辑模块";
		default:
			return StringUtils.EMPTY;
		}
	}
}
