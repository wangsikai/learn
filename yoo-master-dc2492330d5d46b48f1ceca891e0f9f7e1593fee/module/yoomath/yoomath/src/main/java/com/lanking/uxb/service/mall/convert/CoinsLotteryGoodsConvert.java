package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.mall.value.VCoinsLotteryGoods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.value.VCoinsGoods;

/**
 * CoinsLotteryGoods -> VCoinsLotteryGoods
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Component
public class CoinsLotteryGoodsConvert extends Converter<VCoinsLotteryGoods, CoinsLotteryGoods, Long> {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsConvert goodsConvert;

	@Override
	protected Long getId(CoinsLotteryGoods coinsLotteryGoods) {
		return coinsLotteryGoods.getId();
	}

	@Override
	protected VCoinsLotteryGoods convert(CoinsLotteryGoods coinsLotteryGoods) {
		VCoinsLotteryGoods v = new VCoinsLotteryGoods();
		v.setId(coinsLotteryGoods.getId());
		v.setCoinsGoodsType(coinsLotteryGoods.getCoinsGoodsType());
		v.setCoinsGoodsSnapshotId(coinsLotteryGoods.getCoinsLotteryGoodsSnapshotId());
		v.setLevel(coinsLotteryGoods.getLevel());

		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VCoinsLotteryGoods, CoinsLotteryGoods, Long, Goods>() {

			@Override
			public boolean accept(CoinsLotteryGoods coinsLotteryGoods) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsLotteryGoods coinsLotteryGoods, VCoinsLotteryGoods vCoinsGoods) {
				return coinsLotteryGoods.getId();
			}

			@Override
			public void setValue(CoinsLotteryGoods coinsLotteryGoods, VCoinsLotteryGoods v, Goods value) {
				goodsConvert.assemblerVO(value, v);
			}

			@Override
			public Goods getValue(Long key) {
				return goodsService.get(key);
			}

			@Override
			public Map<Long, Goods> mgetValue(Collection<Long> keys) {
				return goodsService.mget(keys);
			}
		});

	}
}
