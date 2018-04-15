package com.lanking.uxb.service.knowpoint.api.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.knowpoint.api.HkClazzNewKnowpointStatService;
import com.lanking.uxb.service.knowpoint.api.HkStuClazzNewKnowpointStatService;
import com.lanking.uxb.service.knowpoint.api.NewKpStatInitService;

@Service
public class NewKpStatInitServiceImpl implements NewKpStatInitService {

	private static final Logger logger = LoggerFactory.getLogger(NewKpStatInitServiceImpl.class);
	@Autowired
	private HkClazzNewKnowpointStatService clazzStatService;
	@Autowired
	private HkStuClazzNewKnowpointStatService stuStatService;
	private static final int HOMEWORK_FETCH_SIZE = 1;

	@Override
	public void initStuKpStat(Date nowTime) {
		stuStatService.deleteNewAll();
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, HOMEWORK_FETCH_SIZE);
		CursorPage<Long, Long> cursorPage = stuStatService.findIssueHkIds(cursorPageable, nowTime);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<Long> hkIds = cursorPage.getItems();
			for (Long hkId : hkIds) {
				try {
					stuStatService.statisticAfterHomework(hkId);
				} catch (Exception e) {
					try {
						stuStatService.statisticAfterHomework(hkId);
					} catch (Exception ee) {
						logger.error("initStuKpStat error", ee);
					}
				}

			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, HOMEWORK_FETCH_SIZE);
			cursorPage = stuStatService.findIssueHkIds(cursorPageable, nowTime);
		}
	}

	@Override
	public void initClazzKpStat(Date nowTime) {
		clazzStatService.deleteNewAll();
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, HOMEWORK_FETCH_SIZE);
		CursorPage<Long, Long> cursorPage = stuStatService.findIssueHkIds(cursorPageable, nowTime);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<Long> hkIds = cursorPage.getItems();
			for (Long hkId : hkIds) {
				clazzStatService.statisticAfterHomework(hkId);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, HOMEWORK_FETCH_SIZE);
			cursorPage = stuStatService.findIssueHkIds(cursorPageable, nowTime);
		}
	}
}
