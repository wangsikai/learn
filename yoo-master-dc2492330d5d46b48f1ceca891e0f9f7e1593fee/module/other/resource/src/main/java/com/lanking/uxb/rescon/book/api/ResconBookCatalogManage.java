package com.lanking.uxb.rescon.book.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookCatalogSection;

/**
 * 书本目录接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月22日
 */
public interface ResconBookCatalogManage {

	/**
	 * 获得目录章节.
	 * 
	 * @param bookCatalogId
	 *            章节ID
	 * @return
	 */
	BookCatalog get(Long bookCatalogId);

	/**
	 * 查询版本目录.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 * @param isDesc
	 *            是否倒序排列
	 * 
	 * @return 版本目录集合.
	 */
	List<BookCatalog> listCatalogs(long bookVersionId, boolean isDesc);

	/**
	 * 获取一个书本版本的目录列表
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * @return 书本目录集合
	 */
	List<BookCatalog> getBookCatalog(long bookVersionId);

	/**
	 * 获取一个书本版本的目录集合
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * @return 书本目录集合
	 */
	Map<Long, BookCatalog> mgetBookCatalog(long bookVersionId);

	/**
	 * 删除目录
	 * 
	 * @param id
	 *            目录ID
	 */
	void delete(long id);

	/**
	 * 创建新的目录结点
	 * 
	 * @param id
	 *            已有目录的ID(生成的目录为此目录下一个)
	 * @param bookVersionId
	 *            书本版本ID
	 * @param name
	 *            新的目录名称
	 * @param updateId
	 *            操作人ID
	 * @return 生成的目录
	 */
	BookCatalog create(long id, long bookVersionId, String name, long updateId);

	/**
	 * 更新名称
	 * 
	 * @param id
	 *            目录ID
	 * @param name
	 *            新的名称
	 * @param updateId
	 *            操作人ID
	 * @return 更新后的目录
	 */
	BookCatalog updateName(long id, String name, long updateId);

	/**
	 * 向上移一层
	 * 
	 * @param id
	 *            目录ID
	 * @param updateId
	 *            操作人ID
	 */
	void upLevel(long id, long updateId);

	/**
	 * 向下移一层
	 * 
	 * @param id
	 *            目录ID
	 * @param updateId
	 *            操作人ID
	 */
	void downLevel(long id, long updateId);

	/**
	 * 向下移
	 * 
	 * @param id
	 *            目录ID
	 * @param updateId
	 *            操作人ID
	 */
	void down(long id, long updateId);

	/**
	 * 向上移
	 * 
	 * @param id
	 *            目录ID
	 * @param updateId
	 *            操作人ID
	 */
	void up(long id, long updateId);

	/**
	 * 目录.
	 * 
	 * @param catalogs
	 * @return
	 */
	List<BookCatalog> save(Collection<BookCatalog> catalogs);

	/**
	 * 保存拷贝目录.
	 * 
	 * @param bookVersionId
	 *            版本ID.
	 * @param catalogs
	 *            要拷贝的目录集合
	 * @param createId
	 *            创建人
	 * @param bookCatalogSectionMap
	 *            书本目录章节对应关系
	 * @return 旧目录ID与新目录结构的对应关系集合
	 */
	Map<Long, BookCatalog> saveCopy(long bookVersionId, Collection<BookCatalog> catalogs, long createId,
			Map<Long, BookCatalogSection> bookCatalogSectionMap);

	/**
	 * 将某个目录下的所有资源移动到未分组中
	 * 
	 * @param bookVersionId
	 *            版本ID.
	 * @param catalogIds
	 *            目录IDs
	 */
	int move2NoCatalog(Long bookVersionId, List<Long> catalogIds);

}
