package com.lanking.uxb.service.mall.convert;

/**
 * 抽奖活动订单转换参数.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
public class LotteryActivityGoodsOrderConvertOption {

	private boolean initUser = false;
	private boolean initGoods = false;
	private boolean initActivityGoods = false;

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
