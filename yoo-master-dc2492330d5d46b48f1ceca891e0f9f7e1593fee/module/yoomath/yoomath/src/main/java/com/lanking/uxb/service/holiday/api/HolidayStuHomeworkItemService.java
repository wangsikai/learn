package com.lanking.uxb.service.holiday.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.holiday.form.HolidayStuHomeworkItemPublishForm;

/**
 * 学生假日作业项接口
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
public interface HolidayStuHomeworkItemService {
	/**
	 * 获取单个假期学生作业专项
	 * 
	 * @param id
	 * @return
	 */
	HolidayStuHomeworkItem get(long id);

	/**
	 * 通过寒假作业专项ID和学生ID获取学生寒假作业专项
	 * 
	 * @param holidayHomeworkItemId
	 *            寒假作业专项ID
	 * @param studentId
	 *            学生ID
	 * @return 学生寒假作业专项
	 */
	HolidayStuHomeworkItem find(long holidayHomeworkItemId, long studentId);

	/**
	 * 查询学生寒假作业(专项信息)
	 * 
	 * @param holidayHomeworkId
	 *            寒假作业Id
	 * @param userId
	 *            创建人ID
	 * @param holidayStuHomeworkId
	 *            假期学生作业ID
	 * @param stuIds
	 *            学生ID集合
	 * @return
	 */
	List<HolidayStuHomeworkItem> queryStuHkItems(Long holidayHomeworkId, Long userId, Long holidayStuHomeworkId,
			List<Long> stuIds);

	/**
	 * 获取一次作业专项的所有学生作业
	 * 
	 * @param homeworkItemId
	 *            作业专项ID
	 * @return
	 */
	List<HolidayStuHomeworkItem> listByHomeworkItem(long hkItemId);

	/**
	 * 保存学生专项练习
	 *
	 * @param form
	 *            {@link HolidayStuHomeworkItemPublishForm}
	 * @return {@link HolidayStuHomeworkItem}
	 */
	HolidayStuHomeworkItem create(HolidayStuHomeworkItemPublishForm form);

	/**
	 * 更新学生专项作业的时间
	 * 
	 * @param holidayStuHomeworkItemId
	 *            学生假期作业专项ID
	 * @param homeworkTime
	 *            学生做专项作业的时间
	 */
	void updateHomeworkTime(Long holidayStuHomeworkItemId, int homeworkTime);

	/**
	 * 更新学生作业状态,学生提交作业
	 * 
	 * @param holidayStuHomeworkItemId
	 *            假期学生作业专项ID
	 */
	void updateStudentHkStatus(Long holidayStuHomeworkItemId, double completionRate);

	/**
	 * 查询已经全部批改完成，但还未计算数据的学生专项作业
	 *
	 * @param pageable
	 *            {@link CursorPageable}
	 * @return {@link CursorPage}
	 */
	CursorPage<Long, HolidayStuHomeworkItem> queryNotCalculate(CursorPageable<Long> pageable);

	/**
	 * 批改学生作业
	 * 
	 * @param holidayStuHomeworkItemId
	 */
	void correctHolidayStuHk(long holidayStuHomeworkItemId);

	/**
	 * 获取班级和学生专项作业的统计
	 * 
	 * @param holidayStuHomeworkId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getClazzStat(long holidayStuHomeworkId);

	/**
	 * 获取学生假期作业下所有专项完成率和<br>
	 * 
	 * @param holidayStuHomeworkId
	 * @return
	 */
	Double getSumComplete(long holidayStuHomeworkId);

	/**
	 * 更新学生专项完成率
	 * 
	 * @param holidayStuHomeworkItemId
	 */
	long updateStuItemCompleteRate(long holidayStuHomeworkItemId, double completionRate);

	/**
	 * 获取假期作业对应没有提交的学生专项集合
	 * 
	 * @param holidayHomeworkId
	 *            假期作业Id
	 * @param status
	 *            学生专项作业状态
	 * @return
	 */
	List<HolidayStuHomeworkItem> queryStuItems(Long holidayHomeworkId, StudentHomeworkStatus status);

}
