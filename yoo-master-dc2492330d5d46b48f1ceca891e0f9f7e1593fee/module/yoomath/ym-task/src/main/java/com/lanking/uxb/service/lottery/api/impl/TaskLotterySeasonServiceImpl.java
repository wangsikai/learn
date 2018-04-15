package com.lanking.uxb.service.lottery.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.lottery.api.TaskLotterySeasonService;

/**
 * @see TaskLotterySeasonService
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Service
@Transactional(readOnly = true)
public class TaskLotterySeasonServiceImpl implements TaskLotterySeasonService {
	@Autowired
	@Qualifier("CoinsLotterySeasonRepo")
	private Repo<CoinsLotterySeason, Long> coinsLotterySeasonRepo;

	@Autowired
	@Qualifier("CoinsLotterySeasonGoodsRepo")
	private Repo<CoinsLotterySeasonGoods, Long> coinsLotterySeasonGoodsRepo;

	@Override
	public CoinsLotterySeason getLatestSeason() {
		return coinsLotterySeasonRepo.find("$findLatest", Params.param()).get();
	}

	@Override
	@Transactional
	public void updateStatus(long id, Status status) {
		CoinsLotterySeason season = coinsLotterySeasonRepo.get(id);
		if (season != null) {
			season.setStatus(status);

			coinsLotterySeasonRepo.save(season);
		}
	}

	@Override
	@Transactional
	public CoinsLotterySeason save(CoinsLotterySeason from) {
		CoinsLotterySeason season = new CoinsLotterySeason();
		season.setStatus(Status.ENABLED);
		season.setUserJoinTimes(from.getUserJoinTimes());
		season.setEveryWeek(from.getEveryWeek());
		season.setCreateAt(new Date());
		season.setCode(from.getCode());
		season.setName(from.getName());
		season.setUserType(from.getUserType());
		season.setType(CoinsLotteryType.COMMON);
		String title = from.getTitle();
		Integer seasonCount = Integer.valueOf(title.substring(1, title.length() - 1)) + 1;
		season.setTitle("第" + seasonCount + "期");

		Date startTime = DateUtils.addDays(from.getStartTime(), 7);
		Date endTime = DateUtils.addDays(from.getEndTime(), 7);

		season.setStartTime(startTime);
		season.setEndTime(endTime);
		season.setEarnCoins(0);

		coinsLotterySeasonRepo.save(season);

		List<CoinsLotterySeasonGoods> goodsList = coinsLotterySeasonGoodsRepo.find("$findBySeason",
				Params.param("seasonId", from.getId())).list();

		for (CoinsLotterySeasonGoods g : goodsList) {
			CoinsLotterySeasonGoods newGoods = new CoinsLotterySeasonGoods();
			newGoods.setSellCount(g.getSellCount());
			newGoods.setSequence(g.getSequence());
			newGoods.setCoinsLotteryGoodsId(g.getCoinsLotteryGoodsId());
			newGoods.setSeasonId(season.getId());

			coinsLotterySeasonGoodsRepo.save(newGoods);
		}

		from = coinsLotterySeasonRepo.get(from.getId());
		from.setStatus(Status.DISABLED);
		coinsLotterySeasonRepo.save(from);

		return season;
	}

	@Override
	public List<CoinsLotterySeason> findNewList() {
		return coinsLotterySeasonRepo.find("$findNewList").list();
	}

}
