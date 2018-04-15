package com.lanking.uxb.service.mall.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单VO
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
public class VGoodsOrder implements Serializable {

	private static final long serialVersionUID = -4896666745938101144L;

	private long id;
	private BigDecimal totalPrice;
	private Date orderAt;
	private String sellerNotes;

	private long goodsId;
	private long goodsSnapshotId;

	// 关联的商品
	private VGoods goods;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getOrderAt() {
		return orderAt;
	}

	public void setOrderAt(Date orderAt) {
		this.orderAt = orderAt;
	}

	public String getSellerNotes() {
		return sellerNotes;
	}

	public void setSellerNotes(String sellerNotes) {
		this.sellerNotes = sellerNotes;
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

	public VGoods getGoods() {
		return goods;
	}

	public void setGoods(VGoods goods) {
		this.goods = goods;
	}

}
