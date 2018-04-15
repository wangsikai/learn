package com.lanking.uxb.channelSales.memberPackage.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 会员卡
 * 
 * @author zemin.song
 * @version 2016年11月15日 14:19:40
 */
public class VMemberPackageCard implements Serializable {

	private static final long serialVersionUID = 8828314336613280266L;

	private String code;

	private UserType userType;

	private MemberType memberType;

	private Integer month;

	private BigDecimal price;

	private String memo;

	private String createName;

	private Date createAt;

	private MemberPackageCardStatus status;

	private Long updateId;

	private String updateName;

	private Date useTime;

	private Date updateAt;

	private Date endAt;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public MemberPackageCardStatus getStatus() {
		return status;
	}

	public void setStatus(MemberPackageCardStatus status) {
		this.status = status;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

}
