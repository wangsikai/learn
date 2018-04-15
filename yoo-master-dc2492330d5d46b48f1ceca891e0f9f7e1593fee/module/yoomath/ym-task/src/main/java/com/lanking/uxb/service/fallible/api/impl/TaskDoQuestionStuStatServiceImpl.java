package com.lanking.uxb.service.fallible.api.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.fallible.api.StaticDoQuestionStuKnowpointStatService;
import com.lanking.uxb.service.fallible.api.StaticDoQuestionStuNaturalMonthStatService;
import com.lanking.uxb.service.fallible.api.TaskDoQuestionStuStatService;

@Service
public class TaskDoQuestionStuStatServiceImpl implements TaskDoQuestionStuStatService {

	@Autowired
	private StaticDoQuestionStuNaturalMonthStatService monthService;
	@Autowired
	private StaticDoQuestionStuKnowpointStatService kpService;

	private static final int STU_FETCH_SIZE = 100;

	@Override
	public void statStuDoQuestion() {
		monthService.deleteMonthStat();
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, STU_FETCH_SIZE);
		CursorPage<Long, Long> cursorPage = monthService.getAllStudent(cursorPageable);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<Long> studentIds = cursorPage.getItems();
			monthService.statByMonth(0, studentIds);
			monthService.statByMonth(1, studentIds);
			monthService.statByMonth(2, studentIds);
			monthService.statByMonth(3, studentIds);
			monthService.statByMonth(4, studentIds);
			monthService.statByMonth(5, studentIds);
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, STU_FETCH_SIZE);
			cursorPage = monthService.getAllStudent(cursorPageable);
		}
	}

	@Override
	public void statStuDoQuestionKp() {
		// 如果是一个月的第一天需要删除，否则不需要删除
		if (isFirstDayInMonth()) {
			kpService.deleteKpStat();
		}
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, STU_FETCH_SIZE);
		CursorPage<Long, Long> cursorPage = monthService.getAllStudent(cursorPageable);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<Long> studentIds = cursorPage.getItems();
			kpService.stat(studentIds, isFirstDayInMonth());
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, STU_FETCH_SIZE);
			cursorPage = monthService.getAllStudent(cursorPageable);
		}
	}

	/**
	 * 判断当前是否是一个月的第一天,因为这边是2号凌晨跑的，所以判断是否等于2。1号凌晨跑的还是前一天的数据
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean isFirstDayInMonth() {
		Calendar c = Calendar.getInstance();
		int today = c.get(c.DAY_OF_MONTH);
		if (today == 2) {
			return true;
		} else {
			return false;
		}

	}

}
