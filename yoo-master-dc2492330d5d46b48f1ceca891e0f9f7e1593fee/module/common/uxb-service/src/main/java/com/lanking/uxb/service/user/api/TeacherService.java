package com.lanking.uxb.service.user.api;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.user.ex.UserException;
import com.lanking.uxb.service.user.form.EditProfileForm;

/**
 * 提供教师个性的接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月18日
 *
 */
public interface TeacherService extends UserService {

	/**
	 * 设置阶段学科
	 * 
	 * @since 2.1
	 * @param userId
	 *            用户ID
	 * @param phaseCode
	 *            阶段代码
	 * @param subjectCode
	 *            学科代码
	 */
	void setPhaseSubject(long userId, int phaseCode, int subjectCode) throws UserException;

	/**
	 * 更新教师信息
	 * 
	 * @since 2.1
	 * @param fm
	 *            更新参数
	 * @return 返回更新后的teacher
	 */
	Teacher updateTeacher(EditProfileForm fm);

	/**
	 * 校验输入是否是用户名
	 * 
	 * @since 2.1
	 * @param name
	 * @return
	 */
	Long isUserName(String name);

	/**
	 * 获取所有老师
	 * 
	 * @param cursorPageable
	 * 
	 * @return
	 */
	CursorPage<Long, Teacher> getAll(CursorPageable<Long> cursorPageable);

	/**
	 * 更新教师 教材版本信息
	 * 
	 * @param textbookCategoryCode
	 * @param textBookCode
	 */
	void updateCategory(long userId, long textbookCategoryCode, long textBookCode);
}
