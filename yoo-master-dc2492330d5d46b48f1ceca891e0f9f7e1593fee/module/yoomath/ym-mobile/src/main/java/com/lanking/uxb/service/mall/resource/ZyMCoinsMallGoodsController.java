package com.lanking.uxb.service.mall.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.value.VUserHonor;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvertOption;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 金币商城相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
@RestController
@RequestMapping("zy/m/mall/coins/goods")
public class ZyMCoinsMallGoodsController {

	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	private GrowthLogService growthLogService;

	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detail(long coinsGoodsId) {
		CoinsGoods coinsGoods = coinsGoodsService.get(coinsGoodsId);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("coinsGoods", coinsGoodsConvert.to(coinsGoods, new CoinsGoodsConvertOption(true)));
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
