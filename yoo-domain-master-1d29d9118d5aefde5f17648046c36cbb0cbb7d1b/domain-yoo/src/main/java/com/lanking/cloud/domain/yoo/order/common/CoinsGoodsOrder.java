package com.lanking.cloud.domain.yoo.order.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 金币兑换订单
 * 
 * <pre>
 * 目前包含三种订单 {@link CoinsGoodsOrderSource}
 * 1.金币兑换商品订单
 * 2.金币抽奖订单
 * 3.金币活动订单
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_goods_order")
public class CoinsGoodsOrder extends CoinsGoodsOrderBaseInfo {

	private static final long serialVersionUID = -8170832039517634219L;

	/**
	 * 金币兑换订单快照ID
	 */
	@Column(name = "coins_goods_order_snapshot_id")
	private long coinsGoodsOrderSnapshotId;

	@Transient
	private boolean initCoinsGoods = false;
	@Transient
	private UserType userType;
	@Transient
	private boolean initUser = false;
	@Transient
	private boolean initGoods = false;
	@Transient
	private boolean initActivityGoods = false;

	public long getCoinsGoodsOrderSnapshotId() {
		return coinsGoodsOrderSnapshotId;
	}

	public void setCoinsGoodsOrderSnapshotId(long coinsGoodsOrderSnapshotId) {
		this.coinsGoodsOrderSnapshotId = coinsGoodsOrderSnapshotId;
	}

	public boolean isInitCoinsGoods() {
		return initCoinsGoods;
	}

	public void setInitCoinsGoods(boolean initCoinsGoods) {
		this.initCoinsGoods = initCoinsGoods;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public boolean isInitUser() {
		return initUser;
	}

	public void setInitUser(boolean initUser) {
		this.initUser = initUser;
	}

	public boolean isInitGoods() {
		return initGoods;
	}

	public void setInitGoods(boolean initGoods) {
		this.initGoods = initGoods;
	}

	public boolean isInitActivityGoods() {
		return initActivityGoods;
	}

	public void setInitActivityGoods(boolean initActivityGoods) {
		this.initActivityGoods = initActivityGoods;
	}
}
