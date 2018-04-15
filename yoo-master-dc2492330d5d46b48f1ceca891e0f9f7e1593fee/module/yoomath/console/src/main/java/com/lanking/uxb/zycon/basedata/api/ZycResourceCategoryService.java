package com.lanking.uxb.zycon.basedata.api;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;

/**
 * 教材接口
 *
 * @author zemin.song
 * @since V2.0.7
 */
public interface ZycResourceCategoryService {

	/**
	 * 得到所有教材
	 *
	 * @return {@link ResourceCategory}
	 */
	List<ResourceCategory> findAll();

}
