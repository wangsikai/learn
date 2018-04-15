package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Status;

import lombok.Data;

/**
 * <p>
 * Description:小优快批批改用户查询请求参数封装
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月13日
 * @since 小优快批
 */
@Data
public class CorrectUserRequest {

	/**
	 * 账户名
	 */
	private String accountName;
	/**
	 * 姓名
	 */
	private String realName;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 学校
	 */
	private String schoolName;
	/**
	 * 教学阶段（对应uxb phase id）
	 */
	private Long phaseId;
	/**
	 * 用户状态
	 */
	private Status status;
	
	/**
	 * 是否需要查询待认证审核的用户数量
	 */
	private boolean queryNeedAuthUserCount = false;
}
