package com.lanking.uxb.zycon.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoodsKey;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotteryGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycGoodsService;
import com.lanking.uxb.zycon.mall.value.VZycCoinsLotteryGoods;

@Component
public class ZycCoinsLotteryGoodsConvert extends
		Converter<VZycCoinsLotteryGoods, CoinsLotterySeasonGoods, CoinsLotterySeasonGoodsKey> {
	@Autowired
	private ZycGoodsService zycGoodsService;
	@Autowired
	private ZycCoinsLotteryGoodsService zycLotteryGoodsService;

	@Override
	protected CoinsLotterySeasonGoodsKey getId(CoinsLotterySeasonGoods s) {
		return new CoinsLotterySeasonGoodsKey(s.getCoinsLotteryGoodsId(), s.getSeasonId());
	}

	@Override
	protected VZycCoinsLotteryGoods convert(CoinsLotterySeasonGoods s) {
		VZycCoinsLotteryGoods v = new VZycCoinsLotteryGoods();
		v.setSellCount(s.getSellCount());
		v.setSequence(s.getSequence());
		v.setId(s.getCoinsLotteryGoodsId());
		v.setSeasonId(s.getSeasonId());
		v.setAwardsRate(s.getAwardsRate());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycCoinsLotteryGoods, CoinsLotterySeasonGoods, Long, Goods>() {

			@Override
			public boolean accept(CoinsLotterySeasonGoods s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsLotterySeasonGoods s, VZycCoinsLotteryGoods d) {
				return s.getCoinsLotteryGoodsId();
			}

			@Override
			public void setValue(CoinsLotterySeasonGoods s, VZycCoinsLotteryGoods d, Goods value) {
				d.setName(value.getName());
				d.setImageId(value.getImage());
				d.setImageUrl(FileUtil.getUrl(value.getImage()));
				d.setPrice(value.getPrice());
			}

			@Override
			public Goods getValue(Long key) {
				return zycGoodsService.get(key);
			}

			@Override
			public Map<Long, Goods> mgetValue(Collection<Long> keys) {
				return zycGoodsService.mget(keys);
			}

		});

		assemblers
				.add(new ConverterAssembler<VZycCoinsLotteryGoods, CoinsLotterySeasonGoods, Long, CoinsLotteryGoods>() {

					@Override
					public boolean accept(CoinsLotterySeasonGoods s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(CoinsLotterySeasonGoods s, VZycCoinsLotteryGoods d) {
						return s.getCoinsLotteryGoodsId();
					}

					@Override
					public void setValue(CoinsLotterySeasonGoods s, VZycCoinsLotteryGoods d, CoinsLotteryGoods value) {
						d.setCoinsGoodsType(value.getCoinsGoodsType());
						d.setLevel(value.getLevel());
					}

					@Override
					public CoinsLotteryGoods getValue(Long key) {
						return zycLotteryGoodsService.get(key);
					}

					@Override
					public Map<Long, CoinsLotteryGoods> mgetValue(Collection<Long> keys) {
						return zycLotteryGoodsService.mget(keys);
					}

				});
	}

}
