package com.lanking.uxb.service.mall.value;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;

/**
 * 抽奖活动订单信息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
public class VLotteryActivityGoodsOrder implements Serializable {
	private static final long serialVersionUID = 2458568386512623853L;

	/**
	 * 订单ID（CoinsGoodsOrder）.
	 */
	private Long orderid;

	/**
	 * 领取人（姓名、电话、QQ号等信息）
	 */
	private String contact;

	/**
	 * 订单状态.
	 */
	private GoodsOrderStatus status;

	/**
	 * 备注、失败原因等.
	 */
	private String memo;

	/**
	 * 奖品商品ID.
	 */
	private Long goodsId;

	/**
	 * 奖品商品的快照ID
	 */
	private Long goodsSnapshotId;

	/**
	 * 奖品名称.
	 */
	private String name;

	/**
	 * 奖品图标.
	 */
	private Long imgid;

	/**
	 * 奖品类型.
	 */
	private CoinsGoodsType goodsType;

	/**
	 * 获奖用户ID.
	 */
	private Long userId;

	/**
	 * 获奖用户name
	 */
	private String userName;

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getGoodsSnapshotId() {
		return goodsSnapshotId;
	}

	public void setGoodsSnapshotId(Long goodsSnapshotId) {
		this.goodsSnapshotId = goodsSnapshotId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getImgid() {
		return imgid;
	}

	public void setImgid(Long imgid) {
		this.imgid = imgid;
	}

	public CoinsGoodsType getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(CoinsGoodsType goodsType) {
		this.goodsType = goodsType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
