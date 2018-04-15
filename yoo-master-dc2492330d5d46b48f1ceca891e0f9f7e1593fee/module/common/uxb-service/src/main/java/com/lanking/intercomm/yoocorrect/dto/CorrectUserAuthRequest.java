package com.lanking.intercomm.yoocorrect.dto;


import lombok.Data;

/**
 * <p>Description:快批用户资格审批认证<p>
 * @author pengcheng.yu
 * @date 2018年3月15日
 * @since 小优秀快批
 */
@Data
public class CorrectUserAuthRequest {
	private Long correctUserId;
	private Integer idCardAuthStatus;
	private Integer qualificationAuthStatus;
	private String tqNoPassReason;
	private String idCardNoPassReason;
}
