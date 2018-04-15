package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsSnapshot;
import com.lanking.uxb.service.mall.api.LotteryActivityGoodsSnapshotService;

@Service
@Transactional(readOnly = true)
public class LotteryActivityGoodsSnapshotServiceImpl implements LotteryActivityGoodsSnapshotService {

	@Autowired
	@Qualifier("LotteryActivityGoodsSnapshotRepo")
	private Repo<LotteryActivityGoodsSnapshot, Long> repo;

	@Override
	public LotteryActivityGoodsSnapshot get(long snapshotId) {
		return repo.get(snapshotId);
	}

	@Override
	public Map<Long, LotteryActivityGoodsSnapshot> mget(Collection<Long> snapshotIds) {
		return repo.mget(snapshotIds);
	}

}
