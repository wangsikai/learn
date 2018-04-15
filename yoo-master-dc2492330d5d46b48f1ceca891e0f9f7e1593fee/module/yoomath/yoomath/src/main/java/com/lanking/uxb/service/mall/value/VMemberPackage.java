package com.lanking.uxb.service.mall.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.member.MemberPackageTag;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 会员套餐.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月27日
 */
public class VMemberPackage implements Serializable {
	private static final long serialVersionUID = -7857352641694815959L;

	private long id;
	private UserType userType;
	private MemberType memberType;
	private int month;
	private BigDecimal originalPrice;
	private BigDecimal presentPrice;
	private BigDecimal discount;
	private MemberPackageTag tag;
	private String tagName;
	private int sequence = 0;
	private Status status;
	/**
	 * 2017.3.8 senhao.wang新增<br>
	 * 额外赠送的月数
	 */
	private int extraMonth = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(BigDecimal presentPrice) {
		this.presentPrice = presentPrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public MemberPackageTag getTag() {
		return tag;
	}

	public void setTag(MemberPackageTag tag) {
		this.tag = tag;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getExtraMonth() {
		return extraMonth;
	}

	public void setExtraMonth(int extraMonth) {
		this.extraMonth = extraMonth;
	}

}
