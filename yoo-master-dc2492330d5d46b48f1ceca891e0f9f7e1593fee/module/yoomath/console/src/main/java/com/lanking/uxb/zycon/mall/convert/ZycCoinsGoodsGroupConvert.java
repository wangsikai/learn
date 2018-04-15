package com.lanking.uxb.zycon.mall.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.mall.value.VZycCoinsGoodsGroup;

@Component
public class ZycCoinsGoodsGroupConvert extends Converter<VZycCoinsGoodsGroup, CoinsGoodsGroup, Long> {

	@Override
	protected Long getId(CoinsGoodsGroup s) {
		return s.getId();
	}

	@Override
	protected VZycCoinsGoodsGroup convert(CoinsGoodsGroup s) {
		VZycCoinsGoodsGroup v = new VZycCoinsGoodsGroup();
		v.setName(s.getName());
		v.setSequence(s.getSequence());
		v.setId(s.getId());
		return v;
	}

}
