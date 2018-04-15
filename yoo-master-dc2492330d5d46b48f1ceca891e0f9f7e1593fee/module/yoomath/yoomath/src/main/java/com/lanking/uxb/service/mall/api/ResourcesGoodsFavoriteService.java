package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsFavorite;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.examPaper.form.ExamQueryForm;

/**
 * 资源商品收藏接口
 * 
 * @author zemin.song
 * @version 2016年9月2日
 */
public interface ResourcesGoodsFavoriteService {

	/**
	 * 通过id查询
	 * 
	 * @param Id
	 * @return
	 */
	ResourcesGoodsFavorite get(Long id);

	/**
	 * 通过ids批量查询
	 * 
	 * @param Ids
	 * @return
	 */
	Map<Long, ResourcesGoodsFavorite> mget(Collection<Long> ids);

	/**
	 * 通过用户ID查询当前收藏ID
	 * 
	 * @param createId
	 *            用户ID
	 * @param resourcesId
	 *            资源ID
	 * @return list
	 */
	List<Long> getFavoriteIdByResourcesId(long createId, Long resourcesId);

	/**
	 * 通过用户ID批量查询当前收藏
	 * 
	 * @param createId
	 *            用户ID
	 * @param resourcesId
	 *            资源ID
	 * @return list
	 */
	List<ResourcesGoodsFavorite> mgetFavoriteIdByResourcesId(long createId, Collection<Long> resourcesIds);

	/**
	 * 通过用户ID查询当权资源Id是否收藏
	 * 
	 * @param createId
	 *            用户ID
	 * @param goodsId
	 *            商品ID
	 * @return list
	 */
	List<Long> getFavoriteIdByUserId(long createId, Long goodsId);

	/**
	 * 加入收藏
	 * 
	 * @param createId
	 *            创建人Id
	 * @param goodsId
	 *            商品ID
	 * @return
	 */
	void addFavorite(Long createId, Long goodsId);

	/**
	 * 取消收藏
	 * 
	 * @param createId
	 *            创建用户ID
	 * @param id
	 *            收藏ID
	 * @return
	 */
	int removeFavorite(Long createId, Long id);

	/**
	 * 收藏列表(精品试卷收藏)
	 * 
	 * @param
	 * @return
	 */
	Page<ResourcesGoodsFavorite> queryExamPaperFavorite(ExamQueryForm form, Pageable pageable);

}
