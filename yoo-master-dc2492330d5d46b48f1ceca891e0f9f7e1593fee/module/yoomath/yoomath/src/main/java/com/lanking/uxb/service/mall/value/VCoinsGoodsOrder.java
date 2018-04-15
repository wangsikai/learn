package com.lanking.uxb.service.mall.value;

import com.alibaba.fastjson.annotation.JSONField;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 金币商品订单VO
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
public class VCoinsGoodsOrder extends VGoodsOrder {

	private static final long serialVersionUID = -8347739953704313543L;

	private String p0;
	private GoodsOrderStatus status;
	private CoinsGoodsOrderSource source;
	private String sourceTitle = StringUtils.EMPTY;
	private String statusTitle;
	// 订单金币总额
	private int totalCoinsPrice;
	@JSONField(serialize = false)
	private long coinsGoodsId;
	@JSONField(serialize = false)
	private long coinsGoodsSnapshotId;

	private CoinsGoodsType coinsGoodsType;

	public String getP0() {
		return p0;
	}

	public void setP0(String p0) {
		this.p0 = p0;
	}

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

	public CoinsGoodsOrderSource getSource() {
		return source;
	}

	public void setSource(CoinsGoodsOrderSource source) {
		this.source = source;
	}

	public String getSourceTitle() {
		return sourceTitle;
	}

	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public String getStatusTitle() {
		return statusTitle;
	}

	public void setStatusTitle(String statusTitle) {
		this.statusTitle = statusTitle;
	}

	public int getTotalCoinsPrice() {
		return totalCoinsPrice;
	}

	public void setTotalCoinsPrice(int totalCoinsPrice) {
		this.totalCoinsPrice = totalCoinsPrice;
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

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}
}
