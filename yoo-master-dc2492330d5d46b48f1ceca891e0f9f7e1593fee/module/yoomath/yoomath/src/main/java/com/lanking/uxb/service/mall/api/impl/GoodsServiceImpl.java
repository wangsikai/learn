package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.uxb.service.mall.api.GoodsService;

@Transactional(readOnly = true)
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	@Qualifier("GoodsRepo")
	private Repo<Goods, Long> goodsRepo;

	@Override
	public Goods get(long id) {
		return goodsRepo.get(id);
	}

	@Override
	public Map<Long, Goods> mget(Collection<Long> ids) {
		return goodsRepo.mget(ids);
	}

	@Override
	public List<Goods> mgetList(Collection<Long> ids) {
		return goodsRepo.mgetList(ids);
	}
}
