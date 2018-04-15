package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsCategory;

/**
 * 活动商品分类接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月23日
 */
public interface LotteryActivityGoodsCategoryService {

	/**
	 * 根据活动获得活动商品分类.
	 * 
	 * @param activityCode
	 *            活动CODE
	 * @return
	 */
	List<LotteryActivityGoodsCategory> listByActivityCode(long activityCode);

	/**
	 * 保存.
	 * 
	 * @param categorys
	 */
	void save(Collection<LotteryActivityGoodsCategory> categorys);
}
