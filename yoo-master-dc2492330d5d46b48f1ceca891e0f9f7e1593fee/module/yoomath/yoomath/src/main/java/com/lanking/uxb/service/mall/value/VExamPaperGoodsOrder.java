package com.lanking.uxb.service.mall.value;

/**
 * 试卷订单VO
 * 
 * @since 2.0.3
 * @author zemin.song
 * @version 2016年9月1日
 */
public class VExamPaperGoodsOrder extends VResourcesGoodsOrder {

	private static final long serialVersionUID = -304582401555408445L;
	// 订单对应的商品
	private VResourcesGoods resourcesGoods;
	// 订单对应的资源商品快照
	private VResourcesGoods resourcesGoodsSnapshot;

	public VResourcesGoods getResourcesGoods() {
		return resourcesGoods;
	}

	public void setResourcesGoods(VResourcesGoods resourcesGoods) {
		this.resourcesGoods = resourcesGoods;
	}

	public VResourcesGoods getResourcesGoodsSnapshot() {
		return resourcesGoodsSnapshot;
	}

	public void setResourcesGoodsSnapshot(VResourcesGoods resourcesGoodsSnapshot) {
		this.resourcesGoodsSnapshot = resourcesGoodsSnapshot;
	}
}
