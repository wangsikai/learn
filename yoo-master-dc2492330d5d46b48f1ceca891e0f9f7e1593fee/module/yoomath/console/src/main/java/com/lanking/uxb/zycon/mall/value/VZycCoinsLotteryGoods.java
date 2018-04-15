package com.lanking.uxb.zycon.mall.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;

public class VZycCoinsLotteryGoods implements Serializable {

	private static final long serialVersionUID = 635044669394459244L;

	private Long id;

	private Long seasonId;

	private Integer sequence;

	private String name;

	private String imageUrl;

	private Long imageId;

	private Integer sellCount;

	/** 金币商品类型 */
	private CoinsGoodsType coinsGoodsType;

	/** 抽奖奖品等级 */
	private CoinsLotteryGoodsLevel level;

	private BigDecimal price;

	// 假期活动新增抽奖概率
	private BigDecimal awardsRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getSellCount() {
		return sellCount;
	}

	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}

	public CoinsLotteryGoodsLevel getLevel() {
		return level;
	}

	public void setLevel(CoinsLotteryGoodsLevel level) {
		this.level = level;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAwardsRate() {
		return awardsRate;
	}

	public void setAwardsRate(BigDecimal awardsRate) {
		this.awardsRate = awardsRate;
	}

}
