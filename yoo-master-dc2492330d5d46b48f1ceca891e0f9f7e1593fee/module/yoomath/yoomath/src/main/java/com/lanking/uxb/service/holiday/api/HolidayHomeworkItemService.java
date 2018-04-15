package com.lanking.uxb.service.holiday.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkItemPublishForm;

/**
 * 假期作业专项相关接口
 * 
 * @since 1.9
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月22日 下午3:12:01
 */
public interface HolidayHomeworkItemService {

	/**
	 * 根据假期作业ID 获取专项列表
	 * 
	 * @param holidayHomeworkId
	 *            假期作业ID
	 * @return
	 */
	List<HolidayHomeworkItem> listHdItemById(long holidayHomeworkId);

	/**
	 * 获取单个专项信息
	 * 
	 * @param id
	 * @return
	 */
	HolidayHomeworkItem get(long id);

	/**
	 * 获取批量专项信息
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, HolidayHomeworkItem> mget(Collection<Long> ids);

	/**
	 * 布置作业
	 *
	 * @param form
	 *            {@link HolidayHomeworkItemPublishForm}
	 * @return {@link HolidayHomeworkItem}
	 */
	HolidayHomeworkItem create(HolidayHomeworkItemPublishForm form);

	/**
	 * 更新纷发数量
	 *
	 * @param id
	 *            作业id
	 * @param size
	 *            纷发数量
	 */
	void updateDistributeCount(long id, int size);

	/**
	 * 更新专项状态
	 *
	 * @param ids
	 *            假期作业专项id
	 * @param status
	 *            状态
	 */
	void updateHomeworkItemStatus(Collection<Long> ids, HomeworkStatus status);
}
