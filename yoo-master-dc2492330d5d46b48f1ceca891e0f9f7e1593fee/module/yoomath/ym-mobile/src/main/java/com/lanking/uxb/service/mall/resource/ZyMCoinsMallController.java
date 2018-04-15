package com.lanking.uxb.service.mall.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.value.VUserHonor;
import com.lanking.uxb.service.mall.api.CoinsGoodsGroupService;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderService;
import com.lanking.uxb.service.mall.api.CoinsGoodsQuery;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvertOption;
import com.lanking.uxb.service.mall.convert.CoinsGoodsGroupConvert;
import com.lanking.uxb.service.mall.value.VCoinsGoodsGroup;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.convert.BannerConvert;

/**
 * 金币商城相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
@RestController
@RequestMapping("zy/m/mall/coins")
public class ZyMCoinsMallController {

	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsGoodsOrderService coinsGoodsOrderService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;
	@Autowired
	private CoinsGoodsGroupService coinsGoodsGroupService;
	@Autowired
	private CoinsGoodsGroupConvert coinsGoodsGroupConvert;

	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestHeader(value = "APP", required = false) YooApp app) {
		Map<String, Object> map = new HashMap<String, Object>(5);
		// banner
		if (app != null) {
			List<Banner> banners = bannerService.listEnable(new BannerQuery(app, BannerLocation.COINS_MALL));
			if (CollectionUtils.isNotEmpty(banners)) {
				map.put("banner", bannerConvert.to(banners));
			} else {
				map.put("banner", Collections.EMPTY_LIST);
			}
		}
		// 跑马灯
		map.put("latestRecord", coinsGoodsOrderService.getLatestRecord(Security.getUserType(), Security.getUserId()));
		// 两种类型的商品
		CoinsGoodsQuery query = new CoinsGoodsQuery();
		query.setLimit(16);
		query.setUserType(Security.getUserType());
		Map<CoinsGoodsType, List<CoinsGoods>> goodsMap = coinsGoodsService.listAllByType(query);
		if (goodsMap.containsKey(CoinsGoodsType.TELEPHONE_CHARGE)) {
			map.put(CoinsGoodsType.TELEPHONE_CHARGE.name(), coinsGoodsConvert
					.to(goodsMap.get(CoinsGoodsType.TELEPHONE_CHARGE), new CoinsGoodsConvertOption(true)));
		} else {
			map.put(CoinsGoodsType.TELEPHONE_CHARGE.name(), Collections.EMPTY_LIST);
		}

		if (goodsMap.containsKey(CoinsGoodsType.QQ_VIP)) {
			map.put(CoinsGoodsType.QQ_VIP.name(),
					coinsGoodsConvert.to(goodsMap.get(CoinsGoodsType.QQ_VIP), new CoinsGoodsConvertOption(true)));
		} else {
			map.put(CoinsGoodsType.QQ_VIP.name(), Collections.EMPTY_LIST);
		}
		// 返回积分相关
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		VUserHonor vhonor = new VUserHonor();
		vhonor.setLevels(userLevelsService.getUserLevel(0, UserLevelsService.MAXLEVEL + 1, Product.YOOMATH));
		vhonor.setCheckIn(growthLogService.getCheck(GrowthAction.DAILY_CHECKIN, Security.getUserId()));
		if (honor != null) {
			vhonor.setCoins(honor.getCoins());
			vhonor.setGrowth(honor.getGrowth());
			vhonor.setLevel(honor.getLevel());
		}
		map.put("honor", vhonor);
		return new Value(map);
	}

	/**
	 * 新金币商城首页
	 *
	 * @param app
	 *            {@link YooApp}
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "2/index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index2(@RequestHeader(value = "APP", required = false) YooApp app) {
		Map<String, Object> map = new HashMap<String, Object>(5);
		// banner
		if (app != null) {
			List<Banner> banners = bannerService.listEnable(new BannerQuery(app, BannerLocation.COINS_MALL));
			if (CollectionUtils.isNotEmpty(banners)) {
				map.put("banner", bannerConvert.to(banners));
			} else {
				map.put("banner", Collections.EMPTY_LIST);
			}
		}
		// 跑马灯
		map.put("latestRecord", coinsGoodsOrderService.getLatestRecord(Security.getUserType(), Security.getUserId()));

		List<CoinsGoodsGroup> groups = coinsGoodsGroupService.find();
		List<VCoinsGoodsGroup> vs = coinsGoodsGroupConvert.to(groups);

		List<VCoinsGoodsGroup> retVs = new ArrayList<VCoinsGoodsGroup>(vs.size());

		for (VCoinsGoodsGroup v : vs) {
			if (v.getGoods() == null || v.getGoods().size() == 0) {
				continue;
			}

			retVs.add(v);
		}

		map.put("groups", retVs);
		// 返回积分相关
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		VUserHonor vhonor = new VUserHonor();
		vhonor.setLevels(userLevelsService.getUserLevel(0, UserLevelsService.MAXLEVEL + 1, Product.YOOMATH));
		vhonor.setCheckIn(growthLogService.getCheck(GrowthAction.DAILY_CHECKIN, Security.getUserId()));
		if (honor != null) {
			vhonor.setCoins(honor.getCoins());
			vhonor.setGrowth(honor.getGrowth());
			vhonor.setLevel(honor.getLevel());
		}
		map.put("honor", vhonor);
		return new Value(map);
	}
}
