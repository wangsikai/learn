package com.lanking.uxb.service.mall.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.value.VUserHonor;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderService;
import com.lanking.uxb.service.mall.api.CoinsLotteryGoodsService;
import com.lanking.uxb.service.mall.api.CoinsLotteryGoodsSnapshotService;
import com.lanking.uxb.service.mall.api.CoinsLotterySeasonService;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;
import com.lanking.uxb.service.mall.cache.CoinsLotteryRecordCacheService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvertOption;
import com.lanking.uxb.service.mall.convert.CoinsLotteryGoodsConvert;
import com.lanking.uxb.service.mall.convert.CoinsLotterySeasonConvert;
import com.lanking.uxb.service.mall.value.VCoinsGoodsOrder;
import com.lanking.uxb.service.mall.value.VCoinsLotteryDraw;
import com.lanking.uxb.service.mall.value.VCoinsLotteryGoods;
import com.lanking.uxb.service.mall.value.VCoinsLotterySeason;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 金币抽奖 Controller
 * 
 * @author xinyu.zhou
 * @since 2.4.0
 */
@RestController
@RequestMapping(value = "zy/mall/coins/lottery")
public class ZyCoinsLotteryController {
	@Autowired
	private CoinsLotteryGoodsService coinsLotteryGoodsService;
	@Autowired
	private CoinsLotteryGoodsConvert coinsLotteryGoodsConvert;
	@Autowired
	private CoinsLotterySeasonService coinsLotterySeasonService;
	@Autowired
	private CoinsLotterySeasonConvert coinsLotterySeasonConvert;
	@Autowired
	private CoinsGoodsOrderService coinsGoodsOrderService;
	@Autowired
	private CoinsGoodsOrderConvert coinsGoodsOrderConvert;
	@Autowired
	private GoodsSnapshotService goodsSnapshotService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private CoinsLotteryRecordCacheService recordCacheService;
	@Autowired
	private CoinsLotteryGoodsSnapshotService coinsLotteryGoodsSnapshotService;

	/**
	 * 获得抽奖的奖品列表
	 *
	 * @param id
	 *            期别id
	 * @param code
	 *            期别code
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index(Long id, Integer code) {
		Map<String, Object> retMap = new HashMap<String, Object>(9);

		VCoinsLotterySeason rule = null;
		if (code != null && code > 0) {
			rule = coinsLotterySeasonConvert.to(coinsLotterySeasonService.findNewestByCode(code));
		} else {
			if (id != null && id > 0) {
				rule = coinsLotterySeasonConvert.to(coinsLotterySeasonService.get(id));
			} else {
				rule = coinsLotterySeasonConvert.to(coinsLotterySeasonService.findNewest());
			}
		}
		retMap.put("season", rule);

		if (rule != null) {
			List<VCoinsLotteryGoods> goods = coinsLotteryGoodsConvert
					.to(coinsLotteryGoodsService.findAll(rule.getId()));
			retMap.put("goods", goods);

			// 处理用户未完成的订单数据
			if (rule.getStatus() == Status.ENABLED) {
				CoinsGoodsOrderConvertOption orderConvertOption = new CoinsGoodsOrderConvertOption();
				orderConvertOption.setInitCoinsGoods(true);
				List<VCoinsGoodsOrder> orders = coinsGoodsOrderConvert.to(
						coinsGoodsOrderService.queryLotteryUnFinish(Security.getUserId(), rule.getId()),
						orderConvertOption);

				// 获得快照商品
				List<Long> goodsSnapshotIds = new ArrayList<Long>(orders.size());
				List<Long> coinsGoodsSnapshotIds = new ArrayList<Long>(orders.size());
				for (VCoinsGoodsOrder v : orders) {
					goodsSnapshotIds.add(v.getGoodsSnapshotId());
					coinsGoodsSnapshotIds.add(v.getCoinsGoodsSnapshotId());
				}
				if (goodsSnapshotIds.size() > 0) {
					Map<Long, GoodsSnapshot> goodsSnapshotMap = goodsSnapshotService.mget(goodsSnapshotIds);
					Map<Long, CoinsLotteryGoodsSnapshot> coinsLotteryGoodsSnapshotMap = coinsLotteryGoodsSnapshotService
							.mget(coinsGoodsSnapshotIds);
					for (VCoinsGoodsOrder v : orders) {
						GoodsSnapshot goodsSnapshot = goodsSnapshotMap.get(v.getGoodsSnapshotId());
						if (goodsSnapshot != null) {
							v.getGoods().setName(goodsSnapshot.getName());
							v.getGoods().setContent(goodsSnapshot.getContent());
							v.setCoinsGoodsType(coinsLotteryGoodsSnapshotMap.get(v.getCoinsGoodsSnapshotId())
									.getCoinsGoodsType());
						}
					}
				}
				retMap.put("orders", orders);
			} else {
				retMap.put("orders", Collections.EMPTY_LIST);
			}

			UserHonor userHonor = userHonorService.getUserHonor(Security.getUserId());
			// 新注册用户没有金币情况
			if (userHonor != null) {
				VUserHonor honor = new VUserHonor();
				honor.setCoins(userHonor.getCoins());
				honor.setGrowth(userHonor.getGrowth());
				honor.setLevel(userHonor.getLevel());

				retMap.put("honor", honor);
			}

			retMap.put("lotteryTimes", rule.getUserJoinTimes());

			List<String> regularLotteryRecords = recordCacheService.getLevelRecords(CoinsLotteryGoodsLevel.REGULAR,
					rule.getId());

			retMap.put(CoinsLotteryGoodsLevel.REGULAR.toString(), assembleRecords(regularLotteryRecords));

			List<String> topLotteryRecords = recordCacheService.getLevelRecords(CoinsLotteryGoodsLevel.TOP,
					rule.getId());

			retMap.put(CoinsLotteryGoodsLevel.TOP.toString(), assembleRecords(topLotteryRecords));
		}

		return new Value(retMap);
	}

	@SuppressWarnings("unchecked")
	private List<VCoinsLotteryDraw> assembleRecords(List<String> regularLotteryRecords) {
		if (CollectionUtils.isNotEmpty(regularLotteryRecords)) {
			List<VCoinsLotteryDraw> userInfos = new ArrayList<VCoinsLotteryDraw>(regularLotteryRecords.size());
			for (String r : regularLotteryRecords) {
				String userInfo = r.substring(0, r.indexOf(";"));
				String goodsInfo = r.substring(r.indexOf(";") + 1);
				VCoinsLotteryDraw v = new VCoinsLotteryDraw();
				v.setUserInfo(userInfo);
				v.setGoodsInfo(goodsInfo);

				userInfos.add(v);
			}

			return userInfos;
		}

		return Collections.EMPTY_LIST;
	}

	/**
	 * 抽奖
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@ApiAllowed(accessRate = 0)
	@RequestMapping(value = "draw", method = { RequestMethod.GET, RequestMethod.POST })
	public Value draw(Long id, Integer code) {

		Map<String, Object> retMap = new HashMap<String, Object>(2);

		try {
			CoinsLotterySeason season = null;
			if (code != null && code > 0) {
				season = coinsLotterySeasonService.findNewestByCode(code);
				id = season == null ? 0 : season.getId();
			}

			if (id == null || id <= 0) {
				return new Value(new IllegalArgException());
			}

			CoinsGoodsOrder order = coinsLotteryGoodsService.lottery(Security.getUserId(), id);

			retMap.put("goodsId", order.getCoinsGoodsId());
			retMap.put("orderId", order.getId());

			coinsService
					.earn(CoinsAction.LOTTERY_DRAW, Security.getUserId(), -10, Biz.COINS_GOODS_ORDER, order.getId());
		} catch (ZuoyeException e) {
			return new Value(e);
		}

		return new Value(retMap);
	}

	/**
	 * 补充表单(中奖信息之后传递过来的)
	 *
	 * @param orderId
	 *            订单id
	 * @param p0
	 *            手机号码或者qq号码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "fill", method = { RequestMethod.GET, RequestMethod.POST })
	public Value fill(long orderId, String p0) {
		try {
			coinsGoodsOrderService.fill(orderId, p0);
		} catch (ZuoyeException e) {
			return new Value(e);
		}
		return new Value();
	}
}
