package com.lanking.uxb.rescon.basedata.api;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.uxb.rescon.basedata.form.ResconResourceCategoryForm;

import java.util.List;

/**
 * 教材接口
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ResconResourceCategoryService {
	/**
	 * 保存教材
	 *
	 * @param form
	 *            {@link ResconResourceCategoryForm}
	 */
	void save(ResconResourceCategoryForm form);

	/**
	 * 得到所有教材
	 *
	 * @return {@link ResourceCategory}
	 */
	List<ResourceCategory> findAll();

	/**
	 * 同步数据
	 */
	void syncData();

	/**
	 * 保存排序值
	 *
	 * @param code
	 *            编码
	 * @param sequence
	 *            排序值
	 */
	void updateSequence(int code, int sequence);

	/**
	 * 更新排序
	 *
	 * @param forms
	 *            {@link ResconResourceCategoryForm}
	 */
	void updateSequence(List<ResconResourceCategoryForm> forms);
}
