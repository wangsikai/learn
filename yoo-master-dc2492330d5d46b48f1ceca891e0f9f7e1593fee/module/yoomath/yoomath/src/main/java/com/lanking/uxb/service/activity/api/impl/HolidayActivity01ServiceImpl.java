package com.lanking.uxb.service.activity.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01User;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01UserLuckyDraw;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.activity.api.HolidayActivity01Service;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserLuckyDrawService;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserService;
import com.lanking.uxb.service.activity.form.HolidayActivity01OrderForm;
import com.lanking.uxb.service.activity.form.HolidayActivityOrderSaveForm;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderService;
import com.lanking.uxb.service.mall.api.CoinsLotteryGoodsService;
import com.lanking.uxb.service.mall.api.CoinsLotterySeasonService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvert;
import com.lanking.uxb.service.mall.convert.CoinsLotteryGoodsConvert;
import com.lanking.uxb.service.mall.value.VCoinsGoodsOrder;
import com.lanking.uxb.service.mall.value.VCoinsLotteryGoods;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 假期活动01接口实现
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01ServiceImpl implements HolidayActivity01Service {
	@Autowired
	@Qualifier("HolidayActivity01Repo")
	private Repo<HolidayActivity01, Long> repo;
	@Autowired
	@Qualifier("CoinsGoodsOrderRepo")
	private Repo<CoinsGoodsOrder, Long> coinsGoodsOrderRepo;

	@Autowired
	private CoinsLotteryGoodsService coinsLotteryGoodsService;
	@Autowired
	private HolidayActivity01UserService holidayActivity01UserService;
	@Autowired
	private CoinsLotterySeasonService ruleService;
	@Autowired
	private HolidayActivity01UserLuckyDrawService drawService;
	@Autowired
	private CoinsGoodsOrderService coinsGoodsOrderService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CoinsLotteryGoodsConvert goodsConvert;
	@Autowired
	private CoinsGoodsOrderConvert coinsGoodsOrderConvert;

	// 活动状态：未开始
	private static final String ACTIVITY_NOTBEGIN = "NOTBEGIN";
	// 活动状态：已结束
	private static final String ACTIVITY_OVER = "OVER";

	@Override
	public Map<String, Object> getActivityCfg(long code) {
		HolidayActivity01 hActive = get(code);
		Map<String, Object> data = new HashMap<String, Object>();
		if (hActive != null) {
			data.put("startTime", hActive.getStartTime());
			data.put("endTime", hActive.getEndTime());
		}
		HolidayActivity01Cfg cfg = hActive.getCfg();
		if (cfg != null) {
			data.put("minClassStudents", cfg.getMinClassStudents());
			data.put("luckyDrawOneHomework", cfg.getLuckyDrawOneHomework());
			data.put("submitRateThreshold", cfg.getSubmitRateThreshold());
			data.put("luckyDrawThreshold", cfg.getLuckyDrawThreshold());
			if (cfg.getSeasonId() != null) {
				List<VCoinsLotteryGoods> vGoodsList = goodsConvert
						.to(coinsLotteryGoodsService.findAll(cfg.getSeasonId()));
				List<String> goodNameList = new ArrayList<String>();
				for (VCoinsLotteryGoods c : vGoodsList) {
					goodNameList.add(c.getName());
				}
				data.put("prizeList", goodNameList);
			}
		}
		return data;
	}

	@Override
	public HolidayActivity01 get(long code) {
		return repo.get(code);
	}

	@Override
	@Transactional
	public int getNewLuckyDraw(long code, long userId) {
		HolidayActivity01User user = holidayActivity01UserService.getByUserId(code, userId);
		if (user == null) {
			return 0;
		}

		int newLuckyDraw = new Integer(user.getNewLuckyDraw());
		// 用户弹框提示后新增抽奖机会置0
		if (newLuckyDraw > 0) {
			holidayActivity01UserService.resetUserNewLuckyDraw(code, userId);
		}

		return newLuckyDraw;
	}

	@Override
	public Map<String, Object> getLotteryIndex(long code, long userId) {
		Map<String, Object> data = new HashMap<String, Object>();
		HolidayActivity01 activity = get(code);
		if (activity == null) {
			return null;
		}
		HolidayActivity01Cfg cfg = activity.getCfg();

		HolidayActivity01User user = holidayActivity01UserService.getByUserId(code, userId);
		// 活动状态status
		long time = System.currentTimeMillis();
		if (activity.getStatus() == Status.ENABLED && time >= activity.getStartTime().getTime()
				&& time <= activity.getEndTime().getTime()) {
			data.put("status", Status.ENABLED);
		} else if (time <= activity.getStartTime().getTime()) {
			// 活动未开始
			data.put("status", ACTIVITY_NOTBEGIN);
		} else if (time >= activity.getEndTime().getTime()) {
			// 活动已结束
			data.put("status", ACTIVITY_OVER);
		}

		// 剩余抽奖次数
		data.put("luckyDraw", user == null ? 0 : user.getLuckyDraw() - user.getCostLuckyDraw());
		// 活动开始时间
		data.put("startTime", activity.getStartTime());
		// 活动结束时间
		data.put("endTime", activity.getEndTime());
		// 当日用户已抽奖次数
		long todayLottery = drawService.getCountByUser(code, userId, new Date());
		data.put("todayLottery", todayLottery);
		// 用户绑定手机号
		Account account = accountService.getAccountByUserId(userId);
		if (account != null) {
			data.put("mobile", account.getMobile());
		} else {
			data.put("mobile", null);
		}
		// 活动奖品
		List<VCoinsLotteryGoods> vGoodsList = goodsConvert.to(coinsLotteryGoodsService.findAll(cfg.getSeasonId()));
		data.put("vGoodsList", vGoodsList);
		// 每天限制次数
		CoinsLotterySeason season = null;
		if (cfg.getSeasonId() != null && cfg.getSeasonId() > 0) {
			season = ruleService.get(cfg.getSeasonId());
		} else {
			season = ruleService.findNewest();
		}
		data.put("userJoinTimes", season.getUserJoinTimes());

		// 中奖未兑换信息
		if (ACTIVITY_OVER.equals(data.get("status"))) {
			data.put("notpayOrder", Lists.newArrayList());
		} else {
			// 从CoinsGoodsOrder表中查询该用户名下该活动，status是未支付的订单
			List<CoinsGoodsOrder> notpayOrder = coinsGoodsOrderService.queryLotteryUnFinish(userId, cfg.getSeasonId(),
					CoinsGoodsOrderSource.HOLIDAY_ACTIVITY_01);
			data.put("notpayOrder", coinsGoodsOrderConvert.to(notpayOrder));
		}

		// 中奖榜单(用户名中间加*,奖品名称)
		@SuppressWarnings("rawtypes")
		List<Map> orderList = coinsGoodsOrderService
				.queryLotteryActivityOrder(CoinsGoodsOrderSource.HOLIDAY_ACTIVITY_01, null, cfg.getSeasonId());
		// 大奖CoinsLotteryGoodsLevel.TOP
		// 其它奖品(不包括谢谢参与,最多展示30个)
		List<HolidayActivity01OrderForm> topOrder = new ArrayList<>();
		List<HolidayActivity01OrderForm> ordinaryOrder = new ArrayList<>();
		int ordinaryCount = 0;
		for (Map order : orderList) {
			Integer level = 0;
			if (order.get("level") != null) {
				level = Integer.parseInt(order.get("level").toString());
			}
			long orderUserId = Long.valueOf(order.get("user_id").toString());
			long orderId = Long.valueOf(order.get("id").toString());
			if (CoinsLotteryGoodsLevel.TOP.getValue() == level) {
				HolidayActivity01OrderForm form = new HolidayActivity01OrderForm();
				form.setId(orderId);
				form.setLevel(CoinsLotteryGoodsLevel.findByValue(level));
				form.setName(order.get("name").toString());
				form.setUserId(orderUserId);
				form.setUserName(encyName(order.get("username").toString()));
				topOrder.add(form);
			} else if (level != CoinsLotteryGoodsLevel.NOTHING.getValue()) {
				if (ordinaryCount >= 30) {
					continue;
				}
				HolidayActivity01OrderForm form = new HolidayActivity01OrderForm();
				form.setId(orderId);
				form.setLevel(CoinsLotteryGoodsLevel.findByValue(level));
				form.setName(order.get("name").toString());
				form.setUserId(orderUserId);
				form.setUserName(encyName(order.get("username").toString()));
				ordinaryOrder.add(form);
				ordinaryCount++;
			}
		}
		data.put("topOrder", topOrder);
		data.put("ordinaryOrder", ordinaryOrder);

		return data;
	}

	/**
	 * 加密用户名
	 * 
	 * @param name
	 * @return
	 */
	private String encyName(String name) {
		StringBuilder builder = new StringBuilder();
		if (name == null) {
			return null;
		}
		if (name.length() == 1) {
			builder.append(name);
			builder.append("**");
		} else if (name.length() >= 2) {
			builder.append(name.substring(0, 1));
			builder.append("**");
			builder.append(name.substring(name.length() - 1, name.length()));
		}

		return builder.toString();
	}

	@Override
	@Transactional
	public Map<String, Object> getHolidayDraw(long code, long userId) {
		Map<String, Object> retMap = new HashMap<>();
		HolidayActivity01 activity = get(code);
		if (activity == null) {
			return null;
		}
		HolidayActivity01Cfg cfg = activity.getCfg();
		CoinsLotterySeason season = null;
		if (cfg.getSeasonId() != null && cfg.getSeasonId() > 0) {
			season = ruleService.get(cfg.getSeasonId());
		} else {
			season = ruleService.findNewest();
		}

		// 抽奖已经结束，不可以再抽
		long time = System.currentTimeMillis();
		if (activity.getEndTime().getTime() < time || activity.getStartTime().getTime() > time) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_SEASON_OVER);
		}

		// 抽奖设置:0正常抽奖,1不可出奖,2达到保底设置
		int drawSet = 0;
		// 当日用户已抽奖次数
		Map<String, Integer> drawCountMap = getUserTodayDraw(code, userId);
		int totalDraw = drawCountMap.get("totalDraw");
		int win = drawCountMap.get("win");
		int nothingDraw = drawCountMap.get("nothingDraw");

		// 用户存在的抽奖次数
		HolidayActivity01User activityUser = holidayActivity01UserService.getByUserId(code, userId);
		if (activityUser != null && (activityUser.getLuckyDraw() <= activityUser.getCostLuckyDraw())) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_USER_TIMES_LIMIT);
		}

		// 用户当日抽奖次数达上限不可抽奖,直接报错
		if (season.getUserJoinTimes() > 0 && totalDraw >= season.getUserJoinTimes()) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_TIMES_LIMIT);
		}
		// 保底设置
		if (season.getMustAwardsTimes() != -1) {
			if (nothingDraw >= season.getMustAwardsTimes()) {
				drawSet = 2;
			}
		}
		// 每人每天的中奖次数限制,超过全部抽中nothing
		if (season.getUserAwardsTimes() != -1) {
			if (win >= season.getUserAwardsTimes()) {
				drawSet = 1;
			}
		}
		// 奖池每天可以中奖的次数限制,超过全部抽中nothing
		if (season.getAwardsTimes() != -1) {
			Map<String, Integer> poolDrawMap = getPoolTodayDraw(code);
			int poolwin = poolDrawMap.get("win");
			if (poolwin >= season.getAwardsTimes()) {
				drawSet = 1;
			}
		}

		Map<String, Object> orderMap = coinsLotteryGoodsService.holidayDraw(userId, code, season, drawSet);
		CoinsGoodsType coinsGoodsType = (CoinsGoodsType) orderMap.get("goodsType");
		CoinsGoodsOrder coinsGoodsOrder = (CoinsGoodsOrder) orderMap.get("order");
		int sequence = (int) orderMap.get("sequence");

		// 更新假期
		holidayActivity01UserService.addUserLuckyDraw(code, userId, -1, false);
		// 抽奖记录
		HolidayActivity01UserLuckyDraw record = new HolidayActivity01UserLuckyDraw();
		record.setOrderId(coinsGoodsOrder.getId());
		record.setActivityCode(code);
		record.setCreateAt(new Date());
		record.setUserId(userId);
		drawService.save(record);

		// 取商品名
		Goods orignalGoods = goodsService.get(coinsGoodsOrder.getGoodsId());

		retMap.put("goodsName", orignalGoods.getName());
		retMap.put("goodsType", coinsGoodsType);
		retMap.put("orderId", coinsGoodsOrder.getId());
		retMap.put("nothing", coinsGoodsType == CoinsGoodsType.NOTHING ? true : false);
		retMap.put("sequence", sequence);

		return retMap;
	}

	/**
	 * 获得用户当日抽奖次数,中奖次数,累计未中奖次数
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户id
	 */
	private Map<String, Integer> getUserTodayDraw(long code, long userId) {
		Map<String, Integer> retMap = new HashMap<>();
		int totalDraw = 0;
		int win = 0;
		int nothingDraw = 0;
		// 当日用户已抽奖次数
		List<HolidayActivity01UserLuckyDraw> drawList = drawService.getByUser(code, userId, new Date());
		if (drawList == null) {
			retMap.put("totalDraw", totalDraw);
			retMap.put("win", win);
			retMap.put("nothing", nothingDraw);
			return retMap;
		}

		List<Long> orderIds = new ArrayList<>();
		for (HolidayActivity01UserLuckyDraw draw : drawList) {
			orderIds.add(draw.getOrderId());
		}
		Map<Long, CoinsGoodsOrder> orderMapList = coinsGoodsOrderService.mget(orderIds);
		List<CoinsGoodsOrder> orderList = Lists.newArrayList();
		for (CoinsGoodsOrder coinsGoodsOrder : orderMapList.values()) {
			orderList.add(coinsGoodsOrder);
		}

		totalDraw = orderList.size();
		List<VCoinsGoodsOrder> vorderList = coinsGoodsOrderConvert.to(orderList);
		for (VCoinsGoodsOrder vorder : vorderList) {
			if (vorder.getCoinsGoodsType() != null) {
				if (vorder.getCoinsGoodsType() != CoinsGoodsType.NOTHING
						&& vorder.getCoinsGoodsType() != CoinsGoodsType.NULL) {
					win++;
				}
			}
		}

		for (int i = (vorderList.size() - 1); i >= 0; i--) {
			if (CoinsGoodsType.NOTHING == vorderList.get(i).getCoinsGoodsType()
					|| CoinsGoodsType.NULL == vorderList.get(i).getCoinsGoodsType()) {
				nothingDraw++;
			} else {
				break;
			}
		}

		retMap.put("totalDraw", totalDraw);
		retMap.put("win", win);
		retMap.put("nothingDraw", nothingDraw);
		return retMap;
	}

	/**
	 * 获得奖池当日抽奖次数,中奖次数
	 * 
	 * @param code
	 *            活动code
	 */
	private Map<String, Integer> getPoolTodayDraw(long code) {
		Map<String, Integer> retMap = new HashMap<>();
		int totalDraw = 0;
		int win = 0;
		// 当日奖池已抽奖次数
		List<HolidayActivity01UserLuckyDraw> drawList = drawService.getByCode(code, new Date());
		if (drawList == null) {
			retMap.put("totalDraw", totalDraw);
			retMap.put("win", win);
			return retMap;
		}

		List<Long> orderIds = new ArrayList<>();
		for (HolidayActivity01UserLuckyDraw draw : drawList) {
			orderIds.add(draw.getOrderId());
		}
		Map<Long, CoinsGoodsOrder> orderMapList = coinsGoodsOrderService.mget(orderIds);
		List<CoinsGoodsOrder> orderList = Lists.newArrayList();
		for (CoinsGoodsOrder coinsGoodsOrder : orderMapList.values()) {
			orderList.add(coinsGoodsOrder);
		}

		totalDraw = orderList.size();
		List<VCoinsGoodsOrder> vorderList = coinsGoodsOrderConvert.to(orderList);
		for (VCoinsGoodsOrder vorder : vorderList) {
			if (vorder.getCoinsGoodsType() != null) {
				if (vorder.getCoinsGoodsType() != CoinsGoodsType.NOTHING
						&& vorder.getCoinsGoodsType() != CoinsGoodsType.NULL) {
					win++;
				}
			}
		}

		retMap.put("totalDraw", totalDraw);
		retMap.put("win", win);
		return retMap;
	}

	@Override
	@Transactional
	public CoinsGoodsOrder updateHolidayOrder(HolidayActivityOrderSaveForm order) {
		CoinsGoodsOrder goodsOrder = coinsGoodsOrderRepo.get(order.getOrderId());
		if (goodsOrder == null) {
			return goodsOrder;
		}

		goodsOrder.setContactName(order.getContactName());
		goodsOrder.setContactPhone(order.getContactPhone());
		goodsOrder.setContactAddress(order.getContactAddress());
		goodsOrder.setP0(order.getP0());
		goodsOrder.setStatus(GoodsOrderStatus.PAY);
		coinsGoodsOrderRepo.save(goodsOrder);

		return goodsOrder;
	}
}
