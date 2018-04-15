package com.lanking.uxb.zycon.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.mall.form.CoinsGoodsForm;

/**
 * 金币商品service
 * 
 * @since V2.0
 * @author wangsenhao
 *
 */
public interface ZycCoinsGoodsService {
	/**
	 * 获取单个金币兑换商品
	 * 
	 * @param id
	 * @return
	 */
	CoinsGoods get(Long id);

	/**
	 * 批量获取多个金币兑换商品
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, CoinsGoods> mget(Collection<Long> ids);

	/**
	 * 保存金币商品
	 * 
	 * @param goods
	 *
	 * @author zdy
	 */
	void saveCoinsGoods(CoinsGoodsForm goods);

	/**
	 * 分页查询金币商品
	 * 
	 * @param coinsGoodsType
	 * @param useTypeStr
	 * @param index
	 * @return
	 *
	 * @author zdy
	 */
	Page<CoinsGoods> queryCoinsGoods(CoinsGoodsType coinsGoodsType, int userType, Pageable p);

	/**
	 * 分页查询金币商品
	 * 
	 * @param id
	 *            商品组id
	 * @param userType
	 *            用户类型
	 * @return
	 *
	 * @author senhao.wang
	 */
	Page<CoinsGoods> queryCoinsGoods2(long id, int userType, Pageable p);

	/**
	 * 更新金币商品状态
	 * 
	 * @param goodsId
	 *
	 * @author zdy
	 */
	void updateCoinsGoodsStatus(Long goodsId, CoinsGoodsStatus status);

	/**
	 * 批量移动坑位，坑位中间去掉一条后，批量移动其他坑位
	 * 
	 * @param cg
	 *            为删除或者下架的商品
	 *
	 * @author zdy
	 */
	void dealBatchMoveSequence(CoinsGoods goods, int _index);

	/**
	 * 交换排序
	 * 
	 * @param goodsId
	 * @param goodsId2
	 * @param userType
	 *
	 * @author zdy
	 */
	void exchangeSequenceOfCoinsGoods(Long goodsId, Integer goodsId2, int userType);

	/**
	 * 查询上架商品
	 * 
	 * @param coinsGoodsType
	 * @param useTypeStr
	 * @return
	 *
	 * @author zdy
	 */
	Integer getSalingGoodsCount(CoinsGoodsType coinsGoodsType, String useTypeStr);

	/**
	 * 把自动上架的商品的seq填充
	 * 
	 * @param _index
	 *            排序号
	 *
	 * @author zdy
	 */
	void dealSequenceOfNull(CoinsGoods goods, int _index);

	/**
	 * 查询出所有上架商品 ，但是seq为空的商品
	 * 
	 * @param _index
	 * @return
	 *
	 * @author zdy
	 */
	List<CoinsGoods> getSalingGoodsOfNullSequence(int _index, CoinsGoodsType coinsGoodsType);

	/**
	 * 查询出所有自动下架的商品
	 * 
	 * @param _index
	 * @return
	 *
	 * @author zdy
	 */
	List<CoinsGoods> getSequenceOffGoods(int _index, CoinsGoodsType coinsGoodsType);

}
