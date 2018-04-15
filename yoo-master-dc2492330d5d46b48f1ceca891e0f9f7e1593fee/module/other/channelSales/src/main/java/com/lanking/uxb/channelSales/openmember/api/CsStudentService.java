package com.lanking.uxb.channelSales.openmember.api;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.channelSales.openmember.form.UserQueryForm;

import java.util.Collection;
import java.util.List;

/**
 * 渠道平台学生管理Service
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public interface CsStudentService {
	/**
	 * 查询学生数据
	 *
	 * @param queryForm
	 *            {@link UserQueryForm}
	 * @return {@link Page}
	 */
	Page<Student> query(UserQueryForm queryForm);

	/**
	 * 获得渠道商学生列表
	 *
	 * @param schoolId
	 *            学校id
	 * @return {@link List}
	 */
	List<Student> findChannelScoolStudent(long schoolId);

	/**
	 * 根据学生id列表查询学生列表
	 *
	 * @param ids
	 *            id列表
	 * @return {@link List}
	 */
	List<Student> mgetList(Collection<Long> ids);
}
