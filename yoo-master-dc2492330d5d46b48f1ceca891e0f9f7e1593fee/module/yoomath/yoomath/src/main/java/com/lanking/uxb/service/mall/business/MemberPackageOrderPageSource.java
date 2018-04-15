package com.lanking.uxb.service.mall.business;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员套餐订单来源.<br>
 * 用于支付成功后的来源跳转等动作
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月5日
 */
public enum MemberPackageOrderPageSource implements Valueable {
	DEFAULT(0),

	/**
	 * 精品试卷列表.
	 */
	EXAM_LIST(1),

	/**
	 * 收藏试卷列表.
	 */
	FAVORITE_LIST(2),

	/**
	 * 手工组卷试卷详情.
	 */
	EXAM_DETAIL_ASSEMBLE(3),

	/**
	 * 教师作业管理-假期作业.
	 */
	TEA_HOLIDAY_HOMEWORK_LIST(4),

	/**
	 * 用户个人中心详情.
	 */
	USER_CENTER(5),

	/**
	 * 学生错题下载页面.
	 */
	STU_FALL_DOWNLOAD(6),

	/**
	 * 学生教学诊断页面.
	 */
	STU_DIAGNOSTIC(7),

	/**
	 * 学生错题统计页面.
	 */
	STU_FALL_STATISTICS(8),

	/**
	 * 教师教学诊断页面.
	 */
	TEA_DIAGNOSTIC(9),

	/**
	 * 教师办公桌页面.
	 */
	TEA_DESK(10),

	/**
	 * 布置作业页面.
	 */
	HOMEWORK_PUBLISH(11),
	
	/**
	 * 学生作业查看页面.
	 */
	STU_HOMEWORK_VIEW(12),
	
	/**
	 * 学生桌面.
	 */
	STU_DESK(13),
	
	/**
	 * 教师作业查看界面
	 */
	TEA_HOMEWORK_VIEW(14),
	
	/**
	 * 题目详情.
	 */
	QUESTION_DETAIL(15);

	private int value;

	MemberPackageOrderPageSource(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}
}
