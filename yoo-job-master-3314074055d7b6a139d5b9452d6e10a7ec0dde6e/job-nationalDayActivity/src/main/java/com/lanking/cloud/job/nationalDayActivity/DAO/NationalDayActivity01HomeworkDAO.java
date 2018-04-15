package com.lanking.cloud.job.nationalDayActivity.DAO;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Homework;

public interface NationalDayActivity01HomeworkDAO extends IHibernateDAO<NationalDayActivity01Homework, Long> {

	void delete(long homeworkId);

	NationalDayActivity01Homework create(long teacherId, long homeworkId);

	boolean hasNoHomework();

	/**
	 * 根据userId查询teacherIds
	 * 
	 * @param userIds
	 */
	List<Long> getHomeworkTeacherIdByUsers(List<Long> userIds);

	/**
	 * 查询指定数量的teacherId,做去重处理
	 * 
	 * @param startindex
	 * @param size
	 * @return
	 */
	List<Long> getTeacherIdsSpecify(int startindex, int size);

	/**
	 * 根据teacherIds查询homework数据
	 * 
	 * @param userIds
	 * @return map key:teacherId value:NationalDayActivity01Homework
	 */
	Map<Long, List<NationalDayActivity01Homework>> getHomeworkByUsers(List<Long> userIds);
}