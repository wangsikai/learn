package com.lanking.uxb.service.zuoye.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.school.UserSchoolBook;
import com.lanking.cloud.sdk.bean.Status;

/**
 * U数学 用户学校书本 相关接口
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月3日 下午6:38:41
 */
public interface ZyUserSchoolBookService {

	/**
	 * 用户选择的书本
	 * 
	 * @param userId
	 *            用户ID
	 * @return map key 书本ID， value userschoolbook Id
	 */
	Map<Long, Long> getUserSelectedBook(long userId);

	/**
	 * 书本选择图书列表（不区分状态）
	 * 
	 * @param bookIds
	 *            书本IDs
	 * @param userId
	 *            用户ID
	 * @return 用户书本 列表
	 */
	List<UserSchoolBook> existBook(List<Long> bookIds, long userId);

	/**
	 * 保存教师选择的教辅
	 * 
	 * @param bookFromMap
	 *            key bookId，value schoolId （用于区分是否为学校图书）
	 * @param userId
	 *            用户ID
	 */
	void save(Map<Long, Long> bookFromMap, long userId);

	/**
	 * 更改书本状态
	 * 
	 * @param bookIds
	 *            书本IDs
	 * @param userId
	 *            用户ID
	 * @param status
	 *            状态
	 */

	void changeBookStatus(List<Long> bookIds, long userId, Status status);

	/**
	 * 根据用户ID ，状态 更改用户选择过的所有书本的状态
	 * 
	 * @param id
	 *            用户ID
	 * @param status
	 *            状态
	 */
	void updateTeacherChoosedBook(Long id, Status status);

}
