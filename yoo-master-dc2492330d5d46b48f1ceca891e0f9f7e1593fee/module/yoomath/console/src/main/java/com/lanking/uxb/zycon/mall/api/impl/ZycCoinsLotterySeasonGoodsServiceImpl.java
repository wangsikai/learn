package com.lanking.uxb.zycon.mall.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoods;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotterySeasonGoodsService;

@Service
@Transactional(readOnly = true)
public class ZycCoinsLotterySeasonGoodsServiceImpl implements ZycCoinsLotterySeasonGoodsService {

	@Autowired
	@Qualifier("CoinsLotterySeasonGoodsRepo")
	private Repo<CoinsLotterySeasonGoods, Long> repo;

	@Override
	public CoinsLotterySeasonGoods getByKey(Long coinsLotteryGoodsId, Long seasonId) {
		return repo.find("$getByKey",
				Params.param("coinsLotteryGoodsId", coinsLotteryGoodsId).put("seasonId", seasonId)).get();
	}

	@Override
	public List<CoinsLotterySeasonGoods> findBySeasonId(Long seasonId) {
		return repo.find("$findBySeasonId", Params.param("seasonId", seasonId)).list();
	}
}
