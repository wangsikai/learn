package com.lanking.uxb.service.mall.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonSellCount;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserLuckyDrawService;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.mall.api.CoinsLotteryGoodsService;
import com.lanking.uxb.service.mall.api.CoinsLotterySeasonService;
import com.lanking.uxb.service.mall.api.GoodsOrderService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.cache.CoinsLotteryCacheService;
import com.lanking.uxb.service.mall.cache.CoinsLotteryRecordCacheService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * @see CoinsLotteryGoodsService
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Service
@Transactional(readOnly = true)
public class CoinsLotteryGoodsServiceImpl implements CoinsLotteryGoodsService {
	@Autowired
	@Qualifier("CoinsLotteryGoodsRepo")
	private Repo<CoinsLotteryGoods, Long> lotteryGoodsRepo;
	@Autowired
	@Qualifier("CoinsGoodsOrderRepo")
	private Repo<CoinsGoodsOrder, Long> coinsGoodsOrderRepo;
	@Autowired
	@Qualifier("CoinsLotterySeasonGoodsRepo")
	private Repo<CoinsLotterySeasonGoods, Long> coinsSeasonGoodsRepo;
	@Autowired
	@Qualifier("CoinsLotterySeasonSellCountRepo")
	private Repo<CoinsLotterySeasonSellCount, Long> lotterySellCountRepo;
	@Autowired
	@Qualifier("CoinsGoodsOrderSnapshotRepo")
	private Repo<CoinsGoodsOrderSnapshot, Long> orderSnapShotRepo;

	// 未开大奖之后的中奖概率zoo keeper key
	private static final String REGULAR_RATE_KEY = "lottery.regular.rate";
	// 可开大奖之后的中奖概率zoo keeper key
	private static final String TOP_RATE_KEY = "lottery.top.rate";
	// 每次参加抽奖励所需的金币
	private static final String JOIN_LOTTERY_COINS = "lottery.join.coins";

	@Autowired
	private CoinsLotterySeasonService ruleService;
	@Autowired
	private CoinsLotteryCacheService lotteryCacheService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private CoinsLotteryRecordCacheService recordCacheService;
	@Autowired
	private UserService userService;
	@Autowired
	private CoinsLotterySeasonService coinsLotterySeasonService;
	@Autowired
	private HolidayActivity01UserLuckyDrawService drawService;

	@Override
	public List<CoinsLotteryGoods> findAll(long seasonId) {
		return lotteryGoodsRepo.find("$findAll", Params.param("seasonId", seasonId)).list();
	}

	@Override
	@Transactional
	public CoinsGoodsOrder lottery(long userId, Long id) {
		List<Boolean> todayLottery = lotteryCacheService.getUserLottery(userId);
		CoinsLotterySeason season = null;
		if (id != null && id > 0) {
			season = ruleService.get(id);
		} else {
			season = ruleService.findNewest();
		}

		UserHonor userHonor = userHonorService.getUserHonor(userId);
		int costCoins = Env.getDynamicInt(JOIN_LOTTERY_COINS);
		// 表明用户已经不可以再参与抽奖了
		if (season.getUserJoinTimes() > 0 && todayLottery.size() >= season.getUserJoinTimes()) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_TIMES_LIMIT);
		}

		// 用户抽奖金币不足
		if (userHonor == null || userHonor.getCoins() < costCoins) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_COINS_LIMIT);
		}

		// 抽奖此期已经结束，不可以再抽
		if (season.getEndTime().getTime() < System.currentTimeMillis()) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_SEASON_OVER);
		}

		Map<String, Object> retMap = null;

		// 表明今天抽奖的次数在0-3次之间
		if (todayLottery.size() < 3) {
			if (todayLottery.size() == 2) {
				boolean win = false;
				for (Boolean v : todayLottery) {
					if (v) {
						win = true;
						break;
					}
				}

				// 表明前两次都没有中，此时第三次必中
				if (win) {
					retMap = this.random(userId, season);
				} else {
					retMap = this.win(userId, season, CoinsLotteryGoodsLevel.PRIMARY);
				}
			} else {
				retMap = this.random(userId, season);
			}
		} else {
			if (todayLottery.size() < 9) {
				retMap = this.random(userId, season);
			} else if (todayLottery.size() == 9) {
				boolean win = false;

				for (int i = 3; i < 9; i++) {
					if (todayLottery.get(i)) {
						win = true;
						break;
					}
				}

				if (win) {
					retMap = this.random(userId, season);
				} else {
					retMap = this.win(userId, season, CoinsLotteryGoodsLevel.REGULAR);
				}
			} else {
				retMap = this.random(userId, season);
			}
		}

		CoinsGoodsOrder order = (CoinsGoodsOrder) retMap.get("order");
		Boolean isNothing = (Boolean) retMap.get("isNothing");

		if (isNothing) {
			lotteryCacheService.push(userId, -order.getId());
		} else {
			lotteryCacheService.push(userId, order.getId());
		}

		return order;
	}

	@Override
	public CoinsLotteryGoods get(long id) {
		return lotteryGoodsRepo.get(id);
	}

	@Override
	public Map<Long, CoinsLotteryGoods> mget(Collection<Long> ids) {
		return lotteryGoodsRepo.mget(ids);
	}

	/**
	 * 根据概率随机中奖励
	 *
	 * @param userId
	 *            用户id
	 * @param season
	 *            抽奖期别
	 */
	@Transactional
	private Map<String, Object> random(long userId, CoinsLotterySeason season) {
		// 获得这一期的各种商品的销售数量
		List<CoinsLotterySeasonSellCount> counters = lotterySellCountRepo
				.find("$findLotterySellCount", Params.param("seasonId", season.getId())).list();

		List<CoinsLotteryGoods> goods = lotteryGoodsRepo
				.find("$findByLevel",
						Params.param("level", CoinsLotteryGoodsLevel.TOP.getValue()).put("seasonId", season.getId()))
				.list();
		Map<Long, CoinsLotteryGoods> lotteryTopGoodsMap = new HashMap<Long, CoinsLotteryGoods>(goods.size());

		List<Long> goodsIds = new ArrayList<Long>(goods.size());
		for (CoinsLotteryGoods g : goods) {
			goodsIds.add(g.getId());
			lotteryTopGoodsMap.put(g.getId(), g);
		}

		List<Goods> topGoods = goodsService.mgetList(goodsIds);
		int earnCoins = season.getEarnCoins() == null ? 0 : season.getEarnCoins();

		List<Goods> canOpenGoods = new ArrayList<Goods>(topGoods.size());
		for (Goods g : topGoods) {
			if (earnCoins / g.getPrice().intValue() >= 5) {
				canOpenGoods.add(g);
			}
		}

		List<Double> rates = new ArrayList<Double>(5);

		String rateStr = null;
		// 根据大奖是否已经出现来确定不同类型奖品的中奖概率
		if (CollectionUtils.isNotEmpty(canOpenGoods)) {
			List<CoinsGoodsOrder> seasonLotteryOrders = coinsGoodsOrderRepo
					.find("$queryUserSeasonLottery", Params.param("seasonId", season.getId())
							.put("level", CoinsLotteryGoodsLevel.TOP.getValue()).put("userId", userId))
					.list();
			// 若用户本期已经中了大奖，则不可以再中
			if (CollectionUtils.isEmpty(seasonLotteryOrders)) {
				rateStr = Env.getDynamicString(TOP_RATE_KEY);
			} else {
				rateStr = Env.getDynamicString(REGULAR_RATE_KEY);
			}
		} else {
			rateStr = Env.getDynamicString(REGULAR_RATE_KEY);
		}

		String rateArr[] = rateStr.split(",");

		for (String rate : rateArr) {
			rates.add(Double.valueOf(rate.trim()));
		}
		int lotteryLevelValue = lottery(rates);

		CoinsLotteryGoodsLevel lotteryLevel = CoinsLotteryGoodsLevel.findByValue(lotteryLevelValue);
		List<CoinsLotteryGoods> lotteryGoods = null;
		if (lotteryLevel == CoinsLotteryGoodsLevel.TOP) {
			lotteryGoods = new ArrayList<CoinsLotteryGoods>(canOpenGoods.size());
			for (Goods g : canOpenGoods) {
				lotteryGoods.add(lotteryTopGoodsMap.get(g.getId()));
			}
		} else {
			lotteryGoods = lotteryGoodsRepo.find("$findByLevel",
					Params.param("level", lotteryLevel.getValue()).put("seasonId", season.getId())).list();
		}
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		if (lotteryLevel == CoinsLotteryGoodsLevel.NOTHING) {
			CoinsGoodsOrder order = createOrder(lotteryGoods.get(0), userId, season, GoodsOrderStatus.COMPLETE);
			retMap.put("order", order);
			retMap.put("isNothing", true);
		} else {

			boolean hasLottery = false;
			Set<CoinsLotteryGoods> setLotteryGoods = new HashSet<CoinsLotteryGoods>(lotteryGoods.size());
			for (CoinsLotteryGoods g : lotteryGoods) {
				setLotteryGoods.add(g);
			}
			for (CoinsLotteryGoods g : setLotteryGoods) {
				CoinsGoodsOrder order = createOrder(g, userId, season, GoodsOrderStatus.NOT_PAY);
				if (order != null) {
					retMap.put("order", order);
					retMap.put("isNothing", false);
					hasLottery = true;
					break;
				}
			}

			// 都没有中
			if (!hasLottery) {
				lotteryGoods = lotteryGoodsRepo.find("$findByLevel", Params
						.param("level", CoinsLotteryGoodsLevel.NOTHING.getValue()).put("seasonId", season.getId()))
						.list();

				CoinsGoodsOrder order = createOrder(lotteryGoods.get(0), userId, season, GoodsOrderStatus.COMPLETE);
				retMap.put("order", order);
				retMap.put("isNothing", true);
			}
		}

		return retMap;
	}

	@Transactional
	private CoinsGoodsOrder createOrder(CoinsLotteryGoods goods, long userId, CoinsLotterySeason season,
			GoodsOrderStatus status) {

		CoinsLotterySeasonSellCount counter = lotterySellCountRepo
				.find("$findByGoodsAndSeason", Params.param("seasonId", season.getId()).put("goodsId", goods.getId()))
				.get();

		CoinsLotterySeasonGoods seasonGoods = coinsSeasonGoodsRepo
				.find("$findByGoodsAndSeasonId", Params.param("seasonId", season.getId()).put("goodsId", goods.getId()))
				.get();

		// 表示每天可抽中的商品数据为0
		if (seasonGoods.getSellCount() == 0) {
			return null;
		}
		// 表示此商品已经被抽走
		if (seasonGoods.getSellCount() > 0 && counter != null && counter.getCount0() >= seasonGoods.getSellCount()) {
			return null;
		}

		Goods orignalGoods = goodsService.get(goods.getId());

		int oneTimeCostCoins = Env.getDynamicInt(JOIN_LOTTERY_COINS);

		CoinsGoodsOrder order = new CoinsGoodsOrder();
		order.setStatus(status);
		order.setOrderAt(new Date());
		order.setUserId(userId);
		order.setGoodsId(goods.getId());
		order.setCoinsGoodsId(goods.getId());
		order.setCoinsGoodsSnapshotId(goods.getCoinsLotteryGoodsSnapshotId());
		order.setP1(String.valueOf(season.getId()));
		order.setSource(CoinsGoodsOrderSource.LUCKY_DRAW);
		order.setGoodsSnapshotId(orignalGoods.getGoodsSnapshotId());
		// order.setTotalPrice(new BigDecimal());
		order.setTotalPrice(new BigDecimal(oneTimeCostCoins));
		order.setDelStatus(Status.ENABLED);
		order.setCode(goodsOrderService.generateCode());
		order.setAmount(1);

		coinsGoodsOrderRepo.save(order);
		if (counter == null) {
			counter = new CoinsLotterySeasonSellCount();
			counter.setCount0(1);
			counter.setCoinsLotteryGoodsId(goods.getId());
			counter.setSeasonId(season.getId());
		} else {
			counter.setCount0(counter.getCount0() + 1);
		}
		lotterySellCountRepo.save(counter);

		CoinsGoodsOrderSnapshot orderSnapshot = createSnapshot(order);
		order.setCoinsGoodsOrderSnapshotId(orderSnapshot.getId());

		// 金币商品则获得相应的金币
		if (goods.getCoinsGoodsType() == CoinsGoodsType.COINS && goods.getLevel() != CoinsLotteryGoodsLevel.NOTHING) {
			order.setStatus(GoodsOrderStatus.COMPLETE);
			orderSnapshot.setStatus(GoodsOrderStatus.COMPLETE);
			coinsService.earn(CoinsAction.LOTTERY_DRAW_EARN_COINS, userId, orignalGoods.getPrice().intValue(),
					Biz.COINS_GOODS_ORDER, order.getId());

			orderSnapShotRepo.save(orderSnapshot);

		}
		if (goods.getLevel() == CoinsLotteryGoodsLevel.NOTHING) {
			season.setEarnCoins(
					season.getEarnCoins() == null ? oneTimeCostCoins : season.getEarnCoins() + oneTimeCostCoins);
		} else {
			season.setEarnCoins(season.getEarnCoins() == null ? oneTimeCostCoins - orignalGoods.getPrice().intValue()
					: season.getEarnCoins() + oneTimeCostCoins - orignalGoods.getPrice().intValue());

			UserInfo user = userService.getUser(userId);
			// 若是大奖，处理大奖相关的数据
			if (goods.getLevel() != CoinsLotteryGoodsLevel.TOP) {
				List<String> records = recordCacheService.getLevelRecords(CoinsLotteryGoodsLevel.REGULAR,
						season.getId());
				if (CollectionUtils.isEmpty(records)) {
					records = new ArrayList<String>(1);
					records.add(StringUtils.getMaskName(user.getName()) + ";" + orignalGoods.getName());
				} else {
					if (records.size() == 30) {
						records.remove(29);
					}

					records.add(0, StringUtils.getMaskName(user.getName()) + ";" + orignalGoods.getName());
				}

				recordCacheService.setLevelRecords(CoinsLotteryGoodsLevel.REGULAR, records, season.getId());
			} else {
				List<String> records = recordCacheService.getLevelRecords(CoinsLotteryGoodsLevel.TOP, season.getId());
				if (CollectionUtils.isEmpty(records)) {
					records = new ArrayList<String>(1);
					records.add(StringUtils.getMaskName(user.getName()) + ";" + orignalGoods.getName());
				} else {

					records.add(0, StringUtils.getMaskName(user.getName()) + ";" + orignalGoods.getName());
				}
				recordCacheService.setLevelRecords(CoinsLotteryGoodsLevel.TOP, records, season.getId());
			}
		}

		// 保存本期净收入金币
		ruleService.updateEarnCoins(season.getId(), season.getEarnCoins());

		coinsGoodsOrderRepo.save(order);
		return order;
	}

	/**
	 * 创建订单快照数据
	 *
	 * @param order
	 *            {@link CoinsGoodsOrder}
	 * @return {@link CoinsGoodsOrderSnapshot}
	 */
	@Transactional
	private CoinsGoodsOrderSnapshot createSnapshot(CoinsGoodsOrder order) {
		CoinsGoodsOrderSnapshot snapshot = new CoinsGoodsOrderSnapshot();
		snapshot.setCoinsGoodsOrderId(order.getId());
		snapshot.setCoinsGoodsId(order.getCoinsGoodsId());
		snapshot.setCoinsGoodsSnapshotId(order.getCoinsGoodsOrderSnapshotId());
		snapshot.setGoodsId(order.getGoodsId());
		snapshot.setGoodsSnapshotId(order.getGoodsSnapshotId());
		snapshot.setStatus(order.getStatus());
		snapshot.setUserId(order.getUserId());
		snapshot.setP1(order.getP1());
		snapshot.setAmount(order.getAmount());
		snapshot.setTotalPrice(order.getTotalPrice());
		snapshot.setDelStatus(order.getDelStatus());
		snapshot.setCode(order.getCode());

		orderSnapShotRepo.save(snapshot);

		return snapshot;
	}

	/**
	 * 必中方法处理
	 *
	 * @param userId
	 *            用户id
	 * @param season
	 *            每期数据
	 * @return 是否中了数据
	 */
	@Transactional
	private Map<String, Object> win(long userId, CoinsLotterySeason season, CoinsLotteryGoodsLevel level) {
		List<CoinsLotteryGoods> goods = lotteryGoodsRepo
				.find("$findByLevel", Params.param("level", level.getValue()).put("seasonId", season.getId())).list();

		CoinsLotteryGoods obj = goods.get(0);

		CoinsLotterySeasonGoods seasonGoods = coinsSeasonGoodsRepo
				.find("$findByGoodsAndSeasonId", Params.param("seasonId", season.getId()).put("goodsId", obj.getId()))
				.get();

		CoinsLotterySeasonSellCount counter = lotterySellCountRepo
				.find("$findByGoodsAndSeason", Params.param("seasonId", season.getId()).put("goodsId", obj.getId()))
				.get();

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		// 基础奖品已经被抽完，则变为谢谢参与
		if (seasonGoods.getSellCount() == 0 || (seasonGoods.getSellCount() > 0 && counter != null
				&& (counter.getCount0() + 1 >= seasonGoods.getSellCount()))) {

			goods = lotteryGoodsRepo.find("$findByLevel",
					Params.param("level", CoinsLotteryGoodsLevel.NOTHING.getValue()).put("seasonId", season.getId()))
					.list();
			obj = goods.get(0);

			CoinsGoodsOrder order = createOrder(obj, userId, season, GoodsOrderStatus.COMPLETE);
			retMap.put("order", order);
			retMap.put("isNothing", true);
		} else {

			CoinsGoodsOrder order = createOrder(obj, userId, season, GoodsOrderStatus.NOT_PAY);

			retMap.put("order", order);
			retMap.put("isNothing", false);
		}

		return retMap;
	}

	/**
	 * 随机抽奖处理
	 *
	 * @param orignalRates
	 *            原生的概率
	 * @return 是哪个级别的商品
	 */
	private int lottery(List<Double> orignalRates) {

		if (CollectionUtils.isEmpty(orignalRates)) {
			return CoinsLotteryGoodsLevel.NOTHING.getValue();
		}

		int size = orignalRates.size();
		double sumRate = 0d;

		for (double rate : orignalRates) {
			sumRate += rate;
		}

		List<Double> sortOrignalRates = new ArrayList<Double>(size);
		Double tempSumRate = 0d;
		for (double rate : orignalRates) {
			tempSumRate += rate;
			sortOrignalRates.add(tempSumRate / sumRate);
		}

		double nextDouble = Math.random();
		sortOrignalRates.add(nextDouble);
		Collections.sort(sortOrignalRates);

		return sortOrignalRates.indexOf(nextDouble);
	}

	@Override
	@Transactional
	public Map<String, Object> holidayDraw(long userId, long code, CoinsLotterySeason season, int drawSet) {
		Map<String, Object> retMap = new HashMap<>();
		// 抽奖此期已经结束，不可以再抽
		// if (season.getEndTime().getTime() < System.currentTimeMillis()) {
		// throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_SEASON_OVER);
		// }
		// 中奖概率
		List<CoinsLotterySeasonGoods> seasonGoods = coinsSeasonGoodsRepo
				.find("$findBySeasonId", Params.param("seasonId", season.getId())).list();
		// 奖品等级
		List<CoinsLotteryGoods> lotteryGoods = this.findAll(season.getId());

		// 未中奖商品
		CoinsLotteryGoods nothing = null;
		CoinsLotterySeasonGoods nothingSeasonGoods = null;
		// 保底奖品
		CoinsLotteryGoods mustAwardsGoods = null;
		CoinsLotterySeasonGoods mustAwardsSeasonGoods = null;
		// 大奖
		List<CoinsLotterySeasonGoods> topSeasonGoods = new ArrayList<>();

		// 找到nothing对应的坑位
		for (CoinsLotterySeasonGoods goods : seasonGoods) {
			for (CoinsLotteryGoods coinsLotteryGood : lotteryGoods) {
				if (goods.getCoinsLotteryGoodsId().longValue() == coinsLotteryGood.getId().longValue()) {
					if (coinsLotteryGood.getCoinsGoodsType() == CoinsGoodsType.NOTHING) {
						nothingSeasonGoods = goods;
						nothing = coinsLotteryGood;
					}
					if (goods.getSequence() == season.getMustAwardsGoods()) {
						mustAwardsGoods = coinsLotteryGood;
						mustAwardsSeasonGoods = goods;
					}
					if (coinsLotteryGood.getLevel() != null
							&& coinsLotteryGood.getLevel() == CoinsLotteryGoodsLevel.TOP) {
						topSeasonGoods.add(goods);
					}
				}
			}
		}

		if (drawSet == 0) {
			// 正常抽奖
			retMap = ordinaryHolidayDraw(seasonGoods, nothingSeasonGoods, userId, season, lotteryGoods, nothing,
					topSeasonGoods);
		} else if (drawSet == 1) {
			// 不可出奖
			// 奖品坑位
			int sequence = -1;
			CoinsGoodsOrder coinsGoodsOrder = createHolidayOrder(nothing, userId, season, GoodsOrderStatus.COMPLETE,
					nothingSeasonGoods);
			CoinsGoodsType goodsType = nothing.getCoinsGoodsType();
			sequence = nothingSeasonGoods.getSequence();
			retMap.put("order", coinsGoodsOrder);
			retMap.put("goodsType", goodsType);
			retMap.put("sequence", sequence);
		} else if (drawSet == 2) {
			// 奖品保底设置
			// 奖品坑位
			int sequence = -1;
			CoinsGoodsOrder coinsGoodsOrder = createHolidayOrder(mustAwardsGoods, userId, season,
					GoodsOrderStatus.NOT_PAY, mustAwardsSeasonGoods);
			CoinsGoodsType goodsType = mustAwardsGoods.getCoinsGoodsType();

			if (coinsGoodsOrder == null) {
				coinsGoodsOrder = createHolidayOrder(nothing, userId, season, GoodsOrderStatus.COMPLETE,
						nothingSeasonGoods);
				goodsType = nothing.getCoinsGoodsType();
				sequence = nothingSeasonGoods.getSequence();
			}
			sequence = mustAwardsSeasonGoods.getSequence();
			retMap.put("order", coinsGoodsOrder);
			retMap.put("goodsType", goodsType);
			retMap.put("sequence", sequence);
		}

		return retMap;
	}

	/**
	 * 正常抽奖
	 */
	private Map<String, Object> ordinaryHolidayDraw(List<CoinsLotterySeasonGoods> seasonGoods,
			CoinsLotterySeasonGoods nothingSeasonGoods, long userId, CoinsLotterySeason season,
			List<CoinsLotteryGoods> lotteryGoods, CoinsLotteryGoods nothing,
			List<CoinsLotterySeasonGoods> topSeasonGoods) {
		Map<String, Object> retMap = new HashMap<>();

		// 奖品坑位
		int sequence = -1;
		boolean isNothing = false;

		// 抽奖
		CoinsLotterySeasonGoods seasonGood = draw(seasonGoods);
		if (seasonGood == null) {
			isNothing = true;
			seasonGood = nothingSeasonGoods;
		}
		// 大奖每个用户只能抽中一次
		List<CoinsGoodsOrder> seasonLotteryOrders = coinsGoodsOrderRepo
				.find("$queryUserSeasonLottery", Params.param("seasonId", season.getId())
						.put("level", CoinsLotteryGoodsLevel.TOP.getValue()).put("userId", userId))
				.list();
		if (CollectionUtils.isNotEmpty(seasonLotteryOrders)) {
			for (CoinsLotterySeasonGoods goods : topSeasonGoods) {
				if (seasonGood.getCoinsLotteryGoodsId() == goods.getCoinsLotteryGoodsId()) {
					isNothing = true;
					break;
				}
			}
		}

		// 命中商品
		CoinsLotteryGoods drawGoods = null;
		sequence = seasonGood.getSequence();

		for (CoinsLotteryGoods coinsLotteryGood : lotteryGoods) {
			if (seasonGood.getCoinsLotteryGoodsId().longValue() == coinsLotteryGood.getId().longValue()) {
				drawGoods = coinsLotteryGood;
			}
			if (seasonGood.getCoinsLotteryGoodsId().longValue() == coinsLotteryGood.getId().longValue()
					&& coinsLotteryGood.getCoinsGoodsType() == CoinsGoodsType.NOTHING) {
				isNothing = true;
			}
		}

		CoinsGoodsOrder coinsGoodsOrder = null;
		CoinsGoodsType goodsType = null;
		// 没中奖
		if (isNothing) {
			coinsGoodsOrder = createHolidayOrder(nothing, userId, season, GoodsOrderStatus.COMPLETE, seasonGood);
			goodsType = nothing.getCoinsGoodsType();
		} else {
			coinsGoodsOrder = createHolidayOrder(drawGoods, userId, season, GoodsOrderStatus.NOT_PAY, seasonGood);
			goodsType = drawGoods.getCoinsGoodsType();
		}

		if (coinsGoodsOrder == null) {
			coinsGoodsOrder = createHolidayOrder(nothing, userId, season, GoodsOrderStatus.COMPLETE,
					nothingSeasonGoods);
			goodsType = nothing.getCoinsGoodsType();
			sequence = nothingSeasonGoods.getSequence();
		}

		retMap.put("order", coinsGoodsOrder);
		retMap.put("goodsType", goodsType);
		retMap.put("sequence", sequence);

		return retMap;
	}

	/**
	 * 抽奖算法,根据每种商品概率计算
	 */
	private CoinsLotterySeasonGoods draw(List<CoinsLotterySeasonGoods> seasonGoods) {
		CoinsLotterySeasonGoods goods = null;
		if (seasonGoods == null || seasonGoods.size() == 0) {
			return goods;
		}

		double rand = Math.random() * 100;
		double tempMin = 0;
		double tempMax = 0;
		for (CoinsLotterySeasonGoods seasonGood : seasonGoods) {
			long awardsRate = 0;
			if (seasonGood.getAwardsRate() != null) {
				awardsRate = seasonGood.getAwardsRate().longValue();
			}
			// 概率为0直接跳过
			if (awardsRate == 0) {
				continue;
			}
			tempMax += awardsRate;
			if (rand >= tempMin && rand <= tempMax) {
				goods = seasonGood;
				break;
			}
			tempMin += awardsRate;
		}

		return goods;
	}

	@Transactional(readOnly = false)
	private CoinsGoodsOrder createHolidayOrder(CoinsLotteryGoods goods, long userId, CoinsLotterySeason season,
			GoodsOrderStatus status, CoinsLotterySeasonGoods seasonGoods) {
		CoinsLotterySeasonSellCount counter = lotterySellCountRepo
				.find("$findByGoodsAndSeason", Params.param("seasonId", season.getId()).put("goodsId", goods.getId()))
				.get();

		// 表示每天可抽中的商品数据为0
		if (seasonGoods.getSellCount() == 0) {
			return null;
		}
		// 表示此商品已经被抽走
		if (seasonGoods.getSellCount() > 0 && counter != null && counter.getCount0() >= seasonGoods.getSellCount()) {
			return null;
		}

		Goods orignalGoods = goodsService.get(goods.getId());

		CoinsGoodsOrder order = new CoinsGoodsOrder();
		order.setStatus(status);
		order.setOrderAt(new Date());
		order.setUserId(userId);
		order.setGoodsId(goods.getId());
		order.setCoinsGoodsId(goods.getId());
		order.setCoinsGoodsSnapshotId(goods.getCoinsLotteryGoodsSnapshotId());
		order.setP1(String.valueOf(season.getId()));
		order.setSource(CoinsGoodsOrderSource.HOLIDAY_ACTIVITY_01);
		order.setGoodsSnapshotId(orignalGoods.getGoodsSnapshotId());
		order.setTotalPrice(BigDecimal.ZERO);
		order.setDelStatus(Status.ENABLED);
		order.setCode(goodsOrderService.generateCode());
		order.setAmount(1);

		coinsGoodsOrderRepo.save(order);
		if (counter == null) {
			counter = new CoinsLotterySeasonSellCount();
			counter.setCount0(1);
			counter.setCoinsLotteryGoodsId(goods.getId());
			counter.setSeasonId(season.getId());
		} else {
			counter.setCount0(counter.getCount0() + 1);
		}
		lotterySellCountRepo.save(counter);

		CoinsGoodsOrderSnapshot orderSnapshot = createSnapshot(order);
		order.setCoinsGoodsOrderSnapshotId(orderSnapshot.getId());

		// 金币商品则获得相应的金币
		if (goods.getCoinsGoodsType() == CoinsGoodsType.COINS && goods.getLevel() != CoinsLotteryGoodsLevel.NOTHING) {
			order.setStatus(GoodsOrderStatus.COMPLETE);
			orderSnapshot.setStatus(GoodsOrderStatus.COMPLETE);
			coinsService.earn(CoinsAction.LOTTERY_DRAW_EARN_COINS, userId, orignalGoods.getPrice().intValue(),
					Biz.COINS_GOODS_ORDER, order.getId());

			orderSnapShotRepo.save(orderSnapshot);
		}

		coinsGoodsOrderRepo.save(order);
		return order;
	}
}
