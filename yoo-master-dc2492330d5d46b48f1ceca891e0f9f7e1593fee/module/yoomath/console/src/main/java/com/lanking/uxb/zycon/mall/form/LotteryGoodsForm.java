package com.lanking.uxb.zycon.mall.form;

import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.goods.GoodsType;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;

/**
 * 后台抽奖管理提交--抽奖奖品相关
 * 
 * @author wangsenhao
 *
 */
public class LotteryGoodsForm {

	private Long goodsId;
	private GoodsType type;
	private String name;
	// 商品需要金币价格
	private BigDecimal price;
	// 每期商品数量
	private Integer sellCount;
	// 图片id
	private Long imageId;

	/** 金币商品类型 */
	private CoinsGoodsType coinsGoodsType;

	/** 抽奖奖品等级 */
	private CoinsLotteryGoodsLevel level;

	// 中奖概率，假期活动抽奖时使用
	private Double awardsRate;

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public GoodsType getType() {
		return type;
	}

	public void setType(GoodsType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getSellCount() {
		return sellCount;
	}

	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
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

	public Double getAwardsRate() {
		return awardsRate;
	}

	public void setAwardsRate(Double awardsRate) {
		this.awardsRate = awardsRate;
	}

}
