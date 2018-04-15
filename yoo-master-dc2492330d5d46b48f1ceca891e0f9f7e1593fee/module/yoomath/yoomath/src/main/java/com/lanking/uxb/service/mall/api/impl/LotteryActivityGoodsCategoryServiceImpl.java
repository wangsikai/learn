package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsCategory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.LotteryActivityGoodsCategoryService;

@Transactional(readOnly = true)
@Service
public class LotteryActivityGoodsCategoryServiceImpl implements LotteryActivityGoodsCategoryService {
	@Autowired
	@Qualifier("LotteryActivityGoodsCategoryRepo")
	private Repo<LotteryActivityGoodsCategory, Long> repo;

	@Override
	public List<LotteryActivityGoodsCategory> listByActivityCode(long activityCode) {
		return repo.find("$listCategorysByActivity", Params.param("activityCode", activityCode)).list();
	}

	@Override
	@Transactional
	public void save(Collection<LotteryActivityGoodsCategory> categorys) {
		repo.save(categorys);
	}
}
