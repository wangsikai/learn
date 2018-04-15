package com.lanking.uxb.zycon.mall.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;

/**
 * 金币商品组
 * 
 * @author wangsenhao
 *
 */
public interface ZycCoinsGoodsGroupService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	CoinsGoodsGroup get(long id);

	/**
	 * 商品组列表
	 * 
	 * @return
	 */
	List<CoinsGoodsGroup> list();

	/**
	 * 删除商品组
	 * 
	 * @param id
	 */
	void delGroup(long id);

	/**
	 * 增加商品组
	 * 
	 * @param name
	 */
	void addGroup(String name);

	/**
	 * 查询商品组底下是否有已上架的商品
	 * 
	 * @param id
	 * @return
	 */
	Integer publishCountInGroup(long id, int userType);

	/**
	 * 移动商品组
	 * 
	 * @param upMoveId
	 * @param downMoveId
	 */
	void move(long upMoveId, long downMoveId);

	/**
	 * 获取最大序号
	 * 
	 * @return
	 */
	Integer getMaxSequence();
}
