package com.lanking.uxb.zycon.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.uxb.zycon.mall.form.ResourcesGoodsForm;

/**
 * 资源商品接口
 * 
 * @author zemin.song
 * @version 2016年9月2日
 */
public interface ZycResourcesGoodsService {

	/**
	 * 通过id查询
	 * 
	 * @param Id
	 * @return
	 */
	ResourcesGoods get(Long id);

	/**
	 * 批处理
	 * 
	 * @param Id
	 * @return
	 */
	Map<Long, ResourcesGoods> mget(Collection<Long> ids);

	/**
	 * 通过来源ID查询
	 * 
	 * @param resourcesId
	 *            来源Id
	 * @return
	 */
	ResourcesGoods getGoodsByResourcesId(Long resourcesId);

	/**
	 * 通过来源ID查询
	 * 
	 * @param resourcesIds
	 *            来源ID
	 * @return list
	 */
	List<ResourcesGoods> mgetGoods(Collection<Long> resourcesIds);

	/**
	 * 改变商品上下架状态
	 * 
	 * @param Id
	 * @return
	 */
	ResourcesGoods chengeStatus(Long id, Long userId);

	/**
	 * 编辑保存
	 * 
	 * @param Id
	 * @return
	 */
	ResourcesGoods save(ResourcesGoodsForm form);
}
