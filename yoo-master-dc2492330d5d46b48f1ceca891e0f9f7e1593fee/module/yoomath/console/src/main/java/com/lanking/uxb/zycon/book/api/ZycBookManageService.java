package com.lanking.uxb.zycon.book.api;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.yoomath.school.UserSchoolBook;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.book.form.BookForm;

/**
 * 书本管理接口
 * 
 * @author wangsenhao
 *
 */
public interface ZycBookManageService {
	/**
	 * 书本查询
	 * 
	 * @param bq
	 * @return
	 */
	Page<BookVersion> queryBook(BookQuery bq);

	/**
	 * 获取开通了校本题库的学校(不翻页)
	 * 
	 * @return
	 */
	Page<School> getQuestionSchoolList(Long bookId, Pageable p);

	/**
	 * 获取开通了校本题库的学校(不翻页)
	 * 
	 * @param bookId
	 * @return
	 */
	List<School> getQuestionSchoolList(Long bookId);

	/**
	 * 书本状态设置
	 * 
	 * @param bookForm
	 *            页面返回的值
	 */
	void updateBookStatus(BookForm bookForm);

	/**
	 * 通过学校获取书本
	 * 
	 * @param schoolId
	 * @param phaseCode
	 * @return
	 */
	Page<BookVersion> getBookBySchool(Long schoolId, Integer phaseCode, Pageable p);

	/**
	 * 根据书本ID 获取 选择该书本的用户的记录列表
	 * 
	 * @param bookId
	 *            书本ID
	 * @return userschoolBookList
	 */
	List<UserSchoolBook> getUserSchoolBookByBookId(long bookId);

	/**
	 * 根据ID 更改userschoolBook 状态
	 * 
	 * @param ids
	 *            useschoolbook IDs
	 * @param status
	 *            状态
	 */
	void updateUserSchoolBookStatus(List<Long> ids, Status status);
}
