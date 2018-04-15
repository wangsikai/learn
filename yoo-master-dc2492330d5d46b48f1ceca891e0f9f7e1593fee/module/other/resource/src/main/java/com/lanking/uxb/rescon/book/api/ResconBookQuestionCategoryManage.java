package com.lanking.uxb.rescon.book.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;

/**
 * 图书结构习题分类接口.
 * 
 * @author wlche
 *
 */
public interface ResconBookQuestionCategoryManage {

	BookQuestionCategory get(long questionCategoryId);

	/**
	 * 创建图书分类结构.
	 * 
	 * @param questionCategoryId
	 *            结构ID
	 * @param bookVersionId
	 *            书本版本ID
	 * @param bookCatalogId
	 *            书本目录ID
	 * @param name
	 *            分类名称
	 * @return
	 */
	BookQuestionCategory createOrUpdate(Long questionCategoryId, long bookVersionId, long bookCatalogId, String name);

	/**
	 * 查询某个目录的图书分类结构.
	 * 
	 * @param bookCatalogId
	 *            目录
	 * @return
	 */
	List<BookQuestionCategory> queryQuestionCategory(long bookCatalogId);

	/**
	 * 查询某个目录的图书分类结构.
	 * 
	 * @param bookCatalogIds
	 *            目录集合
	 * @return
	 */
	Map<Long, List<BookQuestionCategory>> queryQuestionCategory(Collection<Long> bookCatalogIds);

	/**
	 * 查询习题数量.
	 * 
	 * @param questionCategoryId
	 *            分类结构ID
	 * @return
	 */
	int getQuestionCount(long questionCategoryId);

	/**
	 * 批量查询习题数量.
	 * 
	 * @param questionCategoryIds
	 *            分类结构ID集合
	 * @return
	 */
	Map<Long, Integer> mgetQuestionCount(Collection<Long> questionCategoryIds);

	/**
	 * 删除分类结构.
	 * 
	 * @param bookCatalogId
	 *            目录ID
	 * @param questionCategoryId
	 *            分类ID
	 */
	void delBookQuestionCategory(long bookCatalogId, Long questionCategoryId);

	/**
	 * 移动习题分类.
	 * 
	 * @param bookCatalogId
	 *            目录
	 * @param bookQuestionCategoryId
	 *            原分类
	 * @param selectMoveCategoryId
	 *            新的分类
	 * @param questionIds
	 *            习题ID集合
	 */
	void moveToCategory(long bookCatalogId, Long bookQuestionCategoryId, long selectMoveCategoryId,
			List<Long> questionIds);

	/**
	 * 删除习题的结构分类关系
	 * 
	 * @param bookCatalogId
	 *            章节目录
	 * @param questionIds
	 *            习题集合
	 */
	void delBookQuestionCategoryRelation(long bookCatalogId, Collection<Long> questionIds);
}
