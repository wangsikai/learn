package com.lanking.uxb.zycon.mall.api.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.GoodsType;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryType;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01ExerciseQuestionService;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01ExerciseService;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01Service;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotteryGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotterySeasonGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotterySeasonService;
import com.lanking.uxb.zycon.mall.form.LotteryGoodsForm;
import com.lanking.uxb.zycon.mall.form.LotterySeasonForm;

@Service
@Transactional(readOnly = true)
public class ZycCoinsLotteryGoodsServiceImpl implements ZycCoinsLotteryGoodsService {

	@Autowired
	@Qualifier("GoodsRepo")
	private Repo<Goods, Long> goodsRepo;

	@Autowired
	@Qualifier("CoinsLotteryGoodsRepo")
	private Repo<CoinsLotteryGoods, Long> lotteryGoodsrepo;

	@Autowired
	@Qualifier("CoinsLotteryGoodsSnapshotRepo")
	private Repo<CoinsLotteryGoodsSnapshot, Long> lotteryGoodsSnapshotrepo;

	@Autowired
	@Qualifier("CoinsLotterySeasonRepo")
	private Repo<CoinsLotterySeason, Long> lotterSeasonrepo;

	@Autowired
	@Qualifier("GoodsSnapshotRepo")
	private Repo<GoodsSnapshot, Long> goodsSnapshotrepo;

	@Autowired
	@Qualifier("CoinsLotterySeasonGoodsRepo")
	private Repo<CoinsLotterySeasonGoods, Long> coinsLotterySeasonGoodsrepo;

	@Autowired
	private ZycCoinsLotterySeasonService seasonService;

	@Autowired
	private ZycCoinsLotterySeasonGoodsService seasonGoodsService;

	@Autowired
	private ZycHolidayActivity01Service zycHolidayActivity01Service;


	@Transactional
	@Override
	public void saveLotteryGoods(LotterySeasonForm form) {
		CoinsLotterySeason season = this.handleSeason(form);
		List<LotteryGoodsForm> goodsList = form.getGoodsList();
		int index = 1;
		for (LotteryGoodsForm goodForm : goodsList) {
			/**
			 * 保存商品表和商品快照
			 */
			Goods goods = null;
			if (goodForm.getGoodsId() == null) {
				goods = new Goods();
				goods.setCreateAt(new Date());
				goods.setCreateId(form.getUserId());
			} else {
				goods = goodsRepo.get(goodForm.getGoodsId());
				goods.setUpdateAt(new Date());
				goods.setUpdateId(form.getUserId());
			}
			goods.setImage(goodForm.getImageId());
			goods.setName(goodForm.getName());
			goods.setPrice(goodForm.getPrice());
			goods.setType(GoodsType.COINS_LOTTERY);
			goodsRepo.save(goods);
			// 每一次编辑和创建都会产生一个快照
			GoodsSnapshot goodsSnapshot = this.createGoodsSnapshot(goods);
			goods.setGoodsSnapshotId(goodsSnapshot.getId());
			goodsRepo.save(goods);
			/**
			 * 保存抽奖商品表和抽奖商品快照
			 */
			CoinsLotteryGoods lotteryGoods = null;
			if (goodForm.getGoodsId() == null) {
				lotteryGoods = new CoinsLotteryGoods();
				lotteryGoods.setId(goods.getId());
			} else {
				lotteryGoods = lotteryGoodsrepo.get(goodForm.getGoodsId());
			}
			lotteryGoods.setCoinsGoodsType(goodForm.getCoinsGoodsType());
			lotteryGoods.setLevel(goodForm.getLevel());
			lotteryGoodsrepo.save(lotteryGoods);
			CoinsLotteryGoodsSnapshot lotteryGoodsSnapshot = this.createLotteryGoodsSnaoshot(lotteryGoods);
			lotteryGoods.setCoinsLotteryGoodsSnapshotId(lotteryGoodsSnapshot.getId());
			lotteryGoodsrepo.save(lotteryGoods);
			/**
			 * 每期与奖品对应关系
			 */
			CoinsLotterySeasonGoods seasonGoods = seasonGoodsService.getByKey(goods.getId(), season.getId());
			if (seasonGoods == null) {
				seasonGoods = new CoinsLotterySeasonGoods();
				seasonGoods.setCoinsLotteryGoodsId(goods.getId());
				seasonGoods.setSeasonId(season.getId());
				seasonGoods.setSequence(index);
				seasonGoods.setSellCount(goodForm.getSellCount());
				seasonGoods.setSeasonId(season.getId());
			} else {
				seasonGoods.setSellCount(goodForm.getSellCount());
			}
			// 中奖概率
			if (goodForm.getAwardsRate() != null) {
				seasonGoods.setAwardsRate(BigDecimal.valueOf(goodForm.getAwardsRate()));
			}
			coinsLotterySeasonGoodsrepo.save(seasonGoods);
			index++;
		}
	}

	/**
	 * 创建商品快照
	 * 
	 * @param goods
	 * @return
	 */
	@Transactional
	public GoodsSnapshot createGoodsSnapshot(Goods goods) {
		GoodsSnapshot s = new GoodsSnapshot();
		s.setCreateAt(goods.getCreateAt());
		s.setCreateId(goods.getCreateId());
		s.setGoodsId(goods.getId());
		s.setImage(goods.getImage());
		s.setName(goods.getName());
		s.setPrice(goods.getPrice());
		s.setType(goods.getType());
		goodsSnapshotrepo.save(s);
		return s;
	}

	/**
	 * 创建抽奖商品的快照
	 * 
	 * @param goods
	 * @return
	 */
	@Transactional
	public CoinsLotteryGoodsSnapshot createLotteryGoodsSnaoshot(CoinsLotteryGoods goods) {
		CoinsLotteryGoodsSnapshot s = new CoinsLotteryGoodsSnapshot();
		s.setCoinsGoodsType(goods.getCoinsGoodsType());
		s.setCoinsLotteryGoodsId(goods.getId());
		s.setLevel(goods.getLevel());
		lotteryGoodsSnapshotrepo.save(s);
		return s;
	}

	/**
	 * 抽奖期别相关逻辑
	 */
	@Transactional
	public CoinsLotterySeason handleSeason(LotterySeasonForm form) {
		CoinsLotterySeason season = null;
		try {
			int seasonIndex = 0;
			if (form.getId() != null) {
				season = lotterSeasonrepo.get(form.getId());
			} else {
				season = new CoinsLotterySeason();
				season.setCreateAt(new Date());
				if (form.getType() == CoinsLotteryType.COMMON) {
					// id为空可能是增加或者编辑界面修改了活动时间
					if (form.getFlag().equals("add")) {
						season.setTitle("第1期");
						// 只有新增的时候才会保存活动code
						season.setCode(this.getActiveCode());
					} else {
						seasonIndex = seasonService.countSeason(form.getCode()).intValue();
						// 只有新增的时候，更新期数
						season.setTitle("第" + (seasonIndex + 1) + "期");
						season.setCode(form.getCode());
					}
				} else {
					// 假期活动只会有一期
					season.setTitle("第1期");
				}

			}
			season.setType(form.getType());
			if (form.getUserAwardsTimes() != null) {
				season.setUserAwardsTimes(form.getUserAwardsTimes());
			}
			if (form.getAwardsTimes() != null) {
				season.setAwardsTimes(form.getAwardsTimes());
			}
			if (form.getMustAwardsGoods() != null) {
				season.setMustAwardsGoods(form.getMustAwardsGoods());
			}
			if (form.getMustAwardsTimes() != null) {
				season.setMustAwardsTimes(form.getMustAwardsTimes());
			}
			season.setEveryWeek(form.getEveryWeek());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (form.getActiveName() != null) {
				season.setName(form.getActiveName());
			}
			season.setUserType(form.getUserType());
			if (form.getStartTime() != null) {
				season.setStartTime(sdf.parse(form.getStartTime()));
				season.setEndTime(sdf.parse(form.getEndTime()));
				season.setEveryWeek(false);
			} else {
				if (form.getId() == null) {
					// 当前时间到当周周日
					season.setStartTime(this.getMondayOfThisWeek());
					// 当前周日
					season.setEndTime(this.getSundayOfThisWeek());
				} else {
					// 原来是指定日期的改为每周的
					if (!season.getEveryWeek()) {
						// 当前时间到当周周日
						season.setStartTime(this.getMondayOfThisWeek());
						// 当前周日
						season.setEndTime(this.getSundayOfThisWeek());
					}
				}
			}
			// 如果无限额，这里存-1
			season.setUserJoinTimes(form.getUserJoinTimes());
			season.setStatus(form.getStatus());
			lotterSeasonrepo.save(season);
			if (form.getType() == CoinsLotteryType.COMMON) {
				// 说明不是第一期,需要去更新之前期别的状态
				if (seasonIndex > 0 && form.getStatus() == Status.ENABLED) {
					if (form.getId() != null) {
						seasonIndex--;
					}
					if (seasonIndex > 0) {
						CoinsLotterySeason season_old = seasonService.getByTitleName("第" + seasonIndex + "期",
								season.getCode());
						season_old.setStatus(Status.DISABLED);
						lotterSeasonrepo.save(season_old);
					}
				}
			}
			// 只有新增并且是假期作业才会触发假期作业初始化数据
			if (form.getType() == CoinsLotteryType.HOLIDAY_ACTIVITY_01 && form.getId() == null) {
				zycHolidayActivity01Service.init(season.getId(),form);
			}
		} catch (Exception e) {

		}
		return season;
	}
	

	@Override
	public CoinsLotteryGoods get(Long id) {
		return lotteryGoodsrepo.get(id);
	}

	/**
	 * 获取本周周日23:59:59时间
	 * 
	 * @return
	 */
	public Date getSundayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public Date getMondayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	@Override
	public Map<Long, CoinsLotteryGoods> mget(Collection<Long> ids) {
		return lotteryGoodsrepo.mget(ids);
	}

	@Override
	public Integer getActiveCode() {
		Integer code = lotterSeasonrepo.find("$getMaxActiveCode").get(Integer.class);
		return code == null ? 1 : code + 1;
	}
}
