package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Status;

import lombok.Data;

/**
 * <p>Description:修改快批用户接口<p>
 * @author pengcheng.yu
 * @date 2018年3月13日
 * @since 小优秀快批
 */
@Data
public class ModifyCorrectUserRequest {

	/**
	 * correct_user中的id
	 */
	private Long id;
	/**
	 * 信任值
	 */
	private Integer trustRank;
	private String schoolName;
	private String mobile;
	private String realName;
	/**
	 * 身份证号
	 */
	private String idCard;
	private Status status;
	/**
	 * 是否重置用户的认证等信息
	 */
	private Boolean isReset = false;
	private Long phaseId;
	private String passWord;
	/**
	 * uxb中的user的id
	 */
	private Long userId;
}
