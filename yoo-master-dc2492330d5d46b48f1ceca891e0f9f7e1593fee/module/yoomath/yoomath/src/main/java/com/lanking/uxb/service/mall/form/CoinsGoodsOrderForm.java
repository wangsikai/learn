package com.lanking.uxb.service.mall.form;

import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 创建订单的参数表单
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
public class CoinsGoodsOrderForm {

	// @since 2.4.0 表示原有的id
	private Long id;

	private long userId;
	private long goodsId;
	private long goodsSnapshotId;
	private long coinsGoodsId;
	private long coinsGoodsSnapshotId;
	private int amount;
	private String mobile;
	private String qq;
	private GoodsOrderStatus status;
	private UserType userType;
	// 绑定的手机号
	private String bindMobile;
	// 验证码
	private String validCode;
	// 绑定的邮箱
	private String bindEmail;
	// 备注信息
	private String memo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public long getGoodsSnapshotId() {
		return goodsSnapshotId;
	}

	public void setGoodsSnapshotId(long goodsSnapshotId) {
		this.goodsSnapshotId = goodsSnapshotId;
	}

	public long getCoinsGoodsId() {
		return coinsGoodsId;
	}

	public void setCoinsGoodsId(long coinsGoodsId) {
		this.coinsGoodsId = coinsGoodsId;
	}

	public long getCoinsGoodsSnapshotId() {
		return coinsGoodsSnapshotId;
	}

	public void setCoinsGoodsSnapshotId(long coinsGoodsSnapshotId) {
		this.coinsGoodsSnapshotId = coinsGoodsSnapshotId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getBindEmail() {
		return bindEmail;
	}

	public void setBindEmail(String bindEmail) {
		this.bindEmail = bindEmail;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
