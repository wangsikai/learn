package com.lanking.uxb.zycon.mall.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotterySeasonService;

@Service
@Transactional(readOnly = true)
public class ZycCoinsLotterySeasonServiceImpl implements ZycCoinsLotterySeasonService {

	@Autowired
	@Qualifier("CoinsLotterySeasonRepo")
	private Repo<CoinsLotterySeason, Long> seasonRepo;

	@Override
	public CoinsLotterySeason get(Long id) {
		return seasonRepo.get(id);
	}

	@Override
	public Long countSeason(Integer code) {
		return seasonRepo.find("$countSeason", Params.param("code", code)).get(Long.class);
	}

	@Override
	public CoinsLotterySeason getByTitleName(String title, Integer code) {
		return seasonRepo.find("$getByTitleName", Params.param("title", title).put("code", code)).get();
	}

	@Override
	public CoinsLotterySeason getLastest() {
		return seasonRepo.find("$getLastest").get();
	}

	@Override
	public List<CoinsLotterySeason> list() {
		return seasonRepo.find("$seasonList").list();
	}

	@Transactional
	@Override
	public void updateSeasonStatus(Status status, Long seasonId) {
		CoinsLotterySeason season = this.get(seasonId);
		season.setStatus(status);
		seasonRepo.save(season);
	}

	@Override
	public List<CoinsLotterySeason> list(Integer code) {
		return seasonRepo.find("$querySeasonsByCode", Params.param("code", code)).list();
	}
}
