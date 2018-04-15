package com.lanking.uxb.service.stuweakkp.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.stuweakkp.api.TaskYmStuExerciseWeakKnowStatService;
import com.lanking.uxb.service.stuweakkp.api.YmStuExerciseWeakKnowStatService;

@Service
public class TaskYmStuExerciseWeakKnowStatServiceImpl implements TaskYmStuExerciseWeakKnowStatService {

	private static final int DATA_SIZE = 500;
	// 正确率小于0.6
	private static final double minRate = 0.6;
	@Autowired
	private YmStuExerciseWeakKnowStatService statService;

	@SuppressWarnings("rawtypes")
	@Override
	public void statWeakKnow(int version) {
		statService.clearWeakStat();
		CursorPage<Long, Map> weaks = statService.queryStuWeak(CP.cursor(Long.MAX_VALUE, DATA_SIZE), minRate, version);
		while (weaks.isNotEmpty()) {
			statService.saveWeakStat(weaks.getItems());
			Long nextCursor = weaks.getNextCursor();
			weaks = statService.queryStuWeak(CP.cursor(nextCursor, DATA_SIZE), minRate, version);
		}
	}

}
