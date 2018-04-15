package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * U数学 书本service 相关
 * 
 * @since 1.6
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月3日 下午4:33:16
 */
public interface ZyBookService {

	/**
	 * ID 获取bookCatalog
	 * 
	 * @param id
	 *            章节code
	 * @return bookCatalog
	 */
	BookCatalog getBookCatalog(Long id);

	/**
	 * mget书本
	 *
	 *
	 * @param ids
	 *            ids
	 * @return {@link Map}
	 */
	Map<Long, Book> mget(Collection<Long> ids);

	/**
	 * 批量获取书本章节目录
	 * 
	 * @param catalogIds
	 *            书本章节目录
	 * @return map Map<Long, BookCatalog> key 章节目录ID value 章节目录
	 */
	Map<Long, BookCatalog> mgetBookCatalogs(List<Long> catalogIds);

	/**
	 * ID 获取bookVersion
	 * 
	 * @param id
	 *            bookversion Id
	 * @return bookVersion
	 */
	BookVersion getBookVersion(Long id);

	/**
	 * 书本对应版本的所有章节目录list
	 * 
	 * @param bookVersionId
	 *            书本版本id
	 * @return 章节目录列表
	 */
	List<BookCatalog> getBookCatalogs(long bookVersionId);

	/**
	 * 获取对应教材版本的学校书本
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @param schoolId
	 *            学校Id
	 * @param pageable
	 *            分页条件
	 * @return 学校书本
	 */
	Page<BookVersion> getSchoolBook(long textCategoryCode, long textbookCode, long schoolId, Pageable pageable);

	/**
	 * 获取对应教材版本的学校书本数量（首页提示）
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @param schoolId
	 *            学校Id
	 * @return 学校书本
	 */
	Long getSchoolBookCount(long textCategoryCode, long textbookCode, long schoolId);

	/**
	 * 获取对应教材版本的免费书本
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @param pageable
	 *            分页条件
	 * @return 免费书本
	 */
	Page<BookVersion> getFreeBook(Integer textCategoryCode, Integer textbookCode, Pageable index);

	/**
	 * 获取对应教材版本的免费书本
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @return 免费书本
	 */
	Long getFreeBookCount(Integer textCategoryCode, Integer textbookCode);

	/**
	 * 获取用户对于教材版本选择的图书
	 * 
	 * @param textCategoryCode
	 *            版本CODE
	 * @param textbookCode
	 *            教材CODE
	 * @param userId
	 *            用户ID
	 * @return
	 */
	List<BookVersion> getUserBookList(Integer textCategoryCode, Integer textbookCode, long userId);

	/**
	 * 获得除校本以外的用户图书
	 *
	 * 外层Controller来判断用户会员类型
	 *
	 * @since 2.6.0
	 *
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @param userId
	 *            用户id
	 * @return {@link List}
	 */
	List<BookVersion> getUserFreeBookList(Integer textCategoryCode, Integer textbookCode, long userId);

	/**
	 * 根据书本目录ID 获取该目录下所有题目ID
	 * 
	 * @param bookcatalogId
	 *            书本目录ID
	 * @return 题目IDs
	 */
	List<Long> listQuestions(Long bookcatalogId);

	/**
	 * 用户在此版本下是否有权限使用此书
	 *
	 * @param categoryCode
	 *            版本码
	 * @param userId
	 *            用户id
	 * @param id
	 *            书的版本id
	 * @return true -> 有 false 无
	 */
	boolean existBook(int categoryCode, long userId, long id);

	/**
	 * 得到当前版本下的目录推荐章节
	 *
	 * levelOneCatalog -> 返回当前在章节目录数据 <br/>
	 * catalogs -> 若有推荐则返回推荐的章节数据
	 *
	 * @param bookVersionId
	 *            书本版本id
	 * @param nowCatalogId
	 *            现章节进度
	 * @param userId
	 *            用户id
	 * @return {@link List}
	 */
	Map<String, Object> getRecommendCatalogs(long bookVersionId, long nowCatalogId, long userId);

	/**
	 * 获得第一层的数据节点
	 *
	 * @param bookVersionId
	 *            书本版本编号
	 * @return {@link List}
	 */
	List<BookCatalog> getLevelOneCatalogs(long bookVersionId);

	/**
	 * 获得此章节的下一条叶子节点数据 若为最后一个则返回0
	 *
	 * @param bookVersionId
	 *            教辅书版本号
	 * @param nowCatalogId
	 *            当前教辅节点id
	 * @return 下一节点id
	 */
	Long getNextCatalog(long bookVersionId, long nowCatalogId);

	/**
	 * 获得图书
	 *
	 * @param id
	 *            图书id
	 * @return {@link Book}
	 */
	Book getBook(long id);

	/**
	 * 获取对应教材版本的学校书本
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @param schoolId
	 *            学校Id
	 * @param resourceCategoryCode
	 *            书本类型：501同步教辅，502假期复习，503总复习
	 * @return 学校书本
	 */
	List<BookVersion> getSchoolBookByDifResource(Integer textCategoryCode, Integer textbookCode, Long schoolId,
			Integer resourceCategoryCode);

	/**
	 * 获取对应教材版本的免费书本
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @param resourceCategoryCode
	 *            书本类型：501同步教辅，502假期复习，503总复习
	 * @return 免费书本
	 */
	List<BookVersion> getFreeBookByDifResource(Integer textCategoryCode, Integer textbookCode,
			Integer resourceCategoryCode);

	/**
	 * 获取对应教材版本的免费书本
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @param resourceCategoryCodes
	 *            书本类型：501同步教辅，502假期复习，503总复习
	 * @return 免费书本
	 */
	Map<Integer, List<BookVersion>> getBooksMapByDifResource(Integer textCategoryCode, Integer textbookCode,
			Collection<Integer> resourceCategoryCodes);
}
