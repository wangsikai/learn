package com.lanking.uxb.zycon.book.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.school.SchoolBook;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 学校书接口
 * 
 * @author wangsenhao
 *
 */
public interface ZycSchoolBookService {
	/**
	 * 获取学校书
	 * 
	 * @param schoolId
	 *            学校id
	 * @param bookId
	 *            书本id 阶段
	 * @return
	 */
	SchoolBook get(Long schoolId, Long bookId);

	/**
	 * 删除学校书本
	 * 
	 * @param schoolBookId
	 */
	void delSchookBook(Long schoolId, Long bookId);

	/**
	 * 添加新书
	 * 
	 * @param bookId
	 *            书本id
	 * @param schoolId
	 *            学校id
	 */
	void addSchoolBook(Long bookId, Long schoolId);

	/**
	 * 
	 * @param bookId
	 * @return
	 */
	List<SchoolBook> get(Long bookId);

	/**
	 * 
	 * @param bookIds
	 * @return
	 */
	Map<Long, List<SchoolBook>> mget(Collection<Long> bookIds);

	/**
	 * 通过书本id 删除对应的学校书本
	 * 
	 * @param bookId
	 */
	void delSchoolBookByBookId(Long bookId);

	/**
	 * 
	 * @param schoolId
	 * @return
	 */
	Long getBookCount(Long schoolId);

	/**
	 * 
	 * @param schoolIds
	 * @return
	 */
	Map<Long, Long> mgetBookCount(Collection<Long> schoolIds);

	/**
	 * 更新对应学校校本的状态
	 * 
	 * @param schoolId
	 * @param status
	 */
	void updateSchoolBook(Long schoolId, Status status);

}
