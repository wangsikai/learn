package com.lanking.uxb.service.holiday.api;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkPublishForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;

/**
 * 假期作业相关接口
 * 
 * @since 1.9
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月22日 下午3:12:01
 */
public interface HolidayHomeworkService {

	/**
	 * 获取 假期作业
	 * 
	 * @param id
	 *            假期作业ID
	 * @return {@link HolidayHomework}
	 */
	HolidayHomework get(long id);

	/**
	 * 批量获取 假期作业
	 * 
	 * @param ids
	 *            假期作业ID集合
	 * @return {@link HolidayHomework}
	 */
	Map<Long, HolidayHomework> mget(Collection<Long> ids);

	/**
	 * 布置假期作业
	 *
	 * @param form
	 *            {@link HolidayHomeworkPublishForm}
	 * @return {@link HolidayHomework}
	 */
	HolidayHomework publish(HolidayHomeworkPublishForm form);

	/**
	 * 查询待发布的假期作业
	 *
	 * @param now
	 *            当前时间
	 * @param pageable
	 *            {@link CursorPageable}
	 * @return {@link CursorPage}
	 */
	CursorPage<Long, HolidayHomework> queryNotPublishHomework(Date now, CursorPageable<Long> pageable);

	/**
	 * 更新作业状态
	 *
	 * @param id
	 *            假期作业id
	 * @param homeworkStatus
	 *            作业状态
	 */
	void updateHolidayHomeworkStatus(long id, HomeworkStatus homeworkStatus);

	/**
	 * 删除寒假作业
	 * 
	 * @since yoomath V1.9
	 * @param teacherId
	 *            教师ID
	 * @param homeworkId
	 *            作业ID
	 * @return 删除的条数
	 */
	int delete(long userId, long id);

	/**
	 * 查询假期作业已经超过过期时间的
	 *
	 * @param now
	 *            当前时间
	 * @param cursor
	 *            {@link CursorPageable}
	 * @return {@link HolidayHomework}
	 */
	CursorPage<Long, HolidayHomework> queryAfterDeadline(Date now, CursorPageable<Long> cursor);

	/**
	 * 更新
	 *
	 * @param id
	 *            假期作业id
	 * @param status
	 *            {@link HomeworkStatus}
	 */
	void updateStatus(Long id, HomeworkStatus status);

	/**
	 * 更新寒假作业整体完成率
	 * 
	 * @param holidayHomeworkId
	 *            寒假作业ID
	 */
	void uptHolidayHomeworkCompleRate(long holidayHomeworkId);

	/**
	 * 系统自动提交并批改，过了截止时间还没有提交的学生专项作业
	 * 
	 * @param holidayHomeworkId
	 *            假日作业ID
	 */
	void updateAfterDeadLine(Long holidayHomeworkId);

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
	Page<HolidayHomework> queryHolidayHomeworkWeb2(ZyHomeworkQuery query, Pageable pageable);

	/**
	 * 获取专项数量
	 * 
	 * @param ids
	 *            假期作业ID集合
	 * @return
	 */
	Map<Long, Integer> queryHolidayHomeworkItemCount(Collection<Long> ids);

	/**
	 * 获取教师最早的作业、假期作业创建时间
	 * 
	 * @param teacherId
	 * @return
	 */
	Date getFirstCreateAt(Long teacherId);

	/**
	 * 根据教师id取count值
	 * 
	 * @param createId
	 */
	long allCountByCreateId(long createId);
}
