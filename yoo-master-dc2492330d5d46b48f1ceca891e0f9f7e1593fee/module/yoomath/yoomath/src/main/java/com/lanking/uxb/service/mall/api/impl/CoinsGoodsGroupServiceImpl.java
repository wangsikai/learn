package com.lanking.uxb.service.mall.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.CoinsGoodsGroupService;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
@Service
@Transactional(readOnly = true)
public class CoinsGoodsGroupServiceImpl implements CoinsGoodsGroupService {
	@Autowired
	@Qualifier("CoinsGoodsGroupRepo")
	private Repo<CoinsGoodsGroup, Long> repo;

	@Override
	public List<CoinsGoodsGroup> find() {
		return repo.find("$findAll", Params.param()).list();
	}
}
