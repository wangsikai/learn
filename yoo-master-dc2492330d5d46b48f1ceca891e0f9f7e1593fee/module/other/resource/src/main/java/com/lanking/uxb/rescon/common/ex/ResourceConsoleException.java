package com.lanking.uxb.rescon.common.ex;

import com.lanking.cloud.ex.support.AbstractSupportSystemException;

/**
 * 中央资源库系统异常父类
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年7月21日
 */
public class ResourceConsoleException extends AbstractSupportSystemException {

	private static final long serialVersionUID = -6193264047785012405L;

	static final int RESCON_ERROR = 1100000;

	/**
	 * Analysis 分析字数超过限制.
	 */
	public static final int ANALYSIS_OUT_OF_LENGTH = RESCON_ERROR + 1;

	/**
	 * Hint 提示字数超过限制.
	 */
	public static final int HINT_OUT_OF_LENGTH = RESCON_ERROR + 2;

	/**
	 * choice 选项超过限制.
	 */
	public static final int CHOICE_OUT_OF_LENGTH = RESCON_ERROR + 3;

	/**
	 * 填空答案asciimath与latex数量不一致.
	 */
	public static final int ASCII_LATEX_ERROR_LENGTH = RESCON_ERROR + 4;

	/**
	 * 相似题组已被处理过.
	 */
	public static final int SIMILAR_HANDLED = RESCON_ERROR + 5;

	/**
	 * 题目重复
	 */
	public static final int QUESTION_EXISTED = RESCON_ERROR + 6;

	/**
	 * 书本名称超过字数限制.
	 */
	public static final int NAME_OUT_OF_LENGTH = RESCON_ERROR + 7;

	/**
	 * 书本描述超过字数限制.
	 */
	public static final int DESC_OUT_OF_LENGTH = RESCON_ERROR + 8;

	/**
	 * 目录名称过长.
	 */
	public static final int CATALOG_NAME_TOO_LONG = RESCON_ERROR + 9;

	/**
	 * 非待发布状态.
	 */
	public static final int VERSION_NOCHECK_STATUS_ERROR = RESCON_ERROR + 10;

	/**
	 * 商品已经转换过书本
	 */
	public static final int PRODUCT_CONVERTED = RESCON_ERROR + 11;

	/**
	 * 版本与目录不对应
	 */
	public static final int VERSION_CATALOG_NOT_MATCH = RESCON_ERROR + 12;

	/**
	 * 教辅名称超过字数限制.
	 */
	public static final int NAME_OUT_OF_LENGTH_TEACHASSIST = RESCON_ERROR + 13;

	/**
	 * 教辅描述超过字数限制.
	 */
	public static final int DESC_OUT_OF_LENGTH_TEACHASSIST = RESCON_ERROR + 14;

	/**
	 * 目录名称过长.
	 */
	public static final int CATALOG_NAME_TOO_LONG_TEACHASSIST = RESCON_ERROR + 15;

	/**
	 * 目录下有内容.
	 */
	public static final int CATALOG_RESOURCE_NOT_NULL = RESCON_ERROR + 16;

	/**
	 * 题目与学科阶段不对应
	 */
	public static final int SUBJECT_PHASE_NOT_MATCH_KNOWPOINT = RESCON_ERROR + 17;

	/**
	 * 不能添加校本题库的题目
	 */
	public static final int SCHOOL_QUESTION_ERROR = RESCON_ERROR + 18;

	/**
	 * 题目较验未通过
	 */
	public static final int QUESTION_NOT_PASS = RESCON_ERROR + 19;

	/**
	 * 题目编码的学科阶段不匹配
	 */
	public static final int SUBJECT_PHASE_NOT_MATCH = RESCON_ERROR + 20;

	/**
	 * 题目状态不正确
	 */
	public static final int QUESTION_STATUS_NOT_MATCH = RESCON_ERROR + 21;

	/**
	 * 供应商不正确
	 */
	public static final int QUESTION_VENDOR_NOT_MATCH = RESCON_ERROR + 22;

	/**
	 * 不能添加校本题目
	 */
	public static final int QUESTION_SCHOOL_NOT_MATCH = RESCON_ERROR + 23;

	/**
	 * 书本版本已经被发布
	 */
	public static final int BOOK_VERSION_PUBLISHED = RESCON_ERROR + 24;
	
	/**
	 * 题目不符合katex解析
	 */
	public static final int QUESTION_NOT_KATEX_SPECS = RESCON_ERROR + 25;

	public ResourceConsoleException(int code, Object... args) {
		super(code, args);
	}
}
