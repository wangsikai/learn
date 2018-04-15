package com.lanking.uxb.service.mall.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderQuery;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderService;
import com.lanking.uxb.service.mall.api.CoinsGoodsQuery;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvertOption;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvertOption;
import com.lanking.uxb.service.mall.value.VCoinsGoods;
import com.lanking.uxb.service.mall.value.VCoinsGoodsOrder;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.sys.value.VBanner;

/**
 * 金币商城首页.
 * 
 * @author wlche
 * @since 2.0.3
 */
@RestController
@RequestMapping("zy/mall/index")
public class ZyCoinsMallIndexController {
	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private CoinsGoodsOrderService coinsGoodsOrderService;
	@Autowired
	private CoinsGoodsOrderConvert coinsGoodsOrderConvert;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private GoodsSnapshotService goodsSnapshotService;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;

	/**
	 * 金币动态.
	 * 
	 * @return
	 */
	@RequestMapping(value = "dynamic")
	public Value dynamic() {
		List<String> list = coinsGoodsOrderService.getLatestRecord(Security.getUserType(), Security.getUserId());
		return new Value(list);
	}

	/**
	 * 首页商品.
	 * 
	 * @return
	 */
	@RequestMapping(value = "goods")
	public Value goods() {
		Map<String, Object> map = new HashMap<String, Object>(4);
		CoinsGoodsQuery query = new CoinsGoodsQuery();
		query.setLimit(Integer.MAX_VALUE);
		query.setUserType(Security.getUserType());
		Map<CoinsGoodsType, List<CoinsGoods>> goodsMap = coinsGoodsService.listAllByType(query);
		List<CoinsGoods> tellGoods = goodsMap.get(CoinsGoodsType.TELEPHONE_CHARGE);
		List<CoinsGoods> qqGoods = goodsMap.get(CoinsGoodsType.QQ_VIP);
		CoinsGoodsConvertOption option = new CoinsGoodsConvertOption();
		option.setInitDaySelledCount(true);
		map.put("tells", tellGoods == null ? Lists.newArrayList(0) : coinsGoodsConvert.to(tellGoods, option));
		map.put("qqs", qqGoods == null ? Lists.newArrayList(0) : coinsGoodsConvert.to(qqGoods, option));

		// 取金币商城首页背景及抽奖banner
		BannerQuery bannerQuery = new BannerQuery();
		bannerQuery.setLocation(BannerLocation.COINS_MALL_LUCKDRAW);
		List<Banner> banners = bannerService.listEnable(bannerQuery);
		if (CollectionUtils.isNotEmpty(banners)) {
			List<VBanner> vbanners = bannerConvert.to(banners);
			map.put("banners", vbanners);
		}

		return new Value(map);
	}

	/**
	 * 兑换记录.
	 * 
	 * @return
	 */
	@RequestMapping(value = "records")
	public Value records(Integer pageNo, Integer size) {
		size = size == null ? 20 : size;
		pageNo = pageNo == null ? 1 : pageNo;
		CoinsGoodsOrderQuery query = new CoinsGoodsOrderQuery();
		query.setUserId(Security.getUserId());
		query.setIgnoreActivity(true);
		Page<CoinsGoodsOrder> cpage = coinsGoodsOrderService.queryPage(query, P.offset((pageNo - 1) * size, size));
		// Page<CoinsGoodsOrderSnapshot> cpage =
		// coinsGoodsOrderService.querySnapshotPage(query,
		// P.offset((pageNo - 1) * size, size));

		VPage<VCoinsGoodsOrder> page = new VPage<VCoinsGoodsOrder>();
		page.setCurrentPage(pageNo);
		CoinsGoodsOrderConvertOption option = new CoinsGoodsOrderConvertOption();
		option.setInitCoinsGoods(true);
		List<VCoinsGoodsOrder> orders = coinsGoodsOrderConvert.to(cpage.getItems(), option);

		// 获得快照商品
		List<Long> goodsSnapshotIds = new ArrayList<Long>(orders.size());
		for (VCoinsGoodsOrder v : orders) {
			goodsSnapshotIds.add(v.getGoodsSnapshotId());
		}
		if (goodsSnapshotIds.size() > 0) {
			Map<Long, GoodsSnapshot> goodsSnapshotMap = goodsSnapshotService.mget(goodsSnapshotIds);
			for (VCoinsGoodsOrder v : orders) {
				GoodsSnapshot goodsSnapshot = goodsSnapshotMap.get(v.getGoodsSnapshotId());
				if (goodsSnapshot != null) {
					v.getGoods().setName(goodsSnapshot.getName());
					v.getGoods().setContent(goodsSnapshot.getContent());
				}
			}
		}

		page.setItems(orders);
		page.setTotal(cpage.getTotalCount());
		page.setPageSize(size);
		page.setTotalPage(cpage.getPageCount());
		return new Value(page);
	}

	/**
	 * 删除订单记录.
	 * 
	 * @param orderId
	 *            订单ID.
	 * @return
	 */
	@RequestMapping(value = "deleteRecord")
	public Value deleteRecord(Long orderId) {
		if (orderId == null) {
			return new Value(new MissingArgumentException());
		}
		try {
			coinsGoodsOrderService.delete(orderId);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 检测是否能够下单.
	 * 
	 * @param goodsId
	 *            商品ID.
	 * @return
	 */
	@RequestMapping(value = "checkBuy")
	public Value checkBuy(Long goodsId) {
		if (null == goodsId) {
			return new Value(new MissingArgumentException());
		}
		CoinsGoods coinsGoods = coinsGoodsService.get(goodsId);
		if (null != coinsGoods) {
			VCoinsGoods vo = coinsGoodsConvert.to(coinsGoods);
			if (vo.getToStartTime() <= 0 || vo.getToEndTime() > -5) {
				// 不再购买时段内
				return new Value(1);
			} else {
				UserHonor userHonor = userHonorService.getUserHonor(Security.getUserId());
				if (userHonor == null || userHonor.getCoins() < vo.getCoinsPrice()) {
					// 金币不够
					return new Value(2);
				}
			}
		}
		return new Value(0);
	}
}
