package com.lanking.uxb.service.mall.convert;

/**
 * 金币商品订单转换选项
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
public class CoinsGoodsOrderConvertOption {

	private boolean initCoinsGoods = false;

	public CoinsGoodsOrderConvertOption() {
		super();
	}

	public CoinsGoodsOrderConvertOption(boolean initCoinsGoods) {
		super();
		this.initCoinsGoods = initCoinsGoods;
	}

	public boolean isInitCoinsGoods() {
		return initCoinsGoods;
	}

	public void setInitCoinsGoods(boolean initCoinsGoods) {
		this.initCoinsGoods = initCoinsGoods;
	}

}
