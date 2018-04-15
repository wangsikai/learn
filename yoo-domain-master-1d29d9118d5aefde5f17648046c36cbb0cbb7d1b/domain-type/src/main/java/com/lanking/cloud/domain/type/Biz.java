package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Bizable;

/**
 * 系统业务类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum Biz implements Bizable {

	NULL(0),
	/**
	 * 用户
	 */
	USER(1),
	/**
	 * 帖子
	 */
	POST(2),
	/**
	 * 评论
	 */
	COMMENT(3),
	/**
	 * 课程
	 */
	COURSE(4),
	/**
	 * 班级
	 */
	CLASS(5),
	/**
	 * 群组
	 */
	GROUP(6),
	/**
	 * 相册
	 */
	ALBUM(7),
	/**
	 * 相册图片
	 */
	ALBUM_PHOTO(8),
	/**
	 * 习题
	 */
	QUESTION(9),
	/**
	 * 练习
	 */
	EXERCISE(10),
	/**
	 * 作业
	 */
	HOMEWORK(11),
	/**
	 * 课时
	 */
	LESSON(12),
	/**
	 * 日历
	 */
	CALENDAR(13),
	/**
	 * 教案
	 */
	PLAN(14),
	/**
	 * 商品
	 */
	PRODUCT(15),
	/**
	 * 商品目录
	 */
	PRODUCT_CATALOG(16),
	/**
	 * 资源市场
	 */
	MARKET_RESOURCE(17),
	/**
	 * 资源池
	 */
	RESOURCE_POOL(18),
	/**
	 * 流媒体
	 */
	STREAM(19),
	/**
	 * 知识点
	 */
	KNOWPOINT(20),
	/**
	 * 书本
	 */
	QUESTIONS(21),
	/**
	 * 专辑
	 */
	STREAMS(22),
	/**
	 * 用户资源
	 */
	USER_RESOURCE(23),
	/**
	 * 投票
	 */
	VOTE(24),
	/**
	 * 投票选项
	 */
	VOTE_OPTION(25),
	/**
	 * 通知通告
	 */
	ANNOUNCE(26),
	/**
	 * 资源
	 */
	RESOURCE(27),
	/**
	 * 圈子
	 */
	CIRCLE(28),
	/**
	 * 试卷
	 */
	EXAM(29),
	/**
	 * 新书本.
	 */
	BOOK(30),
	/**
	 * 书本目录.
	 */
	BOOK_CATALOG(31),
	/**
	 * 书本库.
	 */
	BOOKS(32),
	/**
	 * 假期作业
	 */
	HOLIDAY_HOMEWORK(33),
	/**
	 * 每日练
	 */
	DAILY_PRACTICE(34),
	/**
	 * 知识点练习
	 */
	KNOWPOINT_EXERCISE(35),
	/**
	 * 章节练习
	 */
	SECTION_EXERCISE(36),
	/**
	 * 智能出卷
	 */
	SMART_PAPER(37),
	/**
	 * 金币商城商品订单
	 */
	COINS_GOODS_ORDER(38),
	/**
	 * 资源库管理用户
	 */
	VENDOR_USER(39),
	/**
	 * 教辅库
	 */
	TEACHASSISTS(40),
	/**
	 * 活动
	 */
	ACTIVITY(41),

	/**
	 * 针对性训练
	 */
	SPECIAL_TRAINING(42),
	/**
	 * 班级学情报告
	 */
	CLASS_STATISTICS_REPORT(43),
	/**
	 * 教师节活动
	 */
	TEACHERS_DAY(44),
	/**
	 * 学生学情周报告
	 */
	STUDENT_WEEK_REPORT(45),
	/**
	 * 题目申诉.
	 */
	QUESTION_APPEAL(46);

	private int value;

	Biz(int value) {
		this.value = value;
	}

	@Override
	public int getBiz() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.toString();
	}

	public int getValue() {
		return this.value;
	}

	public static Biz findByValue(int value) {
		switch (value) {
		case 0:
			return NULL;
		case 1:
			return USER;
		case 2:
			return POST;
		case 3:
			return COMMENT;
		case 4:
			return COURSE;
		case 5:
			return CLASS;
		case 6:
			return GROUP;
		case 7:
			return ALBUM;
		case 8:
			return ALBUM_PHOTO;
		case 9:
			return QUESTION;
		case 10:
			return EXERCISE;
		case 11:
			return HOMEWORK;
		case 12:
			return LESSON;
		case 13:
			return CALENDAR;
		case 14:
			return PLAN;
		case 15:
			return PRODUCT;
		case 16:
			return PRODUCT_CATALOG;
		case 17:
			return MARKET_RESOURCE;
		case 18:
			return RESOURCE_POOL;
		case 19:
			return STREAM;
		case 20:
			return KNOWPOINT;
		case 21:
			return QUESTIONS;
		case 22:
			return STREAMS;
		case 23:
			return USER_RESOURCE;
		case 24:
			return VOTE;
		case 25:
			return VOTE_OPTION;
		case 26:
			return ANNOUNCE;
		case 27:
			return RESOURCE;
		case 28:
			return CIRCLE;
		case 29:
			return EXAM;
		case 30:
			return BOOK;
		case 31:
			return BOOK_CATALOG;
		case 32:
			return BOOKS;
		case 33:
			return HOLIDAY_HOMEWORK;
		case 34:
			return DAILY_PRACTICE;
		case 35:
			return KNOWPOINT_EXERCISE;
		case 36:
			return SECTION_EXERCISE;
		case 37:
			return SMART_PAPER;
		case 38:
			return COINS_GOODS_ORDER;
		case 39:
			return VENDOR_USER;
		case 40:
			return TEACHASSISTS;
		case 41:
			return ACTIVITY;
		case 42:
			return SPECIAL_TRAINING;
		case 43:
			return CLASS_STATISTICS_REPORT;
		case 44:
			return TEACHERS_DAY;
		case 45:
			return STUDENT_WEEK_REPORT;
		case 46:
			return QUESTION_APPEAL;
		default:
			return NULL;
		}
	}
}
