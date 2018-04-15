package com.lanking.uxb.service.mall.convert;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.mall.api.CoinsGoodsQuery;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.value.VCoinsGoods;
import com.lanking.uxb.service.mall.value.VCoinsGoodsGroup;
import com.lanking.uxb.service.session.api.impl.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
@Component
public class CoinsGoodsGroupConvert extends Converter<VCoinsGoodsGroup, CoinsGoodsGroup, Long> {
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private CoinsGoodsService coinsGoodsService;


	@Override
	protected Long getId(CoinsGoodsGroup coinsGoodsGroup) {
		return coinsGoodsGroup.getId();
	}

	@Override
	protected VCoinsGoodsGroup convert(CoinsGoodsGroup coinsGoodsGroup) {

		VCoinsGoodsGroup v = new VCoinsGoodsGroup();

		v.setId(coinsGoodsGroup.getId());
		v.setName(coinsGoodsGroup.getName());
		v.setSequence(coinsGoodsGroup.getSequence());

		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VCoinsGoodsGroup, CoinsGoodsGroup, Long, List<VCoinsGoods>>() {

			@Override
			public boolean accept(CoinsGoodsGroup coinsGoodsGroup) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsGroup coinsGoodsGroup, VCoinsGoodsGroup vCoinsGoodsGroup) {
				return coinsGoodsGroup.getId();
			}

			@Override
			public void setValue(CoinsGoodsGroup coinsGoodsGroup, VCoinsGoodsGroup vCoinsGoodsGroup, List<VCoinsGoods> value) {
				vCoinsGoodsGroup.setGoods(value);
			}

			@Override
			public List<VCoinsGoods> getValue(Long key) {
				if (key == null) {
					return Collections.EMPTY_LIST;
				}
				List<Long> groupIds = new ArrayList<Long>(1);
				groupIds.add(key);
				CoinsGoodsQuery query = new CoinsGoodsQuery();
				query.setUserType(Security.getUserType());
				query.setLimit(Integer.MAX_VALUE);
				return coinsGoodsConvert.to( coinsGoodsService.listAllByGroup(query, groupIds).get(key),
				                new CoinsGoodsConvertOption(true));
			}

			@Override
			public Map<Long, List<VCoinsGoods>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				CoinsGoodsQuery query = new CoinsGoodsQuery();
				query.setUserType(Security.getUserType());
				query.setLimit(Integer.MAX_VALUE);

				Map<Long, List<CoinsGoods>> goodsMap = coinsGoodsService.listAllByGroup(query, keys);
				if (goodsMap == null || goodsMap.size() == 0) {
					return Collections.EMPTY_MAP;
				}
				Map<Long, List<VCoinsGoods>> retMap = new HashMap<Long, List<VCoinsGoods>>(goodsMap.size());
				for (Map.Entry<Long, List<CoinsGoods>> e : goodsMap.entrySet()) {
					retMap.put(e.getKey(), coinsGoodsConvert.to(e.getValue(),
					                new CoinsGoodsConvertOption(true)));
				}

				return retMap;
			}
		});
	}
}
