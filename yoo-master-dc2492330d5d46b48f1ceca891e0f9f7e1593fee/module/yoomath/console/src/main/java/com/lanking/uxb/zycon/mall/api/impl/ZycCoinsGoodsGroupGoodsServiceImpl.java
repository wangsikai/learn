package com.lanking.uxb.zycon.mall.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroupGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsGroupGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsService;
import com.lanking.uxb.zycon.mall.form.CoinsGoodsForm;

@Service
@Transactional(readOnly = true)
public class ZycCoinsGoodsGroupGoodsServiceImpl implements ZycCoinsGoodsGroupGoodsService {

	@Autowired
	@Qualifier("CoinsGoodsGroupGoodsRepo")
	private Repo<CoinsGoodsGroupGoods, Long> repo;
	@Autowired
	private ZycCoinsGoodsService goodsService;

	@Override
	public Long getGroupIdByGoodsId(long goodsId) {
		return repo.find("$getGroupIdByGoodsId", Params.param("goodsId", goodsId)).get(Long.class);
	}

	@Override
	public Map<Long, Long> getGroupIdsByGoodsIds(Collection<Long> goodsIds) {
		List<CoinsGoodsGroupGoods> list = repo.find("$getGroupIdsByGoodsIds", Params.param("goodsIds", goodsIds))
				.list();
		Map<Long, Long> map = new HashMap<Long, Long>();
		for (CoinsGoodsGroupGoods c : list) {
			map.put(c.getGoodsId(), c.getGroupId());
		}
		return map;
	}

	@Transactional
	@Override
	public void clearSequence(long goodsId) {
		repo.execute("$clearSequence", Params.param("goodsId", goodsId));
	}

	@Transactional
	@Override
	public void saveGroupGoods(Long goodsId, CoinsGoodsForm goods, CoinsGoodsStatus oldStatus) {
		CoinsGoodsGroupGoods c = this.getGroupGoodsByGoodsId(goodsId);
		if (c == null) {
			c = new CoinsGoodsGroupGoods();
			c.setCreateAt(new Date());
			c.setGroupId(goods.getGroupId());
			c.setGoodsId(goodsId);
		} else {
			c.setUpdateAt(new Date());
			c.setGroupId(goods.getGroupId());
			// 如果状态没有发生改变，对序号不进行操作
			if (oldStatus == goods.getStatus()) {
				return;

			}
			c.setSequence0(null);
			c.setSequence1(null);
			c.setSequence2(null);
		}
		if (goods.getStatus() == CoinsGoodsStatus.PUBLISH && goods.getSalesTime().before(new Date())
				&& goods.getSoldOutTime().after(new Date())) {
			int userType = Integer.parseInt(goods.getUserTypeStr(), 2);

			if (userType == 7) {
				// 面向全部
				c.setSequence0(this.getMaxSequence(1, goods.getGroupId()) + 1);
				c.setSequence1(this.getMaxSequence(2, goods.getGroupId()) + 1);
				c.setSequence2(this.getMaxSequence(4, goods.getGroupId()) + 1);
			} else if (userType == 1) {
				// 面向老师
				c.setSequence0(this.getMaxSequence(userType, goods.getGroupId()) + 1);
			} else if (userType == 2) {
				// 面向学生
				c.setSequence1(this.getMaxSequence(userType, goods.getGroupId()) + 1);
			} else if (userType == 4) {
				// 面向家长
				c.setSequence2(this.getMaxSequence(userType, goods.getGroupId()) + 1);
			}
		}
		repo.save(c);
	}

	@Override
	public CoinsGoodsGroupGoods getGroupGoodsByGoodsId(long goodsId) {
		return repo.find("$getGroupGoodsByGoodsId", Params.param("goodsId", goodsId)).get();
	}

	@Override
	public Integer getMaxSequence(int userType, long groupId) {
		Integer temp = repo.find("$getMaxSequence", Params.param("userType", userType).put("groupId", groupId)).get(
				Integer.class);
		return temp == null ? 0 : temp;
	}

	@Transactional
	@Override
	public void move(long upMoveId, long downMoveId, int userType) {
		CoinsGoodsGroupGoods up = this.getGroupGoodsByGoodsId(upMoveId);
		CoinsGoodsGroupGoods down = this.getGroupGoodsByGoodsId(downMoveId);
		Params params = Params.param("userType", userType);
		if (userType == 1) {
			repo.execute("$zycUpMove", params.put("upMoveId", upMoveId).put("sequence", down.getSequence0()));
			repo.execute("$zycDownMove", params.put("downMoveId", downMoveId).put("sequence", up.getSequence0()));
		} else if (userType == 2) {
			repo.execute("$zycUpMove", params.put("upMoveId", upMoveId).put("sequence", down.getSequence1()));
			repo.execute("$zycDownMove", params.put("downMoveId", downMoveId).put("sequence", up.getSequence1()));
		} else if (userType == 4) {
			repo.execute("$zycUpMove", params.put("upMoveId", upMoveId).put("sequence", down.getSequence2()));
			repo.execute("$zycDownMove", params.put("downMoveId", downMoveId).put("sequence", up.getSequence2()));
		}
	}

	@Transactional
	@Override
	public void reviewSequence(long groupId) {
		// 处理到了上架时间还没处理的
		dealSequenceGroundGoodsOfNull(groupId, 1);
		dealSequenceGroundGoodsOfNull(groupId, 2);
		dealSequenceGroundGoodsOfNull(groupId, 4);
		// 处理下架的
		dealSequenceOffGoods(groupId, 1);
		dealSequenceOffGoods(groupId, 2);
		dealSequenceOffGoods(groupId, 4);
	}

	@Transactional
	@Override
	public void dealSequenceGroundGoodsOfNull(long groupId, int userType) {
		// 查询到了上架时间序号为空的数据
		List<CoinsGoodsGroupGoods> list = repo.find("$QuerySequenceGroundGoodsOfNull",
				Params.param("groupId", groupId).put("userType", userType).put("nowTime", new Date())).list();
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (CoinsGoodsGroupGoods c : list) {
			if (userType == 1) {
				// 面向老师
				c.setSequence0(this.getMaxSequence(userType, groupId) + 1);
			} else if (userType == 2) {
				// 面向学生
				c.setSequence1(this.getMaxSequence(userType, groupId) + 1);
			} else if (userType == 4) {
				// 面向家长
				c.setSequence2(this.getMaxSequence(userType, groupId) + 1);
			}
			repo.save(c);
		}
	}

	@Transactional
	@Override
	public void dealSequenceOffGoods(long groupId, int userType) {
		repo.execute("$dealSequenceOffGoods",
				Params.param("groupId", groupId).put("userType", userType).put("nowTime", new Date()));
	}
}
