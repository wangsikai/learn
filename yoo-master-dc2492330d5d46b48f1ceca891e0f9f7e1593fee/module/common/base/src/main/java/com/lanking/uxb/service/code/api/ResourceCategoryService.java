package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;

/**
 * 资源类别
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年7月30日 下午2:48:53
 */
public interface ResourceCategoryService {
	/**
	 * 获取所有父级 的分类
	 * 
	 * @return
	 */
	List<ResourceCategory> getParentCategory();

	/**
	 * 根据COde 获取 对象
	 * 
	 * @param resourceCategory
	 * @return
	 */
	ResourceCategory getResCategory(Integer resourceCategory);

	/**
	 * 根据父类获取子类
	 * 
	 * @param code
	 * @return
	 */
	List<ResourceCategory> findCategoryByParent(int code);

	List<ResourceCategory> findAll();

	/**
	 * 获取所有子类
	 * 
	 * @return
	 */
	List<ResourceCategory> getSubCategories();

	/**
	 * 批量获取category
	 * 
	 * @param keys
	 * @return
	 */
	Map<Integer, ResourceCategory> getcategories(Collection<Integer> keys);

	/**
	 * @since2.1 根据子类获取父类
	 * @param key
	 * @return
	 */
	ResourceCategory getResParentType(Integer key);

	List<ResourceCategory> mgetList(Collection<Integer> ids);

	Map<Integer, ResourceCategory> mget(Collection<Integer> ids);
}
