package com.lanking.uxb.service.mall.api.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.mall.api.CoinsGoodsDaySellCountService;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderQuery;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderService;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.api.CoinsLotteryGoodsService;
import com.lanking.uxb.service.mall.api.CoinsLotterySeasonService;
import com.lanking.uxb.service.mall.api.GoodsOrderService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.cache.GoodsOrderCacheService;
import com.lanking.uxb.service.mall.form.CoinsGoodsOrderForm;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

@Transactional(readOnly = true)
@Service
public class CoinsGoodsOrderServiceImpl implements CoinsGoodsOrderService {
	// 兑换成功信息滚动条目数
	private static final int MAX_LATEST_ORDER = 20;

	@Autowired
	@Qualifier("CoinsGoodsOrderRepo")
	private Repo<CoinsGoodsOrder, Long> orderRepo;
	@Autowired
	@Qualifier("CoinsGoodsOrderSnapshotRepo")
	private Repo<CoinsGoodsOrderSnapshot, Long> orderSnapshotRepo;

	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private GoodsOrderCacheService goodsOrderCacheService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsGoodsDaySellCountService coinsGoodsDaySellCountService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CoinsLotteryGoodsService coinsLotteryGoodsService;
	@Autowired
	private CoinsLotterySeasonService coinsLotterySeasonService;

	@SuppressWarnings("deprecation")
	@Transactional
	@Override
	public CoinsGoodsOrder createOrder(CoinsGoodsOrderForm form) {
		if (StringUtils.isNotBlank(form.getBindEmail())) {
			accountService.updateAccountEmailStatus(form.getBindEmail(), Status.ENABLED, form.getUserId());
		} else if (StringUtils.isNotBlank(form.getBindMobile())) {
			accountService.updateAccountMobileStatus(form.getBindMobile(), Status.ENABLED, form.getUserId());
		}
		CoinsGoods coinsGoods = coinsGoodsService.get(form.getCoinsGoodsId());
		Goods goods = goodsService.get(form.getGoodsId());
		// 金币商品未上架
		long nowTs = System.currentTimeMillis();
		if (coinsGoods.getStatus() == CoinsGoodsStatus.DRAFT || goods.getSalesTime().getTime() > nowTs) {
			// 商品未到兑换时间
			throw new ZuoyeException(ZuoyeException.ZUOYE_GOODS_NOTIN_SALESTIME);
		}
		if (coinsGoods.getStatus() == CoinsGoodsStatus.DELETE || coinsGoods.getStatus() == CoinsGoodsStatus.UN_PUBLISH
				|| goods.getSoldOutTime().getTime() < nowTs) {
			// 商品已下架
			throw new ZuoyeException(ZuoyeException.ZUOYE_GOODS_SOLDOUT);
		}

		Date nowDate = new Date();
		int nowHour = nowDate.getHours();
		int nowMin = nowDate.getMinutes();
		long nowMins = nowHour * 60 + nowMin;
		long startMins = coinsGoods.getDayStartHour() * 60 + coinsGoods.getDayStartMin();
		long endMins = coinsGoods.getDayEndHour() * 60 + coinsGoods.getDayEndMin();
		if (!(nowMins > startMins && nowMins < endMins)) {
			// 商品未到兑换时间
			throw new ZuoyeException(ZuoyeException.ZUOYE_GOODS_NOTIN_SALESTIME);
		}
		// dateStart限制判断
		if (coinsGoods.getDateStart() != null && System.currentTimeMillis() < coinsGoods.getDateStart().getTime()) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_GOODS_NOTIN_SALESTIME);
		}

		// 周内的限制
		if (coinsGoods.getWeekdayLimit() != null) {
			Calendar calendar = Calendar.getInstance();
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			dayOfWeek = dayOfWeek == 1 ? 6 : dayOfWeek - 2;
			int pow = BigDecimal.valueOf(Math.pow(10, dayOfWeek)).intValue();
			if (!(coinsGoods.getWeekdayLimit().intValue() / pow % 10 == 1)) {
				throw new ZuoyeException(ZuoyeException.ZUOYE_GOODS_NOTIN_SALESTIME);
			}
		}

		if (coinsGoods.getDayBuyCount() > -1 && countTodayCoinsGoodsBuyCount(form.getCoinsGoodsId(),
				form.getUserId()) >= coinsGoods.getDayBuyCount()) {// 每人每天购买限制
			throw new ZuoyeException(ZuoyeException.ZUOYE_GOODS_BUY_LIMIT);
		}

		int costCoins = goods.getPrice().intValue() * form.getAmount();
		// 金币不足兑换
		UserHonor honor = userHonorService.getUserHonor(form.getUserId());
		if (honor == null || honor.getCoins() < costCoins) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_COINS_NOT_ENOUGH);
		}

		int ut = coinsGoods.getUserType();
		// 商品不对这种类型的用户开放
		if (isWrongUserType(ut, form.getUserType())) {
			throw new NoPermissionException();
		}

		long date0 = Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(nowDate));

		int incrVal = 0;
		try {
			incrVal = coinsGoodsDaySellCountService.incrCount(form.getCoinsGoodsId(), date0, form.getAmount(),
					coinsGoods.getDaySellCount());

		} catch (Exception e) {
			throw new ServerException();
		}

		// 商品售完
		if (incrVal == 0) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_GOODS_SELL_OUT);
		}

		// 锁商品
		// 创建订单以及订单快照
		CoinsGoodsOrder order = new CoinsGoodsOrder();
		order.setAmount(form.getAmount());
		order.setCode(goodsOrderService.generateCode());
		order.setCoinsGoodsId(form.getCoinsGoodsId());
		order.setCoinsGoodsSnapshotId(form.getCoinsGoodsSnapshotId());
		order.setGoodsId(form.getGoodsId());
		order.setGoodsSnapshotId(form.getGoodsSnapshotId());
		order.setOrderAt(new Date());
		order.setPayMod(PayMode.COINS);
		if (coinsGoods.getCoinsGoodsType() == null) {
			order.setP0(form.getMemo());
		} else {
			if (coinsGoods.getCoinsGoodsType() == CoinsGoodsType.QQ_VIP) {
				order.setP0(form.getQq());
			} else if (coinsGoods.getCoinsGoodsType() == CoinsGoodsType.TELEPHONE_CHARGE
					|| coinsGoods.getCoinsGoodsType() == CoinsGoodsType.COUPONS) {
				order.setP0(form.getMobile());
			}
		}
		order.setStatus(form.getStatus());
		order.setTotalPrice(BigDecimal.valueOf(goods.getPrice().intValue() * form.getAmount()));
		order.setUserId(form.getUserId());
		order.setDelStatus(Status.ENABLED);
		orderRepo.save(order);
		CoinsGoodsOrderSnapshot snapshot = createOrderSnapshot(order);
		order.setCoinsGoodsOrderSnapshotId(snapshot.getId());
		orderRepo.save(order);

		// 扣除金币
		coinsService.earn(CoinsAction.BUY_COINS_GOODS, form.getUserId(), -costCoins, Biz.COINS_GOODS_ORDER,
				order.getId());

		return order;
	}

	@Override
	@Transactional
	public CoinsGoodsOrder createLotteryOrder(CoinsGoodsOrderForm form) {
		CoinsGoodsOrder order = null;
		CoinsLotteryGoods goods = coinsLotteryGoodsService.get(form.getGoodsId());
		if (form.getId() != null) {
			order = orderRepo.get(form.getId());
		} else {
			order = new CoinsGoodsOrder();
		}

		order.setUserType(form.getUserType());
		order.setAmount(form.getAmount());
		order.setCode(goodsOrderService.generateCode());
		order.setCoinsGoodsOrderSnapshotId(form.getCoinsGoodsSnapshotId());
		if (goods.getCoinsGoodsType() == CoinsGoodsType.QQ_VIP) {
			order.setP0(form.getQq());
		} else if (goods.getCoinsGoodsType() == CoinsGoodsType.TELEPHONE_CHARGE) {
			order.setP0(form.getMobile());
		}

		order.setStatus(form.getStatus());
		order.setUserId(form.getUserId());
		order.setDelStatus(Status.ENABLED);

		orderRepo.save(order);
		CoinsGoodsOrderSnapshot snapshot = createOrderSnapshot(order);
		order.setCoinsGoodsOrderSnapshotId(snapshot.getId());
		orderRepo.save(order);

		return order;
	}

	@Transactional
	@Override
	public CoinsGoodsOrderSnapshot createOrderSnapshot(CoinsGoodsOrder order) {
		CoinsGoodsOrderSnapshot snapshot = new CoinsGoodsOrderSnapshot();
		order.setPayMod(PayMode.COINS);
		snapshot.setAmount(order.getAmount());
		snapshot.setBuyerNotes(order.getBuyerNotes());
		snapshot.setCode(order.getCode());
		snapshot.setCoinsGoodsOrderId(order.getId());
		snapshot.setGoodsId(order.getGoodsId());
		snapshot.setGoodsSnapshotId(order.getGoodsSnapshotId());
		snapshot.setCoinsGoodsId(order.getCoinsGoodsId());
		snapshot.setCoinsGoodsSnapshotId(order.getCoinsGoodsSnapshotId());
		snapshot.setOrderAt(order.getOrderAt());
		snapshot.setP0(order.getP0());
		snapshot.setSellerNotes(order.getSellerNotes());
		snapshot.setStatus(order.getStatus());
		snapshot.setTotalPrice(order.getTotalPrice());
		snapshot.setUpdateAt(order.getUpdateAt());
		snapshot.setUpdateId(order.getUpdateId());
		snapshot.setUserId(order.getUserId());
		snapshot.setDelStatus(order.getDelStatus());
		return orderSnapshotRepo.save(snapshot);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<String> getLatestRecord(UserType userType, Long userId) {
		List<String> retValues = goodsOrderCacheService.getLatestOrder(userType);
		if (CollectionUtils.isEmpty(retValues)) {
			Params params = Params.param("limitValue", MAX_LATEST_ORDER).put("userType", userType.getValue());
			if (userId != null) {
				params.put("userId", userId);
			}
			List<Map> result = orderRepo.find("$findLatestOrder", params).list(Map.class);

			List<String> cacheOrders = new ArrayList<String>(MAX_LATEST_ORDER);
			for (Map m : result) {
				StringBuffer info = new StringBuffer();
				info.append(StringUtils.getMaskName((String) m.get("user_name"))).append("：").append("已兑换")
						.append(m.get("name"));
				cacheOrders.add(info.toString());
			}
			goodsOrderCacheService.addLatestOrder(userType, cacheOrders);
			return cacheOrders;
		}
		return retValues;
	}

	@Override
	public CursorPage<Long, CoinsGoodsOrder> query(CoinsGoodsOrderQuery query, CursorPageable<Long> pageable) {
		Params params = Params.param("userId", query.getUserId());
		if (query.getOrderSource() != null) {
			params.put("orderSource", query.getOrderSource().getValue());
		}
		params.put("ignoreActivity", query.getIgnoreActivity());
		return orderRepo.find("$query", params).fetch(pageable);
	}

	@Override
	public Page<CoinsGoodsOrder> queryPage(CoinsGoodsOrderQuery query, Pageable pageable) {
		Params params = Params.param("userId", query.getUserId());
		params.put("ignoreActivity", query.getIgnoreActivity());
		return orderRepo.find("$query", params).fetch(pageable);
	}

	@Transactional
	@Override
	public void delete(long id) {
		CoinsGoodsOrder order = orderRepo.get(id);
		if (order.getDelStatus() == Status.ENABLED) {
			if (!(order.getStatus() == GoodsOrderStatus.COMPLETE || order.getStatus() == GoodsOrderStatus.FAIL)) {
				throw new ServerException();
			}
			order.setDelStatus(Status.DISABLED);
			orderRepo.save(order);
		}
	}

	@Override
	public CoinsGoodsOrder get(long id) {
		return orderRepo.get(id);
	}

	@Override
	public List<CoinsGoodsOrder> queryLotteryUnFinish(long userId, long seasonId) {
		return orderRepo.find("$queryLotteryUnFinish", Params.param("userId", userId).put("seasonId", seasonId)).list();
	}

	@Override
	@Transactional
	public CoinsGoodsOrder fill(long id, String p0) {
		CoinsLotterySeason season = coinsLotterySeasonService.findNewest();
		if (StringUtils.isBlank(p0)) {
			throw new IllegalArgException();
		}
		// 说明已经过期了，不再允许兑换
		if (season.getEndTime().getTime() < System.currentTimeMillis()) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_SEASON_OVER);
		}
		CoinsGoodsOrder order = orderRepo.get(id);
		if (order == null) {
			throw new IllegalArgException();
		}
		if (order.getStatus() != GoodsOrderStatus.NOT_PAY) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_LOTTERY_ORDER_COMPLETE);
		}
		CoinsGoodsOrderSnapshot orderSnapshot = orderSnapshotRepo.get(order.getCoinsGoodsOrderSnapshotId());
		order.setP0(p0);
		orderSnapshot.setP0(p0);

		order.setStatus(GoodsOrderStatus.PAY);
		orderSnapshot.setStatus(GoodsOrderStatus.PAY);

		orderRepo.save(order);
		orderSnapshotRepo.save(orderSnapshot);

		return order;
	}

	@Override
	public long countTodayCoinsGoodsBuyCount(long coinsGoodsId, long userId) {
		return orderRepo
				.find("$countTodayCoinsGoodsBuyCount",
						Params.param("coinsGoodsId", coinsGoodsId).put("userId", userId).put("nowDate", new Date()))
				.count();
	}

	/**
	 * 判断是不是错误的用户类型,如果此商品不对当前用户类型开放则为true 若对此开放返回false
	 *
	 * @param permission
	 *            权限值(10进制)
	 * @param userType
	 *            当前用户类型
	 * @return true or false
	 */
	private boolean isWrongUserType(int permission, UserType userType) {
		String hexStr = String.format("%03d", Integer.parseInt(Integer.toBinaryString(permission)));
		switch (userType) {
		case PARENT:
			return hexStr.charAt(0) == '0';
		case STUDENT:
			return hexStr.charAt(1) == '0';
		case TEACHER:
			return hexStr.charAt(2) == '0';
		default:
			return true;
		}
	}

	@Override
	public Page<CoinsGoodsOrder> queryLotteryActivityOrderPage(Long activityCode, Long userId, Pageable pageable) {
		Params params = Params.param("orderSource", CoinsGoodsOrderSource.LOTTERY_ACTIVITY.getValue());
		if (null != activityCode) {
			params.put("activityCode", activityCode);
		}
		if (null != userId) {
			params.put("userId", userId);
		}
		return orderRepo.find("$queryLotteryActivityOrderPage", params).fetch(pageable);
	}

	@Override
	@Transactional
	public void fillLotteryActivityOrderP0(long orderId, String p0) {
		CoinsGoodsOrder order = orderRepo.get(orderId);
		order.setP0(p0);
		order.setStatus(GoodsOrderStatus.PAY);
		orderRepo.save(order);
	}

	@Override
	@Transactional
	public void deleteLotteryActivityOrders(long activityCode) {
		orderRepo.execute("delete from coins_goods_order where source=2 and pay_mod=4", Params.param());
		orderRepo.execute("delete from coins_goods_order_snapshot where source=2 and pay_mod=4", Params.param());
		orderRepo.execute("update lottery_activity_goods set sell_count=0 where activity_code=:activityCode",
				Params.param("activityCode", activityCode));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryLotteryActivityOrder(CoinsGoodsOrderSource orderSource, Long userId, Long seasonId) {
		Params params = Params.param();
		params.put("orderSource", orderSource.getValue());
		if (null != userId) {
			params.put("userId", userId);
		}
		if (null != seasonId) {
			params.put("seasonId", seasonId);
		}
		return orderRepo.find("$queryLotteryActivityOrder", params).list(Map.class);
	}

	@Override
	public List<CoinsGoodsOrder> queryLotteryUnFinish(long userId, long seasonId, CoinsGoodsOrderSource orderSource) {
		Params params = Params.param();
		params.put("userId", userId);
		params.put("seasonId", seasonId);
		params.put("orderSource", orderSource.getValue());
		return orderRepo.find("$queryLotteryUnFinishBySource", params).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, CoinsGoodsOrder> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}

		return orderRepo.mget(ids);
	}
}
