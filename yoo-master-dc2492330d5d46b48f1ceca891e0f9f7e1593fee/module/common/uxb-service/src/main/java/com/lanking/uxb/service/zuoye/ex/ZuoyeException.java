package com.lanking.uxb.service.zuoye.ex;

import com.lanking.cloud.ex.AbstractException;

/**
 * 作业相关异常[1651-1750]
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public class ZuoyeException extends AbstractException {

	private static final long serialVersionUID = 4014574573257793165L;

	static final int ZUOYE_ERROR = 1650;

	/**
	 * 班级名称存在
	 */
	public static final int ZUOYE_CLASSNAME_EXIST = ZUOYE_ERROR + 1;
	/**
	 * 班级不存在
	 */
	public static final int ZUOYE_CLASS_NOT_EXIST = ZUOYE_ERROR + 2;
	/**
	 * 班级已经加入了
	 */
	public static final int ZUOYE_CLASS_JOINED = ZUOYE_ERROR + 3;
	/**
	 * 班级数量限制
	 */
	public static final int ZUOYE_CLASS_MAXLIMIT = ZUOYE_ERROR + 4;

	/**
	 * 班级的学生数量限制
	 */
	public static final int ZUOYE_CLASSSTUDENT_MAXLIMIT = ZUOYE_ERROR + 5;

	/**
	 * 班级被锁定
	 */
	public static final int ZUOYE_CLASSS_LOCKED = ZUOYE_ERROR + 6;

	/**
	 * 学生不能退出班级
	 */
	public static final int ZUOYE_STUDENT_CANNOT_EXIT = ZUOYE_ERROR + 7;

	/**
	 * 作业篮子已经存在题目
	 */
	public static final int ZUOYE_CAR_QUESTION_EXIST = ZUOYE_ERROR + 8;
	/**
	 * 作业已经被自动提交
	 */
	public static final int ZUOYE_AUTO_COMMITED = ZUOYE_ERROR + 9;

	/**
	 * 作业已经被学生提交过了
	 */
	public static final int ZUOYE_STUDENT_COMMITED = ZUOYE_ERROR + 10;
	/**
	 * 作业已经被下发
	 */
	public static final int ZUOYE_ISSUED = ZUOYE_ERROR + 11;
	/**
	 * 作业被提前下发
	 */
	public static final int ZUOYE_ISSUED_INADVANCE = ZUOYE_ERROR + 12;
	/**
	 * 作业没有全部批改完
	 */
	public static final int ZUOYE_NOT_ALLCORRECTED = ZUOYE_ERROR + 13;
	/**
	 * 此教师还未创建班级
	 */
	public static final int ZUOYE_HOLIDAY_HAS_NO_CLASS = ZUOYE_ERROR + 14;
	/**
	 * 金币不足
	 */
	public static final int ZUOYE_COINS_NOT_ENOUGH = ZUOYE_ERROR + 15;
	/**
	 * 商品已经售完
	 */
	public static final int ZUOYE_GOODS_SELL_OUT = ZUOYE_ERROR + 16;
	/**
	 * 发送验证码错误
	 */
	public static final int ZUOYE_VALID_CODE_ERROR = ZUOYE_ERROR + 17;
	/**
	 * 商品已经下架
	 */
	public static final int ZUOYE_GOODS_SOLDOUT = ZUOYE_ERROR + 18;
	/**
	 * 商品未到兑换时间
	 */
	public static final int ZUOYE_GOODS_NOTIN_SALESTIME = ZUOYE_ERROR + 19;
	/**
	 * 购买限制
	 */
	public static final int ZUOYE_GOODS_BUY_LIMIT = ZUOYE_ERROR + 20;
	/**
	 * 用户一天的抽奖次数已经使用完
	 */
	public static final int ZUOYE_LOTTERY_TIMES_LIMIT = ZUOYE_ERROR + 21;
	/**
	 * 抽奖已经过期
	 */
	public static final int ZUOYE_LOTTERY_SEASON_OVER = ZUOYE_ERROR + 22;
	/**
	 * 抽奖订单已经完成不可以再填写信息
	 */
	public static final int ZUOYE_LOTTERY_ORDER_COMPLETE = ZUOYE_ERROR + 23;
	/**
	 * 抽奖金币不足
	 */
	public static final int ZUOYE_LOTTERY_COINS_LIMIT = ZUOYE_ERROR + 24;
	/**
	 * 校级会员用户更新学校字段
	 */
	public static final int ZUOYE_SCHOOLVIP_UPDATE_SCHOOL = ZUOYE_ERROR + 25;
	/**
	 * 渠道商用户不能更新学校字段
	 */
	public static final int ZUOYE_CHANNELUSER_UPDATE_SCHOOL = ZUOYE_ERROR + 26;

	/**
	 * 录题限制
	 */
	public static final int ZUOYE_QUESTION_RECORD_LIMIT = ZUOYE_ERROR + 27;

	/**
	 * 学生已经在班级里
	 */
	public static final int ZUOYE_STUDENT_ALREADY_INCLASS = ZUOYE_ERROR + 28;

	/**
	 * 请求已经被拒绝
	 */
	public static final int ZUOYE_APPLY_ALREADY_REFUSE = ZUOYE_ERROR + 29;

	/**
	 * 班机人数超出
	 */
	public static final int ZUOYE_STUDENT_JOIN_CLASS_MAXLIMIT = ZUOYE_ERROR + 30;
	/**
	 * 教师更新作业截止时间过期
	 */
	public static final int ZUOYE_HOMEWORK_UPDATEDEADLINE_EXPIRE = ZUOYE_ERROR + 31;
	/**
	 * 教师更新作业截止时间，作业已下发
	 */
	public static final int ZUOYE_HOMEWORK_UPDATEDEADLINE_ISSUED = ZUOYE_ERROR + 32;
	/**
	 * 教师更新普通作业截止时间时，作业已被删除
	 */
	public static final int ZUOYE_HOMEWORK_UPDATEDEADLINE_DELETED = ZUOYE_ERROR + 33;
	/**
	 * 教师创建班级分组，分组名已经存在
	 */
	public static final int ZUOYE_CLASS_GROUP_NAME_EXISTS = ZUOYE_ERROR + 34;
	/**
	 * 教师创建班级分组，已达到最大数量
	 */
	public static final int ZUOYE_CLASS_GROUP_MAXLIMIT = ZUOYE_ERROR + 35;

	/**
	 * 此教师还未设置教材
	 */
	public static final int ZUOYE_HOLIDAY_HAS_NO_TEXTBOOK = ZUOYE_ERROR + 36;

	/**
	 * 抽奖次数不足
	 */
	public static final int ZUOYE_LOTTERY_USER_TIMES_LIMIT = ZUOYE_ERROR + 37;
	
	/**
	 * 批改错误申述超时
	 */
	public static final int ZUOYE_ANSWER_APPEAL_EXCEED_TIMES = ZUOYE_ERROR + 38;
	
	/**
	 * 当前题目已经在申述中
	 */
	public static final int ZUOYE_QUESTION_ALREADY_IN_APPEAL = ZUOYE_ERROR + 39;
	
	/**
	 * 当前题目已经在申述中，且有结果
	 */
	public static final int ZUOYE_QUESTION_ALREADY_PROCESSED = ZUOYE_ERROR + 40;

	public ZuoyeException() {
		super();
	}

	public ZuoyeException(int code, Object... args) {
		super(code, args);
	}

	public ZuoyeException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public ZuoyeException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}

}
