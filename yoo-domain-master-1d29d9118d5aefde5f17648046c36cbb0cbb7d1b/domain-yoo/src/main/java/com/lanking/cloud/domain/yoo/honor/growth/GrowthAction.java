package com.lanking.cloud.domain.yoo.honor.growth;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 成长值动作
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum GrowthAction implements Valueable {
	NULL(0),
	/**
	 * 每日签到<br>
	 * 10 <br>
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
	 * 上限60<br>
	 * 20 <br>
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
	 * 20 上限<br>
	 * 20<br>
	 */
	SUBMIT_HOMEWORK(6),
	/**
	 * 班级学生人数超过20<br>
	 * 300上限<br>
	 * 100<br>
	 */
	CLASS_STU_NUM(7),
	/**
	 * 在线时长超过30分钟<br>
	 * 5<br>
	 * 5<br>
	 */
	ONLINE_TIME_ENOUGH(8),
	/**
	 * 首次布置作业奖励100<br>
	 * 100<br>
	 * 100<br>
	 */
	FIRST_PUBLISH_HOMEWORK(9),
	/**
	 * 30天内首次布置寒假作业<br>
	 * 50<br>
	 */
	FIRST_PUBLISH_HOLIDAY_HOMEWORK(10),
	/**
	 * 完成假期作业 <br>
	 * 每份假期作业不超过100<br>
	 * 单次20
	 */
	FINISH_HOLIDAY_HOMEWORK(11);

	private final int value;

	private GrowthAction(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
