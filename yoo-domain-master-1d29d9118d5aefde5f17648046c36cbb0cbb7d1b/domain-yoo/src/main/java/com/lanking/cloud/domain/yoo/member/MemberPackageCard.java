package com.lanking.cloud.domain.yoo.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 会员兑换卡
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_card")
public class MemberPackageCard implements Serializable {
	private static final long serialVersionUID = -3257347896839929348L;

	/**
	 * 卡号
	 */
	@Id
	@Column(name = "code", length = 16)
	private String code;

	/**
	 * 原始加密JSON串
	 */
	@Column(name = "encrypt_code", length = 128)
	private String encryptCode;

	/**
	 * 会员卡限定用户类型
	 */
	@Column(name = "user_type", precision = 3)
	private UserType userType;

	/**
	 * 会员卡类型
	 */
	@Column(name = "member_type", precision = 3)
	private MemberType memberType;

	/**
	 * 会员时长（月数）
	 */
	@Column(name = "month0")
	private Integer month;

	/**
	 * 会员卡到期时间，包含（精确到日）
	 */
	@Column(name = "end_at", columnDefinition = "date")
	private Date endAt;

	/**
	 * 单价（元）
	 */
	@Column(name = "price", scale = 2)
	private BigDecimal price;

	/**
	 * 备注
	 */
	@Column(name = "memo", length = 100)
	private String memo;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间（同一批次产生的使用相同时间）
	 */
	@Column(name = "create_time", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 修改人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 会员卡状态
	 * 
	 * @see MemberPackageCardStatus
	 */
	@Column(name = "status", precision = 3)
	private MemberPackageCardStatus status = MemberPackageCardStatus.DEFAULT;

	/**
	 * 使用用户
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 使用时间
	 */
	@Column(name = "used_time", columnDefinition = "datetime(3)")
	private Date usedAt;

	/**
	 * 使用订单ID
	 */
	@Column(name = "order_id")
	private Long orderId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEncryptCode() {
		return encryptCode;
	}

	public void setEncryptCode(String encryptCode) {
		this.encryptCode = encryptCode;
	}

	public UserType getUserType() {
		return userType == null ? UserType.NULL : userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public MemberType getMemberType() {
		return memberType == null ? MemberType.NONE : memberType;
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

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
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

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public MemberPackageCardStatus getStatus() {
		return status;
	}

	public void setStatus(MemberPackageCardStatus status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getUsedAt() {
		return usedAt;
	}

	public void setUsedAt(Date usedAt) {
		this.usedAt = usedAt;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}
