package com.lanking.uxb.zycon.mall.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.uxb.zycon.mall.api.ZycGoodsService;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class ZycGoodsServiceImpl implements ZycGoodsService {

	@Autowired
	@Qualifier("GoodsRepo")
	private Repo<Goods, Long> repo;

	@Override
	public Goods get(Long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, Goods> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

}
