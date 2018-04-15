package com.lanking.cloud.domain.yoo.member;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 会员套餐
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package")
public class MemberPackage implements Serializable {

	private static final long serialVersionUID = -3152903578633289752L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户类型
	 * 
	 * @see UserType
	 */
	@Column(name = "user_type", precision = 3)
	private UserType userType;

	/**
	 * 会员类型
	 * 
	 * @see MemberType
	 */
	@Column(name = "member_type", precision = 3)
	private MemberType memberType;

	/**
	 * 月数(只能为12的倍数)
	 */
	@Column(name = "month0", precision = 5)
	private int month;

	/**
	 * 额外赠送的月数(不能为空,默认值为0)
	 */
	@Column(name = "extra_month", precision = 5, columnDefinition = "smallint default 0")
	private int extraMonth;

	/**
	 * 原价
	 */
	@Column(name = "original_price", scale = 2)
	private BigDecimal originalPrice;

	/**
	 * 现价
	 */
	@Column(name = "present_rice", scale = 2)
	private BigDecimal presentPrice;

	/**
	 * 折扣
	 */
	@Column(name = "discount", scale = 2)
	private BigDecimal discount;

	/**
	 * 会员套餐标签
	 */
	@Column(name = "tag", precision = 3)
	private MemberPackageTag tag;

	/**
	 * 自定义套餐标签名称
	 */
	@Column(name = "custom_tag", length = 8)
	private String customTag;

	/**
	 * 顺序
	 */
	@Column(name = "sequence", precision = 3)
	private int sequence = 0;

	/**
	 * 所属套餐组ID
	 */
	@Column(name = "member_package_group_id")
	private Long memberPackageGroupId;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public int getExtraMonth() {
		return extraMonth;
	}

	public void setExtraMonth(int extraMonth) {
		this.extraMonth = extraMonth;
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

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Long getMemberPackageGroupId() {
		return memberPackageGroupId;
	}

	public void setMemberPackageGroupId(Long memberPackageGroupId) {
		this.memberPackageGroupId = memberPackageGroupId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
