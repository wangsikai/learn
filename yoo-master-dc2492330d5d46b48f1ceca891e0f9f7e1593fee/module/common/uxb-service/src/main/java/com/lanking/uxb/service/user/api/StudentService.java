package com.lanking.uxb.service.user.api;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.user.form.EditProfileForm;

public interface StudentService extends UserService {

	void updateStudent(EditProfileForm ef);

	/**
	 * 设置教材
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param userId
	 *            用户ID
	 * @param phaseCode
	 *            阶段代码
	 * @param categoryCode
	 *            版本代码
	 * @param textbookCode
	 *            教材代码
	 */
	void setTextbook(long userId, int phaseCode, int categoryCode, int textbookCode);

	/**
	 * 设置学生入学年份
	 *
	 * @param userId
	 *            用户ID
	 * @param enterYear
	 *            入学年份
	 */
	void setYear(long userId, Integer enterYear);

	/**
	 * 查询已经设定教材的学生
	 *
	 * @param p
	 *            {@link Pageable}
	 * @return {@link Page}
	 */
	Page<Student> queryStudentByTextbookCode(Pageable p);

}
