package com.lanking.uxb.zycon.mall.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.GoodsBaseInfo;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsBaseInfo;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroupGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsGroupGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsService;
import com.lanking.uxb.zycon.mall.form.CoinsGoodsForm;

@Service
@Transactional(readOnly = true)
public class ZycCoinsGoodsServiceImpl implements ZycCoinsGoodsService {

	@Autowired
	@Qualifier("CoinsGoodsRepo")
	private Repo<CoinsGoods, Long> repo;

	@Autowired
	@Qualifier("GoodsRepo")
	private Repo<Goods, Long> goodsRepo;

	@Autowired
	@Qualifier("GoodsSnapshotRepo")
	private Repo<GoodsSnapshot, Long> goodsSnapshotRepo;

	@Autowired
	@Qualifier("CoinsGoodsSnapshotRepo")
	private Repo<CoinsGoodsSnapshot, Long> coinsGoodsSnapshotRepo;

	@Autowired
	@Qualifier("CoinsGoodsGroupGoodsRepo")
	private Repo<CoinsGoodsGroupGoods, Long> coinsGoodsGroupGoodsRepo;
	private Logger logger = LoggerFactory.getLogger(ZycCoinsGoodsServiceImpl.class);

	@Autowired
	private ZycCoinsGoodsGroupGoodsService groupGoodsService;

	@Override
	public CoinsGoods get(Long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, CoinsGoods> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Transactional
	@Override
	public void saveCoinsGoods(CoinsGoodsForm goods) {
		//
		Goods insertGoods = convertBaseGoodsInfo(goods);
		CoinsGoods coinsGoods = convertCoinsGoodsInfo(goods);
		CoinsGoodsStatus oldStatus = coinsGoods.getStatus();
		coinsGoods.setStatus(goods.getStatus());

		// 保存基本信息快照
		GoodsSnapshot gss = new GoodsSnapshot();
		BeanUtils.copyProperties(insertGoods, gss, GoodsBaseInfo.class);
		gss.setGoodsId(insertGoods.getId() == null ? 0 : insertGoods.getId());
		gss.setId(null);// 置空
		goodsSnapshotRepo.save(gss);

		// 保存基本信息
		insertGoods.setGoodsSnapshotId(gss.getId());
		goodsRepo.save(insertGoods);

		// 保存金币商品信息快照
		CoinsGoodsSnapshot cgss = new CoinsGoodsSnapshot();
		BeanUtils.copyProperties(coinsGoods, cgss, CoinsGoodsBaseInfo.class);
		cgss.setCoinsGoodsId(insertGoods.getId());
		cgss.setId(null);// 置空
		coinsGoodsSnapshotRepo.save(cgss);

		// 保存金币商品信息
		coinsGoods.setId(insertGoods.getId());// 保存商品Id一致
		coinsGoods.setCoinsGoodsSnapshotId(cgss.getId());
		/**
		 * 新增保存groupGoods表<br>
		 * senhao.wang 20170321
		 */
		groupGoodsService.saveGroupGoods(insertGoods.getId(), goods, oldStatus);
		repo.save(coinsGoods);

		// 在保存一次快照
		if (gss.getGoodsId() <= 0) {
			gss.setGoodsId(insertGoods.getId());
			goodsSnapshotRepo.save(gss);
		}
	}

	private Goods convertBaseGoodsInfo(CoinsGoodsForm goods) {
		Goods insertGoods = new Goods();
		if (goods.getGoodsId() > 0) {// 修改
			insertGoods = goodsRepo.get(goods.getGoodsId());
			Assert.notNull(insertGoods);
			insertGoods.setUpdateAt(new Date());
			insertGoods.setUpdateId(Security.getUserId());
		} else { // 新增
			insertGoods.setCreateAt(new Date());
			insertGoods.setCreateId(Security.getUserId());
		}

		insertGoods.setName(goods.getName());
		insertGoods.setIntroduction(goods.getIntroduction());
		insertGoods.setContent(goods.getContent());
		insertGoods.setImage(goods.getImageId());
		insertGoods.setPrice(goods.getPrice());
		insertGoods.setSalesTime(goods.getSalesTime());
		insertGoods.setSoldOutTime(goods.getSoldOutTime());

		return insertGoods;
	}

	private CoinsGoods convertCoinsGoodsInfo(CoinsGoodsForm goods) {
		CoinsGoods insertGoods = new CoinsGoods();
		if (goods.getGoodsId() > 0) {// 修改
			insertGoods = repo.get(goods.getGoodsId());
			Assert.notNull(insertGoods);
			// 状态没有变的时候，不更新坑位
			if (insertGoods.getStatus() != goods.getStatus()) {
				setSequence(goods, insertGoods);
			}
		} else {
			// 发布商品时，设置坑位
			setSequence(goods, insertGoods);
		}

		insertGoods.setUserType(Integer.parseInt(goods.getUserTypeStr(), 2));
		// 商品属性 senhao.wang 20170321新增
		insertGoods.setCoinsGoodsType(goods.getCoinsGoodsType());
		insertGoods.setDaySellCount(goods.getDaySellCount());
		insertGoods.setDayStartHour(goods.getDayStartHour());
		insertGoods.setDayEndHour(goods.getDayEndHour());
		insertGoods.setDayStartMin(goods.getDayStartMin());
		insertGoods.setDayEndMin(goods.getDayEndMin());
		insertGoods.setWeekdayLimit(goods.getWeekdayLimit());
		insertGoods.setDayBuyCount(goods.getDayBuyCount());
		// 新增前台展示 senhao.wang 20170321新增
		insertGoods.setDaySellShowCount(goods.getDaySellShowCount());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (goods.getDateStart() != null) {
			try {
				insertGoods.setDateStart(sdf.parse(goods.getDateStart() + ":00"));
			} catch (ParseException e) {
				logger.error("convertCoinsGoodsInfo error", e);
			}
			// 如果修改的话，需要把之前的清空
			insertGoods.setWeekdayLimit(null);
		} else {
			insertGoods.setDateStart(null);
		}
		if (goods.getWeekdayLimit() != null) {
			insertGoods.setWeekdayLimit(goods.getWeekdayLimit());
			insertGoods.setDateStart(null);
		} else {
			insertGoods.setWeekdayLimit(null);
		}
		return insertGoods;
	}

	private void setSequence(CoinsGoodsForm goods, CoinsGoods insertGoods) {
		insertGoods.setSequence0(null);
		insertGoods.setSequence1(null);
		insertGoods.setSequence2(null);
		if (goods.getStatus() == CoinsGoodsStatus.PUBLISH && goods.getSalesTime().before(new Date())
				&& goods.getSoldOutTime().after(new Date())) {
			// 排坑号 二进制转十进制 111 {家长,学生,老师}
			int userType = Integer.parseInt(goods.getUserTypeStr(), 2);
			switch (userType) {
			case 7:// 面向全部
				insertGoods.setSequence0(getCoinGoodsMaxSequence(0, goods.getVirtualGoodsType()));
				insertGoods.setSequence1(getCoinGoodsMaxSequence(1, goods.getVirtualGoodsType()));
				insertGoods.setSequence2(getCoinGoodsMaxSequence(2, goods.getVirtualGoodsType()));
				break;
			case 1:// 面向老师
				insertGoods.setSequence0(getCoinGoodsMaxSequence(0, goods.getVirtualGoodsType()));
				break;
			case 2:// 面向学生
				insertGoods.setSequence1(getCoinGoodsMaxSequence(1, goods.getVirtualGoodsType()));
				break;
			case 4:// 面向家长
				insertGoods.setSequence2(getCoinGoodsMaxSequence(2, goods.getVirtualGoodsType()));
				break;
			}
		}
	}

	private int getCoinGoodsMaxSequence(int _index, CoinsGoodsType coinsGoodsType) {
		int seq = 0;
		Params prams = Params.param();
		prams.put("_index", _index);
		if (coinsGoodsType != null) {
			prams.put("virtualGoodsType", coinsGoodsType.getValue());
		}
		prams.put("nowTime", new Date());
		Integer temp = repo.find("$getCoinsGoodsMaxSequence", prams).get(Integer.class);
		if (temp == null) {
			temp = 0;
		}
		seq = temp + 1;
		return seq;
	}

	@Override
	public Page<CoinsGoods> queryCoinsGoods(CoinsGoodsType coinsGoodsType, int userType, Pageable p) {
		Params params = Params.param();
		if (coinsGoodsType != null) {
			params.put("virtualGoodsType", coinsGoodsType.getValue());
		}
		params.put("userType", userType);
		params.put("nowTime", new Date());
		return repo.find("$queryCoinsGoods", params).fetch(p);
	}

	@Override
	public Integer getSalingGoodsCount(CoinsGoodsType coinsGoodsType, String useTypeStr) {
		Params params = Params.param();
		params.put("virtualGoodsType", coinsGoodsType.getValue());
		params.put("userType", Integer.parseInt(useTypeStr, 2));
		params.put("nowTime", new Date());
		Integer temp = repo.find("$getSalingGoodsCount", params).get(Integer.class);

		return (temp == null ? 0 : temp);
	}

	@Transactional
	@Override
	public void updateCoinsGoodsStatus(Long goodsId, CoinsGoodsStatus status) {
		CoinsGoods cg = repo.get(goodsId);
		if (cg == null) {
			return;
		}
		if (cg.getStatus() == status) {// 状态相同，则直接返回
			return;
		}

		//
		if (status.getValue() == CoinsGoodsStatus.UN_PUBLISH.getValue()
				|| status.getValue() == CoinsGoodsStatus.DELETE.getValue()) {
			// 把坑位置空
			cg.setSequence0(null);
			cg.setSequence1(null);
			cg.setSequence2(null);

		}

		cg.setStatus(status);
		repo.save(cg);
		/**
		 * 更新groupGoods表<br>
		 * senhao.wang 20170322
		 */
		CoinsGoodsGroupGoods cc = groupGoodsService.getGroupGoodsByGoodsId(goodsId);
		cc.setSequence0(null);
		cc.setSequence1(null);
		cc.setSequence2(null);
		cc.setUpdateAt(new Date());
		coinsGoodsGroupGoodsRepo.save(cc);
	}

	// 移动坑位 上移或者下移，既是两者交换
	@Transactional
	@Override
	public void exchangeSequenceOfCoinsGoods(Long goodsId, Integer dic, int userType) {
		CoinsGoods cg = repo.get(goodsId);
		if (cg == null || cg.getStatus().getValue() != CoinsGoodsStatus.PUBLISH.getValue()) {
			return;
		}

		// 查询出需要移动的数据
		Params params = Params.param();
		params.put("dic", dic);
		params.put("userType", userType);
		params.put("virtualGoodsType", cg.getCoinsGoodsType().getValue());
		params.put("nowTime", new Date());
		if (userType == 1) {
			params.put("sequence", cg.getSequence0());
		}
		if (userType == 2) {
			params.put("sequence", cg.getSequence1());
		}
		if (userType == 4) {
			params.put("sequence", cg.getSequence2());
		}
		// 因为存在删除的情况，不可以直接取+1或者-1的序列号，这里取离序列号最近的序列号(倒序或者正序)
		CoinsGoods cg2 = repo.find("$getExchangeSeqGoods", params).get();
		if (cg2 == null || cg2.getStatus().getValue() != CoinsGoodsStatus.PUBLISH.getValue()) {
			return;
		}

		// 二进制转十进制 111 {家长,学生,老师}
		switch (userType) {
		case 1:// 面向老师
			int seq0_1 = cg.getSequence0();
			int seq0_2 = cg2.getSequence0();
			cg.setSequence0(seq0_2);
			cg2.setSequence0(seq0_1);
			repo.save(cg);
			repo.save(cg2);
			break;
		case 2:// 面向学生
			int seq1_1 = cg.getSequence1();
			int seq1_2 = cg2.getSequence1();
			cg.setSequence1(seq1_2);
			cg2.setSequence1(seq1_1);
			repo.save(cg);
			repo.save(cg2);
			break;
		case 4:// 面向家长
			int seq2_1 = cg.getSequence2();
			int seq2_2 = cg2.getSequence2();
			cg.setSequence2(seq2_2);
			cg2.setSequence2(seq2_1);
			repo.save(cg);
			repo.save(cg2);
			break;
		}

	}

	// 坑位中间去掉一条后，批量移动其他坑位
	private void batchMoveSequenceOfCoinsGoods(CoinsGoods cg) {
		// 二进制转十进制 111 {家长,学生,老师}
		switch (cg.getUserType()) {
		case 7:// 面向所有
			dealBatchMoveSequence(cg, 0);
			dealBatchMoveSequence(cg, 1);
			dealBatchMoveSequence(cg, 2);
			break;
		case 1:// 面向老师
			dealBatchMoveSequence(cg, 0);
			break;
		case 2:// 面向学生
			dealBatchMoveSequence(cg, 1);
			break;
		case 4:// 面向家长
			dealBatchMoveSequence(cg, 2);
			break;
		}
	}

	@Transactional
	@Override
	public void dealBatchMoveSequence(CoinsGoods goods, int _index) {

		Params params = Params.param();
		params.put("_index", _index);
		params.put("virtualGoodsType", goods.getCoinsGoodsType().getValue());
		params.put("nowTime", new Date());

		//
		CoinsGoods g = repo.get(goods.getId());

		if (_index == 0 && goods.getSequence0() != null && goods.getSequence0() > 0) {
			params.put("oldSequence", goods.getSequence0());
			repo.execute("$batchMoveSequence", params);
			//
			g.setSequence0(null);
			repo.save(g);
		} else if (_index == 1 && goods.getSequence1() != null && goods.getSequence1() > 0) {
			params.put("oldSequence", goods.getSequence1());
			repo.execute("$batchMoveSequence", params);
			//
			g.setSequence1(null);
			repo.save(g);
		} else if (_index == 2 && goods.getSequence2() != null && goods.getSequence2() > 0) {
			params.put("oldSequence", goods.getSequence2());
			repo.execute("$batchMoveSequence", params);
			//
			g.setSequence2(null);
			repo.save(g);
		}

	}

	@Override
	public List<CoinsGoods> getSalingGoodsOfNullSequence(int _index, CoinsGoodsType coinsGoodsType) {
		// 查询出所有上架商品 ，但是seq为空的商品
		Params params = Params.param();
		params.put("_index", _index);
		params.put("virtualGoodsType", coinsGoodsType.getValue());
		params.put("nowTime", new Date());
		return repo.find("$getSalingGoodsOfNullSequence", params).list();
	}

	@Transactional
	@Override
	public void dealSequenceOfNull(CoinsGoods goods, int _index) {
		if (goods == null) {
			return;
		}
		CoinsGoods gg = repo.get(goods.getId());
		if (gg == null) {
			return;
		}
		switch (_index) {
		case 0:// 面向老师
			gg.setSequence0(getCoinGoodsMaxSequence(0, gg.getCoinsGoodsType()));
			repo.save(gg);
			break;
		case 1:// 面向学生
			gg.setSequence1(getCoinGoodsMaxSequence(1, gg.getCoinsGoodsType()));
			repo.save(gg);
			break;
		case 2:// 面向家长
			gg.setSequence2(getCoinGoodsMaxSequence(2, gg.getCoinsGoodsType()));
			repo.save(gg);
			break;
		}
	}

	// 查询出所有自动下架的商品
	@Override
	public List<CoinsGoods> getSequenceOffGoods(int _index, CoinsGoodsType coinsGoodsType) {
		Params params = Params.param();
		params.put("_index", _index);
		params.put("virtualGoodsType", coinsGoodsType.getValue());
		params.put("nowTime", new Date());

		return repo.find("$getSequenceOffGoods", params).list();
	}

	@Override
	public Page<CoinsGoods> queryCoinsGoods2(long id, int userType, Pageable p) {
		Params params = Params.param();
		params.put("id", id);
		params.put("userType", userType);
		params.put("nowTime", new Date());
		return repo.find("$queryCoinsGoods2", params).fetch(p);
	}

}
