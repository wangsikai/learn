package com.lanking.uxb.ycoorect.ex;

import com.lanking.cloud.ex.AbstractException;

/**
 * 小优快批相关错误码.
 * 
 * @author wanlong.che
 *
 */
public class YooCorrectException extends AbstractException {
	private static final long serialVersionUID = -7335145431705570664L;

	public YooCorrectException() {
		super();
	}

	public YooCorrectException(String message) {
		super(message);
	}

	public YooCorrectException(int code, Object... args) {
		super(code, args);
	}

	static final int YOO_CORRECT_ERROR = 150100;

	/**
	 * 请用教师账户登录.
	 */
	public final static int NEED_TEACHER_LOGIN = YOO_CORRECT_ERROR + 1;

	/**
	 * 不在提现时间范围内.
	 */
	public final static int WRONG_TIME_WITHDRAW = YOO_CORRECT_ERROR + 2;

	/**
	 * 可提现余额不足.
	 */
	public final static int BALANCE_NOT_ENOUGH = YOO_CORRECT_ERROR + 3;

	/**
	 * 超过最大单日提现金额.
	 */
	public final static int MAX_DAY_WITHDRAW_AMOUNT = YOO_CORRECT_ERROR + 4;

	/**
	 * 支付宝帐号未找到.
	 */
	public final static int ALIPAY_NOT_FOUND = YOO_CORRECT_ERROR + 5;

	/**
	 * 支付宝账号和姓名不匹配.
	 */
	public final static int ALIPAY_NAME_ERROR = YOO_CORRECT_ERROR + 6;

	/**
	 * 超过当日提现次数.
	 */
	public final static int MAX_DAY_WITHDRAW_COUNT = YOO_CORRECT_ERROR + 7;

	/**
	 * 您的认证正在审核或已通过，请勿重复提交.
	 */
	public final static int QUALIFICA_ALREADY_SUBMIT = YOO_CORRECT_ERROR + 8;

	/**
	 * 身份证号码不正确.
	 */
	public final static int IDCARD_WRONG = YOO_CORRECT_ERROR + 9;

	/**
	 * 手机号码不正确.
	 */
	public final static int MOBILE_WRONG = YOO_CORRECT_ERROR + 10;

	/**
	 * 您还没有进行过手机认证.
	 */
	public final static int MOBILE_NOT_AUTH_WRONG = YOO_CORRECT_ERROR + 11;

	/**
	 * 请首先验证原手机.
	 */
	public final static int NEED_CHECK_OLD_MOBILE = YOO_CORRECT_ERROR + 12;

	/**
	 * 请首先设置教学阶段.
	 */
	public final static int NEED_SET_PHASE = YOO_CORRECT_ERROR + 13;

	/**
	 * 未到提现时间.
	 */
	public final static int NOT_RIGHT_WITHDRAW_TIME = YOO_CORRECT_ERROR + 14;

	/**
	 * 低于最小提现金额.
	 */
	public final static int LOW_WITHDRAW_MONEY = YOO_CORRECT_ERROR + 15;

	/**
	 * 超过最大可提现金额.
	 */
	public final static int OUT_WITHDRAW_MONEY = YOO_CORRECT_ERROR + 16;

	/**
	 * 当日提现仅限1次，您今日已提现.
	 */
	public final static int OUT_WITHDRAW_TODAY = YOO_CORRECT_ERROR + 17;

	/**
	 * 密码不能与用户名一样.
	 */
	public final static int PASSWOR_ACCOUNT_SAME = YOO_CORRECT_ERROR + 18;

	/**
	 * 该题目已被批改完成，请重新接收题目.
	 */
	public final static int QUESTION_ALLREADY_CORRECT = YOO_CORRECT_ERROR + 19;

	/**
	 * 手机号已存在.
	 */
	public final static int MOBILE_EXISTS = YOO_CORRECT_ERROR + 20;
}
