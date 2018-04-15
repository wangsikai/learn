package com.lanking.uxb.zycon.mall.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroupGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.uxb.zycon.mall.form.CoinsGoodsForm;

public interface ZycCoinsGoodsGroupGoodsService {

	/**
	 * 通过商品id获取商品组id<br>
	 * 目前一个商品id只会对应一个商品组
	 * 
	 * @param goodsId
	 * @return
	 */
	Long getGroupIdByGoodsId(long goodsId);

	/**
	 * 批量获取商品组Id
	 * 
	 * @param goodsIds
	 * @return
	 */
	Map<Long, Long> getGroupIdsByGoodsIds(Collection<Long> goodsIds);

	/**
	 * 通过商品id获取商品组对象<br>
	 * 目前一个商品id只会对应一个商品组
	 * 
	 * @param goodsId
	 * @return
	 */
	CoinsGoodsGroupGoods getGroupGoodsByGoodsId(long goodsId);

	/**
	 * 下架时需要置空之前的序号,以防上架后顺序错乱
	 * 
	 * @param goodsId
	 */
	void clearSequence(long goodsId);

	/**
	 * 保存GroupGoods
	 * 
	 * @param goodsId
	 *            为空时表示是新增，反之为编辑
	 * @param goods
	 */
	void saveGroupGoods(Long goodsId, CoinsGoodsForm goods, CoinsGoodsStatus oldStatus);

	/**
	 * 对当前商品组内到了上架时间和下架时间的商品重新进行排序<br>
	 * 1.到了上架时间的，Sequence设为当前商品组最大Sequence+1<br>
	 * 2.到了下架时间的，Sequence设为null
	 * 
	 * @param groupId
	 */
	void reviewSequence(long groupId);

	/**
	 * 处理到了上架时间，还没有处理的数据
	 * 
	 * @param groupId
	 * @param userType
	 */
	void dealSequenceGroundGoodsOfNull(long groupId, int userType);

	/**
	 * 处理到了下架时间，还没有处理的数据
	 * 
	 * @param groupId
	 * @param userType
	 */
	void dealSequenceOffGoods(long groupId, int userType);

	/**
	 * 通过用户和商品组ID，获取最大序号
	 * 
	 * @param userType
	 * @param groupId
	 * @return
	 */
	Integer getMaxSequence(int userType, long groupId);

	/**
	 * 
	 * @param upMoveId
	 * @param downMoveId
	 * @param userType
	 */
	void move(long upMoveId, long downMoveId, int userType);

}
