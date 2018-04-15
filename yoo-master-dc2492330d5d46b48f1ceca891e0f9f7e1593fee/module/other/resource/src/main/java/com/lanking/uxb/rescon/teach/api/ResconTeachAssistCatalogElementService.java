package com.lanking.uxb.rescon.teach.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;

/**
 * TeachAssistCatalogElement service
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public interface ResconTeachAssistCatalogElementService {
	/**
	 * 根据目录id查找数据
	 *
	 * @param catalogId
	 *            目录id
	 * @return {@link List}
	 */
	List<TeachAssistCatalogElement> findByCatalog(long catalogId);

	/**
	 * 更新排序值
	 *
	 * @param elementId
	 *            模块id
	 * @param catalogId
	 *            目录id
	 * @param sequence
	 *            排序值
	 */
	void updateSequence(long elementId, long catalogId, int sequence);

	/**
	 * 根据模块id删除数据
	 *
	 * @param elementId
	 *            模块id
	 */
	void deleteByElement(long elementId);
}
