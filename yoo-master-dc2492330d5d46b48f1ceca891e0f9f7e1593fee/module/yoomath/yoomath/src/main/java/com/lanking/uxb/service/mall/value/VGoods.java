package com.lanking.uxb.service.mall.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.yoo.goods.GoodsType;

/**
 * 商品VO基类
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
public class VGoods implements Serializable {

	private static final long serialVersionUID = 2248020547746619664L;

	private long id;
	private long goodsSnapshotId;
	private String name;
	private String introduction;
	private String content;
	private long image;
	private String imageUrl;
	private String imageMidUrl;
	private String imageMinUrl;
	private BigDecimal price;
	private BigDecimal priceRMB;
	private Date salesTime;
	private Date soldOutTime;
	private GoodsType type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGoodsSnapshotId() {
		return goodsSnapshotId;
	}

	public void setGoodsSnapshotId(long goodsSnapshotId) {
		this.goodsSnapshotId = goodsSnapshotId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getImage() {
		return image;
	}

	public void setImage(long image) {
		this.image = image;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageMidUrl() {
		return imageMidUrl;
	}

	public void setImageMidUrl(String imageMidUrl) {
		this.imageMidUrl = imageMidUrl;
	}

	public String getImageMinUrl() {
		return imageMinUrl;
	}

	public void setImageMinUrl(String imageMinUrl) {
		this.imageMinUrl = imageMinUrl;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceRMB() {
		return priceRMB;
	}

	public void setPriceRMB(BigDecimal priceRMB) {
		this.priceRMB = priceRMB;
	}

	public Date getSalesTime() {
		return salesTime;
	}

	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}

	public Date getSoldOutTime() {
		return soldOutTime;
	}

	public void setSoldOutTime(Date soldOutTime) {
		this.soldOutTime = soldOutTime;
	}

	public GoodsType getType() {
		return type;
	}

	public void setType(GoodsType type) {
		this.type = type;
	}

}
