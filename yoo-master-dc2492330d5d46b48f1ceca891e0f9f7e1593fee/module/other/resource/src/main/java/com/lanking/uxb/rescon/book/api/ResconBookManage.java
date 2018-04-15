package com.lanking.uxb.rescon.book.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.rescon.book.form.BookForm;
import com.lanking.uxb.rescon.book.form.BookQueryForm;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;

/**
 * 书本接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月22日
 */
public interface ResconBookManage {

	/**
	 * 获得书本.
	 * 
	 * @param bookId
	 *            书本ID.
	 * @return
	 */
	Book get(long bookId);

	/**
	 * 获得书本版本.
	 * 
	 * @param bookVersionId
	 *            版本ID.
	 * @return
	 */
	BookVersion getVersion(long bookVersionId);

	/**
	 * 创建书本.
	 * 
	 * @param form
	 *            参数
	 * @param creater
	 *            操作人
	 * @param 书本ID
	 */
	BookVersion saveBook(BookForm form, VendorUser creater) throws ResourceConsoleException;

	/**
	 * 提交版本.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 */
	void submitBookVersion(long bookVersionId) throws ResourceConsoleException;

	/**
	 * 保存版本.
	 * 
	 * @param bookVersion
	 *            版本
	 * @throws VBookException
	 */
	void saveBookVersion(BookVersion bookVersion) throws ResourceConsoleException;

	/**
	 * 版本发布.
	 * 
	 * @param bookId
	 *            书本ID.
	 * @param bookVersionId
	 *            版本ID
	 * @param createId
	 *            操作人
	 * @throws VBookException
	 */
	void publishBook(long bookId, long bookVersionId, long createId) throws ResourceConsoleException;

	/**
	 * 批量查询书本版本
	 * 
	 * @param bookId
	 * @return
	 */
	List<BookVersion> listBookVersion(long bookId);

	/**
	 * 批量查询书本版本
	 * 
	 * @param bookId
	 * @return
	 */
	List<BookVersion> listBookVersion(Collection<Long> bookIds);

	/**
	 * 批量查询书本版本
	 * 
	 * @param bookId
	 * @return
	 */
	List<BookVersion> listBookVersion(Collection<Long> bookIds, BookStatus bookStatus);

	/**
	 * 批量查询书本主版本
	 * 
	 * @param bookId
	 * @return
	 */
	List<BookVersion> listMainBookVersion(Collection<Long> bookIds, BookStatus bookStatus);

	/**
	 * 批量查询书本
	 * 
	 * @param bookIds
	 *            书本ID.
	 * @return
	 */
	Map<Long, Book> mgetBook(Collection<Long> bookIds);

	/**
	 * 更新书本封面.
	 * 
	 * @param bookVersionId
	 *            书本版本
	 * @param fileId
	 *            书本文件
	 * @throws VBookException
	 */
	void updateCover(long bookVersionId, long fileId) throws ResourceConsoleException;

	/**
	 * 更新书本版本的状态.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 * @param status
	 *            状态
	 * @throws VBookException
	 */
	void changeBookVersionStatus(long bookVersionId, BookStatus status) throws ResourceConsoleException;

	/**
	 * 根据商品ID获取书本
	 * 
	 * @param productId
	 *            商品ID
	 * @return
	 */
	Book getFromProduct(Long productId);

	/**
	 * 查询书本.
	 * 
	 * @param vendorId
	 *            供应商限定
	 * @param form
	 *            查询参数表单
	 * @param pageable
	 *            分页参数
	 * @return
	 */
	Page<Long> queryBook(Long vendorId, BookQueryForm form, Pageable pageable);

	/**
	 * 获得书本整体统计数据.
	 * 
	 * @param vendorId
	 *            运营商
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> queryBooksCounts(Long vendorId);

	/**
	 * 学科书本统计数据.
	 * 
	 * @param subjectCode
	 *            学科
	 * @param vendorId
	 *            运营商
	 * @return
	 */
	Map<BookStatus, Integer> subjectBooksCounts(Integer subjectCode, Long vendorId);

	/**
	 * 获取书本标签关系.
	 * 
	 * @param bookVersion
	 *            书本版本
	 * @return
	 */
	List<Book2Tag> listBook2Tags(long bookVersionId);

	/**
	 * 保存书本标签关系.
	 * 
	 * @param bookVersion
	 *            书本版本
	 * @param tagNames
	 *            标签名称集合
	 */
	void saveBook2Tags(long bookVersionId, List<String> tagNames);
}