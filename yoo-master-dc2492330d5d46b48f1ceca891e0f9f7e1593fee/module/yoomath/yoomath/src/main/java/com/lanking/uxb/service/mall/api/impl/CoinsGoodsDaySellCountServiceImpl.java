package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsDaySellCount;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsDaySellCountKey;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.CoinsGoodsDaySellCountService;

@Transactional(readOnly = true)
@Service
public class CoinsGoodsDaySellCountServiceImpl implements CoinsGoodsDaySellCountService {

	@Autowired
	@Qualifier("CoinsGoodsDaySellCountRepo")
	private Repo<CoinsGoodsDaySellCount, CoinsGoodsDaySellCountKey> countRepo;

	@Override
	public int incrCount(long coinsGoodsId, long date0, int delta, int max) {
		int ret = countRepo.execute(
				"$incrCount",
				Params.param("coinsGoodsId", coinsGoodsId).put("date0", date0).put("delta", delta)
						.put("maxCount0", max - delta));
		if (ret == 1) {
			return ret;
		} else {
			CoinsGoodsDaySellCount count = countRepo.find("$get",
					Params.param("coinsGoodsId", coinsGoodsId).put("date0", date0)).get();
			if (count == null) {
				count = new CoinsGoodsDaySellCount();
				count.setCoinsGoodsId(coinsGoodsId);
				count.setCount0(delta);
				count.setDate0(date0);
				countRepo.save(count);
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public CoinsGoodsDaySellCount get(long date0, long coinsGoodsId) {
		CoinsGoodsDaySellCount count = countRepo.find("$get",
				Params.param("coinsGoodsId", coinsGoodsId).put("date0", date0)).get();
		if (count == null) {
			count = new CoinsGoodsDaySellCount();
			count.setCoinsGoodsId(coinsGoodsId);
			count.setCount0(0);
			count.setDate0(date0);
		}
		return count;
	}

	@Override
	public Map<Long, CoinsGoodsDaySellCount> mget(long date0, Collection<Long> coinsGoodsIds) {
		List<CoinsGoodsDaySellCount> counts = countRepo.find("$mget",
				Params.param("coinsGoodsIds", coinsGoodsIds).put("date0", date0)).list();
		Map<Long, CoinsGoodsDaySellCount> countMap = new HashMap<Long, CoinsGoodsDaySellCount>(coinsGoodsIds.size());
		for (CoinsGoodsDaySellCount count : counts) {
			countMap.put(count.getCoinsGoodsId(), count);
		}
		for (Long coinsGoodsId : coinsGoodsIds) {
			if (!countMap.containsKey(coinsGoodsId)) {
				CoinsGoodsDaySellCount count = new CoinsGoodsDaySellCount();
				count.setCoinsGoodsId(coinsGoodsId);
				count.setCount0(0);
				count.setDate0(date0);
				countMap.put(coinsGoodsId, count);
			}
		}
		return countMap;
	}
}
