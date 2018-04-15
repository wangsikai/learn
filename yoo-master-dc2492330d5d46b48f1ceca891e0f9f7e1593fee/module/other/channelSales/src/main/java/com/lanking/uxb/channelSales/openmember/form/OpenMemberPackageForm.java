package com.lanking.uxb.channelSales.openmember.form;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 开通会员套餐form
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class OpenMemberPackageForm {
	// 会员套餐id
	private Long memberPackageId;
	// 开通几月 若传此值，则开始时间和结束时间会忽略
	private Integer month;
	// 开通结束时间
	private String endAt;
	// 为哪个用户开通
	private List<Long> userIds;
	// 校级会员学校id
	private Long schoolId;
	// 套餐会员类型
	private MemberType memberType;
	// 是否开放教师
	private UserType userType;
	// 总额
	private BigDecimal totalPrice;
	// 渠道商Id
	private Integer channelCode;

	public Long getMemberPackageId() {
		return memberPackageId;
	}

	public void setMemberPackageId(Long memberPackageId) {
		this.memberPackageId = memberPackageId;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}
}
