package com.lanking.intercomm.yoocorrect.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Data;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月13日
 * @since 小优快批
 */
@Data
public class CorrectUserResponse implements Serializable {
	private static final long serialVersionUID = 5526241434140737819L;

	private Long id;

	/**
	 * 快批用户类型.
	 */
	private CorrectUserType correctUserType;

	/**
	 * 用户ID（根据不同的CorrectUserType，对应UXB服务中的各自用户ID）.
	 * 
	 */
	private Long userId;

	/**
	 * 快批用户头像.
	 */
	private Long avatarId;

	/**
	 * 快批系统中的用户名.
	 */
	private String name;

	/**
	 * 快批系统中的手机号码.
	 */
	private String mobile;

	/**
	 * 教学阶段（对应uxb phase id）.
	 */
	private Long phaseId;

	/**
	 * 学校ID（对应uxb school id）.
	 */
	private Long schoolId;

	/**
	 * 教师资格认证状态.
	 */
	private CorrectAuthStatus qualificationAuthStatus;

	/**
	 * 教师资格证照片ID列表
	 */
	private List<Long> qualificationImgs = Lists.newArrayList();

	/**
	 * 身份实名认证状态.
	 */
	private CorrectAuthStatus idCardAuthStatus;

	/**
	 * 身份证号.
	 */
	private String idCard;

	/**
	 * 真实姓名.
	 */
	private String realname;

	/**
	 * 身份证照片ID列表
	 */
	private List<Long> idCardImgs = Lists.newArrayList();

	/**
	 * 信任值.
	 */
	private Integer trustRank = 0;

	/**
	 * 总余额.
	 */
	private BigDecimal balance;

	/**
	 * 创建时间.
	 */
	private Date createAt;

	/**
	 * 支付宝帐号.
	 */
	private String alipayNo;

	/**
	 * 快批用户状态.
	 * <p>
	 * 与uxb用户状态独立，但可能同时管理
	 * </p>
	 */
	private Status status = Status.ENABLED;

	/**
	 * 是否通过模拟批改
	 */
	private boolean isPassSimulation = false;

	/**
	 * 账户名称
	 */
	private String accountName;

	/**
	 * 学校名称
	 */
	private String schoolName;

	/**
	 * 教师资格认证不通过原因
	 */
	private String tQualificationNoPassReason;

	/**
	 * 实名认证不通过原因
	 */
	private String idCardNoPassReason;

	// --- 以下为可选数据 ---

	/**
	 * 可提现余额.
	 */
	private BigDecimal obalance;
}
