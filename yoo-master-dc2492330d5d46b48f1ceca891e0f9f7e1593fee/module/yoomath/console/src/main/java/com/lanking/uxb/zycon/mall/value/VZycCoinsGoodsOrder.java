package com.lanking.uxb.zycon.mall.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;

/**
 * 金币兑换管理VO
 * 
 * @since v2.0
 * @author wangsenhao
 *
 */
public class VZycCoinsGoodsOrder implements Serializable {

	private static final long serialVersionUID = -1660051845852569276L;
	/**
	 * 订单时间
	 */
	private Date orderAt;
	/**
	 * 订单Id
	 */
	private Long orderId;
	/**
	 * 订单编号
	 */
	private String code;
	/**
	 * 账户名
	 */
	private String accountName;
	/**
	 * 身份
	 */
	private String roleName;
	/**
	 * 学校
	 */
	private String schoolName;
	/**
	 * 商品名称
	 */
	private String goodName;
	/**
	 * 兑换号码
	 */
	private String number;
	/**
	 * 兑换状态
	 */
	private GoodsOrderStatus status;
	/**
	 * 卖家备注或者失败原因
	 */
	private String sellerNotes;

	private Long accountId;
	private Long schoolId;
	// 来源
	private CoinsGoodsOrderSource source;
	/**
	 * 收货人
	 */
	private String contactName;

	/**
	 * 收货人电话
	 */
	private String contactPhone;

	/**
	 * 收货详细地址
	 */
	private String contactAddress;

	public Date getOrderAt() {
		return orderAt;
	}

	public void setOrderAt(Date orderAt) {
		this.orderAt = orderAt;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

	public String getSellerNotes() {
		return sellerNotes;
	}

	public void setSellerNotes(String sellerNotes) {
		this.sellerNotes = sellerNotes;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public CoinsGoodsOrderSource getSource() {
		return source;
	}

	public void setSource(CoinsGoodsOrderSource source) {
		this.source = source;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

}
