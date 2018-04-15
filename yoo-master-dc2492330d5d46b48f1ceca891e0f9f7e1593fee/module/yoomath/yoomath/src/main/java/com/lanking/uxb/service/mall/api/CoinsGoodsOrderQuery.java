package com.lanking.uxb.service.mall.api;

import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;

/**
 * 金币商品订单查询条件
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
public class CoinsGoodsOrderQuery {

	private Long userId;
	private CoinsGoodsOrderSource orderSource;

	// 是否忽略活动抽奖 @since 3.0.4
	private boolean ignoreActivity = false;

	public boolean getIgnoreActivity() {
		return ignoreActivity;
	}

	public void setIgnoreActivity(boolean ignoreActivity) {
		this.ignoreActivity = ignoreActivity;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public CoinsGoodsOrderSource getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(CoinsGoodsOrderSource orderSource) {
		this.orderSource = orderSource;
	}

}
