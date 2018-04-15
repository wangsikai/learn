package com.lanking.uxb.channelSales.common.ex;

import com.lanking.cloud.ex.support.AbstractSupportSystemException;

/**
 * 渠道销售系统异常
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年12月1日
 */
public class ChannelSalesConsoleException extends AbstractSupportSystemException {

	private static final long serialVersionUID = -2388879758685911090L;

	// 不包含1300000
	private static final int CHANNELSALES_CONSOLE_ERROR = 1300000;

	/**
	 * 您输入的渠道号不存在
	 */
	public static int CHANNELCODE_NOT_EXIST = CHANNELSALES_CONSOLE_ERROR + 1;

	/** 开通会员-渠道代码与其他不同/不存在 */
	public static final int CHANNELSALES_OPENMEMBER_CODEERROR = CHANNELSALES_CONSOLE_ERROR + 2;
	/** 开通会员-非本渠道用户 */
	public static final int CHANNELSALES_OPENMEMBER_NOT_CHANNELUSER = CHANNELSALES_CONSOLE_ERROR + 3;
	/** 开通会员-非正确用户类型 */
	public static final int CHANNELSALES_OPENMEMBER_NOT_CORRECT_USERTYPE = CHANNELSALES_CONSOLE_ERROR + 4;
	/** 开通会员-已经是校级会员，再开通普通会员错误 */
	public static final int CHANNELSALES_OPENMEMBER_ISSCHOOL = CHANNELSALES_CONSOLE_ERROR + 5;
	/** 开通会员-账户名不存在 */
	public static final int CHANNELSALES_OPENMEMBER_ACCOUNT_NOT_EXISTS = CHANNELSALES_CONSOLE_ERROR + 6;
	/** 开通会员-导入条数超过最大值 */
	public static final int CHANNELSALES_OPENMEMBER_IMPORT_MAX_COUNT = CHANNELSALES_CONSOLE_ERROR + 7;
	/** 数据格式错误-例如渠道商code中包含非数字 */
	public static final int CHANNELSALES_OPENMEMBER_DATAFORMAT_ERROR = CHANNELSALES_CONSOLE_ERROR + 8;
	/** 指定渠道关联内容异常 */
	public static final int CHANNELSALES_MEMBERPACKAGEGROUPCHANNEL_ERROR = CHANNELSALES_CONSOLE_ERROR + 9;
	/** 开通超过最大限度 */
	public static final int CHANNELSALES_OPENMEMBER_LIMIT_ERROR = CHANNELSALES_CONSOLE_ERROR + 10;

	/** 导出学生纸质报告，月份选择错误 */
	public static final int CHANNELSALES_STUREPORT_MONTHSELECT_ERROR = CHANNELSALES_CONSOLE_ERROR + 11;

	public ChannelSalesConsoleException(int code, Object... args) {
		super(code, args);
	}
}
