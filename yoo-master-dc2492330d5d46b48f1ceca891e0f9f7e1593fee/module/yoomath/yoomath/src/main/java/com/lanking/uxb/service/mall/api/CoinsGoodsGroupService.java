package com.lanking.uxb.service.mall.api;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;
import com.lanking.cloud.domain.yoo.user.UserType;

import java.util.List;

/**
 * 金币商城组管理
 *
 * @author xinyu.zhou
 * @since 3.9.3
 */
public interface CoinsGoodsGroupService {

	/**
	 * 查询金币商城商品组
	 *
	 * @return {@link List}
	 */
	List<CoinsGoodsGroup> find();
}
