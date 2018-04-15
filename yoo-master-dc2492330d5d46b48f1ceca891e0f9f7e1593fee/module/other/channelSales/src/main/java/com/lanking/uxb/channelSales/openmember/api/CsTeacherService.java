package com.lanking.uxb.channelSales.openmember.api;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.channelSales.openmember.form.UserQueryForm;

import java.util.Collection;
import java.util.List;

/**
 * 渠道管理教师相关接口
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public interface CsTeacherService {
	/**
	 * 查找渠道商关联的学校教师
	 *
	 * @param schoolId
	 *            学校id
	 * @return {@link List}
	 */
	List<Teacher> findChannelSchoolTeacher(long schoolId);

	/**
	 * 分页查询教师数据
	 *
	 * @param queryForm
	 *            {@link UserQueryForm}
	 * @return {@link Page}
	 */
	Page<Teacher> queryTeacher(UserQueryForm queryForm);

	/**
	 * 根据id列表查询教师列表
	 *
	 * @param ids
	 *            id列表
	 * @return {@link List}
	 */
	List<Teacher> mgetList(Collection<Long> ids);

}
