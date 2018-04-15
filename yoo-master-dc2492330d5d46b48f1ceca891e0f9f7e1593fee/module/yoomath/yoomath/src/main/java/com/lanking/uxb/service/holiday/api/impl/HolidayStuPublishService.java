package com.lanking.uxb.service.holiday.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHolidayHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.form.HolidayStuHomeworkItemPublishForm;
import com.lanking.uxb.service.holiday.form.HolidayStuHomeworkPublishForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 异步生成学生假期作业数据
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
public class HolidayStuPublishService {
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private HolidayHomeworkItemService holidayHomeworkItemService;
	@Autowired
	private HolidayHomeworkItemQuestionService holidayHomeworkItemQuestionService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkItemService holidayStuHomeworkItemService;
	@Autowired
	private HolidayStuHomeworkItemQuestionService holidayStuHomeworkItemQuestionService;
	@Autowired
	private ZyHomeworkStudentClazzService homeworkStudentClazzService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	/**
	 * 异步给学生布置假期作业
	 *
	 * @param holidayHomework
	 *            假期作业
	 */
	@Async
	public void asyncPublish(HolidayHomework holidayHomework) {
		publish(holidayHomework);
	}

	/**
	 * 非异步形式布置给学生
	 *
	 * @param holidayHomework
	 *            假期作业
	 */
	public void publish(HolidayHomework holidayHomework) {
		List<Long> stuIds = homeworkStudentClazzService.listClassStudents(holidayHomework.getHomeworkClassId());
		List<HolidayHomeworkItem> hkItems = holidayHomeworkItemService.listHdItemById(holidayHomework.getId());
		List<Long> itemIds = new ArrayList<Long>(hkItems.size());
		for (HolidayHomeworkItem item : hkItems) {
			itemIds.add(item.getId());
		}
		Map<Long, List<Question>> itemQuestionMap = holidayHomeworkItemQuestionService.mgetByItemIds(itemIds);
		int countFlag = 0;
		List<Long> ids = new ArrayList<Long>(10);
		int stuSize = stuIds.size();
		long lastStudentId = stuSize > 0 ? stuIds.get(stuSize - 1) : 0;
		for (long studentId : stuIds) {
			ids.add(studentId);
			countFlag++;
			if (countFlag >= 10 || lastStudentId == studentId) {
				asyncPublishToStudent(holidayHomework, hkItems, itemQuestionMap, ids);
				countFlag = 0;
				ids = new ArrayList<Long>(10);
			}

		}

		for (HolidayHomeworkItem hkItem : hkItems) {
			holidayHomeworkItemService.updateDistributeCount(hkItem.getId(), stuSize);
		}

		// 若此时的作业是处始状态则更新为发布状态
		if (HomeworkStatus.INIT == holidayHomework.getStatus()) {
			holidayHomeworkService.updateHolidayHomeworkStatus(holidayHomework.getId(), HomeworkStatus.PUBLISH);
			holidayHomeworkItemService.updateHomeworkItemStatus(itemIds, HomeworkStatus.PUBLISH);
		}

		// 若在学生作业分发完之前就将此作业删除，再更新为删除状态
		if (holidayHomework.getDelStatus() == Status.DELETED) {
			holidayHomeworkService.delete(holidayHomework.getCreateId(), holidayHomework.getId());
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("teacherId", holidayHomework.getCreateId());
		jsonObject.put("holidayHomeworkItemIds", itemIds);
		mqSender.send(MqYoomathHolidayHomeworkRegistryConstants.EX_YM_HOLIDAYHOMEWORK,
				MqYoomathHolidayHomeworkRegistryConstants.RK_YM_HOLIDAYHOMEWORK_PUBLISH,
				MQ.builder().data(jsonObject).build());
	}

	/**
	 * 布置作业给学生，每10个一组开辟一个线程。
	 *
	 * @param holidayHomework
	 *            假期作业
	 * @param hkItems
	 *            假期作业专项
	 * @param itemQuestionMap
	 *            专项对应的题目map
	 * @param ids
	 *            学生id
	 */
	private void asyncPublishToStudent(final HolidayHomework holidayHomework, final List<HolidayHomeworkItem> hkItems,
			final Map<Long, List<Question>> itemQuestionMap, final List<Long> ids) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				for (final long studentId : ids) {
					HolidayStuHomeworkPublishForm form = new HolidayStuHomeworkPublishForm();
					form.setHolidayHomeworkId(holidayHomework.getId());
					form.setStudentId(studentId);
					form.setType(holidayHomework.getType());
					form.setItemCount(hkItems.size());
					HolidayStuHomework stuHomework = holidayStuHomeworkService.create(form);

					for (HolidayHomeworkItem hkItem : hkItems) {
						HolidayStuHomeworkItemPublishForm itemForm = new HolidayStuHomeworkItemPublishForm();
						itemForm.setHolidayHomeworkId(holidayHomework.getId());
						itemForm.setHolidayHomeworkItemId(hkItem.getId());
						itemForm.setHolidayStuHomeworkId(stuHomework.getId());
						itemForm.setStudentId(studentId);
						itemForm.setType(holidayHomework.getType());

						HolidayStuHomeworkItem hshItem = holidayStuHomeworkItemService.create(itemForm);

						holidayStuHomeworkItemQuestionService.create(itemQuestionMap.get(hkItem.getId()),
								holidayHomework.getId(), hkItem.getId(), stuHomework.getId(), hshItem.getId(),
								studentId, holidayHomework.getType());
					}
				}

			}
		});

	}
}
