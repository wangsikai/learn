package com.lanking.uxb.zycon.mall.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsGroupService;

@Service
@Transactional(readOnly = true)
public class ZycCoinsGoodsGroupServiceImpl implements ZycCoinsGoodsGroupService {

	@Autowired
	@Qualifier("CoinsGoodsGroupRepo")
	private Repo<CoinsGoodsGroup, Long> repo;

	@Override
	public List<CoinsGoodsGroup> list() {
		return repo.find("$zycList").list();
	}

	@Transactional
	@Override
	public void delGroup(long id) {
		repo.execute("$zycDelGroup", Params.param("id", id));
	}

	@Transactional
	@Override
	public void addGroup(String name) {
		CoinsGoodsGroup c = new CoinsGoodsGroup();
		c.setCreateAt(new Date());
		c.setName(name);
		c.setSequence(getMaxSequence() + 1);
		c.setUpdateAt(new Date());
		c.setStatus(Status.ENABLED);
		repo.save(c);
	}

	@Override
	public Integer publishCountInGroup(long id, int userType) {
		Params params = Params.param("id", id);
		params.put("nowTime", new Date());
		if (userType != 7) {
			params.put("userType", userType);
		}
		return repo.find("$publishCountInGroup", params).get(Integer.class);
	}

	@Transactional
	@Override
	public void move(long upMoveId, long downMoveId) {
		CoinsGoodsGroup up = this.get(upMoveId);
		CoinsGoodsGroup down = this.get(downMoveId);
		repo.execute("$zycUpMove", Params.param("upMoveId", upMoveId).put("sequence", down.getSequence()));
		repo.execute("$zycDownMove", Params.param("downMoveId", downMoveId).put("sequence", up.getSequence()));
	}

	@Override
	public CoinsGoodsGroup get(long id) {
		return repo.get(id);
	}

	@Override
	public Integer getMaxSequence() {
		Integer count = repo.find("$getMaxSequence").get(Integer.class);
		return count == null ? 1 : count;
	}

}
