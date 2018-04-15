package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoods;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.value.VLotteryActivityGoods;

@Component
public class LotteryActivityGoodsConvert extends Converter<VLotteryActivityGoods, LotteryActivityGoods, Long> {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsConvert goodsConvert;

	@Override
	protected Long getId(LotteryActivityGoods s) {
		return s.getId();
	}

	@Override
	protected VLotteryActivityGoods convert(LotteryActivityGoods s) {
		VLotteryActivityGoods v = new VLotteryActivityGoods();
		v.setId(s.getId());
		v.setCoinsGoodsType(s.getType());
		return v;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VLotteryActivityGoods, LotteryActivityGoods, Long, Goods>() {

			@Override
			public boolean accept(LotteryActivityGoods s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(LotteryActivityGoods s, VLotteryActivityGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(LotteryActivityGoods s, VLotteryActivityGoods d, Goods value) {
				goodsConvert.assemblerVO(value, d);
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
