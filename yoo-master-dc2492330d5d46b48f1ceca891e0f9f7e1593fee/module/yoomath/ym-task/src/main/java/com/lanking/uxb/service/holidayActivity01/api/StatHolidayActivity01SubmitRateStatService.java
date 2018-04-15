package com.lanking.uxb.service.holidayActivity01.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Class;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 假期作业提交率统计
 * 
 * @author wangsenhao
 *
 */
public interface StatHolidayActivity01SubmitRateStatService {

	/**
	 * 分页获取假期活动作业
	 * 
	 * @param cursorPageable
	 * @return
	 */
	CursorPage<Long, Long> findHolidayActivity01HkList(CursorPageable<Long> cursorPageable);

	/**
	 * 分页获取假期班级学生
	 * 
	 * @param cursorPageable
	 * @param activityCode
	 * @return
	 */
	CursorPage<Long, Long> findClassUserList(CursorPageable<Long> cursorPageable);

	/**
	 * 分页获取假期班级
	 * 
	 * @param cursorPageable
	 * @return
	 */
	CursorPage<Long, Long> findClassList(CursorPageable<Long> cursorPageable);

	/**
	 * 更新作业的提交率
	 * 
	 * @param list
	 */
	void statHkSubmitRate(List<Long> list);

	/**
	 * 更新班级的提交率
	 * 
	 * @param list
	 */
	void statClassSubmitRate(List<Long> list);

	/**
	 * 更新学生的提交率
	 * 
	 * @param list
	 */
	void statClassUserSubmitRate(List<Long> list);

	/**
	 * 重置班级的提交率，每次都需要重新计算
	 */
	void resetClassSubmitRate(List<Long> ids);

	/**
	 * 获取班级对象
	 * 
	 * @param activityCode
	 * @param classId
	 * @return
	 */
	HolidayActivity01Class get(long activityCode, long classId);

	/**
	 * 获取班级用户提交率
	 * 
	 * @param studentId
	 * @param classId
	 * @return
	 */
	Integer userSubmitRate(long studentId, long classId, long code);

	/**
	 * 获取班级提交率
	 * 
	 * @param teacherId
	 * @param classId
	 * @return
	 */
	Integer classSubmitRate(long teacherId, long classId, long code);
}
