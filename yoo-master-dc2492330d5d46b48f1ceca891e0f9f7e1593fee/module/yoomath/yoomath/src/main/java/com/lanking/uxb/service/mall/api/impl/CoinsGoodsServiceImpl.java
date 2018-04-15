package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroupGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.mall.api.CoinsGoodsQuery;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class CoinsGoodsServiceImpl implements CoinsGoodsService {

	@Autowired
	@Qualifier("CoinsGoodsRepo")
	private Repo<CoinsGoods, Long> coinsGoodsRepo;
	@Autowired
	@Qualifier("CoinsGoodsGroupGoodsRepo")
	private Repo<CoinsGoodsGroupGoods, Long> coinsGoodsGroupGoodsRepo;

	@Override
	public CoinsGoods get(long id) {
		return coinsGoodsRepo.get(id);
	}

	@Override
	public Map<Long, CoinsGoods> mget(Collection<Long> ids) {
		return coinsGoodsRepo.mget(ids);
	}

	@Override
	public Map<CoinsGoodsType, List<CoinsGoods>> listAllByType(CoinsGoodsQuery query) {
		int userType = 7;
		if (query.getUserType() == UserType.TEACHER) {
			userType = 1;
		} else if (query.getUserType() == UserType.STUDENT) {
			userType = 2;
		} else if (query.getUserType() == UserType.PARENT) {
			userType = 4;
		}
		List<CoinsGoods> all = coinsGoodsRepo
				.find("$listAll",
						Params.param("userType", userType).put("limit", query.getLimit()).put("nowDate", new Date()))
				.list();
		Map<CoinsGoodsType, List<CoinsGoods>> map = Maps.newHashMap();
		for (CoinsGoods coinsGoods : all) {
			List<CoinsGoods> oneTypeList = map.get(coinsGoods.getCoinsGoodsType());
			if (oneTypeList == null) {
				oneTypeList = Lists.newArrayList();
			}
			oneTypeList.add(coinsGoods);
			map.put(coinsGoods.getCoinsGoodsType(), oneTypeList);
		}
		return map;
	}

	@Override
	public Map<Long, List<CoinsGoods>> listAllByGroup(CoinsGoodsQuery query, Collection<Long> groupIds) {
		if (CollectionUtils.isEmpty(groupIds)) {
			return Collections.EMPTY_MAP;
		}
		int userType = 7;
		if (query.getUserType() == UserType.TEACHER) {
			userType = 1;
		} else if (query.getUserType() == UserType.STUDENT) {
			userType = 2;
		} else if (query.getUserType() == UserType.PARENT) {
			userType = 4;
		}
		List<CoinsGoods> all = coinsGoodsRepo.find("$listAllByGroup", Params.param("userType", userType)
				.put("limit", query.getLimit()).put("groupIds", groupIds).put("nowDate", new Date())).list();

		Map<Long, CoinsGoods> goodsMap = new HashMap<Long, CoinsGoods>(all.size());
		for (CoinsGoods c : all) {
			goodsMap.put(c.getId(), c);
		}

		List<CoinsGoodsGroupGoods> groupGoods = coinsGoodsGroupGoodsRepo
				.find("$findByGroups", Params.param("groupIds", groupIds).put("userType", userType)).list();

		Map<Long, List<CoinsGoods>> retMap = Maps.newHashMap();

		for (CoinsGoodsGroupGoods g : groupGoods) {
			List<CoinsGoods> list = retMap.get(g.getGroupId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			if (goodsMap.get(g.getGoodsId()) != null) {
				list.add(goodsMap.get(g.getGoodsId()));
			}

			retMap.put(g.getGroupId(), list);
		}

		return retMap;
	}
}
