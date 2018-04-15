package com.lanking.uxb.channelSales.base.api;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.base.form.SchoolForm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CsSchoolService {

	/**
	 * 查询学校
	 * 
	 * @param form
	 * @return
	 */
	Page<School> query(SchoolForm query, Pageable p);

	/**
	 * 学校绑定渠道
	 * 
	 * @param schoolId
	 * @param channelCode
	 * @param userId
	 */
	void bindChannel(Long schoolId, Integer channelCode, Long userId);

	/**
	 * 判断渠道是否存在
	 * 
	 * @param code
	 * @return
	 */
	boolean isExistChannel(Integer code);

	/**
	 * 根据教师id查找学校数据
	 *
	 * @param teacherId
	 *            教师id
	 * @return {@link School}
	 */
	School findByTeacherId(long teacherId);

	/**
	 * 根据教师id列表查找数据
	 *
	 * @param teacherIds
	 *            教师id列表
	 * @return 教师id -> 学校数据
	 */
	Map<Long, School> findByTeacherIds(Collection<Long> teacherIds);

	/**
	 * 统计学校各用户数量
	 *
	 * @param schoolIds
	 *            学校id列表
	 * @return {@link List}
	 */
	List<Map> countSchoolUserNum(Collection<Long> schoolIds, UserType userType);

	/**
	 * 统计学校教师会员数量
	 *
	 * @param schoolIds
	 *            学校id列表
	 * @return {@link List}
	 */
	List<Map> countSchoolTeacherMemberNum(Collection<Long> schoolIds);

	/**
	 * 统计学生教师会员数量
	 *
	 * @param schoolIds
	 *            学校id列表
	 * @return {@link List}
	 */
	List<Map> countSchoolStudentMemberNum(Collection<Long> schoolIds);

}
