package com.lanking.uxb.service.mall.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvert;
import com.lanking.uxb.service.mall.value.VCoinsGoods;

/**
 * 金币商品.
 * 
 * @author wlche
 * @since 2.0.3
 */
@RestController
@RequestMapping("zy/mall/goods")
public class ZyCoinsMallGoodsController {
	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;

	/**
	 * 获得单个商品信息.
	 * 
	 * @param goodsId
	 *            商品ID
	 * @return
	 */
	@RequestMapping(value = "get", method = { RequestMethod.GET, RequestMethod.POST })
	public Value get(Long goodsId) {
		if (null == goodsId) {
			return new Value(new MissingArgumentException());
		}
		CoinsGoods coinsGoods = coinsGoodsService.get(goodsId);
		if (null != coinsGoods) {
			VCoinsGoods vo = coinsGoodsConvert.to(coinsGoods);
			return new Value(vo);
		}
		return new Value();
	}
}
