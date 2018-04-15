package com.lanking.uxb.zycon.common.ex;

import com.lanking.cloud.ex.support.AbstractSupportSystemException;

/**
 * 悠数学后台系统异常汇总
 * 
 * @author wangsenhao
 *
 */
public class YoomathConsoleException extends AbstractSupportSystemException {

	private static final long serialVersionUID = -3378041117495339284L;

	static final int YOOMATH_CONSOLE_ERROR = 1200000;

	/**
	 * 练习名称不能为空
	 */
	public static int EXERCISE_NAME_BLANK = YOOMATH_CONSOLE_ERROR + 1;
	/**
	 * 练习不存在
	 */
	public static int EXERCISE_NOT_EXIST = YOOMATH_CONSOLE_ERROR + 2;
	/**
	 * 习题页中的习题已经存在
	 */
	public static int EXERCISE_QUESTION_EXIST = YOOMATH_CONSOLE_ERROR + 3;
	/**
	 * 练习名称重复
	 */
	public static int EXERCISE_NAME_MULTIPLE = YOOMATH_CONSOLE_ERROR + 4;
	/**
	 * 一个题目编号对应多个题目
	 */
	public static int BAD_QUESTION_CODE = YOOMATH_CONSOLE_ERROR + 5;
	/**
	 * 不能预置校本题目
	 */
	public static int SCHOOLQUESTION_FORBIDDEN = YOOMATH_CONSOLE_ERROR + 6;
	/**
	 * 题目状态不正确
	 */
	public static int BAD_QUESTION_STATUS = YOOMATH_CONSOLE_ERROR + 7;
	/**
	 * 作业已经发布,不能重新设置时间
	 */
	public static final int HOMEWORK_HAS_PUBLISH = YOOMATH_CONSOLE_ERROR + 8;
	/**
	 * 作业时间非法
	 */
	public static final int HOMEWORK_TIME_ILLEGAL = YOOMATH_CONSOLE_ERROR + 9;
	/**
	 * 作业不存在
	 */
	public static final int HOMEWORK_NOT_EXIST = YOOMATH_CONSOLE_ERROR + 10;
	/**
	 * 不能查看作业批改结果
	 */
	public static final int HOMEWORK_CANNOT_VIEWRESULT = YOOMATH_CONSOLE_ERROR + 11;
	/**
	 * 作业里面习题已经存在
	 */
	public static final int HOMEWORK_QUESTION_EXIST = YOOMATH_CONSOLE_ERROR + 12;
	/**
	 * 作业没有批改完
	 */
	public static final int HOMEWORK_NOT_CORRECTED = YOOMATH_CONSOLE_ERROR + 13;
	/**
	 * 学生作业还没有提交
	 */
	public static final int HOMEWORK_NOT_COMMITED = YOOMATH_CONSOLE_ERROR + 14;
	/**
	 * 作业已经下发了不可以再进行批改
	 */
	public static final int HOMEWORK_ISSUED = YOOMATH_CONSOLE_ERROR + 15;

	/**
	 * 学校不存在
	 */
	public static final int QUESTION_SCHOOL_NOT_EXISTS = YOOMATH_CONSOLE_ERROR + 16;

	/**
	 * 添加时学校已经存在并在启用状态下
	 */
	public static final int QUESTION_SCHOOL_ADD_EXISTS = YOOMATH_CONSOLE_ERROR + 17;

	/**
	 * 添加学校按学校名进行搜索不可以为空
	 */
	public static final int QUESTION_SCHOOL_ADD_NAME_IS_NULL = YOOMATH_CONSOLE_ERROR + 18;

	/**
	 * 绑定的教师已经存在
	 */
	public static final int QUESTION_SCHOOL_BIND_TEACHER_EXISTS = YOOMATH_CONSOLE_ERROR + 19;

	/**
	 * 绑定的教师已经认证通过
	 */
	public static final int QUESTION_SCHOOL_BIND_TEACHER_RZ = YOOMATH_CONSOLE_ERROR + 20;

	/**
	 * 已经认证过的教师不可以解除绑定
	 */
	public static final int QUESTION_SCHOOL_UNBIND_TEACHER_RZ = YOOMATH_CONSOLE_ERROR + 21;

	/**
	 * 绑定的教师不存在
	 */
	public static final int QUESTION_SCHOOL_BIND_TEACHER_NOT_EXISTS = YOOMATH_CONSOLE_ERROR + 22;

	/**
	 * 已经绑定在本校
	 */
	public static final int QUESTION_SCHOOL_BIND_OWN_SCHOOL = YOOMATH_CONSOLE_ERROR + 23;

	/**
	 * 系统配置key值重复
	 */
	public static final int PARAMETER_KEY_REPEAT = YOOMATH_CONSOLE_ERROR + 24;

	/**
	 * 商品组下存在已上架的商品
	 */
	public static final int GROUP_GOODS_HAS_PUBLISH = YOOMATH_CONSOLE_ERROR + 25;

	/**
	 * 学校和渠道的对应关系不存在
	 */
	public static final int USER_CHANNEL_SCHOOL_ERROR = YOOMATH_CONSOLE_ERROR + 26;

	/**
	 * 活动操作时间异常
	 */
	public static final int ACTIVITY_UPDATEDATE_NO_NOW = YOOMATH_CONSOLE_ERROR + 27;
	/**
	 * 非老师用户
	 */
	public static final int NOT_TEACHER_ERROR = YOOMATH_CONSOLE_ERROR + 28;
	/**
	 * 非渠道用户
	 */
	public static final int NOT_CHANNEL_USER_ERROR = YOOMATH_CONSOLE_ERROR + 29;
	/**
	 * 非当前学校用户
	 */
	public static final int NOT_CURRENT_SCHOOL_ERROR = YOOMATH_CONSOLE_ERROR + 30;
	/**
	 * 老师班级数达到上限
	 */
	public static final int TEACHER_CLASSNUM_MAX_ERROR = YOOMATH_CONSOLE_ERROR + 31;
	/**
	 * 班级状态异常
	 */
	public static final int CLASS_STATUS_ABNORMAL_ERROR = YOOMATH_CONSOLE_ERROR + 32;

	public YoomathConsoleException(int code, Object... args) {
		super(code, args);
	}

}
