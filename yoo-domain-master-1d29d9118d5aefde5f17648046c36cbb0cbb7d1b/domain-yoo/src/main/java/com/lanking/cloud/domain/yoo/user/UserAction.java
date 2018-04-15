package com.lanking.cloud.domain.yoo.user;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 统一的用户行为定义枚举（根据场景需求添加，不局限于某个业务）.
 * <p>
 * 用于动作触发事件统一使用
 * </p>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum UserAction implements Valueable {
	/**
	 * 默认，无行为.
	 */
	DEFUALT(0),

	/**
	 * 用户登录.
	 */
	LOGIN(1),

	/**
	 * 用户主动注销.
	 */
	LOGOUT(2),

	/**
	 * 布置普通作业.
	 */
	PUBLISH_HOMEWORK(3),

	/**
	 * 布置假期作业.
	 */
	PUBLISH_HOLIDAY_HOMEWORK(4),

	/**
	 * 下发作业.
	 */
	ISSUE_HOMEWORK(5),

	/**
	 * 分享抽奖活动.
	 */
	SHARE_LOTTERY_ACTIVITY(6),

	/**
	 * 提交作业.
	 */
	SUBMIT_HOMEWORK(7),

	/**
	 * 提交每日练.
	 */
	SUBMIT_DAILY_PRACTISE(8),

	/**
	 * 提交章节练习.
	 */
	SUBMIT_SECTION_PRACTISE(9),

	/**
	 * 提交错题练习.
	 */
	SUBMIT_FALLIBLE_PRACTISE(10),

	/**
	 * 开通普通会员.
	 */
	OPEN_VIP(11),

	/**
	 * 绑定手机.
	 */
	BIND_MOBILE(12),

	/**
	 * 绑定QQ.
	 */
	BIND_QQ(13),

	/**
	 * 绑定微信.
	 */
	BIND_WEIXIN(14);

	private int value;

	private UserAction(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static UserAction findByValue(int value) {
		switch (value) {
		case 0:
			return DEFUALT;
		case 1:
			return LOGIN;
		case 2:
			return LOGOUT;
		case 3:
			return PUBLISH_HOMEWORK;
		case 4:
			return PUBLISH_HOLIDAY_HOMEWORK;
		case 5:
			return ISSUE_HOMEWORK;
		case 6:
			return SHARE_LOTTERY_ACTIVITY;
		case 7:
			return SUBMIT_HOMEWORK;
		case 8:
			return SUBMIT_DAILY_PRACTISE;
		case 9:
			return SUBMIT_SECTION_PRACTISE;
		case 10:
			return SUBMIT_FALLIBLE_PRACTISE;
		case 11:
			return OPEN_VIP;
		case 12:
			return BIND_MOBILE;
		case 13:
			return BIND_QQ;
		case 14:
			return BIND_WEIXIN;
		default:
			return DEFUALT;
		}
	}
}
