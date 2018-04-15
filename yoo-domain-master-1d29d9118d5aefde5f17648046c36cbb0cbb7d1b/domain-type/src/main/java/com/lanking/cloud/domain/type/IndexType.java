package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.IndexTypeable;

/**
 * 索引类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum IndexType implements IndexTypeable {
	/**
	 * 知识点
	 */
	KNOWPOINT,
	/**
	 * 习题
	 */
	QUESTION,
	/**
	 * 校本题库
	 */
	SCHOOL_QUESTION,
	/**
	 * 书本
	 */
	BOOK,
	/**
	 * 试卷
	 */
	EXAM_PAPER,
	/**
	 * 教辅
	 */
	TEACH_ASSIST,
	/**
	 * 用户收藏题目
	 */
	USER_QUESTION_COLLECT,
	/**
	 * 教师错题
	 */
	TEACHER_FALLIBLE_QUESTION,
	/**
	 * 学生错题
	 */
	STUDENT_FALLIBLE_QUESTION,
	/**
	 * 相似题组.
	 */
	QUESTION_SIMILAR,
	/**
	 * 相似题组搜索基本题目数据.
	 */
	QUESTION_SIMILAR_BASE;
}
