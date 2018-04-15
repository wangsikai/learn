package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Status;

import lombok.Data;

/**
 * <p>
 * Description:添加小优快批用户请求参数封装
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月13日
 * @since 小优秀快批
 */
@Data
public class AddCorrectUserRequest {

	/**
	 * 账号名.
	 */
	private String accountName;

	/**
	 * UXB用户名
	 */
	private String name;

	/**
	 * uxb用户id.
	 */
	private Long userId;

	/**
	 * 用户类型.
	 */
	private Integer correctUserType = 0;

	/**
	 * 来源.
	 */
	private Integer source = 0;

	/**
	 * 阶段.
	 */
	private Long phaseId;

	/**
	 * 状态.
	 */
	private Status status = Status.ENABLED;
	
	private String password;
}
