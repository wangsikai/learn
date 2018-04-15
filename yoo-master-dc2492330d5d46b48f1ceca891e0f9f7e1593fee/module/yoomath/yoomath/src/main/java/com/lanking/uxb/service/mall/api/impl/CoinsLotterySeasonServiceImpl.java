package com.lanking.uxb.service.mall.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.CoinsLotterySeasonService;

/**
 *
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Service
@Transactional(readOnly = true)
public class CoinsLotterySeasonServiceImpl implements CoinsLotterySeasonService {
	@Autowired
	@Qualifier("CoinsLotterySeasonRepo")
	private Repo<CoinsLotterySeason, Long> ruleRepo;

	@Override
	public CoinsLotterySeason findNewest() {
		return ruleRepo.find("$findEnable", Params.param()).get(CoinsLotterySeason.class);
	}

	@Override
	@Transactional
	public void updateEarnCoins(Long id, Integer earnCoins) {
		ruleRepo.execute("$updateEarnCoins", Params.param("id", id).put("earnCoins", earnCoins));
	}

	@Override
	public CoinsLotterySeason get(long id) {
		return ruleRepo.get(id);
	}

	@Override
	public CoinsLotterySeason findNewestByCode(Integer code) {
		return ruleRepo.find("$findNewestByCode", Params.param("code", code)).get();
	}
}
