package com.lanking.uxb.service.base.ex;

import com.lanking.cloud.ex.AbstractException;

/**
 * 作业移动端异常[1751-2750]
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
public class YoomathMobileException extends AbstractException {

	private static final long serialVersionUID = 4014574573257793165L;

	static final int YOOMATH_MOBILE_ERROR = 1750;
	/**
	 * 账号或密码错误，请重新填写。
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_PWD_WRONG = YOOMATH_MOBILE_ERROR + 1;
	/**
	 * 不支持的用户类型登录
	 */
	public static final int YOOMATH_MOBILE_NOT_SUPPORT_USERTYPE = YOOMATH_MOBILE_ERROR + 2;
	/**
	 * 账号被禁用
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_FORBIDDEN = YOOMATH_MOBILE_ERROR + 3;
	/**
	 * 用户名已被注册，请使用其他用户名
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_NAME_EXIST = YOOMATH_MOBILE_ERROR + 4;
	/**
	 * 原密码错误
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_OLDPWD_WRONG = YOOMATH_MOBILE_ERROR + 5;
	/**
	 * 已经设置过密保。
	 */
	public static final int YOOMATH_MOBILE_HAD_PASSWORD_QUESTION = YOOMATH_MOBILE_ERROR + 6;
	/**
	 * 账号不存在
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_NOT_EXIST = YOOMATH_MOBILE_ERROR + 7;
	/**
	 * 无法找回密码,请联系客服：025-86553466，客服人员将帮助您找回密码。
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_CANNOT_FINDPASSWORD = YOOMATH_MOBILE_ERROR + 8;
	/**
	 * 验证码错误
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE = YOOMATH_MOBILE_ERROR + 9;
	/**
	 * 答案错误
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_WRONG_PASSWORD_QUESTION_ANSWER = YOOMATH_MOBILE_ERROR + 10;
	/**
	 * 找回密码的账户类型和客户端类型不一致
	 */
	public static final int YOOMATH_MOBILE_FINDPASSWORD_APP_WRONG = YOOMATH_MOBILE_ERROR + 11;
	/**
	 * 该手机号已绑定其他账号
	 */
	public static final int YOOMATH_MOBILE_MOBILE_HAS_BIND_ACCOUNT = YOOMATH_MOBILE_ERROR + 12;
	/**
	 * 登陆用户类型异常
	 */
	public static final int YOOMATH_MOBILE_LOGIN_NOT_SUPPORT = YOOMATH_MOBILE_ERROR + 13;
	/**
	 * 手机已经存在
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_MOBILE_EXIST = YOOMATH_MOBILE_ERROR + 14;
	/**
	 * 需要补充信息
	 */
	public static final int YOOMATH_MOBILE_NEED_FILL_PROFILE = YOOMATH_MOBILE_ERROR + 15;
	/**
	 * 密码错误
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_PASSWORD_WRONG = YOOMATH_MOBILE_ERROR + 16;
	/**
	 * 邮箱已经存在
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_EMAIL_EXIST = YOOMATH_MOBILE_ERROR + 17;
	/**
	 * 只能修改一次用户名
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_NAME_MODIFIED_ONCE = YOOMATH_MOBILE_ERROR + 18;

	/**
	 * 班级码错误，加入班级失败！
	 */
	public static final int YOOMATH_MOBILE_CLZZCODE_WRONG = YOOMATH_MOBILE_ERROR + 101;
	/**
	 * 连接qq出错
	 */
	public static final int YOOMATH_MOBILE_CONNECTQQ_FAIL = YOOMATH_MOBILE_ERROR + 102;
	/**
	 * 没有密码不能解绑第三方绑定
	 */
	public static final int YOOMATH_MOBILE_NOPWD_CANNOT_DELETECREDENTIAL = YOOMATH_MOBILE_ERROR + 103;
	/**
	 * 截止时间已到，作业已被自动提交。
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_ISSUED_INADVANCE = YOOMATH_MOBILE_ERROR + 104;
	/**
	 * 截止时间已到，作业已被自动提交。
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_AUTO_SUBMITTED = YOOMATH_MOBILE_ERROR + 105;
	/**
	 * 作业已被提交。
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_SUBMITTED = YOOMATH_MOBILE_ERROR + 106;
	/**
	 * 不支持此作业的答题
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_NOT_SUPPORT = YOOMATH_MOBILE_ERROR + 107;
	/**
	 * 此qq已经和其他账号绑定
	 */
	public static final int YOOMATH_MOBILE_QQ_BINDED = YOOMATH_MOBILE_ERROR + 108;
	/**
	 * 寒假作业专项已经被提交
	 */
	public static final int YOOMATH_MOBILE_HOLIDAY_HOMEWORK_SUBMITTED = YOOMATH_MOBILE_ERROR + 109;
	/**
	 * 寒假作业专项已经被提交,不能继续答题。
	 */
	public static final int YOOMATH_MOBILE_CANNOTDO_SUBMITTED_HOLIDAY_HOMEWORK = YOOMATH_MOBILE_ERROR + 110;
	/**
	 * 金币不足
	 */
	public static final int YOOMATH_MOBILE_COINS_NOT_ENOUGH = YOOMATH_MOBILE_ERROR + 111;
	/**
	 * 商品售完
	 */
	public static final int YOOMATH_MOBILE_GOODS_SELL_OUT = YOOMATH_MOBILE_ERROR + 112;

	/**
	 * 商品已经下架
	 */
	public static final int YOOMATH_MOBILE_GOODS_SOLDOUT = YOOMATH_MOBILE_ERROR + 113;
	/**
	 * 商品未到兑换时间
	 */
	public static final int YOOMATH_MOBILE_GOODS_NOTIN_SALESTIME = YOOMATH_MOBILE_ERROR + 114;
	/**
	 * 创建班级数量限制
	 */
	public static final int YOOMATH_MOBILE_CLASS_MAXLIMIT = YOOMATH_MOBILE_ERROR + 115;
	/**
	 * 班级名称存在
	 */
	public static final int YOOMATH_MOBILE_CLASSNAME_EXIST = YOOMATH_MOBILE_ERROR + 116;
	/**
	 * 作业已经下发
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_ISSUED = YOOMATH_MOBILE_ERROR + 117;

	/**
	 * 作业名称不能超过20字
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_NAME_TOO_LONG = YOOMATH_MOBILE_ERROR + 118;

	/**
	 * 题目不存在
	 */
	public static final int YOOMATH_MOBILE_QUESTION_NOT_EXIST = YOOMATH_MOBILE_ERROR + 119;

	/**
	 * 该账号已绑定了QQ号,请登录并解绑后，再绑定其他QQ号
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_HAS_BIND_QQ = YOOMATH_MOBILE_ERROR + 120;

	/**
	 * 连接weixin出错
	 */
	public static final int YOOMATH_MOBILE_CONNECTWEIXIN_FAIL = YOOMATH_MOBILE_ERROR + 121;

	/**
	 * 此weixin已经和其他账号绑定
	 */
	public static final int YOOMATH_MOBILE_WEIXIN_BINDED = YOOMATH_MOBILE_ERROR + 122;

	/**
	 * 该账号已绑定了weixin号,请登录并解绑后，再绑定其他weixin号
	 */
	public static final int YOOMATH_MOBILE_ACCOUNT_HAS_BIND_WEIXIN = YOOMATH_MOBILE_ERROR + 123;

	/**
	 * 拉取不到题目了
	 */
	public static final int YOOMATH_MOBILE_CAN_NOT_PULL_QUESTIONS = YOOMATH_MOBILE_ERROR + 124;
	/**
	 * 超过购买限制
	 */
	public static final int YOOMATH_MOBILE_GOODS_BUY_LIMIT = YOOMATH_MOBILE_ERROR + 125;
	/**
	 * 你已经加入了此班级
	 */
	public static final int YOOMATH_MOBILE_CLASS_JOINED = YOOMATH_MOBILE_ERROR + 126;
	/**
	 * 作业被删除
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_DELETE = YOOMATH_MOBILE_ERROR + 127;

	/**
	 * 会员套餐已下架
	 */
	public static final int YOOMATH_MOBILE_MEMPACK_UNPUBLISH = YOOMATH_MOBILE_ERROR + 128;

	/**
	 * 已是校级会员，不能开通个人会员
	 */
	public static final int YOOMATH_MOBILE_MEMPACK_NOT_MATCH = YOOMATH_MOBILE_ERROR + 129;

	/**
	 * 当期套餐购买重复
	 */
	public static final int YOOMATH_MOBILE_MEMPACK_BUY_DUPLICATE = YOOMATH_MOBILE_ERROR + 130;

	/**
	 * 订单生成失败
	 */
	public static final int YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE = YOOMATH_MOBILE_ERROR + 131;

	/**
	 * 订单不存在
	 */
	public static final int YOOMATH_MOBILE_MEMPACK_ORDER_NOTFOUND = YOOMATH_MOBILE_ERROR + 132;
	/**
	 * 校级会员不能修改学校
	 */
	public static final int YOOMATH_MOBILE_SCHOOLVIP_UPDATE_SCHOOL = YOOMATH_MOBILE_ERROR + 133;
	/**
	 * 会员卡号过期
	 */
	public static final int YOOMATH_MOBILE_MEMCARD_EXPIRE = YOOMATH_MOBILE_ERROR + 134;
	/**
	 * 会员卡错误(不存在，格式错误)
	 */
	public static final int YOOMATH_MOBILE_MEMCARD_ILLEGAL = YOOMATH_MOBILE_ERROR + 135;
	/**
	 * 会员卡已经使用了
	 */
	public static final int YOOMATH_MOBILE_MEMCARD_USED = YOOMATH_MOBILE_ERROR + 136;
	/**
	 * 会员卡被冻结
	 */
	public static final int YOOMATH_MOBILE_MEMCARD_FREEZE = YOOMATH_MOBILE_ERROR + 137;
	/**
	 * 班级无教师存在
	 */
	public static final int YOOMATH_MOBILE_STU_NOCATEGORYCODE = YOOMATH_MOBILE_ERROR + 138;
	/**
	 * 尝试过多输入会员卡号
	 */
	public static final int YOOMATH_MOBILE_MEMCARD_FREQUENT = YOOMATH_MOBILE_ERROR + 139;
	/**
	 * 添加至作业篮子中超出最大数量
	 */
	public static final int YOOMATH_MOBILE_ADDTOCAR_FULL = YOOMATH_MOBILE_ERROR + 140;
	/**
	 * 渠道用户不可以更新学校
	 */
	public static final int YOOMATH_MOBILE_USERCHANNEL_UPDATESCHOOL = YOOMATH_MOBILE_ERROR + 141;
	/**
	 * 用户使用会员卡开通会员、用户类型错误
	 */
	public static final int YOOMATH_MOBILE_MEMCARD_USERTYPE_ERROR = YOOMATH_MOBILE_ERROR + 142;
	/**
	 * 渠道用户不可以更改阶段
	 */
	public static final int YOOMATH_MOBILE_USERCHANNEL_UPDATEPHASE = YOOMATH_MOBILE_ERROR + 143;
	/**
	 * 班级可加入的数量超过最大数量
	 */
	public static final int YOOMATH_MOBILE_JOIN_CLASS_MAXLIMIT = YOOMATH_MOBILE_ERROR + 144;
	/**
	 * 教师普通作业更新截止时间时，作业已经过了截止时间
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_UPDATEDEADLINE_EXPIRE = YOOMATH_MOBILE_ERROR + 145;
	/**
	 * 教师普通作业更新截止时间时，作业已经下发
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_UPDATEDEADLINE_ISSUED = YOOMATH_MOBILE_ERROR + 146;
	/**
	 * 待加入的班级已经锁闭，不可以再加入
	 */
	public static final int YOOMATH_MOBILE_JOIN_CLAZZ_REQUEST_LOCK = YOOMATH_MOBILE_ERROR + 147;
	/**
	 * 处理待加入班级，学生已经在班级中了
	 */
	public static final int YOOMATH_MOBILE_JOIN_CLAZZ_REQUEST_EXISTS = YOOMATH_MOBILE_ERROR + 148;
	/**
	 * 学生申请加入的班级已经关闭或不存在/班级已经被删除或者关闭
	 */
	public static final int YOOMATH_MOBILE_JOIN_CLAZZ_REQUEST_CLOSE = YOOMATH_MOBILE_ERROR + 149;
	/**
	 * 学生的申请已经在其他端被拒绝了
	 */
	public static final int YOOMATH_MOBILE_JOIN_CLASS_REQUEST_DENIED = YOOMATH_MOBILE_ERROR + 150;
	/**
	 * 教师班级下班级组已经到最大限度
	 */
	public static final int YOOMATH_MOBILE_CLASS_GROUP_MAXLIMIT = YOOMATH_MOBILE_ERROR + 151;
	/**
	 * 教师班级班级组名称已经存在
	 */
	public static final int YOOMATH_MOBILE_CLASS_GROUP_NAME_EXISTS = YOOMATH_MOBILE_ERROR + 152;
	/**
	 * 班级已经不存在
	 */
	public static final int YOOMATH_MOBILE_CLASS_NOT_EXISTS = YOOMATH_MOBILE_ERROR + 153;

	// 科举活动相关...
	/**
	 * 科举考试---当前时间不在科举考试时间范围内
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_TIME_ERROR = YOOMATH_MOBILE_ERROR + 154;

	/**
	 * 科举考试---未到颁奖时间
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_AWARDS_TIME_ERROR = YOOMATH_MOBILE_ERROR + 155;

	/**
	 * 科举考试---收货人长度超出限制
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_AWARDS_CONTACT_ERROR = YOOMATH_MOBILE_ERROR + 156;

	/**
	 * 科举考试---收货人地址超出限制
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_AWARDS_ADDRESS_ERROR = YOOMATH_MOBILE_ERROR + 157;

	/**
	 * 科举考试---收货人手机号码不正确
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_AWARDS_MOBILE_ERROR = YOOMATH_MOBILE_ERROR + 158;

	/**
	 * 科举考试---当前用户已经错过报名时间，没有报名
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR = YOOMATH_MOBILE_ERROR + 159;

	/**
	 * 科举考试---当前时间不属于活动中
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_ACTIVITY_TIME_ERROR = YOOMATH_MOBILE_ERROR + 160;

	/**
	 * 科举考试---老师重复报名
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_REPEAT_SIGNUP_ERROR = YOOMATH_MOBILE_ERROR + 161;

	/**
	 * 微信支付暂不支持
	 */
	public static final int YOOMATH_MOBILE_WX_NOT_SUPPORT = YOOMATH_MOBILE_ERROR + 162;
	/**
	 * 当前阶段没有查看会试的权限
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_METROPOLITAN_AUTHORITY_ERROR = YOOMATH_MOBILE_ERROR + 163;
	/**
	 * 当前阶段没有查看殿试的权限
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR = YOOMATH_MOBILE_ERROR + 164;
	/**
	 * 当前不是报名阶段
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_PROCESS_SIGNUP_ERROR = YOOMATH_MOBILE_ERROR + 165;
	/**
	 * 设置自动下发截止时间已经过了最终时间内
	 */
	public static final int YOOMATH_MOBILE_HOMEWORK_SETAUTOISSUE_OVERTIME = YOOMATH_MOBILE_ERROR + 166;
	/**
	 * 验证码不正确
	 */
	public static final int YOOMATH_MOBILE_VERIFYCODE_INVALID = YOOMATH_MOBILE_ERROR + 167;

	/**
	 * 老师阶段为空
	 */
	public static final int YOOMATH_MOBILE_PHASE_NULL = YOOMATH_MOBILE_ERROR + 168;
	/**
	 * 学情报告没有分享，当前报告没有被分享
	 */
	public static final int YOOMATH_MOBILE_REPORT_SHARE_ERROR = YOOMATH_MOBILE_ERROR + 169;
	/**
	 * 班级转让，班级被删除或关闭
	 */
	public static final int YOOMATH_MOBILE_TRANSFER_CLAZZ_REQUEST_CLOSE = YOOMATH_MOBILE_ERROR + 170;
	/**
	 * 该教师班级数已达上限
	 */
	public static final int YOOMATH_MOBILE_TRANSFER_CLASS_MAXLIMIT = YOOMATH_MOBILE_ERROR + 171;
	/**
	 * 教师节标签已达上限
	 */
	public static final int YOOMATH_MOBILE_TEACHERSDAY_TAG_MAXLIMIT = YOOMATH_MOBILE_ERROR + 172;
	/**
	 * 教师节标签为空
	 */
	public static final int YOOMATH_MOBILE_TEACHERSDAY_TAG_NULL = YOOMATH_MOBILE_ERROR + 173;
	/**
	 * 学生教材为空
	 */
	public static final int YOOMATH_MOBILE_STU_TEXTBOOKCODE_NULL = YOOMATH_MOBILE_ERROR + 174;

	/**
	 * IOS客户端IAP应用内支付用户身份不正确
	 */
	public static final int YOOMATH_MOBILE_IOSIAP_USER_ERROR = YOOMATH_MOBILE_ERROR + 175;

	/**
	 * 会员订单已被支付或处理过
	 */
	public static final int YOOMATH_MOBILE_MEMPACK_ORDER_MANAGED = YOOMATH_MOBILE_ERROR + 176;

	/**
	 * 会员订单支付方式不正确
	 */
	public static final int YOOMATH_MOBILE_MEMPACK_ORDER_PLATFORM_ERROR = YOOMATH_MOBILE_ERROR + 177;

	/**
	 * 科举考试---已超过成绩公布时间不能冲刺
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_ACTIVITY_SPRINT_TIMEOUT = YOOMATH_MOBILE_ERROR + 178;

	/**
	 * 科举考试---已进行过相同冲刺
	 */
	public static final int YOOMATH_MOBILE_IMPERIAL_ACTIVITY_SPRINT_SAME = YOOMATH_MOBILE_ERROR + 179;
	
	/**
	 * 留言超过数量限制
	 */
	public static final int YOOMATH_MOBILE_MESSAGES_COUNT_EXCEED = YOOMATH_MOBILE_ERROR + 180;

	/**
	 * iOS版本过低不可以使用
	 */
	public static final int YOOMATH_MOBILE_IOS_UNCORRECT_CLIENT_VERSION = YOOMATH_MOBILE_ERROR + 1000;

	public YoomathMobileException() {
		super();
	}

	public YoomathMobileException(int code, Object... args) {
		super(code, args);
	}

	public YoomathMobileException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public YoomathMobileException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}

}
