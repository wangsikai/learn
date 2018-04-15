package com.lanking.uxb.service.holiday.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.holiday.form.HolidayStuHomeworkPublishForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;

/**
 * 学生假日作业接口
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
public interface HolidayStuHomeworkService {

	/**
	 * 获取学生假日作业
	 * 
	 * @param id
	 * @return
	 */
	HolidayStuHomework get(long id);

	/**
	 * 批量获取学生假期作业
	 * 
	 * @param ids
	 * @return
	 */
	List<HolidayStuHomework> mget(Collection<Long> ids);

	Map<Long, HolidayStuHomework> mgetMap(Collection<Long> ids);

	/**
	 * 保存学生假期布置作业
	 *
	 * @param form
	 *            {@link HolidayStuHomeworkPublishForm}
	 * @return {@link HolidayStuHomework}
	 */
	HolidayStuHomework create(HolidayStuHomeworkPublishForm form);

	/**
	 * 查询学生假期作业
	 * 
	 * @param holidayHomeworkId
	 *            假期作业ID
	 * 
	 * @param stuIds
	 * @return
	 */
	List<HolidayStuHomework> queryStuHomework(long holidayHomeworkId, List<Long> stuIds);

	/**
	 * 更新假期学生作业完成率（实时调用）
	 * 
	 * @param holidayStuHomeworkId
	 *            学生假期作业ID
	 */
	void uptStuHomeworkCompleteRate(long holidayStuHomeworkId);

	/**
	 * 获取某个学生未完成寒假作业的数量
	 * 
	 * @since yoomath mobile V1.1.0
	 * @param studentId
	 *            学生ID
	 * @return 寒假作业数量
	 */
	long countNotSubmit(long studentId);

	/**
	 * 更新已经下发的假期学生作业查看状态
	 *
	 * @param id
	 *            假期作业id
	 */
	void updateViewStatus(long id);

	/**
	 * 分页查询作业列表
	 * 
	 * @since 2.0.3 (web v2.0)
	 * @param query
	 *            查询条件{@link ZyHomeworkQuery}
	 * @param pageable
	 *            分页条件
	 * @return 分页数据 {@link Page}
	 */
	Page<HolidayStuHomework> queryHolidayHomeworkWeb(ZyStudentHomeworkQuery query, Pageable pageable);

	/**
	 * 获取学生最早的作业、假期作业创建时间
	 * 
	 * @since 2.0.3 (web v2.0)
	 * @param studentId
	 * @return
	 */
	Date getFirstStartAt(Long studentId);
}
