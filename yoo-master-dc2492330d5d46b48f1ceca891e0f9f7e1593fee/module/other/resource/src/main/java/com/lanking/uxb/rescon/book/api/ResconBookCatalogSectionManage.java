package com.lanking.uxb.rescon.book.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.common.resource.book.BookCatalogSection;

/**
 * 书本目录章节对应关系服务.
 * 
 * @author wlche
 *
 */
public interface ResconBookCatalogSectionManage {

	/**
	 * 根据书本版本查询目录章节对应关系.
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * 
	 * @return
	 */
	List<BookCatalogSection> findByBookVersion(long bookVersionId);

	/**
	 * 根据书本目录查询章节对应关系.
	 * 
	 * @param catalogId
	 *            目录ID
	 * @return
	 */
	List<BookCatalogSection> findByCatalog(long bookVersionId, long catalogId);

	/**
	 * 保存书本目录与章节对应关系.
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * @param bookCatalogId
	 *            目录ID
	 * @param textbookCode
	 *            教材
	 * @param sectionCode
	 *            章节
	 * @param createId
	 *            创建人
	 * @param sequence
	 *            序号
	 */
	void save(Long bookVersionId, Long bookCatalogId, Integer textbookCode, Long sectionCode, long createId,
			int sequence);

	/**
	 * 去除某个教辅目录的教材章节对应关系.
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * @param bookCatalogId
	 *            书本目录ID
	 */
	void deleteCatalogRelation(long bookVersionId, long bookCatalogId);

	/**
	 * 去除某个教辅目录的教材章节对应关系.
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * @param bookCatalogIds
	 *            书本目录ID集合
	 */
	void deleteCatalogRelation(long bookVersionId, Collection<Long> bookCatalogIds);

	/**
	 * 根据教材章节去除某个教辅目录的教材章节对应关系.
	 * 
	 * @param textbookCode
	 *            教材
	 * @param sectionCode
	 *            章节
	 */
	void deleteCatalogRelationBySection(int textbookCode, long sectionCode);

	/**
	 * 根据书本去除某个教辅目录的教材章节对应关系.
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * 
	 */
	void deleteCatalogRelationByBookVersion(long bookVersionId);
}
