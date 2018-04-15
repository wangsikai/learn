package com.lanking.uxb.service.mall.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.GoodsType;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoods;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsCategory;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.LotteryActivityGoodsService;

@Transactional(readOnly = true)
@Service
public class LotteryActivityGoodsServiceImpl implements LotteryActivityGoodsService {

	@Autowired
	@Qualifier("LotteryActivityGoodsRepo")
	private Repo<LotteryActivityGoods, Long> repo;
	@Autowired
	@Qualifier("LotteryActivityGoodsSnapshotRepo")
	private Repo<LotteryActivityGoodsSnapshot, Long> snapshotRepo;
	@Autowired
	@Qualifier("GoodsRepo")
	private Repo<Goods, Long> goodsRepo;
	@Autowired
	@Qualifier("GoodsSnapshotRepo")
	private Repo<GoodsSnapshot, Long> goodsSnapshotrepo;

	@Override
	public LotteryActivityGoods get(long id) {
		return repo.get(id);
	}

	@Override
	public List<LotteryActivityGoods> listByActivity(long activityCode) {
		return repo.find("$listByActivityCode", Params.param("activityCode", activityCode)).list();
	}

	@Override
	public LotteryActivityGoods getNothingGoods(long activityCode) {
		return repo.find("$getNothingGoods", Params.param("activityCode", activityCode)).list().get(0);
	}

	@Transactional
	@Override
	public void incrGoodsSelled(long goodsId) {
		LotteryActivityGoods goods = repo.get(goodsId);
		if (goods != null) {
			goods.setSellCount(goods.getSellCount() + 1);
			repo.save(goods);
		}
	}

	@Transactional
	@Override
	public List<LotteryActivityGoods> initNYD2017ActivityGoods(long activityCode,
			List<LotteryActivityGoodsCategory> categorys) {
		List<LotteryActivityGoods> list = new ArrayList<LotteryActivityGoods>();

		// 心灵鸡汤商品
		fillLotteryActivityGoods(activityCode, 0, 0, UserType.NULL, CoinsGoodsType.NOTHING, "心灵鸡汤", new BigDecimal(0),
				null);

		// 教师 铜蛋
		LotteryActivityGoods activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(0).getId(), 10,
				UserType.TEACHER, CoinsGoodsType.COINS, "5金币", new BigDecimal(5), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(0).getId(), 10, UserType.TEACHER,
				CoinsGoodsType.COINS, "10金币", new BigDecimal(10), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(0).getId(), 5, UserType.TEACHER,
				CoinsGoodsType.CASH, "5元现金红包", new BigDecimal(5), null);
		list.add(activityGoods);

		// 教师 银蛋
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(1).getId(), 5, UserType.TEACHER,
				CoinsGoodsType.COINS, "10金币", new BigDecimal(10), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(1).getId(), 5, UserType.TEACHER,
				CoinsGoodsType.COINS, "20金币", new BigDecimal(20), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(1).getId(), 25, UserType.TEACHER,
				CoinsGoodsType.CASH, "5元现金红包", new BigDecimal(5), null);
		list.add(activityGoods);

		// 教师 金蛋
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 6, UserType.TEACHER,
				CoinsGoodsType.TELEPHONE_CHARGE, "10元话费", new BigDecimal(10), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 3, UserType.TEACHER,
				CoinsGoodsType.TELEPHONE_CHARGE, "20元话费", new BigDecimal(20), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 1, UserType.TEACHER,
				CoinsGoodsType.TELEPHONE_CHARGE, "50元话费", new BigDecimal(50), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 1, UserType.TEACHER,
				CoinsGoodsType.COUPONS, "元祖西点50元电子券", new BigDecimal(50), 1L);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 1, UserType.TEACHER,
				CoinsGoodsType.COUPONS, "星巴克50元电子券", new BigDecimal(50), 2L);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 1, UserType.TEACHER,
				CoinsGoodsType.COUPONS, "肯德基50元电子券", new BigDecimal(50), 3L);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 1, UserType.TEACHER,
				CoinsGoodsType.COUPONS, "来伊份50元电子券", new BigDecimal(50), 4L);
		list.add(activityGoods);

		// 学生 铜蛋
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(0).getId(), 140, UserType.STUDENT,
				CoinsGoodsType.COINS, "5金币", new BigDecimal(5), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(0).getId(), 10, UserType.STUDENT,
				CoinsGoodsType.COINS, "10金币", new BigDecimal(10), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(0).getId(), 10, UserType.STUDENT,
				CoinsGoodsType.COINS, "20金币", new BigDecimal(20), null);
		list.add(activityGoods);

		// 学生 银蛋
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(1).getId(), 50, UserType.STUDENT,
				CoinsGoodsType.COINS, "5金币", new BigDecimal(5), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(1).getId(), 75, UserType.STUDENT,
				CoinsGoodsType.COINS, "10金币", new BigDecimal(10), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(1).getId(), 25, UserType.STUDENT,
				CoinsGoodsType.COINS, "20金币", new BigDecimal(20), null);
		list.add(activityGoods);

		// 学生 金蛋
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 10, UserType.STUDENT,
				CoinsGoodsType.COINS, "20金币", new BigDecimal(20), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 20, UserType.STUDENT,
				CoinsGoodsType.COINS, "50金币", new BigDecimal(50), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 10, UserType.STUDENT,
				CoinsGoodsType.COINS, "100金币", new BigDecimal(100), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 10, UserType.STUDENT,
				CoinsGoodsType.CASH, "5元现金红包", new BigDecimal(5), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 4, UserType.STUDENT,
				CoinsGoodsType.TELEPHONE_CHARGE, "10元话费", new BigDecimal(10), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 2, UserType.STUDENT,
				CoinsGoodsType.TELEPHONE_CHARGE, "20元话费", new BigDecimal(20), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 1, UserType.STUDENT,
				CoinsGoodsType.TELEPHONE_CHARGE, "50元话费", new BigDecimal(50), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 5, UserType.STUDENT,
				CoinsGoodsType.TELEPHONE_FLOW, "100M流量", new BigDecimal(0), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 10, UserType.STUDENT,
				CoinsGoodsType.QQ_VIP, "QQ会员一个月", new BigDecimal(0), null);
		list.add(activityGoods);
		activityGoods = fillLotteryActivityGoods(activityCode, categorys.get(2).getId(), 1, UserType.STUDENT,
				CoinsGoodsType.COUPONS, "来伊份50元电子券", new BigDecimal(50), 4L);
		list.add(activityGoods);

		return list;
	}

	private LotteryActivityGoods fillLotteryActivityGoods(long activityCode, long categoryId, int initCount,
			UserType userType, CoinsGoodsType goodsType, String name, BigDecimal price, Long image) {

		// 保存商品表和商品快照
		Goods goods = new Goods();
		goods.setCreateAt(new Date());
		goods.setUpdateAt(new Date());
		goods.setImage(image);
		goods.setName(name);
		if (goodsType == CoinsGoodsType.COINS) {
			goods.setPrice(price);
		} else {
			goods.setPriceRMB(price);
		}
		goods.setType(GoodsType.LOTTERY_ACTIVITY);
		goodsRepo.save(goods);

		GoodsSnapshot goodsSnapshot = new GoodsSnapshot();
		BeanUtils.copyProperties(goods, goodsSnapshot, "id");
		goodsSnapshot.setGoodsId(goods.getId());
		goodsSnapshotrepo.save(goodsSnapshot);
		goods.setGoodsSnapshotId(goodsSnapshot.getId());
		goodsRepo.save(goods);

		// 保存抽奖活动商品表和快照
		LotteryActivityGoods activityGoods = new LotteryActivityGoods();
		activityGoods.setId(goods.getId());
		activityGoods.setActivityCode(activityCode);
		activityGoods.setCategoryId(categoryId);
		activityGoods.setInitCount(initCount);
		activityGoods.setSellCount(0);
		activityGoods.setMemberType(MemberType.NONE);
		activityGoods.setUserType(userType);
		activityGoods.setType(goodsType);
		repo.save(activityGoods);

		LotteryActivityGoodsSnapshot activityGoodsSnapshot = new LotteryActivityGoodsSnapshot();
		BeanUtils.copyProperties(activityGoods, activityGoodsSnapshot, "id");
		activityGoodsSnapshot.setGoodsId(activityGoods.getId());
		snapshotRepo.save(activityGoodsSnapshot);
		activityGoods.setSnapshotId(activityGoodsSnapshot.getId());
		repo.save(activityGoods);

		return activityGoods;
	}
}
