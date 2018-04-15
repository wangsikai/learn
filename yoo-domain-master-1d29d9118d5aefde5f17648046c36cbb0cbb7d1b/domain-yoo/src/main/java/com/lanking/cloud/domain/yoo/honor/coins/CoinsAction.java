package com.lanking.cloud.domain.yoo.honor.coins;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 金币值动作
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CoinsAction implements Valueable {
	NULL(0),
	/**
	 * 每日签到
	 */
	DAILY_CHECKIN(1),
	/**
	 * 创建班级<br>
	 * 一次性<br>
	 * 50 <br>
	 */
	CREATE_CLAZZ(2),
	/**
	 * 布置作业<br>
	 * 上限30<br>
	 * 10 <br>
	 */
	PUBLISH_HOMEWORK(3),
	/**
	 * 下发作业<br>
	 * 15<br>
	 * 5 <br>
	 */
	ISSUE_HOMEWORK(4),
	/**
	 * 单日自主练习+错题练习 下发作业<br>
	 * 20 上限<br>
	 * 1<br>
	 */
	DOING_DAILY_EXERCISE(5),
	/**
	 * 提交作业 每日<br>
	 * 10 上限<br>
	 * 10<br>
	 */
	SUBMIT_HOMEWORK(6),
	/**
	 * 班级学生人数超过20<br>
	 * 300上限<br>
	 * 100<br>
	 */
	CLASS_STU_NUM(7),
	/**
	 * 升级奖励<br>
	 * 无上限<br>
	 * 50<br>
	 */
	GRADE_UP_REWARD(8),
	/**
	 * 老师布置作业完成率 完成率80%以上得10金币、90%以上得20金币、100%得30金币<br>
	 * 上限30<br>
	 * 10-30<br>
	 */
	TEA_HOMEOWORK_RESULT(9),
	/**
	 * 在线时长超过30分钟<br>
	 * 5<br>
	 * 5<br>
	 */
	ONLINE_TIME_ENOUGH(10),
	/**
	 * 学生作业完成效果 正确率80%以上得10金币、90%以上得20金币、100%得30金币<br>
	 * 上限30<br>
	 * 10-30<br>
	 */
	STU_HOMEWORK_RESULT(11),
	/**
	 * 单次作业排名前5, 单次作业完成人数≥20人，第一名到第五名依次可得50、40、30、20、10个金币
	 * 
	 */
	ONE_HOMEWORK_TOP5(12),
	/**
	 * 班级作业平均正确率进入TOP5，奖励50金币，以后每占领1天奖励1金币 如 50 51 51...
	 * 
	 */
	CLASS_HOMEWORK_TOP5(13),
	/**
	 * 单次作业与上一次作业比较，进步最快的前3名分别得到30、20、10金币
	 */
	MOST_IMPROVED_STU(14),
	/**
	 * 首次布置作业奖励100
	 */
	FIRST_PUBLISH_HOMEWORK(15),
	/**
	 * 30天内首次布置假期作业<br>
	 * 50<br>
	 */
	FIRST_PUBLISH_HOLIDAY_HOMEWORK(16),
	/**
	 * 完成假期作业 <br>
	 * 每份假期作业不超过100<br>
	 * 单次20
	 */
	FINISH_HOLIDAY_HOMEWORK(17),
	/**
	 * 学生假期作业完成率在80%
	 */
	TEA_HOLIDAY_HOMEWORK_RESULT(18),
	/**
	 * 金币兑换商品
	 */
	BUY_COINS_GOODS(19),
	/**
	 * 金币兑换商品失败，退换金币
	 */
	BUY_COINS_GOODS_FAIL(20),
	/**
	 * 绑定QQ
	 */
	BIND_QQ(21),
	/**
	 * 金币兑换错题本文档
	 */
	BUY_COINS_FALLIBLE_DOC(22),
	/**
	 * 绑定微信
	 */
	BIND_WEIXIN(23),
	/**
	 * 抽奖消耗金币
	 */
	LOTTERY_DRAW(24),
	/**
	 * 抽奖获得金币
	 */
	LOTTERY_DRAW_EARN_COINS(25),
	/**
	 * 活动抽奖获得金币
	 */
	LOTTERY_ACTIVITY_EARN_COINS(26),
	/**
	 * 绑定手机
	 */
	BIND_MOBILE(27),
	
	/**
	 * 错题申述
	 */
	QUESTION_APPEAL(28);

	private final int value;

	private CoinsAction(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
